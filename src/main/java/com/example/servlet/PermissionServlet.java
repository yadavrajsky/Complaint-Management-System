package com.example.servlet;

import java.io.IOException;
import java.util.List;

import com.example.model.Permission;
import com.example.model.Role;
import com.example.model.User;
import com.example.service.PermissionService;
import com.example.service.RoleService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PermissionServlet", value = "/permissions")
public class PermissionServlet extends HttpServlet {
    private final PermissionService permissionService = new PermissionService();
    private final RoleService roleService = new RoleService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = (User) request.getSession(false).getAttribute("loggedInUser");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            if (!user.isAdmin()) {
                response.sendRedirect(request.getContextPath()+"/dashboard");
                return;
            }
            String action = request.getParameter("action");

            if (action != null) {
                switch (action) {
                    case "create":
                        handleCreatePermission(request, response);
                        break;
                    case "update":
                        handleUpdatePermission(request, response);
                        break;
                    case "delete":
                        handleDeletePermission(request, response);
                        break;
                    default:
                        request.setAttribute("errorMessage", "Invalid action.");
                        doGet(request, response);
                        break;
                }
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            doGet(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute("loggedInUser");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            if (!user.isAdmin()) {
                response.sendRedirect(request.getContextPath()+ "/dashboard");
                return;
            }

            List<Role> roles = roleService.findAllRoles();
            List<Permission> permissions = permissionService.findAllPermissions();

            request.setAttribute("roles", roles);
            request.setAttribute("permissions", permissions);
            request.setAttribute("title", "Manage Permissions");
            request.setAttribute("body", "/views/manage_permissions.jsp");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/base.jsp");
            dispatcher.forward(request, response);
        
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.setAttribute("title", "Manage Permissions");
            request.setAttribute("body", "/views/manage_permissions.jsp");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/base.jsp");
            dispatcher.forward(request, response);
           
        }
        
    }

    private void handleCreatePermission(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long roleId = Long.valueOf(request.getParameter("roleId"));
            boolean canView = Boolean.parseBoolean(request.getParameter("canView"));
            boolean canCreate = Boolean.parseBoolean(request.getParameter("canCreate"));
            boolean canUpdate = Boolean.parseBoolean(request.getParameter("canUpdate"));
            boolean canDelete = Boolean.parseBoolean(request.getParameter("canDelete"));

            Role role = roleService.findRoleById(roleId);

            if (role != null) {
                permissionService.createPermission(new Permission(role, canView, canCreate, canUpdate, canDelete));
                request.setAttribute("successMessage", "Permission created successfully.");
            } else {
                request.setAttribute("errorMessage", "Invalid role.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid input.");
        }
        doGet(request, response);
    }

    private void handleUpdatePermission(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long roleId = Long.valueOf(request.getParameter("roleId"));
            boolean canView = request.getParameter("canView") != null;
            boolean canCreate = request.getParameter("canCreate") != null;
            boolean canUpdate = request.getParameter("canUpdate") != null;
            boolean canDelete = request.getParameter("canDelete") != null;

            Permission permission = permissionService.findPermissionByRoleId(roleId);

            if (permission != null) {
                permission.setCanView(canView);
                permission.setCanCreate(canCreate);
                permission.setCanUpdate(canUpdate);
                permission.setCanDelete(canDelete);
                permissionService.updatePermission(permission);
                request.setAttribute("successMessage", "Permission updated successfully.");
            } else {
                request.setAttribute("errorMessage", "Invalid permission.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid input.");
        }
        doGet(request, response);
    }

    private void handleDeletePermission(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long roleId = Long.valueOf(request.getParameter("roleId"));
            Permission permission = permissionService.findPermissionByRoleId(roleId);

            if (permission != null) {
                permissionService.deletePermission(permission);
                request.setAttribute("successMessage", "Permission deleted successfully.");
            } else {
                request.setAttribute("errorMessage", "Invalid permission.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid input.");
        }
        doGet(request, response);
    }
}
