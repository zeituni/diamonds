package com.ttc.diamonds.service.converter;

import com.ttc.diamonds.dto.UserDTO;
import com.ttc.diamonds.model.Manufacturer;
import com.ttc.diamonds.model.Store;
import com.ttc.diamonds.model.User;

public class UserConverter {

    public static User convertDtoToEntity(UserDTO dto, Manufacturer manufacturer, Store store) {
        User toReturn = new User();
        if (dto.getId() != null) {
            toReturn.setId(dto.getId());
        }
        toReturn.setFirstName(dto.getFirstName());
        toReturn.setLastName(dto.getLastName());
        toReturn.setManufacturer(manufacturer);
        toReturn.setUsername(dto.getUsername());
        toReturn.setPassword(dto.getPassword());
        toReturn.setStore(store);
        toReturn.setRole(dto.getRole());
        return toReturn;
    }

    public static UserDTO convertEntityToDto(User entity) {
        UserDTO dto = new UserDTO();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setStore(entity.getStore().getName());
        return dto;
    }
}
