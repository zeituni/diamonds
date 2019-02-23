package com.ttc.diamonds.service;

import com.ttc.diamonds.dto.UserDTO;
import com.ttc.diamonds.model.Manufacturer;
import com.ttc.diamonds.model.Store;
import com.ttc.diamonds.model.User;
import com.ttc.diamonds.repository.ManufacturerRepository;
import com.ttc.diamonds.repository.StoreRepository;
import com.ttc.diamonds.repository.UserRepository;
import com.ttc.diamonds.service.converter.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean addUSer(UserDTO userDTO, Long manufacturerId) {
        Manufacturer manufacturer = manufacturerRepository.getOne(manufacturerId);
        Store store = storeRepository.findByManufacturerAndName(manufacturer, userDTO.getStore());
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if (userRepository.findByUsernameAndManufacturer(userDTO.getUsername(), manufacturer) != null) {
            return false;
        }
        User user = userRepository.save(UserConverter.convertDtoToEntity(userDTO, manufacturer, store));
        return user != null;

    }

    @Override
    public boolean deleteUser(String username, Long manufacturerId) {
        Manufacturer manufacturer = manufacturerRepository.getOne(manufacturerId);
        User user = userRepository.findByUsernameAndManufacturer(username, manufacturer);
        if (user != null) {
            userRepository.delete(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<User> getAllUsersPerStore(Long manufacturerId, String storeName) {
        return userRepository.getUsersByStore(manufacturerId, storeName);
    }

    @Override
    public List<User> getAllUsers(Long manufacturer) {
        return userRepository.getAllManufacturerUsers(manufacturer);
    }

    @Override
    public List<User> getUsersByCity(Long manufacturerId, String state, String city) {
        return userRepository.getUsersByCity(manufacturerId, state, city);
    }

    @Override
    public UserDTO getUserDtoByUsernameAndManufacturer(String username, Manufacturer manufacturer) {
        User user = userRepository.findByUsernameAndManufacturer(username, manufacturer);
        if (user != null) {
            return UserConverter.convertEntityToDto(user);
        }
        return null;
    }

    @Override
    public boolean updateUser(UserDTO userDto, Long manufacturerId) {
        Manufacturer manufacturer = manufacturerRepository.getOne(manufacturerId);
        User user = userRepository.findByUsernameAndManufacturer(userDto.getUsername(), manufacturer);
        if (user == null) {
            return false;
        }
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Store store = storeRepository.findByManufacturerAndName(manufacturer, userDto.getStore());
        user.setStore(store);
        userRepository.save(user);
        return true;
    }


}
