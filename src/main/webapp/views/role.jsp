<%@ page import="com.example.model.Role"%>
<%@ page import="com.example.service.RoleService"%>
<%@ page import="java.util.List"%>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<!-- Role Management Page -->
<div class="container mx-auto p-4">
	<h1 class="text-2xl font-bold mb-4">Manage Roles</h1>

	<!-- Add Role Form -->
	<div class="bg-white p-6 rounded-lg shadow-md mb-6">
		<h2 class="text-xl font-semibold mb-4">Add New Role</h2>
		<form action="roles" method="post" class="space-y-4">
			<div>
				<label for="role" class="block text-sm font-medium text-gray-700">Role
					Name</label> <input type="text" id="role" name="role" required
					class="mt-1 p-2 w-full border border-gray-300 rounded-md">
			</div>
			<div>
				<button type="submit"
					class="bg-blue-500 text-white px-4 py-2 rounded-md">Add
					Role</button>
			</div>
		</form>
	</div>
	<!-- Update Role Modal -->
	<div id="updateRoleModal"
		class="fixed z-10 inset-0 overflow-y-auto hidden bg-black bg-opacity-50">
		<div class="flex items-center justify-center min-h-screen">
			<div class="relative bg-white w-1/3 p-8 rounded shadow-lg">
				<button class="absolute top-0 right-0 p-2"
					onclick="closeUpdateModal()">&times;</button>
				<h2 class="text-xl font-semibold mb-4">Update Role</h2>
				<form action="roles" method="post" id="updateRoleForm"
					class="space-y-4">
					<div>
						<input type="hidden" name="roleId" id="updateRoleId"> <label
							for="updateRole" class="block text-sm font-medium text-gray-700">Role
							Name</label> <input type="text" id="updateRole" name="role" required
							class="mt-1 p-2 w-full border border-gray-300 rounded-md">
					</div>
					<div>
						<button type="submit" name="action" value="edit"
							class="bg-blue-500 text-white px-4 py-2 rounded-md">Update
							Role</button>
					</div>
				</form>
			</div>
		</div>
	</div>


	<!-- Roles List -->
	<div class="bg-white p-6 rounded-lg shadow-md">
		<h2 class="text-xl font-semibold mb-4">Existing Roles</h2>
		<table class="min-w-full divide-y divide-gray-200">
			<thead class="bg-gray-50">
				<tr>
					<th scope="col"
						class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
						Role Name</th>
					<th scope="col"
						class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider text-right">
						Actions</th>
				</tr>
			</thead>
			<tbody class="bg-white divide-y divide-gray-200">
				<%
				RoleService roleService = new RoleService();
				List<Role> roles = roleService.findAllRoles();
				if (roles == null || roles.isEmpty()) {
				%>
				<tr>
					<td colspan="2" class="px-6 py-4 text-center">No roles found.</td>
				</tr>
				<%
				} else {
				for (Role role : roles) {
				%>
				<tr>
					<td class="px-6 py-4 whitespace-nowrap"><%=role.getRole()%>

					</td>
					<td class="px-6 py-4 whitespace-nowrap text-right">
						<form action="roles" method="post" class="inline-block">
							<input type="hidden" name="roleId" value="<%=role.getId()%>">
							<button type="submit" name="action" value="delete"
								class="text-red-600 hover:text-red-900">Delete</button>
						</form>
						<button
							onclick="openUpdateModal('<%=role.getId()%>', '<%=role.getRole()%>')"
							class="text-blue-600 hover:text-blue-900">Edit</button>
					</td>
				</tr>
				<%
				}
				}
				%>
			</tbody>
		</table>
	</div>
</div>
<script>
	// JavaScript functions to handle update modal
	function openUpdateModal(roleId, roleName) {
		document.getElementById('updateRole').value = roleName;
		document.getElementById('updateRoleId').value = roleId;
		document.getElementById('updateRoleModal').classList.remove('hidden');
	}

	function closeUpdateModal() {
		document.getElementById('updateRoleModal').classList.add('hidden');
	}
</script>
