package com.example.servlet;

import java.io.IOException;

import com.example.model.User;
import com.example.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Check if user is already authenticated
            if (request.getSession().getAttribute("loggedInUser") != null) {
                // Redirect to dashboard
                request.getRequestDispatcher("/dashboard").forward(request, response);
                return;
            }
            // Retrieve user input from the registration form
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            boolean isAdmin = Boolean.parseBoolean(request.getParameter("isAdmin"));
            // throw new RuntimeException("User with the provided email already exists.");

            // Create a new UserService instance
            UserService userService = new UserService();

            // Check if the user already exists
            User existingUser = userService.findUserByEmail(email);
            if (existingUser != null) {
                throw new Exception("User with the provided email already exists.");
            }

            // Create a new user instance
            User newUser = new User(name, email, password, phone, address, isAdmin);
            // Register the user
            userService.registerUser(newUser);

            // Set success message as an attribute
            request.setAttribute("successMessage", "User registered successfully.");

            // Optionally, redirect the user to a login page
            request.setAttribute("title", "Login Page");
            request.setAttribute("body", "/views/login.jsp");
            request.getRequestDispatcher("/views/base.jsp").forward(request, response);
        } catch (Exception e) {
            // Set error message as an attribute
            request.setAttribute("errorMessage", e.getMessage());
            // Redirect the user back to the registration form
            request.setAttribute("title", "Register Page");
            request.setAttribute("body", "/views/register.jsp");
            request.getRequestDispatcher("/views/base.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET requests to the registration form page
        request.setAttribute("title", "Register Page");
        request.setAttribute("body", "/views/register.jsp");
        request.getRequestDispatcher("/views/base.jsp").forward(request, response);

    }
}
