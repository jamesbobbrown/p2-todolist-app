package todolist.service;

import todolist.dto.UsuarioData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    // Método para inicializar los datos de prueba en la BD
    // Devuelve el identificador del usuario de la BD
    Long addUsuarioBD() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("richard@umh.es");
        usuario.setNombre("Richard Stallman");
        usuario.setPassword("1234");
        UsuarioData nuevoUsuario = usuarioService.registrar(usuario);
        return nuevoUsuario.getId();
    }
    @Test
    public void testFindAllUsuarios() {
        UsuarioData usuario1 = new UsuarioData();
        usuario1.setEmail("richard@umh.es");
        usuario1.setPassword("1234");
        usuarioService.registrar(usuario1);

        UsuarioData usuario2 = new UsuarioData();
        usuario2.setEmail("james@umh.es");
        usuario2.setPassword("1234");
        usuarioService.registrar(usuario2);

        List<UsuarioData> usuarios = usuarioService.findAllUsuarios();

        assertThat(usuarios).hasSize(2);
        assertThat(usuarios.get(0).getEmail()).isEqualTo("richard@umh.es");
        assertThat(usuarios.get(1).getEmail()).isEqualTo("james@umh.es");
    }

    @Test
    public void servicioLoginUsuario() {
        // GIVEN
        // Un usuario en la BD

        addUsuarioBD();

        // WHEN
        // intentamos logear un usuario y contraseña correctos
        UsuarioService.LoginStatus loginStatus1 = usuarioService.login("richard@umh.es", "1234");

        // intentamos logear un usuario correcto, con una contraseña incorrecta
        UsuarioService.LoginStatus loginStatus2 = usuarioService.login("richard@umh.es", "0000");

        // intentamos logear un usuario que no existe,
        UsuarioService.LoginStatus loginStatus3 = usuarioService.login("ricardo.perez@gmail.com", "12345678");

        // THEN

        // el valor devuelto por el primer login es LOGIN_OK,
        assertThat(loginStatus1).isEqualTo(UsuarioService.LoginStatus.LOGIN_OK);

        // el valor devuelto por el segundo login es ERROR_PASSWORD,
        assertThat(loginStatus2).isEqualTo(UsuarioService.LoginStatus.ERROR_PASSWORD);

        // y el valor devuelto por el tercer login es USER_NOT_FOUND.
        assertThat(loginStatus3).isEqualTo(UsuarioService.LoginStatus.USER_NOT_FOUND);
    }

    @Test
    public void servicioRegistroUsuario() {
        // WHEN
        // Registramos un usuario con un e-mail no existente en la base de datos,

        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("usuario.prueba2@gmail.com");
        usuario.setPassword("12345678");

        usuarioService.registrar(usuario);

        // THEN
        // el usuario se añade correctamente al sistema.

        UsuarioData usuarioBaseDatos = usuarioService.findByEmail("usuario.prueba2@gmail.com");
        assertThat(usuarioBaseDatos).isNotNull();
        assertThat(usuarioBaseDatos.getEmail()).isEqualTo("usuario.prueba2@gmail.com");
    }

    @Test
    public void servicioRegistroUsuarioExcepcionConNullPassword() {
        // WHEN, THEN
        // Si intentamos registrar un usuario con un password null,
        // se produce una excepción de tipo UsuarioServiceException

        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("usuario.prueba@gmail.com");

        Assertions.assertThrows(UsuarioServiceException.class, () -> {
            usuarioService.registrar(usuario);
        });
    }
    @Test
    public void testExisteAdministrador() {
        assertThat(usuarioService.existeAdministrador()).isFalse();

        UsuarioData admin = new UsuarioData();
        admin.setEmail("admin@umh.es");
        admin.setPassword("1234");
        admin.setAdmin(true);
        usuarioService.registrar(admin);

        assertThat(usuarioService.existeAdministrador()).isTrue();
    }
    @Test
    public void testEsAdministrador() {
        UsuarioData admin = new UsuarioData();
        admin.setEmail("admin@umh.es");
        admin.setPassword("1234");
        admin.setAdmin(true);

        UsuarioData adminRegistrado = usuarioService.registrar(admin);

        assertThat(usuarioService.esAdministrador(adminRegistrado.getId())).isTrue();
    }
    @Test
    public void testNoSePuedeRegistrarMasDeUnAdministrador() {
        UsuarioData admin1 = new UsuarioData();
        admin1.setEmail("admin1@umh.es");
        admin1.setPassword("1234");
        admin1.setAdmin(true);
        usuarioService.registrar(admin1);

        UsuarioData admin2 = new UsuarioData();
        admin2.setEmail("admin2@umh.es");
        admin2.setPassword("1234");
        admin2.setAdmin(true);

        Assertions.assertThrows(UsuarioServiceException.class, () -> {
            usuarioService.registrar(admin2);
        });
    public void testFindUsuarioDescripcionById() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("richard@umh.es");
        usuario.setPassword("1234");
        usuario.setNombre("Richard Stallman");

        UsuarioData usuarioRegistrado = usuarioService.registrar(usuario);

        UsuarioData usuarioBD = usuarioService.findUsuarioDescripcionById(usuarioRegistrado.getId());

        assertThat(usuarioBD).isNotNull();
        assertThat(usuarioBD.getEmail()).isEqualTo("richard@umh.es");
        assertThat(usuarioBD.getNombre()).isEqualTo("Richard Stallman");
    }

    @Test
    public void servicioRegistroUsuarioExcepcionConEmailRepetido() {
        // GIVEN
        // Un usuario en la BD

        addUsuarioBD();

        // THEN
        // Si registramos un usuario con un e-mail ya existente en la base de datos,
        // , se produce una excepción de tipo UsuarioServiceException

        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("richard@umh.es");
        usuario.setPassword("12345678");

        Assertions.assertThrows(UsuarioServiceException.class, () -> {
            usuarioService.registrar(usuario);
        });
    }

    @Test
    public void servicioRegistroUsuarioDevuelveUsuarioConId() {

        // WHEN
        // Si registramos en el sistema un usuario con un e-mail no existente en la base de datos,
        // y un password no nulo,

        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("usuario.prueba@gmail.com");
        usuario.setPassword("12345678");

        UsuarioData usuarioNuevo = usuarioService.registrar(usuario);

        // THEN
        // se actualiza el identificador del usuario

        assertThat(usuarioNuevo.getId()).isNotNull();

        // con el identificador que se ha guardado en la BD.

        UsuarioData usuarioBD = usuarioService.findById(usuarioNuevo.getId());
        assertThat(usuarioBD).isEqualTo(usuarioNuevo);
    }

    @Test
    public void servicioConsultaUsuarioDevuelveUsuario() {
        // GIVEN
        // Un usuario en la BD

        Long usuarioId = addUsuarioBD();

        // WHEN
        // recuperamos un usuario usando su e-mail,

        UsuarioData usuario = usuarioService.findByEmail("richard@umh.es");

        // THEN
        // el usuario obtenido es el correcto.

        assertThat(usuario.getId()).isEqualTo(usuarioId);
        assertThat(usuario.getEmail()).isEqualTo("richard@umh.es");
        assertThat(usuario.getNombre()).isEqualTo("Richard Stallman");
    }
    @Test
    public void testBloquearUsuario() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@umh.es");
        usuario.setPassword("1234");

        UsuarioData usuarioRegistrado = usuarioService.registrar(usuario);

        usuarioService.bloquearUsuario(usuarioRegistrado.getId());

        assertThat(usuarioService.usuarioBloqueado(usuarioRegistrado.getId())).isTrue();
    }
    @Test
    public void testDesbloquearUsuario() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@umh.es");
        usuario.setPassword("1234");

        UsuarioData usuarioRegistrado = usuarioService.registrar(usuario);

        usuarioService.bloquearUsuario(usuarioRegistrado.getId());
        usuarioService.desbloquearUsuario(usuarioRegistrado.getId());

        assertThat(usuarioService.usuarioBloqueado(usuarioRegistrado.getId())).isFalse();
    }
    @Test
    public void testLoginUsuarioBloqueado() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("blocked@umh.es");
        usuario.setPassword("1234");

        UsuarioData usuarioRegistrado = usuarioService.registrar(usuario);
        usuarioService.bloquearUsuario(usuarioRegistrado.getId());

        UsuarioService.LoginStatus status = usuarioService.login("blocked@umh.es", "1234");

        assertThat(status).isEqualTo(UsuarioService.LoginStatus.USER_BLOCKED);
    }
}