# ElGuPoServer

## Description

ElGuPoServer is the backend component of [ElGuPo](https://github.com/El-GuPo/ElGuPo), a event-based social platform.

## Features

- **User Authentication**
  - Secure registration and login system
  - Email validation
  - Password hashing with BCrypt
  - Profile management

- **Event Management**
  - Real-time event fetching from Afisha7 API
  - Automatic hourly event updates
  - Event categorization and filtering
  - Location-based event discovery

- **Social Features**
  - Tinder-like matching system for both users and events
  - User profile management
  - Like/match system for events and users

- **Media Management**
  - S3 integration for photo storage
  - Secure file upload and retrieval

## Technology Stack

- **Framework**: Spring Boot 3.4.2
- **Language**: Java 23
- **Database**: PostgreSQL
- **Security**: Spring Security
- **Build Tool**: Gradle 8.12.1
- **Cloud Storage**: Yandex S3
- **Documentation**: SpringDoc OpenAPI

## Prerequisites

- JDK 23 or higher
- PostgreSQL
- Yandex Cloud Account (for S3 storage)
- Gradle 8.12.1+

## Building and Running

1. Clone the repository:
```bash
git clone https://github.com/El-GuPo/ElGuPoServer.git
```

2. Configure your database settings in `application.properties`

3. Build the project:
```bash
./gradlew build
```

4. Run the application:
```bash
./gradlew bootRun
```

## Configuration

The application requires several configuration parameters:

- Database configuration
- Yandex S3 credentials
- Afisha7 API credentials
- Security settings

Please ensure all necessary credentials are properly configured before running the application.

## Testing

The project includes a comprehensive test suite. To run the tests:

```bash
./gradlew test
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is part of the ElGuPo platform. For licensing information, please contact the repository owners.

## Contact

For more information about the ElGuPo platform, visit [ElGuPo](https://github.com/El-GuPo/ElGuPo).

