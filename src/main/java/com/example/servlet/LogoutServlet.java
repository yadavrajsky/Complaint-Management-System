package com.example.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Call doGet method for logout
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Invalidate the session, effectively logging out the user
        	 request.getSession(false).removeAttribute("loggedInUser");
            request.getSession(false).invalidate();
            
            // Redirect to the login page
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (Exception e) {
            // Handle exceptions, if any
            // e.printStackTrace();
            // You can also forward to an error page if needed
            // request.getRequestDispatcher("/views/error.jsp").forward(request, response);
            request.setAttribute("errorMessage", e.toString());
            request.setAttribute("body", "/views/dashboard.jsp");
            request.getRequestDispatcher("/views/base.jsp").forward(request, response);
        }
    }
}
