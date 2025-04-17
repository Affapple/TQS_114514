package ies.carbox.api.RestAPI.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ies.carbox.api.RestAPI.entity.User;
import ies.carbox.api.RestAPI.repository.UserRepository;

/**
 * userService
 */
/*
    * This class is responsible for handling the user's account
 */
@Service
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    CacheService cacheService;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, CacheService cacheService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cacheService = cacheService;
        this.passwordEncoder = passwordEncoder;
    }

    public Boolean belongsToUser(String ecuId, String userEmail) {
        User user = loadUserByUsername(userEmail);
        return user.getCarsList().stream().anyMatch(car -> car.get(0).equals(ecuId));

    }

    // ! Maybe correct this later to be email and not username
    // May not be necessary this method
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        // Check if in cache
        User user = cacheService.getUser(email);
        if (user != null)
            return user;

        try {
            user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                    String.format("User with userId=\"%s\" not found", email)
                ));
            
            cacheService.saveUser(user);
            return user;
        } catch (UsernameNotFoundException e) {
            System.out.println("INFO: User \"" + email + "\" not found");
        } catch (Exception e) {
            System.out.println("ERROR: Error fetching user from database!");
            e.printStackTrace();
        }
        return null;
    }

    public List<List<String>> getListOfEcuIds(String userEmail) {
        User user = loadUserByUsername(userEmail);
        return user.getCarsList();
    }

    public User removeUserCar(String userEmail, String carId) {
        User user = loadUserByUsername(userEmail);
        userRepository.deleteByEmail(user.getEmail());
        
        List<List<String>> carList = user.getCarsList();
        for (List<String> car : carList) {
            if (car.get(0).equals(carId)) {
                carList.remove(car);
                break;
            }
        }
        user.setCarsList(carList);
        
        // Update cache
        userRepository.save(user);
        cacheService.saveUser(user);
        return user;
    }

    public User addUserCar(String userEmail, String vehicleId, String vehicleName) {
        User user = loadUserByUsername(userEmail);

        userRepository.deleteByEmail(user.getEmail());
        List<List<String>> carList = user.getCarsList();
        List<String> car = new ArrayList<>();
        car.add(vehicleId);
        car.add(vehicleName);
        carList.add(car);

        user.setCarsList(carList);

        // Update cache
        userRepository.save(user);
        cacheService.saveUser(user);
        return user;
    }

    public void delUser(User user) {
        userRepository.delete(user);
        cacheService.deleteUser(user.getEmail());
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUserDetails(String email, User updatedUser) {
        try {
            User existingUser = loadUserByUsername(email);
            if (existingUser == null) {
                throw new UsernameNotFoundException("User not found: " + email);
            }

            updatedUser.set_id(existingUser.get_id());
            // Update only the necessary fields
            if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
            if (updatedUser.getName() != null) existingUser.setName(updatedUser.getName());
            if (updatedUser.getPhone() != 0) existingUser.setPhone(updatedUser.getPhone());
            if (updatedUser.getPassword() != null) existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            if (updatedUser.getCarsList() != null) existingUser.setCarsList(updatedUser.getCarsList());
    
            userRepository.save(existingUser);
    
            cacheService.saveUser(existingUser);
    
            return existingUser;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating user details");
        }
    }

//     public List<User> getAllUsersWithCars() {
//     return userRepository.findAllWithCars(); // Custom query to fetch users and their cars
// }

}