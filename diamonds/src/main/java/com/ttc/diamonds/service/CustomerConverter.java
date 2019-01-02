package com.ttc.diamonds.service;


import com.ttc.diamonds.dto.CustomerDTO;
import com.ttc.diamonds.model.Customer;
import com.ttc.diamonds.model.Jewelry;
import com.ttc.diamonds.model.Manufacturer;

public class CustomerConverter {

    public static Customer convertDtoToEntity(CustomerDTO dto, Manufacturer manufacturer, Jewelry jewelry) {
        Customer toReturn = new Customer();
        toReturn.setName(dto.getName());
        toReturn.setPhone(dto.getPhone());
        toReturn.setEmail(dto.getEmail());
        toReturn.setManufacturer(manufacturer);
        toReturn.setJewelry(jewelry);
        return toReturn;
    }

    public static CustomerDTO convertEntityToDto(Customer entity) {
        CustomerDTO toReturn = new CustomerDTO();
        toReturn.setBarcode(entity.getJewelry().getBarcode());
        toReturn.setEmail(entity.getEmail());
        toReturn.setName(entity.getName());
        toReturn.setPhone(entity.getPhone());
        toReturn.setVideoUrl("https://s3.amazonaws.com/" + entity.getJewelry().getVideo());
        return toReturn;
    }
}
