# p2-todolist-app

## Project Boards

Trello Board: https://trello.com/invite/b/69c2935290a0ac85ae2141ce/ATTIf564af8b5d4f6324f5240db4706d0cf654A42334/p2-to-do-list-app
## GitHub Repository

Repository: https://github.com/jamesbobbrown/p2-todolist-app
## Development Workflow

This project is being developed following a feature branch workflow.  
Each feature is associated with a GitHub issue, developed in its own branch, tested, integrated through a pull request, and tracked in both GitHub Project and Trello.

## Current Versions

- Release example completed: `1.0.1`
- Current development version: `1.1.0-SNAPSHOT`

## Implemented Features So Far

### 1. About Page
The About Page was implemented as the example feature for version `1.0.1`.  
It includes the `/about` endpoint, the `about.html` Thymeleaf template, a link from the login page, and an automated web test to verify that the page loads and contains the application name.

### 2. Menu Bar
A common Bootstrap navbar was added as required for version `1.1.0`.  
It appears on all pages except login and registration.  
It includes:
- ToDoList link to the About page
- Tasks link to the logged user task list
- Username dropdown on the right
- Login and registration links on the About page when no user is logged in

## Features In Progress

### 3. Users List Page
The Users List Page was implemented in version `1.1.0-SNAPSHOT`.  
A new service method was added to return all registered users as `UsuarioData` DTOs.  
A new controller endpoint `GET /registered` was added and connected to the Thymeleaf template `listaUsuarios.html`.  
The page shows the identifier and email of all registered users and provides a link to access each user description page.  
This feature also includes a service-layer test and a controller/web test.

### 4. User Description Page
Pending implementation.

## Main Endpoints

### Existing endpoints
- `GET /login`
- `POST /login`
- `GET /registro` or `/registration` depending on the controller implementation
- `POST /registro` or `/registration`
- `GET /logout`
- `GET /about`
- `GET /usuarios/{id}/tareas`
- `GET /usuarios/{id}/tareas/nueva`
- `POST /usuarios/{id}/tareas/nueva`
- `GET /tareas/{id}/editar`
- `POST /tareas/{id}/editar`
- `DELETE /tareas/{id}`
- `GET /registered`

### New endpoints planned for version 1.1.0

- `GET /registered/{id}`

## Tests

The project includes repository, service and controller/web tests using Spring Boot test support, MockMvc and H2 in-memory database.  
Each new feature added in version `1.1.0` will also include:
- service-layer tests for new methods
- controller/web tests for new endpoints and views

## Docker

### Example release 1.0.1
A Docker image was created and verified for release `1.0.1`.

Docker Hub image:
- `jamesbobbrown/p2-todolistapp:1.0.1`

Commands used:
```bash
mvn package
docker build -t jamesbobbrown/p2-todolistapp .
docker tag jamesbobbrown/p2-todolistapp jamesbobbrown/p2-todolistapp:1.0.1
docker push jamesbobbrown/p2-todolistapp:1.0.1
docker pull jamesbobbrown/p2-todolistapp:1.0.1
docker run --rm -p 8081:8080 jamesbobbrown/p2-todolistapp:1.0.1
