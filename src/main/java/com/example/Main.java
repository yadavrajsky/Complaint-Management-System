
package com.example;

import com.example.model.User;
import com.example.service.UserService;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        
        // Create a fake user
        User fakeUser = new User("Fake User", "fake@example.com", "fakepassword", "1234567890", "Fake Address", false);
        
        userService.registerUser(fakeUser);
        
        System.out.println("Fake user registered successfully.");
    }
}
