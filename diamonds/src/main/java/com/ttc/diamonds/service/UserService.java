package com.ttc.diamonds.service;

import com.ttc.diamonds.dto.UserDTO;
import com.ttc.diamonds.model.Manufacturer;
import com.ttc.diamonds.model.User;

import java.util.List;


public interface UserService {

    User getUser(String username);

    boolean addUSer(UserDTO userDTO, Long manufacturerId);

    boolean deleteUser(String username, Long manufactirerId);

    List<User> getAllUsersPerStore(Long manufacturerId, String storeName);

    List<User> getAllUsers(Long manufacturer);

    List<User> getUsersByCity(Long manufacturerId, String state, String city);

    UserDTO getUserDtoByUsernameAndManufacturer(String username, Manufacturer manufacturer);

    boolean updateUser(UserDTO user, Long manufacturerId);
}
