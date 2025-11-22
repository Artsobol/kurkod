# КурКод - Система управления птицефабрикой

**КурКод** - это полнофункциональная программная система для управления птицефабрикой. Система позволяет вести учёт работников, кур, пород, клеток и цехов, а также формировать аналитические отчёты о производительности фабрики.

---

## Содержание

- [Описание](#описание)
- [Основные возможности](#основные-возможности)
- [Структура проекта](#структура-проекта)
- [Технологии](#технологии)
- [Быстрый старт](#быстрый-старт)
  - [Требования](#требования)
  - [Запуск Backend](#запуск-backend)
  - [Запуск Frontend](#запуск-frontend)
- [Документация](#документация)

---

## Описание

Система моделирует работу птицефабрики и предоставляет инструменты для:

- **Управления персоналом**: хранение паспортных данных, трудовых договоров и заработных плат работников
- **Управления поголовьем**: отслеживание кур (вес, возраст, порода, клетка, количество яиц)
- **Организации структуры**: управление цехами, рядами и клетками с возможностью пересаживания кур
- **Управления рационами**: назначение диет породам с учётом сезонности
- **Аналитики**: формирование отчётов по производительности, цехам, породам и работникам
- **Безопасности**: система аутентификации и авторизации с ролями (директор, администратор, сотрудник)

---

## Основные возможности

### Управление данными
-  Учёт кур, пород, клеток и цехов
-  Хранение паспортных данных работников
-  Управление трудовыми договорами и заработными платами
-  Отслеживание перемещений кур между клетками
-  Управление рационами питания с учётом сезонов

### Аналитика и отчёты
-  Анализ производительности по породам
-  Статистика по цехам
-  Отчёты по возрасту кур
-  Месячные отчёты по фабрике (количество яиц, средняя производительность, свод по цехам)

### Безопасность
-  JWT аутентификация (Access + Refresh токены)
-  Система ролей: директор, администратор, сотрудник
-  Оптимистичная блокировка для предотвращения конфликтов

---

## Структура проекта

```
kurkod/
├── apps/
│   ├── backend/          # Backend API (Spring Boot)
│   │   ├── src/          # Исходный код
│   │   ├── docs/         # Документация API
│   │   ├── docker-compose.yml
│   │   └── README.md     # Подробная документация Backend
│   │
│   └── web/              # Frontend приложение (Vue.js)
│       ├── src/          # Исходный код
│       │   ├── api/      # API клиенты
│       │   ├── components/  # Vue компоненты
│       │   ├── pages/    # Страницы приложения
│       │   ├── router/   # Маршрутизация
│       │   └── stores/   # Pinia stores
│       └── package.json
│
└── README.md             # Этот файл
```

---

## Технологии

### Backend
- **Java 21** - язык программирования
- **Spring Boot 3.5.6** - фреймворк
- **PostgreSQL 16** - база данных
- **Liquibase** - управление миграциями БД
- **Spring Security** - безопасность и JWT
- **Swagger/OpenAPI** - документация API
- **MapStruct** - маппинг объектов
- **Lombok** - уменьшение boilerplate кода

### Frontend
- **Vue 3** - прогрессивный JavaScript фреймворк
- **Vite** - сборщик и dev-сервер
- **Vue Router** - маршрутизация
- **Pinia** - управление состоянием
- **Axios** - HTTP клиент
- **SCSS** - препроцессор CSS

### Инфраструктура
- **Docker** - контейнеризация
- **Maven** - управление зависимостями (Backend)
- **npm** - управление зависимостями (Frontend)

---

## Быстрый старт

### Требования

**Для Backend:**
- Java 21 (JDK)
- Maven 3.6+ (или используйте `mvnw` из проекта)
- PostgreSQL 14+ (или Docker)

**Для Frontend:**
- Node.js 18+
- npm или yarn

**Опционально:**
- Docker 20.10+ и Docker Compose 2.0+ (для запуска через Docker)

---

### Запуск Backend

#### Вариант 1: Через Docker (рекомендуется)

```bash
cd apps/backend
docker-compose up --build
```

Backend будет доступен по адресу: http://localhost:8080

#### Вариант 2: Локальный запуск

1. **Настройте базу данных:**
   ```bash
   # Через Docker
   docker run -d --name postgres \
     -e POSTGRES_DB=kurkod_db \
     -e POSTGRES_USER=kurkod \
     -e POSTGRES_PASSWORD=kurkod \
     -p 5432:5432 \
     postgres:16-alpine
   ```

2. **Настройте переменные окружения:**
   
   **Windows (PowerShell):**
   ```powershell
   $env:SPRING_PROFILES_ACTIVE="dev"
   $env:DB_URL="jdbc:postgresql://localhost:5432/kurkod_db"
   $env:DB_USER="kurkod"
   $env:DB_PASSWORD="kurkod"
   $env:SERVER_PORT="8189"
   $env:JWT_SECRET="your-secret-key-change-in-production-min-256-bits"
   ```

   **Linux/Mac:**
   ```bash
   export SPRING_PROFILES_ACTIVE=dev
   export DB_URL=jdbc:postgresql://localhost:5432/kurkod_db
   export DB_USER=kurkod
   export DB_PASSWORD=kurkod
   export SERVER_PORT=8189
   export JWT_SECRET=your-secret-key-change-in-production-min-256-bits
   ```

3. **Запустите приложение:**
   ```bash
   cd apps/backend
   
   # Windows
   mvnw.cmd spring-boot:run
   
   # Linux/Mac
   ./mvnw spring-boot:run
   ```

Backend будет доступен по адресу: http://localhost:8189

**Подробная документация по Backend:** [apps/backend/README.md](apps/backend/README.md)

---

### Запуск Frontend

1. **Установите зависимости:**
   ```bash
   cd apps/web
   npm install
   ```

2. **Настройте API endpoint** (если необходимо):
   
   Отредактируйте файл `apps/web/src/api/http.js` и укажите правильный URL backend API.

3. **Запустите dev-сервер:**
   ```bash
   npm run dev
   ```

4. **Соберите для production:**
   ```bash
   npm run build
   ```

Frontend будет доступен по адресу: http://localhost:5173 (или другому порту, указанному Vite)

---

## Документация

### Backend
- **Подробная документация Backend:** [apps/backend/README.md](apps/backend/README.md)
  - Быстрый старт
  - Конфигурация
  - Структура проекта
  - API документация
  - Безопасность и аутентификация
  - Разработка
  - Типичные проблемы

- **Swagger UI:** http://localhost:8189/swagger-ui.html (локально) или http://localhost:8080/swagger-ui.html (Docker)
- **OpenAPI JSON:** http://localhost:8189/v3/api-docs

### Frontend
- Frontend приложение использует Vue 3 Composition API
- API клиенты находятся в `apps/web/src/api/`
- Компоненты организованы по функциональности в `apps/web/src/components/`

---

## Полезные ссылки

- [Backend README](apps/backend/README.md) - подробная документация по backend
- [Backend API Documentation](apps/backend/docs/API.md) - полная документация API