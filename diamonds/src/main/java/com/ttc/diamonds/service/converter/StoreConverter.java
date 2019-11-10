package com.ttc.diamonds.service.converter;

import com.ttc.diamonds.dto.StoreDTO;
import com.ttc.diamonds.model.Store;

public class StoreConverter {

    public static Store convertDtoToEntity(StoreDTO dto) {
        Store toReturn = new Store();
        toReturn.setCity(dto.getCity());
        toReturn.setId(dto.getId());
        toReturn.setName(dto.getName());
        toReturn.setState(dto.getState());
        toReturn.setLatitude(dto.getLatitude());
        toReturn.setLongitude(dto.getLongitude());

        return toReturn;
    }

    public static StoreDTO convertEntityToDto(Store store) {
        StoreDTO toReturn = new StoreDTO();

        toReturn.setCity(store.getCity());
        toReturn.setId(store.getId());
        toReturn.setName(store.getName());
        toReturn.setState(store.getState());
        toReturn.setLatitude(store.getLatitude());
        toReturn.setLongitude(store.getLongitude());

        return toReturn;

    }
}
