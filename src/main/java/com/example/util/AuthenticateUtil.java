package com.example.util;

import com.example.model.User;
import com.example.service.UserService;

public class AuthenticateUtil {
        // Method to authenticate user (replace with your authentication logic)
    public static User authenticateUser(String email, String password) {
        // Example: Check against database using UserService
        UserService userService = new UserService();
        User user = userService.findUserByEmail(email);
         if(user != null && user.getPassword().equals(PasswordConverter.hashPassword(password)))
        	 return user; 
         else 
        	 return null;
    }
}
