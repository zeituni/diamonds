package com.ttc.diamonds.service;

import com.ttc.diamonds.dto.UserDTO;
import com.ttc.diamonds.model.User;
import com.ttc.diamonds.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean addUSer(UserDTO userDTO) {

        return false;
    }

    @Override
    public boolean deleteUser(String username) {
        return false;
    }

    @Override
    public List<User> getAllUsersPerStore(String storeName) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }
}
