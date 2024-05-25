<%@page import="com.example.model.Role"%>
<%@page import="com.example.model.UserRole"%>
<%@page import="com.example.service.UserRoleService"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="com.example.service.UserService"%>
<%@page import="com.example.model.Complain"%>
<%@page import="com.example.model.User"%>
<%@page import="com.example.model.Permission"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html;charset=UTF-8"%>

<!-- Manage Complaints Page -->
<div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">Manage Complaints</h1>

    <%
    User user = (User) request.getSession().getAttribute("loggedInUser");
    boolean isAdmin = user != null && user.isAdmin();
    Permission permission = (Permission) request.getAttribute("permission");
    %>

    <!-- Create Complaint Form -->
 

            <%
            if (isAdmin || (permission == null || permission.canCreate())) {
            %>
            <div class="bg-white p-6 rounded-lg shadow-md">
                <h2 class="text-xl font-semibold mb-4">Create Complaint</h2>
                <form action="manage_complains" method="post" class="space-y-4">
                    <div>
                        <label for="complainType" class="block text-sm font-medium text-gray-700">Complaint Type</label>
                        <input type="text" id="complainType" name="complainType" required class="mt-1 p-2 w-full border border-gray-300 rounded-md">
                    </div>
                    <div>
                        <label for="complainDescription" class="block text-sm font-medium text-gray-700">Description</label>
                        <textarea id="complainDescription" name="complainDescription" required class="mt-1 p-2 w-full border border-gray-300 rounded-md"></textarea>
                    </div>
                    <% if (isAdmin || ( permission!=null && permission.canCreate())) { %>
                    <div>
                        <label for="createdForUserId" class="block text-sm font-medium text-gray-700">Created For User ID</label>
                        <select id="createdForUserId" name="createdForUserId" class="mt-1 p-2 w-full border border-gray-300 rounded-md">
                            <% List<User> users = new UserService().findAllUsers();
                            if (users.isEmpty()) { %>
                            <option value="" disabled>No users available</option>
                            <% } else {
                                for (User u : users) { %>
                            <option value="<%=u.getId()%>"><%=u.getName()%> (<%=u.getEmail()%>)</option>
                            <% }
                            } %>
                        </select>
                    </div>
                    <% } %>
                    <div>
                        <button type="submit" name="action" value="create" class="bg-blue-500 text-white px-4 py-2 rounded-md">Create Complaint</button>
                    </div>
                </form>
            </div>
            <%
            }
            %>
      

    <!-- Complaints List -->
    <div class="bg-white p-6 rounded-lg shadow-md mt-6">
        <h2 class="text-xl font-semibold mb-4">Existing Complaints</h2>
        <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
                <tr>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Type</th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Description</th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Created On</th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                    <% if (isAdmin) { %>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Assigned to</th>
                    <% } %>
                    <% if (isAdmin || (permission != null && permission.getRole() != null)) { %>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Created By</th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Created For</th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider text-center">Actions</th>
                    <% } %>
                </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
                <%
                List<Complain> complains = (List<Complain>) request.getAttribute("complains");
                if (complains != null && complains.size() > 0){
                    for (Complain complain : complains) {
                        if (complain == null)
                            continue;
                %>
                <tr>
                    <td class="px-6 py-4 whitespace-nowrap"><%=complain.getId()%></td>
                    <td class="px-6 py-4 whitespace-nowrap"><%=complain.getComplainType()%></td>
                    <td class="px-6 py-4 whitespace-nowrap"><%=complain.getComplainDescription()%></td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <%
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime createdOnDateTime = complain.getCreatedOnDateTime();
                        String createdOn = createdOnDateTime.format(dateTimeFormatter);
                        out.println(createdOn);
                        %>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap"><%=complain.getStatus() %></td>
                    <% if (isAdmin) { %>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <% if (complain.getAssignedTo() != null) {
                            UserRoleService userRoleService = new UserRoleService();
                        %>
                        <div class="flex flex-col">
                            <span class="font-semibold"><%=complain.getAssignedTo().getName()%></span>
                            <span class="text-gray-500"><%=complain.getAssignedTo().getEmail()%></span>
                            <span class="text-gray-500"><%=complain.getAssignedTo().getPhone()%></span>
                            <span class="text-gray-500"><%=complain.getAssignedTo().getAddress()%></span>
                            <span class="text-gray-500">Role: <%=userRoleService.findUserRoleByUserId(complain.getAssignedTo().getId()).getRole().getRole()%></span>
                            <a href="mailto:<%=complain.getAssignedTo().getEmail()%>" class="text-blue-500">Send Email</a>
                        </div>
                        <% } else { %>
                        <button type="button" class="bg-blue-500 text-white px-4 py-2 rounded-md" onclick="assignComplain('<%=complain.getId()%>')">Assign To</button>
                        <% } %>
                    </td>
                    <% } %>
                    <% if (isAdmin || (permission != null && permission.getRole() != null)) { %>
                    <td class="px-6 py-4 whitespace-nowrap">
                            <div class="flex flex-col">
                                <span class="font-semibold"><%=complain.getCreatedByUser().getName()%></span>
                                <span class="text-gray-500"><%=complain.getCreatedByUser().getEmail()%></span>
                                <span class="text-gray-500"><%=complain.getCreatedByUser().getPhone()%></span>
                                <a href="mailto:<%=complain.getCreatedForUser().getEmail()%>" class="text-blue-500">Send Email</a>
                            </div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <div class="flex flex-col">
                            <span class="font-semibold"><%=complain.getCreatedForUser().getName()%></span>
                            <span class="text-gray-500"><%=complain.getCreatedForUser().getEmail()%></span>
                            <span class="text-gray-500"><%=complain.getCreatedForUser().getPhone()%></span>
                            <span class="text-gray-500"><%=complain.getCreatedForUser().getAddress()%></span>
                            <a href="mailto:<%=complain.getCreatedForUser().getEmail()%>" class="text-blue-500">Send Email</a>
                        </div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-right">
                        <% if (isAdmin || (permission != null && permission.canUpdate())) { %>
                        <button type="button" class="bg-blue-500 text-white px-4 py-2 rounded-md" onclick="updateComplain('<%=complain.getId()%>', '<%=complain.getComplainType()%>', '<%=complain.getComplainDescription()%>', '<%=complain.getStatus()%>')">
                            Update
                        </button>
                        <% } %>
                        <% if (isAdmin || (permission != null && permission.canDelete())) { %>
                        <form action="manage_complains" method="post" style="display: inline;">
                            <input type="hidden" name="complainId" value="<%=complain.getId()%>">
                            <button type="submit" class="bg-red-500 text-white px-4 py-2 rounded-md" name="action" value="delete">Delete</button>
                        </form>
                        <% } %>
                    </td>
                    <% } %>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <% if (isAdmin) { %>
                    <td colspan="9" class="px-6 py-4 text-center">No complaints available.</td>
                    <% } else if (permission != null && permission.getRole() != null) { %>
                    <td colspan="8" class="px-6 py-4 text-center">No complaints available.</td>
                    <% } else { %>
                    <td colspan="5" class="px-6 py-4 text-center">No complaints available.</td>
                    <% } %>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>
    </div>
</div>

<!-- Update Complaint Modal -->
<div id="updateComplainModal" class="fixed z-10 inset-0 overflow-y-auto hidden bg-black bg-opacity-50">
    <div class="flex items-center justify-center min-h-screen">
        <div class="relative bg-white w-1/3 p-8 rounded shadow-lg">
            <button class="absolute top-0 right-0 p-2" onclick="closeUpdateModal()">&times;</button>
            <h2 class="text-xl font-semibold mb-4">Update Complaint</h2>
            <form action="manage_complains" method="post" id="updateComplainForm" class="space-y-4">
                <input type="hidden" id="updateComplainId" name="complainId">
                <div>
                    <label for="updateComplainType" class="block text-sm font-medium text-gray-700">Complaint Type</label>
                    <input type="text" id="updateComplainType" name="complainType" required class="mt-1 p-2 w-full border border-gray-300 rounded-md">
                </div>
                <div>
                    <label for="updateComplainDescription" class="block text-sm font-medium text-gray-700">Description</label>
                    <textarea id="updateComplainDescription" name="complainDescription" required class="mt-1 p-2 w-full border border-gray-300 rounded-md"></textarea>
                </div>
                <div>
                    <label for="updateStatus" class="block text-sm font-medium text-gray-700">Status</label>
                    <input type="text" id="updateStatus" name="status" required class="mt-1 p-2 w-full border border-gray-300 rounded-md">
                </div>
                <div>
                    <label for="updateAssignUserId" class="block text-sm font-medium text-gray-700">Assign To User</label>
                    <select id="updateAssignUserId" name="assignUserId" class="mt-1 p-2 w-full border border-gray-300 rounded-md">
                        <% 
                        List<UserRole> userRoles = new UserRoleService().findAllUserRoles();
                        for (UserRole userRole : userRoles) {
                            User u = userRole.getUser();
                            Role r = userRole.getRole();
                        %>
                        <option value="<%=u.getId()%>"><%=u.getName()%> - <%=r.getRole()%></option>
                        <% } %>
                    </select>
                </div>
                <div>
                    <button type="submit" name="action" value="update" class="bg-blue-500 text-white px-4 py-2 rounded-md">Update Complaint</button>
                </div>
            </form>
        </div>
    </div>
</div>

    <!-- Assign Complaint Modal -->
    <div id="assignComplainModal" class="fixed z-10 inset-0 overflow-y-auto hidden bg-black bg-opacity-50">
        <div class="flex items-center justify-center min-h-screen">
            <div class="relative bg-white w-1/3 p-8 rounded shadow-lg">
                <button class="absolute top-0 right-0 p-2" onclick="closeAssignModal()">&times;</button>
                <h2 class="text-xl font-semibold mb-4">Assign Complaint</h2>
                <form action="manage_complains" method="post" id="assignComplainForm" class="space-y-4">
                    <input type="hidden" id="assignComplainId" name="complainId">
                    <div>
                        <label for="assignUserId" class="block text-sm font-medium text-gray-700">Assign To User</label>
                        <select id="assignUserId" name="assignUserId" required class="mt-1 p-2 w-full border border-gray-300 rounded-md">
                            <% 
                            for (UserRole userRole : userRoles) {
                                User u = userRole.getUser();
                                Role r = userRole.getRole();
                            %>
                            <option value="<%=u.getId()%>"><%=u.getName()%> - <%=r.getRole()%></option>
                            <% } %>
                        </select>
                    </div>
                    <div>
                        <button type="submit" name="action" value="assign" class="bg-blue-500 text-white px-4 py-2 rounded-md">Assign Complaint</button>
                    </div>
                </form>
            </div>
        </div>
    </div>


<!-- Include necessary scripts for modal -->
<script>
    function updateComplain(id, type, description, status) {
        document.getElementById("updateComplainId").value = id;
        document.getElementById("updateComplainType").value = type;
        document.getElementById("updateComplainDescription").value = description;
        document.getElementById("updateStatus").value = status;
        document.getElementById('updateComplainModal').classList.remove('hidden');
    }

    function closeUpdateModal() {
        document.getElementById('updateComplainModal').classList.add('hidden');
    }

    function assignComplain(id) {
            document.getElementById("assignComplainId").value = id;
            document.getElementById('assignComplainModal').classList.remove('hidden');
        }

        function closeAssignModal() {
            document.getElementById('assignComplainModal').classList.add('hidden');
        }
    </script>
</script>
