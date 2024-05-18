<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>${title}</title>
  <!-- Additional custom styles for notifications -->
  <style>
    .alert {
      position: fixed;
      top: 1rem;
      right: 1rem;
      z-index: 9999;
    }
  </style>
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body>
  <div class="min-h-screen">

    <div>
    <!-- Success Notification -->
    <div class="alert" id="successNotification" style="display: none">
      <div class="bg-green-500 text-white p-2 rounded" id="successMessageContainer"></div>
    </div>

    <!-- Error Notification -->
    <div class="alert" id="errorNotification" style="display: none">
      <div class="bg-red-500 text-white p-2 rounded" id="errorMessageContainer"></div>
    </div>
    <jsp:include page="/views/navbar.jsp" />
    <jsp:include page="${body}" />
  </div>
</div>
<jsp:include page="/views/footer.jsp" />

  <script>

    // Function to show success notification
    function showSuccessNotification(message) {
      let successNotification = document.getElementById("successNotification");
      let successMessageContainer = document.getElementById("successMessageContainer");
      successMessageContainer.innerText = message;
      successNotification.style.display = "block";
      setTimeout(function () {
        successNotification.style.display = "none";
      }, 5000); // Hide after 5 seconds
    }

    // Function to show error notification
    function showErrorNotification(message) {
      let errorNotification = document.getElementById("errorNotification");
      let errorMessageContainer = document.getElementById("errorMessageContainer");
      errorMessageContainer.innerText = message;
      errorNotification.style.display = "block";
      setTimeout(function () {
        errorNotification.style.display = "none";
      }, 5000); // Hide after 5 seconds
    }

    // Check if successMessage or errorMessage variables are present and show notifications accordingly
    var successMessage = "${successMessage}";
    var errorMessage = "${errorMessage}";
    if (successMessage && successMessage.trim() !== "") {
      showSuccessNotification(successMessage);
    }
    if (errorMessage && errorMessage.trim() !== "") {
      showErrorNotification(errorMessage);
    }
  </script>
  <script>
    const dropdownButton = document.getElementById("profile-dropdown-button"); // Replace with actual button ID
    const dropdownMenu = document.getElementById("profile-dropdown-menu"); // Replace with actual menu ID

    dropdownButton.addEventListener("click", function () {
      dropdownMenu.classList.toggle("hidden");
    });
  </script>
</body>
</html>
