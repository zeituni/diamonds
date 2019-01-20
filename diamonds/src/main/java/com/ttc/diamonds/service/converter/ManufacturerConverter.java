package com.ttc.diamonds.service.converter;

import com.ttc.diamonds.dto.ManufacturerDTO;
import com.ttc.diamonds.model.Manufacturer;

public class ManufacturerConverter {

    public static ManufacturerDTO convertEntityToDTO(Manufacturer manufacturer) {
        ManufacturerDTO dto = new ManufacturerDTO();
        dto.setId(manufacturer.getId());
        dto.setName(manufacturer.getName());
        return dto;
    }
}
