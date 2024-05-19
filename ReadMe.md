# 🛠️ Complaint Management System 🛠️

Our Complaint Management System allows users to submit complaints, which are then assigned to different management roles by the admin. The assigned roles can update the status of the complaints based on their designated permissions, ensuring a smooth and efficient resolution process.

## 📝 Prerequisites

- **☕ JDK 11**: Ensure that JDK 11 is installed on your system.
- **🐱‍💻 Apache Tomcat 10.1**: Download and install Tomcat version 10.1.

## ⚙️ Setup Instructions

1. **📂 Clone the Repository**:
```bash
   git clone https://github.com/yadavrajsky/Complaint-Management-System.git
   cd complaint-management-system
```

2. **🔧 Configure Database Properties**:
   - Go to `src/main/resources/persistence.xml` and update the database properties with your database configuration:
```xml
<property name="javax.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver" />
<property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/your_db_name" />
<property name="javax.persistence.jdbc.user" value="user" />
<property name="javax.persistence.jdbc.password" value="password" />
```

3. **📦 Install Dependencies**:
   - Ensure you have Maven installed, then run the following command to install the necessary dependencies:
```bash
mvn clean install
```

4. **🚀 Deploy to Tomcat & Start the Tomcat server**:

5. **🌐 Access the Application**:
   - Open your browser and navigate to [Complaint Management System](http://localhost:8080/complaint-management-system).

## 🛠️ Usage

### 📝 Submitting a Complaint
Users can submit their complaints through the designated form on the homepage.

### 👥 Assigning Roles
The admin can assign complaints to different management roles.

### 🔄 Updating Complaint Status
Assigned roles can update the status of the complaints based on their permissions.

### 🛡️ Allocating Different Permissions to Different Users
Different users can be assigned various permissions to ensure they have the appropriate level of access and control over the complaint management process.

## 🚑 Troubleshooting

- Ensure your database server is running and accessible.
- Verify the database credentials and URL in the `persistence.xml` file.
- Check the Tomcat logs for any deployment issues.

## 🤝 Contributing

- Feel free to contribute to this project.

## 📜 License

This project is licensed under the MIT License.
