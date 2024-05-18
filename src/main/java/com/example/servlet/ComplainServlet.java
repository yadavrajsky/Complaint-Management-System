package com.example.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.example.model.Complain;
import com.example.model.Permission;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.service.ComplainService;
import com.example.service.PermissionService;
import com.example.service.RoleService;
import com.example.service.UserRoleService;
import com.example.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ComplainServlet", value = "/manage_complains")
public class ComplainServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final ComplainService complainService = new ComplainService();
    private final UserRoleService userRoleService = new UserRoleService();
    private final RoleService roleService = new RoleService();
    private final PermissionService permissionService = new PermissionService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = (User) request.getSession(false).getAttribute("loggedInUser");
            if (user == null) {
                request.getRequestDispatcher("/login").forward(request, response);
                return;
            }
            boolean isAdmin = user.isAdmin();
            Permission permission = getUserPermission(user);

            String action = request.getParameter("action");

            if (action != null) {
                switch (action) {
                    case "create":
                        if (isAdmin || permission.canCreate()) {
                            handleCreateComplain(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You do not have permission to create complaints.");
                        }
                        break;
                    case "update":
                        if (isAdmin || permission.canUpdate()) {
                            handleUpdateComplain(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You do not have permission to update complaints.");
                        }
                        break;
                    case "assign":
                        if (isAdmin) {
                            handleAssignComplain(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You do not have permission to assign complaints.");
                        }
                        break;
                    case "delete":
                        if (isAdmin || permission.canDelete()) {
                            handleDeleteComplain(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You do not have permission to delete complaints.");
                        }
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
        List<Complain> complains = null;

        try {
            User user = (User) request.getSession().getAttribute("loggedInUser");
            if (user == null) {
                request.getRequestDispatcher("/login").forward(request, response);
                return;
            }
            boolean isAdmin = user.isAdmin();
            Permission permission = getUserPermission(user);

            if (isAdmin)
                complains = complainService.findAllComplains();
            else if (permission.canView())
                complains = complainService.findComplainsAssignedToUserId(user.getId());
            else
                complains = complainService.findComplainsCreatedByUserId(user.getId());
            request.setAttribute("complains", complains);
            request.setAttribute("permission", permission);
            request.setAttribute("title", "Manage Complains");
            request.setAttribute("body", "/views/manage_complains.jsp");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/base.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.setAttribute("body", "/views/manage_complains.jsp");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/base.jsp");
            dispatcher.forward(request, response);

        }
    }

    private void handleCreateComplain(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String complainType = request.getParameter("complainType");
        String complainDescription = request.getParameter("complainDescription");
        LocalDateTime createdDateTime = LocalDateTime.now();
        User createdByUser = (User) request.getSession().getAttribute("loggedInUser");
        String createdForuserId = request.getParameter("createdForUserId");
        User createdForUser = null;
        if (createdByUser.isAdmin()) {
            createdForUser = userService.findUserById(Long.valueOf(createdForuserId));
            if (createdForUser == null) {
                request.setAttribute("errorMessage", "Invalid user.");
                doGet(request, response);
                return;
            }
        } else {
            createdForUser = createdByUser;
        }

        Complain complain = new Complain(complainType, complainDescription, createdByUser, createdDateTime, null,
                "Open", createdForUser);
        complainService.createComplain(complain);
        request.setAttribute("successMessage", "Complain created successfully.");
        doGet(request, response);
    }

    private void handleUpdateComplain(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
Long complainId = Long.valueOf(request.getParameter("complainId"));
String complainType = request.getParameter("complainType");
String complainDescription = request.getParameter("complainDescription");
String status = request.getParameter("status");
String assignUserIdParam = request.getParameter("assignUserId");

Complain complain = complainService.findComplainById(complainId);
if (complain != null) {
    complain.setComplainType(complainType);
    complain.setComplainDescription(complainDescription);
    complain.setStatus(status);

    if (assignUserIdParam != null && !assignUserIdParam.isEmpty()) {
        Long assignUserId = Long.valueOf(assignUserIdParam);
        User assignedUser = new UserService().findUserById(assignUserId);
        if (assignedUser != null) {
            complain.setAssignedTo(assignedUser);
        } else {
            request.setAttribute("errorMessage", "Assigned User not found.");
            doGet(request, response);
            return;
        }
    }

    complainService.updateComplain(complain);
    request.setAttribute("successMessage", "Complain updated successfully.");
} else {
    request.setAttribute("errorMessage", "Complain not found.");
}
doGet(request, response);
}


    private void handleAssignComplain(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long complainId = Long.valueOf(request.getParameter("complainId"));
        Long assignUserId = Long.valueOf(request.getParameter("assignUserId"));

        Complain complain = complainService.findComplainById(complainId);
        User assignedUser = new UserService().findUserById(assignUserId);

        if (complain != null && assignedUser != null) {
            complain.setAssignedTo(assignedUser);
            complainService.updateComplain(complain);
            request.setAttribute("successMessage", "Complain assigned successfully.");
        } else {
            request.setAttribute("errorMessage", "Complain or User not found.");
        }
        doGet(request, response);
    }

    private void handleDeleteComplain(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long complainId = Long.valueOf(request.getParameter("complainId"));
        Complain complain = complainService.findComplainById(complainId);
        if (complain != null) {
            complainService.deleteComplain(complain);
            request.setAttribute("successMessage", "Complain deleted successfully.");
        } else {
            request.setAttribute("errorMessage", "Complain not found.");
        }
        doGet(request, response);
    }

    private Permission getUserPermission(User user) {
        UserRole userRole = userRoleService.findUserRoleByUserId(user.getId());
        if (userRole != null) {
            Role role = userRole.getRole();
            return permissionService.findPermissionByRoleId(role.getId());
        }
        return new Permission(null, false, false, false, false); // default permission if none found
    }
}
