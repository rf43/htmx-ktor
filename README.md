# HTMX-Ktor

A modern full-stack web application built with Ktor, HTMX, and Tailwind CSS. This project demonstrates how to create a reactive web application using server-side rendering with HTMX for dynamic updates, eliminating the need for a separate JavaScript framework.

## 🚀 Features

- **Server-Side Rendering**: Built with Ktor for efficient server-side rendering
- **Dynamic Updates**: HTMX for seamless dynamic content updates without full page reloads
- **Modern Styling**: Tailwind CSS for utility-first styling
- **Kotlin Backend**: Leveraging Kotlin's powerful features for type-safe backend development
- **Development Mode**: Hot reload support for rapid development
- **Comprehensive Testing**: High test coverage with JaCoCo reporting

## 🛠️ Tech Stack

- **Backend**: 
  - Ktor 3.1.2
  - Kotlin 2.1.20
  - Netty server
  - Logback 1.5.18
- **Frontend**:
  - HTMX for dynamic updates
  - Tailwind CSS for styling
- **Build Tool**: Gradle with Kotlin DSL
- **Testing**: JaCoCo for code coverage reporting

## 📋 Prerequisites

- JDK 17 or later
- Gradle 8.0 or later

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/htmx-ktor.git
   cd htmx-ktor
   ```

2. Run the application:
   ```bash
   ./gradlew run
   ```

3. Access the application at `http://localhost:8080`

## 🏗️ Development

- Run in development mode:
  ```bash
   ./gradlew run --args="-Dio.ktor.development=true"
   ```

- Build the project:
  ```bash
   ./gradlew build
   ```

- Run tests:
  ```bash
   ./gradlew test
   ```

- Generate test coverage report:
  ```bash
   ./gradlew jacocoTestReport
   ```

- Verify test coverage meets minimum requirements (80%):
  ```bash
   ./gradlew jacocoTestCoverageVerification
   ```

## 📊 Test Coverage

The project maintains a minimum test coverage requirement of 80%. Current coverage metrics:

- Instructions: 99% (1,995 of 2,015)
- Lines: 99% (359 of 363)
- Methods: 96% (54 of 56)
- Classes: 100% (25 of 25)

Coverage reports are generated when running the test coverage tasks.

## 📁 Project Structure

```
htmx-ktor/
├── src/
│   ├── main/
│   │   ├── kotlin/        # Kotlin source files
│   │   │   └── io/ivycreek/
│   │   │       ├── about/     # About page components
│   │   │       ├── calendar/  # Calendar components
│   │   │       ├── contact/   # Contact page components
│   │   │       ├── dashboard/ # Dashboard components
│   │   │       ├── navbar/    # Navigation components
│   │   │       ├── plugins/   # Ktor plugins
│   │   │       ├── projects/  # Projects page components
│   │   │       └── team/      # Team page components
│   │   └── resources/     # Static resources and templates
│   └── test/              # Test files
├── gradle/                # Gradle wrapper files
├── build.gradle.kts       # Gradle build configuration
├── settings.gradle.kts    # Gradle settings
├── gradle.properties      # Gradle properties
├── gradlew                # Gradle wrapper script (Unix)
├── gradlew.bat           # Gradle wrapper script (Windows)
├── .gitignore            # Git ignore rules
├── LICENSE               # License file
└── README.md             # Project documentation
```

## 🔧 Configuration

The project uses several configuration files:

- `gradle.properties` - Gradle and project properties
- `build.gradle.kts` - Build configuration and dependencies
- `settings.gradle.kts` - Project settings

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. Ensure that all tests pass and maintain the minimum test coverage requirement of 80%.
