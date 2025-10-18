# КурКод - Backend (Spring Boot)

Backend-часть системы управления птицефермой. Реализует REST API для работы с пользователями, ролями, курами, сотрудниками и контрактами. 
Проект использует миграции БД (Flyway) и документирует API через Swagger.

- Репозиторий: backend-модуль Maven
- Версия Java: 21
- Spring Boot: 3.5.6
- Порт по умолчанию: 8189
- База данных: PostgreSQL
- Документация API (Swagger UI): http://localhost:8189/swagger-ui.html

## Стек и зависимости

- Spring Boot Starters: Web, Data JPA, Validation, Security
- БД и миграции: PostgreSQL, Flyway
- Документация: springdoc-openapi-starter-webmvc-ui
- Маппинг и утилиты: MapStruct, Lombok, Apache Commons Lang
- JWT: JJWT (api/impl/jackson)

См. `pom.xml` для полного списка и версий.

## Быстрый старт (Windows, cmd.exe)

1) Установите зависимости
- Java 21 (JDK)
- PostgreSQL 14+ (локально или в контейнере)

2) Подготовьте окружение (рекомендуется через переменные среды). Примеры значений:

```
set SPRING_PROFILES_ACTIVE=prod
set SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/kurkod_db
set SPRING_DATASOURCE_USERNAME=postgres
set SPRING_DATASOURCE_PASSWORD=your_password
set JWT_SECRET=change_me_base64
set JWT_LIFETIME=3600000
set JWT_EXPIRATION=103600000
```

3) Запуск приложения

- Быстрый запуск (dev, без профиля):
```
mvnw.cmd spring-boot:run
```

- Сборка jar и запуск:
```
mvnw.cmd clean package -DskipTests
java -jar target/kurkod-0.0.1-SNAPSHOT.jar
```

- Запуск с профилем `prod` (если не выставляли переменную среды заранее):
```
set SPRING_PROFILES_ACTIVE=prod
mvnw.cmd spring-boot:run
```

После старта откройте Swagger UI: http://localhost:8189/swagger-ui.html

## Конфигурация

Основные файлы конфигурации находятся в `src/main/resources`:
- `application.properties` - значения по умолчанию (порт 8189, пути Swagger, ключи JWT и пр.).
- `application-prod.properties` - «prod»-настройки БД (локальный PostgreSQL, Flyway).

Ключевые свойства (рекомендуется переопределять через переменные окружения):
- `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`
- `jwt.secret`, `jwt.lifetime`, `jwt.expiration`
- `springdoc.swagger-ui.path=/swagger-ui.html`, `springdoc.api-docs.path=/v3/api-docs`

Примечание: секреты и пароли не храните в репозитории - переопределяйте их переменными среды.

## База данных и миграции

- Миграции Flyway лежат в `classpath:db/migration` и применяются автоматически при старте.
- Набор миграций по именам файлов указывает на доменные сущности: курицы (chicken), сотрудники/работники (staff/worker), контракты (employment_contract), паспорт (passport), пользователи/роли (user/role), refresh-токены и др.

Если запускаете локально «с нуля», создайте пустую БД `kurkod_db` и укажите корректные креды в переменных среды - Flyway создаст схему.

## Документация API

- Подробная спецификация: [docs](docs/API.md)
- Swagger UI: http://localhost:8189/swagger-ui.html
- OpenAPI JSON: `GET /v3/api-docs`

Базовый URL: `http://localhost:8189/`

Список и формы запросов доступны в Swagger UI после запуска приложения.

## Безопасность и аутентификация

- Используется Spring Security + JWT (JJWT). Время жизни и секрет задаются свойствами `jwt.*`.
- Для тестирования защищённых эндпоинтов получите JWT-токен (см. соответствующие эндпоинты в Swagger) и передавайте его в заголовке `Authorization: Bearer <token>`.

## Сборка и тесты

- Запуск тестов:
```
mvnw.cmd test
```

- Полная сборка (с тестами):
```
mvnw.cmd clean verify
```

## Структура проекта (укороченно)

```
src/
  main/
    java/io/github/artsobol/kurkod/
      KurkodApplication.java        # точка входа Spring Boot
      common/ ...                   # общие утилиты, константы, валидации
      security/ ...                 # конфигурация безопасности, фильтры, JWT
      web/ ...                      # контроллеры, конфиги web-слоя, DTO/response
    resources/
      application.properties        # конфигурация по умолчанию
      application-prod.properties   # профиль prod
      db/migration/*.sql            # миграции Flyway
```

## Переменные окружения (рекомендации)

- `SPRING_PROFILES_ACTIVE` - активный профиль (например, `prod`).
- `SPRING_DATASOURCE_URL` - URL БД, например `jdbc:postgresql://localhost:5432/kurkod_db`.
- `SPRING_DATASOURCE_USERNAME` - пользователь БД.
- `SPRING_DATASOURCE_PASSWORD` - пароль БД.
- `JWT_SECRET` - секрет для подписи JWT (base64 или строка; хранить в секрете!).
- `JWT_LIFETIME` - время жизни access-токена, мс.
- `JWT_EXPIRATION` - (опцион.) расширенный параметр сроков, мс.

## Типичные проблемы

- «Connection refused/timeout к БД»: проверьте доступность PostgreSQL и креды в переменных среды.
- «org.flywaydb.core.api.FlywayException»: убедитесь, что схема пуста при первом запуске или миграции согласованы.
- «Access denied» при вызове эндпоинтов: получите JWT и передавайте заголовок `Authorization`.
- Swagger UI не открывается: проверьте порт 8189 и свойство `springdoc.swagger-ui.path`.

---

Вопросы и улучшения приветствуются - создавайте issue/PR. Если нужен Docker/Compose или примеры Postman - можно добавить в следующих итерациях.
