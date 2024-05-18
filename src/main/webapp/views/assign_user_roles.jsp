<%@ page import="com.example.model.User" %>
<%@ page import="com.example.model.Role" %>
<%@ page import="com.example.model.UserRole" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!-- Assign Roles Page -->
<div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">Assign Roles to Users</h1>

    <!-- Assign Role Form -->
    <div class="bg-white p-6 rounded-lg shadow-md">
        <h2 class="text-xl font-semibold mb-4">Assign Role</h2>
    <form action="assign_user_roles" method="post" class="space-y-4">
            <div>
                <label for="userId" class="block text-sm font-medium text-gray-700">User</label>
                <select id="userId" name="userId" required class="mt-1 p-2 w-full border border-gray-300 rounded-md">
                    <%
                        List<User> users = (List<User>) request.getAttribute("users");
                        if (users.isEmpty()) {
                    %>
                    <option value="" disabled selected>No Users found</option>
                    <% } else {
                        for (User user : users) {
                    %>
                    <option value="<%= user.getId() %>"><%= user.getName() %> (<%= user.getEmail() %>)</option>
                    <% }
                    } %>
                </select>
                </select>
            </div>
            <div>
                <label for="roleId" class="block text-sm font-medium text-gray-700">Role</label>
                <select id="roleId" name="roleId" required class="mt-1 p-2 w-full border border-gray-300 rounded-md">
                    <%
                        List<Role> roles = (List<Role>) request.getAttribute("roles");
                        if (roles.isEmpty()) {
                    %>
                    <option value="" disabled selected>No Roles found</option>
                    <% } else {
                        for (Role role : roles) {
                    %>
                    <option value="<%= role.getId() %>"><%= role.getRole() %></option>
                    <% }
                    } %>
                </select>
            </div>
            <div>
                <button type="submit" name="action" value="assign" class="bg-blue-500 text-white px-4 py-2 rounded-md">Assign Role</button>
            </div>
        </form>
    </div>

    <!-- Assigned Roles List -->
    <div class="bg-white p-6 rounded-lg shadow-md mt-6">
        <h2 class="text-xl font-semibold mb-4">Assigned Roles</h2>
        <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
                <tr>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        User
                    </th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Role
                    </th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider text-right">
                        Actions
                    </th>
                </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
                <%
                        List<UserRole> userRoles = (List<UserRole>) request.getAttribute("userRoles");
                        if (userRoles.isEmpty()) {
                    %>
                    <tr>
                        <td colspan="3" class="px-6 py-4 whitespace-nowrap text-center">
                            No users associated with some role found
                        </td>
                    </tr>
                    <% } else {
                        for (UserRole userRole : userRoles) {
                    %>
                    <tr>
                        <td class="px-6 py-4 whitespace-nowrap">
                            <%= userRole.getUser().getName() %> (<%= userRole.getUser().getEmail() %>)
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap">
                            <%= userRole.getRole().getRole() %>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-right">
                            <button type="button" class="bg-blue-500 text-white px-4 py-2 rounded-md" onclick="updateUpdatedModalFormFields('<%= userRole.getUser().getId() %>', '<%= userRole.getUser().getName() %>')">
                                Update
                            </button>
                            <form action="assign_user_roles" method="post" style="display:inline;">
                                <input type="hidden" name="userId" value="<%= userRole.getUser().getId() %>">
                                <input type="hidden" name="userRoleId" value="<%= userRole.getRole().getId() %>">
                                <button type="submit" class="bg-red-500 text-white px-4 py-2 rounded-md" name="action" value="delete">Delete</button>
                            </form>
                        </td>
                    </tr>
                    <% }
                    } %>
            </tbody>
        </table>
    </div>
</div>

<!-- Update Role Modal -->
<div id="updateRoleModal" class="fixed z-10 inset-0 overflow-y-auto hidden bg-black bg-opacity-50">
    <div class="flex items-center justify-center min-h-screen">
        <div class="relative bg-white w-1/3 p-8 rounded shadow-lg">
            <button class="absolute top-0 right-0 p-2" onclick="closeUpdateModal()">&times;</button>
            <h2 class="text-xl font-semibold mb-4">Update Role</h2>
            <form action="assign_user_roles" method="post" id="updateUserRoleForm" class="space-y-4">
                    <div>
                        <input type="hidden" name="userId" id="updatedUserId">
                        <div>
                            <label for="updateRole" class="block text-sm font-medium text-gray-700">User Name</label>
                            <div id="updatedUserName"></div>
                        </div>
                        <div>

                            <label for="updatedRoleId" class="block text-sm font-medium text-gray-700">New Role</label>
                            <select id="updatedRoleId" name="roleId" required class="mt-1 p-2 w-full border border-gray-300 rounded-md">
                                <%
                                    for (Role role : roles) {
                                %>
                                <option value="<%= role.getId() %>"><%= role.getRole() %></option>
                                <% } %>
                            </select>
                        </div>
                    </div>
                    <div>
                        <button type="submit" name="action" value="update" class="bg-blue-500 text-white px-4 py-2 rounded-md">Update Role</button>
                    </div>
            </form>
        </div>
    </div>
</div>

<!-- Include necessary scripts for modal -->
<script>
   function updateUpdatedModalFormFields(userId, roleName) {
       document.getElementById("updatedUserId").value = userId;
       document.getElementById("updatedUserName").innerHTML = roleName;
       document.getElementById('updateRoleModal').classList.remove('hidden');
   }


function closeUpdateModal() {
  document.getElementById('updateRoleModal').classList.add('hidden');
}

</script>
