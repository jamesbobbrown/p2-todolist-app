package todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import todolist.authentication.ManagerUserSession;
import todolist.dto.UsuarioData;
import todolist.service.UsuarioService;

@Controller
public class AboutController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ManagerUserSession managerUserSession;

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/registered")
    public String registeredUsers(Model model) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();

        if (idUsuarioLogeado == null || !usuarioService.esAdministrador(idUsuarioLogeado)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "No tienes permisos suficientes para acceder a esta página"
            );
        }

        model.addAttribute("usuarios", usuarioService.findAllUsuarios());
        return "listaUsuarios";
    }

    @GetMapping("/registered/{id}")
    public String registeredUserDescription(@PathVariable Long id, Model model) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();

        if (idUsuarioLogeado == null || !usuarioService.esAdministrador(idUsuarioLogeado)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "No tienes permisos suficientes para acceder a esta página"
            );
        }

        UsuarioData usuario = usuarioService.findUsuarioDescripcionById(id);
        model.addAttribute("usuario", usuario);
        return "descripcionUsuario";
    }
}