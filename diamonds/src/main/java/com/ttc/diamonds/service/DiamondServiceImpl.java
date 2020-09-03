package com.ttc.diamonds.service;

import com.ttc.diamonds.dto.*;
import com.ttc.diamonds.model.*;
import com.ttc.diamonds.repository.*;
import com.ttc.diamonds.service.converter.*;
import com.ttc.diamonds.service.exception.ManufacturerAlreadyExistsException;
import com.ttc.diamonds.service.exception.CustomerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        Manufacturer manufacturer = manufacturerRepository.getOne(manufacturerId);
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
    public boolean addJewelry(String barcode, String customer, String videoUrl) throws CustomerNotFoundException {
        Jewelry jewelry = new Jewelry();
        jewelry.setBarcode(barcode);
        jewelry.setVideo(videoUrl);
        Manufacturer manufacturer = manufacturerRepository.findByName(customer);
        if (manufacturer == null) {
            throw new CustomerNotFoundException(customer);
        }
        jewelry.setManufacturer(manufacturer);
        jewelry.setCreationDate(new Date(System.currentTimeMillis()));
        Jewelry persistedJewelry = jewelryRepository.save(jewelry);
        if (persistedJewelry != null) {
            return true;
        }
        return false;
    }


    public List<CustomerDTO> getAllCustomersByManufacturer(Long manufacturerId) {
        List<CustomerDTO> toReturn = new ArrayList<>();
        Manufacturer manufacturer = manufacturerRepository.getOne(manufacturerId);
        List<Customer> customers = customerRepository.findByManufacturer(manufacturer);
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

    @Override
    public void removeJewelleryVideoFromCloud(String barcode) {
        amazonS3Util.removeVideo(barcode, s3AccessKey, s3SecretKey, s3Region);
        LOG.info(barcode + " was removed");
    }

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

    private String extractFileSuffix(String video) {
        int dotLocation = video.lastIndexOf('.');
        return video.substring(dotLocation);
    }


}
