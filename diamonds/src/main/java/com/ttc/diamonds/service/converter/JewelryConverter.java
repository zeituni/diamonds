package com.ttc.diamonds.service.converter;

import com.ttc.diamonds.dto.JewelryDTO;
import com.ttc.diamonds.model.Jewelry;
import com.ttc.diamonds.model.Manufacturer;

import java.sql.Date;

public class JewelryConverter {
    public static JewelryDTO convertEntityToDTO(Jewelry jewelry) {
        JewelryDTO dto = new JewelryDTO();
        dto.setId(jewelry.getId());
        dto.setBarcode(jewelry.getBarcode());
        dto.setAdditionalInfo(jewelry.getAdditionalInfo());
        dto.setManufacturer(jewelry.getManufacturer().getId());
        dto.setCreationDate(jewelry.getCreationDate());
        dto.setVideoLink("https://s3.amazonaws.com/" + jewelry.getVideo());
        return dto;
    }

    public static Jewelry convertDtoToEntity(JewelryDTO jewelryDTO, Manufacturer manufacturer) {
        Jewelry jewelry = new Jewelry();
        jewelry.setBarcode(jewelryDTO.getBarcode());
        jewelry.setAdditionalInfo(jewelryDTO.getAdditionalInfo());
        jewelry.setVideo(jewelryDTO.getVideoLink());
        jewelry.setManufacturer(manufacturer);
        jewelry.setCreationDate(new Date(System.currentTimeMillis()));
        return jewelry;
    }
}
