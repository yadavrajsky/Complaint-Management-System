package com.example.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.model.Complain;
import com.example.model.Permission;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.service.ComplainService;
import com.example.service.PermissionService;
import com.example.service.UserRoleService;
import com.example.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ComplainServlet", value = "/manage_complains")
public class ComplainServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final ComplainService complainService = new ComplainService();
    private final UserRoleService userRoleService = new UserRoleService();
    private final PermissionService permissionService = new PermissionService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("loggedInUser") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            User user = (User) session.getAttribute("loggedInUser");
            boolean isAdmin = user.isAdmin();
            Permission permission = getUserPermission(user);
            request.setAttribute("permission", permission);
            String action = request.getParameter("action");

            if (action != null) {
                switch (action) {
                    case "create":
                        if (isAdmin || permission == null || permission.canCreate()) {
                            handleCreateComplain(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You do not have permission to create complaints.");
                        }
                        break;
                    case "update":
                        if (isAdmin || (permission!=null && permission.canUpdate())) {
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
                        if (isAdmin || (permission!=null && permission.canDelete())) {
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
        String  searchQuery = request.getParameter("search");

            boolean isAdmin = user.isAdmin();
            Permission permission = getUserPermission(user);
            if (isAdmin)
                complains = complainService.findAllComplains(searchQuery);
            else if (permission!=null && permission.canView())
                complains = complainService.findComplainsAssignedToUserId(user.getId(), searchQuery);
            else
                complains = complainService.findComplainsCreatedByUserId(user.getId(), searchQuery);
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
        if (complainType == null || complainType.isEmpty()) {
            request.setAttribute("errorMessage", "Complain type is required.");
            doGet(request, response);
            return;
        } else if (complainDescription == null || complainDescription.isEmpty()) {
            request.setAttribute("errorMessage", "Complain description is required.");
            doGet(request, response);
            return;
        }

        LocalDateTime createdDateTime = LocalDateTime.now();
        Permission permission = (Permission) request.getAttribute("permission");
        // Get the logged-in user
        User createdByUser = (User) request.getSession().getAttribute("loggedInUser");
        UUID createdForuserId = null;
        User createdForUser = null;
        if (permission != null && (permission.canCreate() || createdByUser.isAdmin())) {
            // Check if the user ID is valid (UUID)
            createdForuserId = UUID.fromString(request.getParameter("createdForUserId"));
            if (createdForuserId == null) {
                request.setAttribute("errorMessage", "Invalid user.");
                doGet(request, response);
                return;
            }
        } else {
            createdForuserId = createdByUser.getId();
        }
        createdForUser = userService.findUserById(createdForuserId);
        if (createdForUser == null) {
            request.setAttribute("errorMessage", "Invalid user.");
            doGet(request, response);
            return;
        }

        Complain complain = new Complain(complainType, complainDescription, createdByUser, createdDateTime, null,
                "Open", createdForUser);
        complainService.createComplain(complain);
        request.setAttribute("successMessage", "Complain created successfully.");
        doGet(request, response);
    }

    private void handleUpdateComplain(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UUID complainId = UUID.fromString(request.getParameter("complainId"));
        String complainType = request.getParameter("complainType");
        String complainDescription = request.getParameter("complainDescription");
        String status = request.getParameter("status");
        UUID assignUserId = UUID.fromString(request.getParameter("assignUserId"));

        Complain complain = complainService.findComplainById(complainId);
        if (complain != null) {
            complain.setComplainType(complainType);
            complain.setComplainDescription(complainDescription);
            complain.setStatus(status);

            if (assignUserId != null && !assignUserId.toString().isEmpty()) {
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
        UUID complainId = UUID.fromString(request.getParameter("complainId"));
        UUID assignUserId = UUID.fromString(request.getParameter("assignUserId"));

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
        UUID complainId = UUID.fromString(request.getParameter("complainId"));

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
        return null; // default permission if none found
    }
}
