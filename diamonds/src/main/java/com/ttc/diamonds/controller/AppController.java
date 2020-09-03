package com.ttc.diamonds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.ttc.diamonds.dto.JewelryDTO;
import com.ttc.diamonds.dto.ManufacturerDTO;
import com.ttc.diamonds.service.DiamondsService;
import com.ttc.diamonds.service.exception.CustomerNotFoundException;
import com.ttc.diamonds.service.exception.ManufacturerAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/diamonds")
public class AppController {

    @Autowired
    private DiamondsService diamondsService;

    @CrossOrigin(origins = "http://${diamonds.host}")
    @RequestMapping(method = RequestMethod.GET, value = "/manufacturers")
    public ResponseEntity<List<ManufacturerDTO>> getManufacturers(){
        return new ResponseEntity<>(diamondsService.getAllManufacturers(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://${diamonds.host}")
    @RequestMapping(method = RequestMethod.GET, value = "/findJewelry")
    public ResponseEntity<JewelryDTO> findByBarcode(@RequestParam String barcode) {
        JewelryDTO dto = diamondsService.findByBarcode(barcode);
        if (dto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
    }

    @CrossOrigin(origins = "http://${diamonds.host}")
    @RequestMapping(method = RequestMethod.GET, value = "findJewelryByManufacturer")
    public ResponseEntity<List<JewelryDTO>> findJewelryByManufacturer(@RequestParam long manufacturerId) {
        List<JewelryDTO> jewelryList = diamondsService.findJewelryByManufacturer(manufacturerId);
        if (jewelryList == null || jewelryList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(jewelryList, HttpStatus.OK);
        }
    }


    @CrossOrigin(origins = "http://${diamonds.host}")
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

    @CrossOrigin(origins = "http://${diamonds.host}")
    @RequestMapping(method = RequestMethod.POST, value = "addCustomer")
    public ResponseEntity<String> addCustomer(@RequestParam("name") String name,
                                              @RequestParam("email") String email,
                                              @RequestParam("phone") String phone,
                                              @RequestParam("barcode") String barcode,
                                              @RequestParam("videoUrl") String videoUrl,
                                              @RequestParam("sales_person") String salesPerson,
                                              @RequestParam("manufacturer") Long manufacturer) {
        if (diamondsService.addCustomer(name, email, phone, barcode, videoUrl, salesPerson, manufacturer)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://${diamonds.host}")
    @RequestMapping(method = RequestMethod.POST, value = "/addHelzbergCustomer")
    public ResponseEntity<String> addHelzbergCustomer(@RequestParam("name") String name,
                                              @RequestParam("email") String email,
                                              @RequestParam("phone") String phone,
                                              @RequestParam("barcode") String barcode,
                                              @RequestParam("sales_person") String salesPerson) {
        if (diamondsService.addCustomer(name, email, phone, barcode, null, salesPerson, 1L)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.POST, value = "/addJewellery")
    public ResponseEntity<String> addJewellery(@RequestBody String parameters) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, String> params = mapper.readValue(parameters, Map.class);
            if (Strings.isNullOrEmpty(params.get("customer")) || params.get("customer").equalsIgnoreCase("NONE")) {
                return new ResponseEntity<>("{\"result_text\": \"Customer is empty or None, doing nothing\"}", HttpStatus.OK);
            }
            if (Strings.isNullOrEmpty(params.get("barcode"))) {
                return new ResponseEntity<>("{\"result_text\": \"Jewellery barcode is missing!\"}", HttpStatus.BAD_REQUEST);
            }
            if (diamondsService.findByBarcode(params.get("barcode")) != null) {
                return new ResponseEntity<>("{\"result_text\": \"Jewellery with this barcode already exists!\"}", HttpStatus.CONFLICT);
            }
            if (params.get("barcode").startsWith("DIS")) {
                return new ResponseEntity<>("{\"result_text\": \"Currently Ignoring DIS!\"}", HttpStatus.BAD_REQUEST);
            } else {
                diamondsService.addJewelry(params.get("barcode"), params.get("customer"), params.get("url"));
            }
            return new ResponseEntity<>("{\"result_text\": \"Jewellery " + params.get("barcode") + " wad added successfully\"}", HttpStatus.OK);
        } catch (CustomerNotFoundException | IOException e) {
            return new ResponseEntity<>("{\"result_text\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.POST, value = "/addNewCustomer")
    public ResponseEntity<String> addCustomer(@RequestBody String parameters) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, String> params = mapper.readValue(parameters, Map.class);
            if (Strings.isNullOrEmpty(params.get("customer")) || params.get("customer").equalsIgnoreCase("NONE")) {
                return new ResponseEntity<>("{\"result_text\": \"Customer is empty or None, doing nothing\"}", HttpStatus.BAD_REQUEST);
            }
            diamondsService.addManufacturer(params.get("customer"));

            return new ResponseEntity<>("{\"result_text\": \"Customer " + params.get("customer") + " wad added successfully\"}", HttpStatus.OK);
        } catch (ManufacturerAlreadyExistsException | IOException e) {
            return new ResponseEntity<>("{\"result_text\": \"" + e.getMessage() + "\"}", HttpStatus.CONFLICT);
        }
    }
}
