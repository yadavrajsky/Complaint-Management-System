<%@page import="java.util.stream.Collectors"%>
<%@page import="com.example.model.Permission"%>
<%@ page import="com.example.model.Role" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!-- Manage Permissions Page -->
<div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">Manage Permissions</h1>

    <!-- Create Permission Form -->
    <div class="bg-white p-6 rounded-lg shadow-md">
        <h2 class="text-xl font-semibold mb-4">Create Permission</h2>
        <form action="permissions" method="post" class="space-y-4">
            <div>
                <label for="roleId" class="block text-sm font-medium text-gray-700">Role</label>
                <select id="roleId" name="roleId" required class="mt-1 p-2 w-full border border-gray-300 rounded-md">
                    <%
                    List<Permission> permissions = (List<Permission>) request.getAttribute("permissions");
                        List<Role> roles = ((List<Role>) request.getAttribute("roles")).stream()
                            .filter(role -> permissions.stream()
                                .noneMatch(permission -> permission.getRole().getId() == role.getId()))
                            .collect(Collectors.toList());
                    %>

                    <% if (roles.isEmpty()) { %>
                        <option value="" disabled>No roles available</option>
                    <% } else { %>
                        <% for (Role role : roles) { %>
                            <option value="<%= role.getId() %>"><%= role.getRole() %></option>
                        <% } %>
                    <% } %>
                </select>
            </div>
            <div>
                <label for="canView" class="block text-sm font-medium text-gray-700">Can View</label>
                <input type="checkbox" id="canView" name="canView" class="mt-1" value="true">
            </div>
            <div>
                <label for="canCreate" class="block text-sm font-medium text-gray-700">Can Create</label>
                <input type="checkbox" id="canCreate" name="canCreate" class="mt-1" value="true">
            </div>
            <div>
                <label for="canUpdate" class="block text-sm font-medium text-gray-700">Can Update</label>
                <input type="checkbox" id="canUpdate" name="canUpdate" class="mt-1" value="true">
            </div>
            <div>
                <label for="canDelete" class="block text-sm font-medium text-gray-700">Can Delete</label>
                <input type="checkbox" id="canDelete" name="canDelete" class="mt-1" value="true">
            </div>
            <div>
                <button type="submit" name="action" value="create" class="bg-blue-500 text-white px-4 py-2 rounded-md">Create Permission</button>
            </div>
        </form>
    </div>

    <!-- Permissions List -->
    <div class="bg-white p-6 rounded-lg shadow-md mt-6">
        <h2 class="text-xl font-semibold mb-4">Existing Permissions</h2>
        <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
                <tr>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Role
                    </th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Can View
                    </th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Can Create
                    </th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Can Update
                    </th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Can Delete
                    </th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider text-right">
                        Actions
                    </th>
                </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">

                <% if (permissions.isEmpty()) { %>
                    <tr>
                        <td colspan="6" class="px-6 py-4 whitespace-nowrap text-center">No permissions available</td>
                    </tr>
                <% } else { %>
                    <% for (Permission permission : permissions) { %>
                        <tr>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <%= permission.getRole().getRole() %>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <%= permission.canView() ? "Yes" : "No" %>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <%= permission.canCreate() ? "Yes" : "No" %>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <%= permission.canUpdate() ? "Yes" : "No" %>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <%= permission.canDelete() ? "Yes" : "No" %>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-right">
                                <button type="button" class="bg-blue-500 text-white px-4 py-2 rounded-md" onclick="updatePermission('<%= permission.getRole().getId() %>','<%= permission.canView() %>','<%= permission.canCreate() %>','<%= permission.canUpdate() %>','<%= permission.canDelete() %>')">
                                    Update
                                </button>
                                <form action="permissions" method="post" style="display:inline;">
                                    <input type="hidden" name="roleId" value="<%= permission.getRole().getId() %>">
                                    <button type="submit" class="bg-red-500 text-white px-4 py-2 rounded-md" name="action" value="delete">Delete</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                <% } %>
            </tbody>
        </table>
    </div>
</div>

<!-- Update Permission Modal -->
<div id="updatePermissionModal" class="fixed z-10 inset-0 overflow-y-auto hidden bg-black bg-opacity-50">
    <div class="flex items-center justify-center min-h-screen">
        <div class="relative bg-white w-1/3 p-8 rounded shadow-lg">
            <button class="absolute top-0 right-0 p-2" onclick="closeUpdateModal()">&times;</button>
            <h2 class="text-xl font-semibold mb-4">Update Permission</h2>
            <form action="permissions" method="post" id="updatePermissionForm" class="space-y-4">
                <input type="hidden" id="updateRoleId" name="roleId">
                <div>
                    <label for="updateCanView" class="block text-sm font-medium text-gray-700">Can View</label>
                    <input type="checkbox" id="updateCanView" name="canView" class="mt-1">
                </div>
                <div>
                    <label for="updateCanCreate" class="block text-sm font-medium text-gray-700">Can Create</label>
                    <input type="checkbox" id="updateCanCreate" name="canCreate" class="mt-1">
                </div>
                <div>
                    <label for="updateCanUpdate" class="block text-sm font-medium text-gray-700">Can Update</label>
                    <input type="checkbox" id="updateCanUpdate" name="canUpdate" class="mt-1">
                </div>
                <div>
                    <label for="updateCanDelete" class="block text-sm font-medium text-gray-700">Can Delete</label>
                    <input type="checkbox" id="updateCanDelete" name="canDelete" class="mt-1">
                </div>
                <div>
                    <button type="submit" name="action" value="update" class="bg-blue-500 text-white px-4 py-2 rounded-md">Update Permission</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Include necessary scripts for modal -->
<script>
    function updatePermission(roleId, canView, canCreate, canUpdate, canDelete) {
        document.getElementById("updateRoleId").value = roleId;
        document.getElementById("updateCanView").checked = canView === "true"; 
        document.getElementById("updateCanCreate").checked = canCreate === "true"; 
        document.getElementById("updateCanUpdate").checked = canUpdate === "true"; 
        document.getElementById("updateCanDelete").checked = canDelete === "true"; 
        
        document.getElementById('updatePermissionModal').classList.remove('hidden');
    }

    function closeUpdateModal() {
        document.getElementById('updatePermissionModal').classList.add('hidden');
    }
</script>
