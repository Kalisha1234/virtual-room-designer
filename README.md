# Virtual Room Designer

Virtual Room Designer is a full-stack application for creating and managing room layouts with furniture items. The app includes a Spring Boot backend for persistence and a React frontend for designing the room.

## Tech Stack

- Frontend: React + Vite
- Backend: Java 17 + Spring Boot 3.4
- Database: MySQL
- API: REST

## Project Structure

- `backend/` – Spring Boot API and persistence layer
- `frontend/` – Vite React app

## Prerequisites

Before running the project, make sure you have:

- Java 17 or later
- Maven
- Node.js 18+ and npm
- MySQL installed and running locally

## Database Setup

The backend is configured to connect to MySQL at:

- Host: `localhost`
- Port: `3306`
- Database: `roomdesigner`
- Username: `root`
- Password: `root`

You can create the database using MySQL:

```sql
CREATE DATABASE roomdesigner;
```

If your local MySQL credentials differ, update the values in:

- `backend/src/main/resources/application.properties`

## Run the Backend

From the project root:

```bash
cd backend
mvn spring-boot:run
```

The API will run on:

- http://localhost:8080

## Run the Frontend

Open a second terminal and run:

```bash
cd frontend
npm install
npm run dev
```

The frontend will run on:

- http://localhost:5173

## Features

- Create and manage rooms
- Add furniture to a room
- Update room dimensions and settings
- Remove furniture and rooms
- View room data from the backend API

## Notes

- The frontend calls the backend at `http://localhost:8080/api`.
- The app expects the MySQL database to be available before starting the backend.
- The backend uses `spring.jpa.hibernate.ddl-auto=update`, so tables can be created automatically when the app starts.

## Useful Commands

Backend tests:

```bash
cd backend
mvn test
```

Frontend production build:

```bash
cd frontend
npm run build
```
