package com.ttc.diamonds.controller;

import com.ttc.diamonds.dto.CustomerDTO;
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
}
