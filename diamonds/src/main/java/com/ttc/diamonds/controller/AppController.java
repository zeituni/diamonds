package com.ttc.diamonds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttc.diamonds.dto.JewelryDTO;
import com.ttc.diamonds.dto.ManufacturerDTO;
import com.ttc.diamonds.model.Jewelry;
import com.ttc.diamonds.repository.JewelryRepository;
import com.ttc.diamonds.service.DiamondsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/diamonds")
public class AppController {

    @Autowired
    private DiamondsService diamondsService;

    @RequestMapping(method = RequestMethod.GET, value = "/manufacturers")
    public ResponseEntity<List<ManufacturerDTO>> getManufacturers(){
        return new ResponseEntity<>(diamondsService.getAllManufacturers(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findJewelry")
    public ResponseEntity<JewelryDTO> findByBarcode(@RequestParam String barcode) {
        JewelryDTO dto = diamondsService.findByBarcode(barcode);
        if (dto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "findJewelryByManufacturer")
    public ResponseEntity<List<JewelryDTO>> findJewelryByManufacturer(@RequestParam long manufacturerId) {
        List<JewelryDTO> jewelryList = diamondsService.findJewelryByManufacturer(manufacturerId);
        if (jewelryList == null || jewelryList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(jewelryList, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "addJewelry")
    public ResponseEntity<String> addJewelry(@RequestParam("jewelryDto") String jewelry, @RequestParam("file")MultipartFile video) {
        ObjectMapper mapper = new ObjectMapper();
        JewelryDTO jewelryDTO = null;
        try {
            jewelryDTO = mapper.readValue(jewelry, JewelryDTO.class);
        } catch (IOException e) {
            return new ResponseEntity<>("Jewelry parsing problem!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (diamondsService.findByBarcode(jewelryDTO.getBarcode()) != null) {
            return new ResponseEntity<>("Jewelry with this barcode already exists!", HttpStatus.CONFLICT);
        }
        if (diamondsService.addJewelry(jewelryDTO, video)) {
            return new ResponseEntity<>("{\"result_text\": \"Jewelry added successfully\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{\"result_text\": \"Problem adding jewelry\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
