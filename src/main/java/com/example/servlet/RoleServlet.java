package com.example.servlet;

import java.io.IOException;

import com.example.model.Role;
import com.example.model.User;
import com.example.service.RoleService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name ="/RoleServlet", value = "/roles")
public class RoleServlet extends HttpServlet {
    private final RoleService roleService = new RoleService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "delete":
                    handleDeleteRole(request, response);
                    break;
                case "edit":
                    handleEditRole(request, response);
                    break;
                default:
                    handleAddRole(request, response);
                    break;
            }
        } else {
            handleAddRole(request, response);
        }
    }

    private void handleAddRole(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String roleName = request.getParameter("role");
        Role role = new Role(roleName);
        roleService.registerRole(role);
        request.setAttribute("successMessage", "Role registered successfully.");
        doGet(request, response);
    }

    private void handleDeleteRole(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long roleId = Long.valueOf(request.getParameter("roleId"));
        Role role = roleService.findRoleById(roleId);
        if (role != null) {
            roleService.deleteRole(role);
            request.setAttribute("successMessage", "Role deleted successfully.");
        } else {
            request.setAttribute("errorMessage", "Role not found.");
        }
        doGet(request, response);
    }

    private void handleEditRole(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long roleId = Long.valueOf(request.getParameter("roleId"));
        String newRoleName = request.getParameter("role");
        Role role = roleService.findRoleById(roleId);
        if (role != null) {
            role.setRole(newRoleName);
            roleService.updateRole(role);
            request.setAttribute("successMessage", "Role updated successfully.");
        } else {
            request.setAttribute("errorMessage", "Role not found.");
        }
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // if a user is not logged in, redirect to login page
        User user = (User) request.getSession().getAttribute("loggedInUser");
        if (user == null) {
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        } else {

            // if a user is logged in and is not an admin, redirect to dashboard
            if (!user.isAdmin()) {
                response.sendRedirect("/dashboard");
                return;
            }
        }

        // Redirect GET requests to the roles management page
        request.setAttribute("title", "Roles");
        request.setAttribute("body", "/views/role.jsp");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/base.jsp");
        dispatcher.forward(request, response);
    }
}
