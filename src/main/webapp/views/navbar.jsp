<%@ page import="com.example.model.User"%>
<%@ page import="com.example.model.UserRole"%>
<%@ page import="com.example.service.UserService"%>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Objects"%>
<% response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); %>
<% response.setHeader("Pragma", "no-cache"); %>
<% response.setDateHeader("Expires", 0); %>
<nav class="bg-gray-800 py-4">
    <div class="container mx-auto px-4 flex justify-between items-center">
        <a href="https://flowbite.com/" class="flex items-center space-x-3 rtl:space-x-reverse">
            <img src="https://flowbite.com/docs/images/logo.svg" class="h-8" alt="Flowbite Logo" />
            <span class="self-center text-2xl font-semibold whitespace-nowrap text-white">CMS</span>
        </a>
        <div class="flex items-center md:order-2 space-x-3 md:space-x-0 rtl:space-x-reverse">
            <%-- Check if the user's sessionUser exists and user is logged in --%>
            <% User loggedInUser = null; %>
            <% HttpSession sessionUser = request.getSession(false); %>
            <% if (sessionUser != null && sessionUser.getAttribute("loggedInUser") != null) { %>
                <% loggedInUser = (User) sessionUser.getAttribute("loggedInUser"); %>
            <% } %>
            <%-- Show profile dropdown if user is logged in --%>
            <% if (loggedInUser != null) { %>
                <button type="button"
                        class="dropdown-toggle flex text-sm bg-gray-800 rounded-full md:me-0 focus:ring-4 focus:ring-gray-300 dark:focus:ring-gray-600"
                        id="user-menu-button" aria-expanded="false" onclick="toggleDropdown()" data-dropdown-toggle="user-dropdown"
                        data-dropdown-placement="bottom">
                    <span class="sr-only">Open user menu</span>
                    <img class="w-8 h-8 rounded-full"
                         src="https://flowbite.com/docs/images/people/profile-picture-3.jpg"
                         alt="user photo">
                </button>
                <!-- Dropdown menu -->
                <div class="relative">
                    <div class="z-50 absolute my-5 -mx-24 text-base list-none divide-y divide-gray-100 rounded-lg shadow bg-gray-700  dropdown-menu"
                         id="user-dropdown" style="display: none">
                        <div class="px-4 py-3">
                            <span class="block text-sm text-gray-100 "><%= loggedInUser.getName() %></span>
                            <span class="block text-sm text-gray-200 truncate">(<%= loggedInUser.getEmail() %>)</span>
                            <% if (loggedInUser.isAdmin()) { %>
                                <span class="block text-sm text-gray-100 truncate w-full text-center"><strong>Admin</strong></span>
                            <% } else { %>
                                <%-- Fetch user roles and display them as badges --%>
                                <%
                                    UserService userService = new UserService();
                                    List<UserRole> userRoles = userService.findUserRoles(loggedInUser);
                                    if (userRoles != null && !userRoles.isEmpty()) {
                                        for (UserRole userRole : userRoles) {
                                            %>
                                            <span class="block text-sm text-gray-100 truncate w-full text-center"><strong><%=userRole.getRole().getRole() %></strong></span>
                                            <%
                                        }
                                    }
                                %>
                            <% } %>
                        </div>
                        <ul class="py-2" aria-labelledby="user-menu-button">
                            <li class="text-center"><a href="update_profile" class="block px-4 py-2 text-sm text-white hover:bg-gray-100 hover:text-gray-900">Profile Settings</a>
                            </li>
                                <li class="text-center"><a href="logout"
                                   class="block px-4 py-2 text-sm text-red-400 hover:bg-gray-100 hover:text-gray-900 ">Sign out</a></li>
                        </ul>
                    </div>
                </div>
            <% } else { %>
                <div class="flex gap-3">
                    <a href="register" class="text-white">Register</a>
                    <a href="login" class="text-white">Login</a>
                </div>
            <% } %>
            <button data-collapse-toggle="navbar-user" type="button"
                    class="inline-flex items-center p-2 w-10 h-10 justify-center text-sm text-gray-500 rounded-lg md:hidden hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-200 dark:hover:bg-gray-700 dark:focus:ring-gray-600"
                    aria-controls="navbar-user" aria-expanded="false" onclick="toggleMobileMenu()">
                <span class="sr-only">Open main menu</span>
                <svg class="w-5 h-5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 17 14">
                    <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M1 1h15M1 7h15M1 13h15"/>
                </svg>
            </button>
        </div>
        <div class="items-center justify-between hidden w-full md:flex md:w-auto md:order-1" id="navbar-user">
            <ul class="flex flex-col font-medium p-4 md:p-0 mt-4 border border-gray-100 rounded-lg md:space-x-8 rtl:space-x-reverse md:flex-row md:mt-0 md:border-0">
                <li class="text-center"><a href="<%= request.getContextPath() %>/" class="block py-2 px-3 text-gray-100 rounded hover:bg-gray-700 md:hover:bg-transparent md:hover:text-blue-700 md:p-0" aria-current="page">Home</a></li>
				<% if (loggedInUser != null && loggedInUser.isAdmin()) { %>
					<li class="text-center"><a href="roles" class="block py-2 px-3 text-gray-100 rounded hover:bg-gray-700 md:hover:bg-transparent md:hover:text-blue-700 md:p-0">Roles</a></li>
				<li class="text-center"><a href="assign_user_roles" class="block py-2 px-3 text-gray-100 rounded hover:bg-gray-700 md:hover:bg-transparent md:hover:text-blue-700 md:p-0">Assign Roles</a></li>
				<li class="text-center"><a href="permissions" class="block py-2 px-3 text-gray-100 rounded hover:bg-gray-700 md:hover:bg-transparent md:hover:text-blue-700 md:p-0">Permission</a></li>
                <% } %>
	
					<% if (loggedInUser != null) { %>
						<li class="text-center"><a href="manage_complains" class="block py-2 px-3 text-gray-100 rounded hover:bg-gray-700 md:hover:bg-transparent md:hover:text-blue-700 md:p-0">Complains</a></li>
					<% } %>
                <li><a href="about" class="block py-2 px-3 text-gray-100 rounded hover:bg-gray-700 md:hover:bg-transparent md:hover:text-blue-700 md:p-0">About</a></li>
                <li><a href="contact" class="block py-2 px-3 text-gray-100 rounded hover:bg-gray-700 md:hover:bg-transparent md:hover:text-blue-700 md:p-0">Contact</a></li>
            </ul>
        </div>
    </div>
    <!-- mobile-menu  -->
    <div class="items-center justify-between w-full md:flex md:w-auto md:order-1" id="navbar-mobile" style="display: none;">
        <ul class="flex flex-col font-medium p-4 md:p-0 mt-4 border border-gray-100 rounded-lg md:space-x-8 rtl:space-x-reverse md:flex-row md:mt-0 md:border-0 md:bg-white dark:bg-gray-800 md:dark:bg-gray-900 dark:border-gray-700 justify-center">
            <li class="text-center"><a href="<%= request.getContextPath() %>/" class="block py-2 px-3 text-gray-100 rounded hover:bg-gray-700 md:hover:bg-transparent md:hover:text-blue-700 md:p-0" aria-current="page">Home</a></li>
			<% if (loggedInUser != null && loggedInUser.isAdmin()) { %>
				<li class="text-center"><a href="roles" class="block py-2 px-3 text-gray-100 rounded hover:bg-gray-700 md:hover:bg-transparent md:hover:text-blue-700 md:p-0">Roles</a></li>
				<li class="text-center"><a href="assign_user_roles" class="block py-2 px-3 text-gray-100 rounded hover:bg-gray-700 md:hover:bg-transparent md:hover:text-blue-700 md:p-0">Assign Roles</a></li>
				<li class="text-center"><a href="permissions" class="block py-2 px-3 text-gray-100 rounded hover:bg-gray-700 md:hover:bg-transparent md:hover:text-blue-700 md:p-0">Permissions</a></li>
				<% } %>

				<% if (loggedInUser != null) { %>
					<li class="text-center"><a href="manage_complains" class="block py-2 px-3 text-gray-100 rounded hover:bg-gray-700 md:hover:bg-transparent md:hover:text-blue-700 md:p-0">Complains</a></li>
				<% } %>
				
				<li class="text-center"><a href="about" class="block py-2 px-3 text-gray-100 rounded hover:bg-gray-700 md:hover:bg-transparent md:hover:text-blue-700 md:p-0">About</a></li>
            <li class="text-center"><a href="contact" class="block py-2 px-3 text-gray-100 rounded hover:bg-gray-700 md:hover:bg-transparent md:hover:text-blue-700 md:p-0">Contact</a></li>
        </ul>
    </div>
</nav>

<script>
    // Function to toggle dropdown menu
    function toggleDropdown() {
        let dropdownMenu = document.getElementById("user-dropdown");
        dropdownMenu.style.display = dropdownMenu.style.display === "none" ? "block" : "none";
    }

    // Add a toggling function for the mobile menu
    function toggleMobileMenu() {
        let mobileMenu = document.getElementById("navbar-mobile");
        mobileMenu.style.display = mobileMenu.style.display === "none" ? "block" : "none";
    }
</script>
