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
    <li><a href="#usage">Usage</a></li>
    <li><a href="#getting-started">Getting Started</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#feedback">Feedback</a></li>
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

| Teknologi                                                                                                                 | Deskripsi             |
|---------------------------------------------------------------------------------------------------------------------------|-----------------------|
| ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)    | Main backend framework |
| ![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=spring&logoColor=white) | Application security  |
| ![OAuth2](https://img.shields.io/badge/OAuth2-007EC6?style=for-the-badge&logo=oauth2&logoColor=white)                     | Authentication with third-party providers |
| ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)                  | JSON Web Token to store user information |
| ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)                        | Application Databases |

---

## 🚀 Usage <span id="usage"></span>

1. **OAuth2 Login:**
   - Untuk login dengan Google atau GitHub, arahkan browser ke:
     ```
     http://localhost:8080/login/oauth2/authorization/google
     http://localhost:8080/login/oauth2/authorization/github
     ```
   - Setelah login, aplikasi akan mengeluarkan **JWT Token** untuk akses API.

2. **Akses API dengan Token:**
   - Gunakan **JWT Token** yang diterima dalam setiap permintaan ke endpoint API:
     ```
     Authorization: Bearer <your_token>
     ```
   - Endpoint contoh:
     ```
     GET /api/user/profile
     ```

---

## 🔧 Getting Started <span id="getting-started"></span>

#### 1. Clone the repository

```shell
git clone https://github.com/prazzon/flexbox-labs.git
```

#### 2. Navigate to the app directory
```shell
cd flexbox-labs
```

#### 3. Install npm dependencies

```shell
npm install
```

#### 4. Run the dev server

```shell
npm run dev
```

#### 5. Open the app in your browser

Visit [http://localhost:5173](http://localhost:5173) in your browser.


