# Приложение управления пользователями

Полноценное Spring Boot приложение с H2 Database и React SPA для управления пользователями.

## Технологии

### Backend
- **Spring Boot 3.2.0** - основной фреймворк
- **Spring Data JPA** - работа с базой данных
- **H2 Database** - встроенная база данных
- **Spring Validation** - валидация данных
- **Maven** - система сборки

### Frontend
- **React 18** - JavaScript библиотека
- **Bootstrap 5** - CSS фреймворк
- **Babel** - транспиляция JSX

## Структура проекта

```
duty/
├── src/main/java/com/example/duty/
│   ├── DutyApplication.java          # Главный класс приложения
│   ├── model/
│   │   └── User.java                # Модель пользователя
│   ├── dto/
│   │   └── UserDto.java             # DTO для передачи данных
│   ├── repository/
│   │   └── UserRepository.java      # Репозиторий для работы с БД
│   ├── service/
│   │   └── UserService.java         # Бизнес-логика
│   ├── controller/
│   │   ├── UserController.java      # REST API контроллер
│   │   └── SpaController.java       # Контроллер для SPA
│   └── config/
│       └── DataInitializer.java     # Инициализация тестовых данных
├── src/main/resources/
│   ├── application.yml              # Конфигурация приложения
│   └── templates/
│       └── index.html               # React SPA
└── pom.xml                         # Maven конфигурация
```

## Функциональность

### Backend API
- `GET /api/users` - получить всех пользователей
- `GET /api/users/{id}` - получить пользователя по ID
- `POST /api/users` - создать нового пользователя
- `PUT /api/users/{id}` - обновить пользователя
- `DELETE /api/users/{id}` - удалить пользователя
- `GET /api/users/search?query=...` - поиск пользователей

### Frontend
- Список всех пользователей с поиском
- Создание нового пользователя
- Редактирование существующего пользователя
- Удаление пользователя
- Валидация форм
- Адаптивный дизайн

## Запуск приложения

### Требования
- Java 17 или выше
- Maven 3.6 или выше

### Команды для запуска

1. **Клонирование и сборка:**
```bash
git clone <repository-url>
cd duty
mvn clean install
```

2. **Запуск приложения:**
```bash
mvn spring-boot:run
```

3. **Доступ к приложению:**
- Веб-интерфейс: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
- REST API: http://localhost:8080/api/users

### Настройки H2 Console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Модель данных

### User Entity
- `id` (Long) - уникальный идентификатор
- `firstName` (String) - имя (обязательное)
- `lastName` (String) - фамилия (обязательное)
- `middleName` (String) - отчество (опциональное)
- `birthDate` (LocalDate) - дата рождения (обязательное)
- `position` (String) - должность (обязательное)

## Особенности

1. **H2 Database** - встроенная база данных, не требует установки
2. **Автоматическая инициализация** - создание тестовых данных при первом запуске
3. **SPA приложение** - React приложение встроено в Spring Boot
4. **REST API** - полный CRUD для работы с пользователями
5. **Валидация** - проверка данных на стороне сервера и клиента
6. **Поиск** - фильтрация пользователей по имени, фамилии и должности

## Разработка

### Добавление новых полей
1. Обновите модель `User.java`
2. Обновите DTO `UserDto.java`
3. Обновите React компоненты в `index.html`
4. Перезапустите приложение

### Изменение стилей
Редактируйте CSS в секции `<style>` файла `index.html`

### Добавление новых API endpoints
Создайте новые методы в `UserController.java` и соответствующие методы в `UserService.java` 