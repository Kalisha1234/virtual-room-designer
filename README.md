# Virtual Room Designer

Virtual Room Designer is a full-stack application for creating and managing room layouts with furniture. It uses a React/Vite frontend, a Spring Boot REST API, and MySQL for persistence.

## Tech Stack

- Frontend: React and Vite, served by Nginx in Docker
- Backend: Java 17 and Spring Boot 3.4
- Database: MySQL 8
- Orchestration: Docker Compose

## Run With Docker Compose

Install Docker Desktop and make sure its Docker Engine is running. From the project root, create a local `.env` file containing a password for the Compose MySQL root account:

```dotenv
MYSQL_ROOT_PASSWORD=replace-with-a-strong-local-password
```

The `.env` file is ignored by Git. Use your own local password and do not commit this file.

Build the images and start all services in the background:

```powershell
docker compose up --build -d
```

Open the application at http://localhost:3000. The REST API is also mapped to http://localhost:8080. For example, the furniture catalog is available at `http://localhost:3000/api/furniture` or `http://localhost:8080/api/furniture`.

Check service status and logs with:

```powershell
docker compose ps
docker compose logs -f
```

Stop the containers without deleting database data:

```powershell
docker compose down
```

Start them again using the same `.env` file:

```powershell
docker compose up -d
```

To remove the database volume and all stored room data as well, run `docker compose down -v`. This is destructive.

## Docker Architecture

- `frontend`: Nginx serves the production Vite build on container port 80, mapped to host port 3000. Nginx forwards `/api` requests to the Compose `backend` service.
- `backend`: A multi-stage Java 17/Maven build creates the Spring Boot JAR. A smaller Java 17 runtime image runs it on port 8080, mapped to host port 8080.
- `mysql`: The official MySQL 8 image creates the `roomdesigner` database. The backend connects to `mysql:3306` on the Compose network. MySQL data is kept in the `mysql_data` named volume; its port is not published to the host by default.

The backend waits for MySQL's health check before starting. Compose supplies the database password to both MySQL and Spring Boot through `MYSQL_ROOT_PASSWORD` from `.env` or the shell environment.

## Local Development Without Docker

For local development, run MySQL on `localhost:3306` with a `roomdesigner` database. Set the password in the current PowerShell session, then start the backend:

```powershell
$env:SPRING_DATASOURCE_PASSWORD = '<your-local-mysql-password>'
cd backend
mvn spring-boot:run
```

In another terminal, start the Vite development server:

```powershell
cd frontend
npm install
npm run dev
```

The frontend runs at http://localhost:5173 and proxies `/api` requests to the local backend. Datasource credentials are supplied through environment variables rather than stored in the application properties files.

## Tests and Production Build

Run the backend tests from the project root after setting the datasource password expected by your local MySQL server:

```powershell
$env:SPRING_DATASOURCE_PASSWORD = '<your-local-mysql-password>'
mvn -f backend/pom.xml test
```

Build the frontend production assets without Docker:

```powershell
cd frontend
npm run build
```

The backend uses `spring.jpa.hibernate.ddl-auto=update`, so Hibernate updates the schema when the application starts.

## Features

- Create and manage rooms
- Add, update, and remove furniture
- Update room dimensions and settings
- Persist room data through the REST API
