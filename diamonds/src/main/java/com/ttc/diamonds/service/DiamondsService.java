package com.ttc.diamonds.service;

import com.ttc.diamonds.dto.*;
import com.ttc.diamonds.service.exception.CustomerNotFoundException;
import com.ttc.diamonds.service.exception.ManufacturerAlreadyExistsException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface DiamondsService {
    List<ManufacturerDTO> getAllManufacturers();

    JewelryDTO findByBarcode(String barcode);

    List<JewelryDTO> findJewelryByManufacturer(long manufacturerId);

    boolean addJewelry(JewelryDTO jewelryDTO, MultipartFile video);

    boolean addJewelry(String barcode, String customer, String videoUrl) throws CustomerNotFoundException, IOException;

    List<CustomerDTO> getAllCustomersByManufacturer(Long manufacturerId);

    boolean addCustomer(String name, String email, String phone, String barcode, String videoUrl, String salesPerson, Long manufacturer);

    List<StateDTO> getAllStates();

    List<StoreDTO> getStoresByState(Long manufacturer, String state);

//    void removeJewelleryVideoFromCloud(String barcode);

    boolean addManufacturer(String manufacturerName) throws ManufacturerAlreadyExistsException, ManufacturerAlreadyExistsException;

    boolean updateJewelry(String barcode, String customer, String url) throws IOException, CustomerNotFoundException;
}
