
package com.example;

import com.example.model.User;
import com.example.service.UserService;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        
        // Create a fake user
        User adminUser = new User("Admin User", "admin@email.com", "Pass@123", "8954524524", "Vrindavan", true);
        
        userService.registerUser(adminUser);
        
        System.out.println("Admin user registered successfully.");
    }
}
