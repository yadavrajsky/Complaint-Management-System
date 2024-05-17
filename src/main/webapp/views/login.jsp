<div class="flex justify-center items-center h-screen">
	<div class="w-full max-w-md">
		<form action="login" method="post"
			class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
			<div class="mb-4">
				<h1 class="text-center text-3xl font-bold mb-4">Login</h1>
				<label for="email"
					class="block text-gray-700 text-sm font-bold mb-2">Email</label> <input
					type="email" id="email" name="email" value="${email}"
					class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
					placeholder="Enter your email" required />
			</div>
			<div class="mb-4">
				<label for="password" value="${password}"
					class="block text-gray-700 text-sm font-bold mb-2">Password</label>
				<input type="password" id="password" name="password"
					class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
					placeholder="Enter your password" required />
			</div>
			<div class="flex items-center justify-center">
				<button type="submit"
					class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Login</button>
			</div>
		</form>
	</div>
</div>
