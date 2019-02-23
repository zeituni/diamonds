package com.ttc.diamonds.controller;

import com.ttc.diamonds.dto.UserDTO;
import com.ttc.diamonds.model.Manufacturer;
import com.ttc.diamonds.model.Store;
import com.ttc.diamonds.model.User;
import com.ttc.diamonds.repository.ManufacturerRepository;
import com.ttc.diamonds.repository.StoreRepository;
import com.ttc.diamonds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(method = RequestMethod.POST, value = "/addUser")
    public ResponseEntity<Boolean> addUser(@RequestBody UserDTO user, @RequestParam Long manufacturerId) {
        return new ResponseEntity(userService.addUSer(user, manufacturerId), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteUser")
    public ResponseEntity<Boolean> deleteUser(@RequestParam String username, @RequestParam Long manufacturerId) {
        Boolean success = userService.deleteUser(username, manufacturerId);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/updateUser")
    public ResponseEntity<Boolean> updateUser(@RequestBody UserDTO user, @RequestParam Long manufacturerId) {
        Boolean success = userService.updateUser(user, manufacturerId);
        return new ResponseEntity(success, HttpStatus.OK);
    }
}
