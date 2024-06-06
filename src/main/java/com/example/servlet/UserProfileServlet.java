package com.example.servlet;

import java.io.IOException;

import com.example.model.User;
import com.example.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/update_profile")
public class UserProfileServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("loggedInUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("user", user);

        request.setAttribute("title", "Update Profile");

        request.setAttribute("body", "/views/update_profile.jsp");
        request.getRequestDispatcher("/views/base.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute("loggedInUser");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            user.setName(name);
            user.setEmail(email);
            user.setPhone(phone);
            user.setAddress(address);

            userService.updateUser(user);
            request.getSession().setAttribute("loggedInUser", user);

            request.setAttribute("successMessage", "User updated successfully.");

            request.setAttribute("title", "Update Profile");

            request.setAttribute("body", "/views/update_profile.jsp");
            request.getRequestDispatcher("/views/base.jsp").forward(request, response);
        } catch (Exception e) {
            // Set error message as an attribute
            request.setAttribute("errorMessage", e.getMessage());
            // Redirect the user back to the registration form
            request.setAttribute("title", "Update Profile");
            request.setAttribute("body", "/views/update_profile.jsp");
            request.getRequestDispatcher("/views/base.jsp").forward(request, response);
        }
    }
}
