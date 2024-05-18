
package com.example.servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "AboutServlet", value = "/about")
public class AboutServlet extends HttpServlet  {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve user input from the registration form
        doGet(request, response);
        

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET requests to the dashboard page
        request.setAttribute("title", "About US");
        request.setAttribute("body", "/views/about.jsp");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/base.jsp");
        dispatcher.forward(request, response);

    }
}
