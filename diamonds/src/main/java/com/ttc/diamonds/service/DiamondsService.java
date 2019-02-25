package com.ttc.diamonds.service;

import com.ttc.diamonds.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DiamondsService {
    List<ManufacturerDTO> getAllManufacturers();

    JewelryDTO findByBarcode(String barcode);

    List<JewelryDTO> findJewelryByManufacturer(long manufacturerId);

    boolean addJewelry(JewelryDTO jewelryDTO, MultipartFile video);

    List<CustomerDTO> getAllCustomersByManufacturer(Long manufacturerId);

    boolean addCustomer(String name, String email, String phone, String barcode, String videoUrl, String salesPerson, Long manufacturer);

    List<StateDTO> getAllStates();

    List<StoreDTO> getStoresByState(Long manufacturer, String state);
}
