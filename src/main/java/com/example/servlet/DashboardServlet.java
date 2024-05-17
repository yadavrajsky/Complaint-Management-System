
package com.example.servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "DashboardServlet", value = "/dashboard")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve user input from the registration form
        doGet(request, response);
        

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // if a user is not logged in, redirect to login page 
        if (request.getSession().getAttribute("loggedInUser") == null) {
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }
        // Redirect GET requests to the dashboard page
        request.setAttribute("title", "Dashboard");
        request.setAttribute("body", "/views/dashboard.jsp");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/base.jsp");
        dispatcher.forward(request, response);

    }
}
