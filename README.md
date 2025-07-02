# ToDo-Spring-Microservices-Jwt
<br>

 ### AuthService 

This service is a secure authentication system built with **Spring Boot**, implementing robust best practices using:

- **JWT access tokens**
- **Refresh tokens (rotating + stored in DB)**
- **HttpOnly cookies**
- **Device fingerprinting (IP + User-Agent)**
- **Session lifecycle management**
- **OTP-based email verification**

### Secuence Diagram 
The diagram shows flow of how we implement User Registration, User Login and Authorization process.
 ## Sequence Diagram 
```mermaid
sequenceDiagram
    participant Client
    participant Server
    participant DB

    %% Signup
    Client->>Server: POST /api/v1/auth/signup {email, password}
    Server->>DB: Check if email exists
    alt email exists
        Server-->>Client: Error (Email already in use)
    else
        Server->>DB: Save new User
        Server->>Client: Return SignupResponse + OTP sent
    end

    %% Activate
    Client->>Server: GET /api/v1/auth/activate?email&code
    Server->>DB: Validate OTP and enable user
    Server-->>Client: 200 Activated

    %% Login
    Client->>Server: POST /api/v1/auth/login {email, password}
    Server->>DB: Authenticate & Load User
    Server->>Server: Generate AccessToken (JWT)
    Server->>Server: Generate RefreshToken (UUID)
    Server->>DB: Save RefreshToken
    Server-->>Client: Set refreshToken as HttpOnly cookie
    Server-->>Client: Return accessToken in body

    %% Access Protected Route
    Client->>Server: GET /protected with Authorization: Bearer <accessToken>
    Server->>Server: Validate JWT Signature
    Server->>DB: Load user & authorize
    Server-->>Client: Return protected content

    %% Refresh Access Token
    Client->>Server: POST /api/v1/auth/refresh (with HttpOnly cookie)
    Server->>DB: Validate refreshToken
    alt Invalid or Expired
        Server-->>Client: 401 Unauthorized
    else
        Server->>Server: Create new AccessToken & RefreshToken
        Server->>DB: Revoke old token & Save new
        Server-->>Client: Set new cookie
        Server-->>Client: Return accessToken in body
    end

    %% Logout
    Client->>Server: POST /api/v1/auth/logout
    Server->>DB: Revoke refreshToken
    Server-->>Client: Clear cookie, return success

    %% Logout All Devices
    Client->>Server: POST /api/v1/auth/logout-all-devices
    Server->>DB: Revoke all tokens by user
    Server-->>Client: Clear cookie, return success

```

### Features

- User Registration with OTP verification
- Secure Login with email & password
- JWT Access Token (short-lived)
- Refresh Token (long-lived, HttpOnly, revoked on use)
- Revoke refresh token on logout
- Logout from all devices
- if a revoked refresh token is reused, all tokens should be deleted and the user logged out.
- Token rotation on refresh
- Session expiration control (30-day max session)
- Device & IP validation
- CSRF protection by avoiding exposing refresh tokens to JavaScript

### core Technologies

- Spring Boot 3
- Spring Security
- Spring data (JPA)
- JWT (io.jsonwebtoken)
- Lombok
- Maven
- MySql
- RESTful APIs
- Java 17
- Java Mail Sender (OTP)


### API Documentation

| Endpoint                     | Method | Description                          |
|-----------------------------|--------|--------------------------------------|
| `/api/v1/auth/signup`       | POST   | Register user with email & password |
| `/api/v1/auth/activate`     | GET    | Verify OTP and activate user        |
| `/api/v1/auth/login`        | POST   | Login and receive access + refresh token |
| `/api/v1/auth/refresh`      | POST   | Get new access token using HttpOnly refresh cookie |
| `/api/v1/auth/logout`       | POST   | Logout (revokes token, clears cookie) |
| `/api/v1/auth/logout-all-devices` | POST | Logout from all sessions/devices |
| `/api/v1/auth/checkToken`   | GET    | Validate access token and return user info |



