package todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import todolist.dto.UsuarioData;
import todolist.service.UsuarioService;

@Controller
public class AboutController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/registered")
    public String registeredUsers(Model model) {
        model.addAttribute("usuarios", usuarioService.findAllUsuarios());
        return "listaUsuarios";
    }

    @GetMapping("/registered/{id}")
    public String registeredUserDescription(@PathVariable Long id, Model model) {
        UsuarioData usuario = usuarioService.findUsuarioDescripcionById(id);
        model.addAttribute("usuario", usuario);
        return "descripcionUsuario";
    }
}