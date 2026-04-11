# p2-todolist-app

## Project Boards

Trello Board: https://trello.com/invite/b/69c2935290a0ac85ae2141ce/ATTIf564af8b5d4f6324f5240db4706d0cf654A42334/p2-to-do-list-app

## GitHub Repository

Repository: https://github.com/jamesbobbrown/p2-todolist-app

## Development Workflow

This project was developed using a feature branch workflow. Each feature was associated with a GitHub issue, implemented in its own branch, tested, integrated through a pull request, and tracked in both GitHub Project and Trello. This workflow was first followed for the `1.0.1` example release and then repeated for the `1.1.0` development.

## Current Versions

- Example release completed: `1.0.1`
- Final release: `1.1.0`

## Implemented Features

### 1. About Page
The About Page was implemented as the example feature for version `1.0.1`. It includes the `/about` endpoint, the `about.html` Thymeleaf template, a link from the login page, and an automated web test to verify that the page loads and contains the application name.

### 2. Menu Bar
A common Bootstrap navbar was added as required for version `1.1.0`. It appears on all pages except login and registration. It includes the ToDoList link to the About page, the Tasks link to the logged user task list, and the username dropdown on the right. When no user is logged in, the navbar provides login and registration links.

### 3. Users List Page
The Users List Page was implemented in version `1.1.0`. A new service method was added to return all registered users as `UsuarioData` DTOs. A new controller endpoint `GET /registered` was added and connected to the Thymeleaf template `listaUsuarios.html`. The page shows the identifier and email of all registered users and provides a link to access each user description page. This feature also includes a service-layer test and a controller/web test.

### 4. User Description Page
The User Description Page was implemented in version `1.1.0`. A new endpoint `GET /registered/{id}` was added to display the description of one registered user. A service method was added to retrieve the selected user as a `UsuarioData` DTO, and a new Thymeleaf template `descripcionUsuario.html` was created to show the user data. The page displays the user identifier, name, email and date of birth, but it does not display the password, following the statement requirements. This feature also includes a service test and a controller/web test.

### 5. Administrator User
An optional administrator user feature was added on top of the required `1.1.0` features. The registration page allows creating an administrator account using a checkbox, but only if no administrator already exists. The application checks this rule in the service layer and prevents registering more than one administrator. If an administrator already exists, the checkbox is hidden from the registration page. After logging in, an administrator is redirected to the users list page instead of the tasks page. This feature required changes in the domain model, DTOs, service layer, controller logic, registration template, and automated tests.

### 6. Protection of User Listing and User Description
An optional protection layer was added to restrict access to the users list page and the user description page. The endpoints `GET /registered` and `GET /registered/{id}` verify the logged user through `ManagerUserSession` and only allow access if the current user is an administrator. If a non-admin user or an anonymous user tries to access these pages, the application returns HTTP `401 Unauthorized` with a message indicating insufficient permissions. This feature extends the administrator functionality and includes controller/web tests for both authorized and unauthorized access cases.

### 7. User Blocking by Administrator
A final optional feature was added so the administrator can disable or enable user access from the registered users page. The users list page includes a button to disable or enable each user. A new boolean field was added to the user model and DTO to store whether the user is blocked. If a blocked user tries to log in, the service layer returns a blocked-user status and the login page shows the error message `Usuario bloqueado`. This feature required updates in the model, DTOs, service layer, controller endpoints, users list template, and automated tests.

## Main Endpoints

- `GET /login`
- `POST /login`
- `GET /registro`
- `POST /registro`
- `GET /logout`
- `GET /about`
- `GET /usuarios/{id}/tareas`
- `GET /usuarios/{id}/tareas/nueva`
- `POST /usuarios/{id}/tareas/nueva`
- `GET /tareas/{id}/editar`
- `POST /tareas/{id}/editar`
- `DELETE /tareas/{id}`
- `GET /registered`
- `GET /registered/{id}`
- `POST /registered/{id}/bloquear`
- `POST /registered/{id}/desbloquear`

## Tests

The project includes repository, service and controller/web tests using Spring Boot testing support, MockMvc and an H2 in-memory database. For the new features, additional service-layer and web/controller tests were added in order to validate both business logic and endpoint behavior. The tests cover the users list, user description, administrator creation, administrator-only protection, and user blocking functionality.

## Docker

### Release 1.0.1
A Docker image was created and verified for release `1.0.1`.

Docker Hub image:
- `jamesbobbrown/p2-todolistapp:1.0.1`

### Final release 1.1.0
A Docker image was also created for the final release `1.1.0`.

Docker Hub image:
- `jamesbobbrown/p2-todolistapp:1.1.0`

Commands used for the final release:
```bash
mvn test
mvn package
docker build -t jamesbobbrown/p2-todolistapp .
docker tag jamesbobbrown/p2-todolistapp jamesbobbrown/p2-todolistapp:1.1.0
docker push jamesbobbrown/p2-todolistapp:1.1.0
docker pull jamesbobbrown/p2-todolistapp:1.1.0
docker run --rm -p 8081:8080 jamesbobbrown/p2-todolistapp:1.1.0
