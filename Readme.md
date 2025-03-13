# 🔐 Spring Security OAuth2

<p align="center"></p>

<details>
  <summary>📌 Table of Contents</summary>
  <ul>
    <li>
      <a href="#about">About</a>
      <ul>
        <li><a href="#features">Features</a></li>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li><a href="#live-demo">Live Demo</a></li>
    <li><a href="#getting-started">Getting Started</a></li>
    <li><a href="#api-documentation">Api Documentation</a></li>
    <li><a href="#license">License</a></li>
  </ul>
</details>

---

## 🔍 About <span id="about"></span>
Spring Security OAuth2 is an OAuth2-based **authentication and authorization project** using **Spring Boot**. This project allows applications to **authenticate users with OAuth2 providers** such as **Google and GitHub.**.

### ✨ Features <span id="features"></span>
- **OAuth2 Login with Google & GitHub**
- **Role Based Access Control (RBAC)**
- **JSON Web Token (JWT)**
- **Http Only & Cookie Based Authentication**

---

## 🛠 Built With <span id="built-with"></span>

| Technology | Description |  
|------------|------------|  
| ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) | Main backend framework |  
| ![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=spring&logoColor=white) | Application security |  
| ![OAuth2](https://img.shields.io/badge/OAuth2-007EC6?style=for-the-badge&logo=oauth2&logoColor=white) | Authentication with third-party providers |  
| ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white) | JSON Web Token (JWT) |  
| ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white) | Application Databases |  
| ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white) | API Documentation |  
| ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white) | API Testing |

## 🚀 Live Demo 🌍 <span id="live-demo"></span>

Check out the live demo of Spring Security OAuth2:  
👉 [Demo Web Application](https://your-live-demo-link.com) 🌐

## 🔧 Getting Started <span id="getting-started"></span>

#### 1. Clone the repository

```shell
git clone https://github.com/adias311/spring-security-oauth2
```

#### 2. Configure application.properties

```
Before running the application, configure the OAuth2 client credentials 
and database connection in src/main/resources/application.properties
```

#### 3. Install Dependencies
```shell
mvn clean install  
```

#### 4. Run the Application

```shell
mvn spring-boot:run
```

#### 5. Open the app in your browser

The server will start at: [http://localhost:8080](http://localhost:8080) in your browser.

---

## 📑 API Documentation <span id="api-documentation"></span>

### **1️⃣ Swagger UI (Live API Documentation)**
The project includes Swagger UI for API documentation and testing.

**📌 Access Swagger UI:**  
👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

**📌 Access OpenAPI JSON:**  
👉 [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

📌 **Example Swagger UI Screenshot:**  
![Swagger UI](https://user-images.githubusercontent.com/12345678/swagger-screenshot.png)

---

### **2️⃣ Postman API Collection**
We provide a **Postman Collection** for easy testing of API endpoints.

📌 **Download Postman Collection:**  
👉 [Postman Collection Link](https://github.com/your-repo/spring-oauth2-api.postman_collection.json)

**📌 Import into Postman:**
1. Open Postman
2. Click **File → Import**
3. Select the downloaded JSON file
4. Test the API endpoints directly!

📌 **Example Postman Screenshot:**  
![Postman Screenshot](https://user-images.githubusercontent.com/12345678/postman-screenshot.png)

---

## 📜 License <span id="license"></span>
Spring Security OAuth2 is distributed under the terms of the [MIT License](https://github.com/prazzon/flexbox-labs/blob/main/LICENSE). Please review the license file for more details.


