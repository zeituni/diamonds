package com.ttc.diamonds.service;

import com.ttc.diamonds.dto.UserDTO;
import com.ttc.diamonds.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User getUser(String username);

    boolean addUSer(UserDTO userDTO);

    boolean deleteUser(String username);

    List<User> getAllUsersPerStore(String storeName);

    List<User> getAllUsers();

}
