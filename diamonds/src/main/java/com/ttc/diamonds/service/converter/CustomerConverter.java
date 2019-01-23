package com.ttc.diamonds.service.converter;


import com.ttc.diamonds.dto.CustomerDTO;
import com.ttc.diamonds.model.Customer;
import com.ttc.diamonds.model.Jewelry;
import com.ttc.diamonds.model.Manufacturer;
import com.ttc.diamonds.model.Store;

public class CustomerConverter {

    public static Customer convertDtoToEntity(CustomerDTO dto, Manufacturer manufacturer, Jewelry jewelry, Store store) {
        Customer toReturn = new Customer();
        toReturn.setName(dto.getName());
        toReturn.setPhone(dto.getPhone());
        toReturn.setEmail(dto.getEmail());
        toReturn.setManufacturer(manufacturer);
        toReturn.setJewelry(jewelry);
        toReturn.setUser(UserConverter.convertDtoToEntity(dto.getUser(), manufacturer, store));
        return toReturn;
    }

    public static CustomerDTO convertEntityToDto(Customer entity) {
        CustomerDTO toReturn = new CustomerDTO();
        toReturn.setBarcode(entity.getJewelry().getBarcode());
        toReturn.setEmail(entity.getEmail());
        toReturn.setName(entity.getName());
        toReturn.setPhone(entity.getPhone());
        toReturn.setVideoUrl("https://s3.amazonaws.com/" + entity.getJewelry().getVideo());
        toReturn.setUser(UserConverter.convertEntityToDto(entity.getUser()));
        return toReturn;
    }
}
