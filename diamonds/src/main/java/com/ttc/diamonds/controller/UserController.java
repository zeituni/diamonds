package com.ttc.diamonds.controller;

import com.ttc.diamonds.dto.UserDTO;
import com.ttc.diamonds.model.User;
import com.ttc.diamonds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAllUsersByManufacturer(@RequestParam Long manufacturerId) {
        return new ResponseEntity(userService.getAllUsers(manufacturerId), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getUsersByStore")
    public ResponseEntity<List<UserDTO>> getUsersByStore(@RequestParam Long manufacturerId, @RequestParam String storeName) {
        return new ResponseEntity(userService.getAllUsersPerStore(manufacturerId, storeName), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getUsersByCity")
    public ResponseEntity<List<UserDTO>> getUsersByCity(@RequestParam Long manufacturerId, @RequestParam String state, @RequestParam String city) {
        return new ResponseEntity(userService.getUsersByCity(manufacturerId, state, city), HttpStatus.OK);
    }
}
