# Task Manager REST API

Spring Boot application for managing tasks. It uses an in-memory H2 database and `JdbcTemplate`.

## Run

```powershell
.\gradlew.bat bootRun
```

The API runs at `http://localhost:8080`. The H2 console is available at `http://localhost:8080/h2-console` with JDBC URL `jdbc:h2:mem:taskmanager`, user `sa`, and an empty password.

## API

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/tasks` | Create a task |
| `GET` | `/api/tasks` | List all tasks |
| `GET` | `/api/tasks?completed=true` | List completed tasks (`false` for open tasks) |
| `GET` | `/api/tasks/{id}` | Get a task |
| `PUT` | `/api/tasks/{id}` | Update supplied fields of a task |
| `DELETE` | `/api/tasks/{id}` | Delete a task |

Create a task:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/tasks -ContentType application/json -Body '{"title":"Buy groceries","description":"Milk and bread"}'
```

Update only its completion status:

```powershell
Invoke-RestMethod -Method Put -Uri http://localhost:8080/api/tasks/1 -ContentType application/json -Body '{"completed":true}'
```

Successful creation returns `201 Created`; deletion returns `204 No Content`. Missing tasks return a JSON `404` response, and invalid titles or descriptions return `400`.
