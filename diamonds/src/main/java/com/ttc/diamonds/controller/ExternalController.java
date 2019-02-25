package com.ttc.diamonds.controller;

import com.ttc.diamonds.dto.CustomerDTO;
import com.ttc.diamonds.dto.StateDTO;
import com.ttc.diamonds.dto.StoreDTO;
import com.ttc.diamonds.service.DiamondsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/external")
public class ExternalController {

    @Autowired
    private DiamondsService diamondsService;

    @CrossOrigin(origins = "http://${diamonds.host}:81")
    @RequestMapping(method = RequestMethod.GET, value = "/getCustomersByManufacturer")
    public ResponseEntity<List<CustomerDTO>> findCustomersByManufacturer(@RequestParam long manufacturerId) {
        List<CustomerDTO> customersList = diamondsService.getAllCustomersByManufacturer(manufacturerId);
        return new ResponseEntity<>(customersList, HttpStatus.OK);

    }

    @CrossOrigin(origins = "http://${diamonds.host}:81")
    @RequestMapping(method = RequestMethod.GET, value = "/getAllStates")
    public ResponseEntity<List<StateDTO>> getAllStates() {
        return new ResponseEntity<>(diamondsService.getAllStates(), HttpStatus.OK);

    }

    @CrossOrigin(origins = "http://${diamonds.host}:81")
    @RequestMapping(method = RequestMethod.GET, value = "/getStoresByState")
    public ResponseEntity<List<StoreDTO>> getStoresByState(@RequestParam("manufacturer") long manufacturerId,
                                                                      @RequestParam("state") String state) {
        List<StoreDTO> storesList = diamondsService.getStoresByState(manufacturerId, state);
        return new ResponseEntity<>(storesList, HttpStatus.OK);

    }
}
