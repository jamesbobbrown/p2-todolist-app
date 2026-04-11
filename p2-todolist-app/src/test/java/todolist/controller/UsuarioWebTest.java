package todolist.controller;

import todolist.dto.UsuarioData;
import todolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import todolist.authentication.ManagerUserSession;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
//
// A diferencia de los tests web de tarea, donde usábamos los datos
// de prueba de la base de datos, aquí vamos a practicar otro enfoque:
// moquear el usuarioService.
public class UsuarioWebTest {

    @Autowired
    private MockMvc mockMvc;

    // Moqueamos el usuarioService.
    // En los tests deberemos proporcionar el valor devuelto por las llamadas
    // a los métodos de usuarioService que se van a ejecutar cuando se realicen
    // las peticiones a los endpoint.
    @MockBean
    private UsuarioService usuarioService;



    @Test
    public void servicioLoginUsuarioOK() throws Exception {
        // GIVEN
        // Moqueamos la llamada a usuarioService.login para que
        // devuelva un LOGIN_OK y la llamada a usuarioServicie.findByEmail
        // para que devuelva un usuario determinado.

        UsuarioData anaGarcia = new UsuarioData();
        anaGarcia.setNombre("Ana García");
        anaGarcia.setId(1L);

        when(usuarioService.login("ana.garcia@gmail.com", "12345678"))
                .thenReturn(UsuarioService.LoginStatus.LOGIN_OK);
        when(usuarioService.findByEmail("ana.garcia@gmail.com"))
                .thenReturn(anaGarcia);

        // WHEN, THEN
        // Realizamos una petición POST al login pasando los datos
        // esperados en el mock, la petición devolverá una redirección a la
        // URL con las tareas del usuario

        this.mockMvc.perform(post("/login")
                        .param("eMail", "ana.garcia@gmail.com")
                        .param("password", "12345678"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios/1/tareas"));
    }

    @Test
    public void servicioLoginUsuarioNotFound() throws Exception {
        // GIVEN
        // Moqueamos el método usuarioService.login para que devuelva
        // USER_NOT_FOUND
        when(usuarioService.login("pepito.perez@gmail.com", "12345678"))
                .thenReturn(UsuarioService.LoginStatus.USER_NOT_FOUND);

        // WHEN, THEN
        // Realizamos una petición POST con los datos del usuario mockeado y
        // se debe devolver una página que contenga el mensaja "No existe usuario"
        this.mockMvc.perform(post("/login")
                        .param("eMail","pepito.perez@gmail.com")
                        .param("password","12345678"))
                .andExpect(content().string(containsString("No existe usuario")));
    }

    @Test
    public void servicioLoginUsuarioErrorPassword() throws Exception {
        // GIVEN
        // Moqueamos el método usuarioService.login para que devuelva
        // ERROR_PASSWORD
        when(usuarioService.login("ana.garcia@gmail.com", "000"))
                .thenReturn(UsuarioService.LoginStatus.ERROR_PASSWORD);

        // WHEN, THEN
        // Realizamos una petición POST con los datos del usuario mockeado y
        // se debe devolver una página que contenga el mensaja "Contraseña incorrecta"
        this.mockMvc.perform(post("/login")
                        .param("eMail","ana.garcia@gmail.com")
                        .param("password","000"))
                .andExpect(content().string(containsString("Contraseña incorrecta")));
    }
    @Test
    public void registroPageShowsAdminCheckboxWhenNoAdminExists() throws Exception {
        when(usuarioService.existeAdministrador()).thenReturn(false);

        this.mockMvc.perform(get("/registro"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Register as administrator")));
    }
    @Test
    public void registroPageHidesAdminCheckboxWhenAdminExists() throws Exception {
        when(usuarioService.existeAdministrador()).thenReturn(true);

        this.mockMvc.perform(get("/registro"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Register as administrator"))));
    }
    @Test
    public void adminLoginRedirectsToRegisteredUsersList() throws Exception {
        UsuarioData admin = new UsuarioData();
        admin.setNombre("Admin User");
        admin.setId(1L);
        admin.setAdmin(true);

        when(usuarioService.login("admin@umh.es", "1234"))
                .thenReturn(UsuarioService.LoginStatus.LOGIN_OK);
        when(usuarioService.findByEmail("admin@umh.es"))
                .thenReturn(admin);
        when(usuarioService.esAdministrador(1L))
                .thenReturn(true);

        this.mockMvc.perform(post("/login")
                        .param("eMail", "admin@umh.es")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registered"));
    }
    @MockBean
    private ManagerUserSession managerUserSession;
    @Test
    public void registeredUsersPageUnauthorizedForNonAdmin() throws Exception {
        when(managerUserSession.usuarioLogeado()).thenReturn(2L);
        when(usuarioService.esAdministrador(2L)).thenReturn(false);

        this.mockMvc.perform(get("/registered"))
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason(containsString("No tienes permisos suficientes")));
    }
    @Test
    public void registeredUserDescriptionUnauthorizedForNonAdmin() throws Exception {
        when(managerUserSession.usuarioLogeado()).thenReturn(2L);
        when(usuarioService.esAdministrador(2L)).thenReturn(false);

        this.mockMvc.perform(get("/registered/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason(containsString("No tienes permisos suficientes")));
    }
    @Test
    public void registeredUsersPageWorksForAdmin() throws Exception {
        UsuarioData usuario = new UsuarioData();
        usuario.setId(1L);
        usuario.setEmail("richard@umh.es");

        List<UsuarioData> usuarios = new ArrayList<>();
        usuarios.add(usuario);

        when(managerUserSession.usuarioLogeado()).thenReturn(1L);
        when(usuarioService.esAdministrador(1L)).thenReturn(true);
        when(usuarioService.findAllUsuarios()).thenReturn(usuarios);

        this.mockMvc.perform(get("/registered"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Registered users")))
                .andExpect(content().string(containsString("richard@umh.es")));
    }
    @Test
    public void registeredUserDescriptionWorksForAdmin() throws Exception {
        UsuarioData usuario = new UsuarioData();
        usuario.setId(1L);
        usuario.setNombre("Richard Stallman");
        usuario.setEmail("richard@umh.es");

        when(managerUserSession.usuarioLogeado()).thenReturn(1L);
        when(usuarioService.esAdministrador(1L)).thenReturn(true);
        when(usuarioService.findUsuarioDescripcionById(1L)).thenReturn(usuario);

        this.mockMvc.perform(get("/registered/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User description")))
                .andExpect(content().string(containsString("Richard Stallman")));
    }
}
