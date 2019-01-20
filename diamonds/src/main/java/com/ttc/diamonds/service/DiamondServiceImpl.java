package com.ttc.diamonds.service;

import com.ttc.diamonds.dto.CustomerDTO;
import com.ttc.diamonds.dto.JewelryDTO;
import com.ttc.diamonds.dto.ManufacturerDTO;
import com.ttc.diamonds.model.Customer;
import com.ttc.diamonds.model.Jewelry;
import com.ttc.diamonds.model.Manufacturer;
import com.ttc.diamonds.repository.CustomerRepository;
import com.ttc.diamonds.repository.JewelryRepository;
import com.ttc.diamonds.repository.ManufacturerRepository;
import com.ttc.diamonds.service.converter.CustomerConverter;
import com.ttc.diamonds.service.converter.JewelryConverter;
import com.ttc.diamonds.service.converter.ManufacturerConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    private String extractFileSuffix(String video) {
        int dotLocation = video.lastIndexOf('.');
        return video.substring(dotLocation);
    }


}
