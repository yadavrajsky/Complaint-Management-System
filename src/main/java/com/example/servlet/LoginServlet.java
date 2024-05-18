package com.example.servlet;

import java.io.IOException;

import com.example.model.User;
import com.example.util.AuthenticateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
        try {
            // Check if user is already authenticated
            if (request.getSession(false).getAttribute("loggedInUser") != null) {
                // Redirect to dashboard
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }
            // Authenticate user using UserService or any authentication service
        	User user=AuthenticateUtil.authenticateUser(email, password);
            if (user!=null) {
                // Set user session
                request.getSession().setAttribute("loggedInUser", user);
                // Redirect to dashboard 
                response.sendRedirect(request.getContextPath() + "/dashboard");

            } else {
                // If authentication fails, set error message
                request.setAttribute("errorMessage", "Invalid email or password");
                // Forward back to login page
                request.setAttribute("body", "/views/login.jsp");
                request.setAttribute("email", email);
                request.setAttribute("password", password);
                request.getRequestDispatcher("/views/base.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Handle exceptions
            // e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred "+e.toString());
            request.setAttribute("body", "/views/login.jsp");
            request.setAttribute("email", email);
            request.setAttribute("password", password);
            request.getRequestDispatcher("/views/base.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET requests to the registration form page
        request.setAttribute("title", "Login Page");
        request.setAttribute("body", "/views/login.jsp");
        request.getRequestDispatcher("/views/base.jsp").forward(request, response);

    }
}
