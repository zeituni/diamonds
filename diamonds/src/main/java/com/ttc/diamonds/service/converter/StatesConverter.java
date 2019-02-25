package com.ttc.diamonds.service.converter;

import com.ttc.diamonds.dto.StateDTO;
import com.ttc.diamonds.model.State;

public class StatesConverter {

    public static StateDTO convertEntityToDto(State entity) {
        StateDTO toReturn = new StateDTO();
        toReturn.setAbbr(entity.getAbbr());
        toReturn.setName(entity.getName());
        return toReturn;
    }
}
