package todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}