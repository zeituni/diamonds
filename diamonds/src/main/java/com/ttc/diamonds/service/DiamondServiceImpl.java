package com.ttc.diamonds.service;

import com.google.common.base.Strings;
import com.ttc.diamonds.dto.*;
import com.ttc.diamonds.model.*;
import com.ttc.diamonds.repository.*;
import com.ttc.diamonds.service.converter.*;
import com.ttc.diamonds.service.exception.CustomerNotFoundException;
import com.ttc.diamonds.service.exception.ManufacturerAlreadyExistsException;
import com.ttc.diamonds.service.exception.StoreAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class DiamondServiceImpl implements DiamondsService {

    private static Logger LOG = LoggerFactory.getLogger(DiamondsService.class);

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private JewelryRepository jewelryRepository;

    @Autowired
    private AmazonS3Util amazonS3Util;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StatesRepository statesRepository;

    @Value("${aws.credentials.access.key}")
    private String s3AccessKey;

    @Value("${aws.credentials.secret.key}")
    private String s3SecretKey;

    @Value("${aws.region}")
    private String s3Region;

    @Override
    public List<ManufacturerDTO> getAllManufacturers() {
        List<Manufacturer> manufacturers = manufacturerRepository.findAll();
        List<ManufacturerDTO> toReturn = new ArrayList<>(manufacturers.size());

        for (Manufacturer manufacturer : manufacturers) {
            toReturn.add(ManufacturerConverter.convertEntityToDTO(manufacturer));
        }
        return toReturn;
    }

    @Override
    public JewelryDTO findByBarcode(String barcode) {
        Jewelry jewelry = jewelryRepository.findByBarcode(barcode);
        if (jewelry == null) {
            return null;
        }

        return JewelryConverter.convertEntityToDTO(jewelry);
    }

    @Override
    public List<JewelryDTO> findJewelryByManufacturer(long manufacturerId) {
        Optional<Manufacturer> manufacturerOptional = manufacturerRepository.findById(manufacturerId);
        Manufacturer manufacturer = null;
        if (manufacturerOptional.isEmpty()) {
           LOG.error("Manufacturer " + manufacturerId + " was not found!");
           return null;
        }
        manufacturer = manufacturerOptional.get();
        if (manufacturer.getReferencedManufacturer() != null) {
            LOG.info("Received a manufacturer with a reference of " + manufacturer.getReferencedManufacturer() + "; fetching it");
            manufacturerOptional = manufacturerRepository.findById(manufacturer.getReferencedManufacturer());
            manufacturer = manufacturerOptional.get();
        }
        LOG.info("Getting data of " + manufacturer.getName());
        List<Jewelry> jewelryList = jewelryRepository.findByManufacturer(manufacturer);
        if (jewelryList == null || jewelryList.size() == 0) {
            return null;
        } else {
            List<JewelryDTO> jewelryDTOList = new ArrayList<>(jewelryList.size());
            for (Jewelry jewelry : jewelryList) {
                jewelryDTOList.add(JewelryConverter.convertEntityToDTO(jewelry));
            }
            return jewelryDTOList;
        }
    }

    @Override
    public boolean addJewelry(JewelryDTO jewelryDTO, MultipartFile video) {
        try {
            Manufacturer manufacturer = manufacturerRepository.getOne(jewelryDTO.getManufacturer());
            String videoSuffix = extractFileSuffix(video.getOriginalFilename());
            String amazonS3Link = amazonS3Util.uploadObject(video.getInputStream(), video.getSize(), jewelryDTO.getBarcode() + videoSuffix, video.getContentType(), s3AccessKey, s3SecretKey, s3Region, manufacturer.getName());
            jewelryDTO.setVideoLink(amazonS3Link);
            Jewelry jewelry = jewelryRepository.save(JewelryConverter.convertDtoToEntity(jewelryDTO, manufacturer));
            if (jewelry != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addJewelry(String barcode, String customer, String videoUrl) throws CustomerNotFoundException, IOException {
        Jewelry jewelry = new Jewelry();
        jewelry.setBarcode(barcode);
        Manufacturer manufacturer = manufacturerRepository.findByName(customer);
        if (manufacturer == null) {
            throw new CustomerNotFoundException(customer);
        }
        String amazonUrl = uploadVideo(barcode, videoUrl, manufacturer);
        jewelry.setVideo(amazonUrl);
        jewelry.setManufacturer(manufacturer);
        jewelry.setCreationDate(new Date(System.currentTimeMillis()));
        Jewelry persistedJewelry = jewelryRepository.save(jewelry);
        if (persistedJewelry != null) {
            return true;
        }
        return false;
    }

    private byte[] downloadUrl(URL toDownload) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = toDownload.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outputStream.toByteArray();
    }

    public List<CustomerDTO> getAllCustomersByManufacturer(Long manufacturerId) {
        List<CustomerDTO> toReturn = new ArrayList<>();
        Manufacturer manufacturer = manufacturerRepository.getOne(manufacturerId);
        List<Customer> customers = customerRepository.getByManufacturerDateDesc(manufacturer.getId());
        if (customers != null && !customers.isEmpty()) {
            for (int i = 0; i < customers.size(); i++) {
                toReturn.add(CustomerConverter.convertEntityToDto(customers.get(i)));
            }
        }
        return toReturn;
    }

    @Override
    public boolean addCustomer(String name, String email, String phone, String barcode, String videoUrl, String username, Long manufacturerId) {
        CustomerDTO customer = new CustomerDTO();
        customer.setName(name);
        customer.setBarcode(barcode);
        customer.setEmail(email);
        customer.setPhone(phone);
        Jewelry jewelry = jewelryRepository.findByBarcode(barcode);
        if (videoUrl == null) {
            customer.setVideoUrl(jewelry.getVideo());
        } else {
            customer.setVideoUrl(videoUrl);
        }
        Manufacturer manufacturer = manufacturerRepository.getOne(manufacturerId);
        UserDTO user = userService.getUserDtoByUsernameAndManufacturer(username, manufacturer);
        customer.setUser(user);
        Store store = storeRepository.findByManufacturerAndName(manufacturer, user.getStore());
        Customer savedCustomer = customerRepository.save(CustomerConverter.convertDtoToEntity(customer, manufacturer, jewelry, store));
        return savedCustomer != null;
    }

    @Override
    public List<StateDTO> getAllStates() {
        Iterable<State> states =  statesRepository.findAll();
        List<StateDTO> toReturn = new ArrayList<>();
        Iterator<State> it = states.iterator();
        while (it.hasNext()) {
            toReturn.add(StatesConverter.convertEntityToDto(it.next()));
        }
        return toReturn;
    }

    @Override
    public List<StoreDTO> getStoresByState(Long manufacturerId, String state) {
        Manufacturer manufacturer = manufacturerRepository.getOne(manufacturerId);
        List<StoreDTO> toReturn  = new ArrayList<>();
        List<Store> stores = storeRepository.findByStateAndManufacturer(state, manufacturer);
        if (stores != null && !stores.isEmpty()) {
            for (int i = 0; i < stores.size(); i++) {
                toReturn.add(StoreConverter.convertEntityToDto(stores.get(i)));
            }
        }
        return toReturn;
    }

//    @Override
//    public void removeJewelleryVideoFromCloud(String barcode) {
//        amazonS3Util.removeVideo(barcode, s3AccessKey, s3SecretKey, s3Region);
//        LOG.info(barcode + " was removed");
//    }

    @Override
    public boolean addManufacturer(String manufacturerName) throws ManufacturerAlreadyExistsException {
        if (manufacturerRepository.findByName(manufacturerName) != null) {
            throw new ManufacturerAlreadyExistsException(manufacturerName);
        }
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(manufacturerName);
        if (manufacturerRepository.save(manufacturer) == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateJewelry(String barcode, String customer, String videoUrl) throws IOException, CustomerNotFoundException {
        Jewelry jewelry = jewelryRepository.findByBarcode(barcode);
        if (jewelry == null) {
            LOG.info("Jewelery was not found, adding a new one");
            return addJewelry(barcode, customer, videoUrl);
        }
        jewelry.setVideo(uploadVideo(barcode, videoUrl, jewelry.getManufacturer()));
        jewelry.setCreationDate(new Date(System.currentTimeMillis()));
        Jewelry persistedJewelry = jewelryRepository.save(jewelry);
        if (persistedJewelry != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean addStoreForLuna(StoreDTO storeDTO) throws StoreAlreadyExistsException {
        User contactPerson = null;
        if (storeDTO.getExternalId() == 0) {
            return false;
        }
        Manufacturer luna = manufacturerRepository.findByName("LunaCollection");
        if (storeDTO.getStoreManager() != null) {
           contactPerson  = userService.getUser(storeDTO.getStoreManager().getUsername());
        }
        LOG.debug("Adding the store as a single chain to manufacturer table");
        Manufacturer singleStoreChain = new Manufacturer();
        singleStoreChain.setName(storeDTO.getName());
        singleStoreChain.setReferencedManufacturer(luna.getId());
        singleStoreChain = manufacturerRepository.save(singleStoreChain);
        UserDTO user = cloneUser(storeDTO.getStoreManager());
        if (contactPerson == null) {
            user.setStore("-1");
            user.setRole("ADMIN");
            userService.addUSer(user, singleStoreChain.getId());
            contactPerson = userService.getUser(user.getUsername());
        }
        Store store = StoreConverter.convertDtoToEntity(storeDTO, contactPerson);

        if (storeRepository.findByExternalId(store.getExternalId()) == null) {
            store.setManufacturer(singleStoreChain);
            store = storeRepository.save(store);
            LOG.info("updating the store to the user");
            user.setStore(store.getId().toString());
            LOG.info("Updating the original password from input: " + storeDTO.getStoreManager().getPassword());
            user.setPassword(storeDTO.getStoreManager().getPassword());
            userService.updateUser(storeDTO.getStoreManager(), singleStoreChain.getId());
        } else {
            throw new StoreAlreadyExistsException();
        }

        return true;
    }

    private UserDTO cloneUser(UserDTO storeManager) {
        UserDTO toReturn = new UserDTO();
        toReturn.setPassword(storeManager.getPassword());
        toReturn.setFirstName(storeManager.getFirstName());
        toReturn.setLastName(storeManager.getLastName());
        toReturn.setUsername(storeManager.getUsername());
        toReturn.setRole(storeManager.getRole());
        toReturn.setEmail(storeManager.getEmail());
        return toReturn;
    }

    private String uploadVideo(String barcode, String videoUrl, Manufacturer manufacturer) throws MalformedURLException, FileNotFoundException {
        LOG.info("downloading video " + videoUrl);
        byte[] videoContent = downloadUrl(new URL(videoUrl));
        InputStream content = new ByteArrayInputStream(videoContent);
        String amazonS3Link = amazonS3Util.uploadObjectFromFirebase(content, barcode + ".mp4", "video/mp4", s3AccessKey, s3SecretKey, s3Region, manufacturer.getName());
        return amazonS3Link;
    }

    private String extractFileSuffix(String video) {
        int dotLocation = video.lastIndexOf('.');
        return video.substring(dotLocation);
    }


}
