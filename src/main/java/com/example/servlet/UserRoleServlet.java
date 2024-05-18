package com.example.servlet;

import java.io.IOException;
import java.util.List;

import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.service.RoleService;
import com.example.service.UserRoleService;
import com.example.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UserRoleServlet", value = "/assign_user_roles")
public class UserRoleServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final RoleService roleService = new RoleService();
    private final UserRoleService userRoleService = new UserRoleService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // if a user is not logged in, redirect to login page
        try {
            User user = (User) request.getSession(false).getAttribute("loggedInUser");
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
            String action = request.getParameter("action");

            if (action != null) {
                switch (action) {
                    case "assign":
                        handleAssignRole(request, response);
                        break;
                    case "update":
                        handleUpdateRole(request, response);
                        break;
                    case "delete":
                        handleDeleteRole(request, response);
                        break;
                    default:
                        request.setAttribute("errorMessage", "Invalid action.");
                        doGet(request, response);
                        break;
                }
            }
        } catch (Exception e) {
            // Handle any exceptions here
            // e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            doGet(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // if a user is not logged in, redirect to login page
        try {
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

            List<User> users = userService.findAllUsers();
            List<Role> roles = roleService.findAllRoles();
            List<UserRole> userRoles = userRoleService.findAllUserRoles();

            // Remove users who already have a role assigned
            for (UserRole userRole : userRoles) {
                users.removeIf(u -> u.getId().equals(userRole.getUser().getId()));
            }

            request.setAttribute("users", users);
            request.setAttribute("roles", roles);
            request.setAttribute("userRoles", userRoles);

            request.setAttribute("title", "Assign Roles");
            request.setAttribute("body", "/views/assign_user_roles.jsp");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/base.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            // Handle any exceptions here
            // e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void handleAssignRole(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long userId = Long.valueOf(request.getParameter("userId"));
        Long roleId = Long.valueOf(request.getParameter("roleId"));

        User user = userService.findUserById(userId);
        Role role = roleService.findRoleById(roleId);

        if (user != null && role != null) {
            if (userRoleService.findUserRoleByUserId(userId) != null) {
                request.setAttribute("errorMessage", "User already has a role assigned.");
                doGet(request, response);
                return;
            }
            UserRole userRole = new UserRole(user, role);

            userRoleService.assignRole(userRole);
            request.setAttribute("successMessage", "Role assigned successfully.");
        } else {
            request.setAttribute("errorMessage", "Invalid user or role.");
        }
        doGet(request, response);
    }

    private void handleUpdateRole(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long userId = Long.valueOf(request.getParameter("userId"));
        Long roleId = Long.valueOf(request.getParameter("roleId"));
        UserRole userRole = userRoleService.findUserRoleByUserId(userId);
        Role role = roleService.findRoleById(roleId);
        if (userRole != null && role != null) {
            userRole.setRole(roleService.findRoleById(roleId));
            userRoleService.updateUserRole(userRole);
            request.setAttribute("successMessage", "Role updated successfully.");

        } else {
            request.setAttribute("errorMessage", "Invalid user or role.");
        }
        doGet(request, response);
    }

    private void handleDeleteRole(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long userId = Long.valueOf(request.getParameter("userId"));
        Long roleId = Long.valueOf(request.getParameter("userRoleId"));
        UserRole userRole = userRoleService.findUserRoleByUserIdAndRoleId(userId, roleId);
        if (userRole == null) {
            request.setAttribute("errorMessage", "User accociated with the Role not found.");
            doGet(request, response);
            return;
        }
        userRoleService.deleteUserRole(userRole);
        request.setAttribute("successMessage", "Role deleted successfully.");
        doGet(request, response);
    }

}
