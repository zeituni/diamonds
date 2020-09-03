package com.ttc.diamonds.service;

import com.ttc.diamonds.dto.*;
import com.ttc.diamonds.service.exception.ManufacturerAlreadyExistsException;
import com.ttc.diamonds.service.exception.CustomerNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DiamondsService {
    List<ManufacturerDTO> getAllManufacturers();

    JewelryDTO findByBarcode(String barcode);

    List<JewelryDTO> findJewelryByManufacturer(long manufacturerId);

    boolean addJewelry(JewelryDTO jewelryDTO, MultipartFile video);

    boolean addJewelry(String barcode, String customer, String videoUrl) throws CustomerNotFoundException;

    List<CustomerDTO> getAllCustomersByManufacturer(Long manufacturerId);

    boolean addCustomer(String name, String email, String phone, String barcode, String videoUrl, String salesPerson, Long manufacturer);

    List<StateDTO> getAllStates();

    List<StoreDTO> getStoresByState(Long manufacturer, String state);

//    void removeJewelleryVideoFromCloud(String barcode);

    boolean addManufacturer(String manufacturerName) throws ManufacturerAlreadyExistsException, ManufacturerAlreadyExistsException;
}
