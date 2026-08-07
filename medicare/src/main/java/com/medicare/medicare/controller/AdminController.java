package com.medicare.medicare.controller;

import com.medicare.medicare.domain.CitaMedica;
import com.medicare.medicare.domain.Especialidad;
import com.medicare.medicare.domain.Rol;
import com.medicare.medicare.domain.Usuario;
import com.medicare.medicare.repository.CitaMedicaRepository;
import com.medicare.medicare.repository.RolRepository;
import com.medicare.medicare.repository.UsuarioRepository;
import com.medicare.medicare.service.CorreoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final CitaMedicaRepository citaMedicaRepository;
    private final CorreoService correoService;

    public AdminController(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            CitaMedicaRepository citaMedicaRepository,
            CorreoService correoService) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.citaMedicaRepository = citaMedicaRepository;
        this.correoService = correoService;
    }

    // =========================
    // USUARIOS
    // =========================

    @GetMapping("/admin/usuarios")
    public String usuarios(Model model) {

        model.addAttribute(
                "usuarios",
                usuarioRepository.findAll());

        return "admin/usuarios";
    }

    @GetMapping("/admin/usuarios/nuevo")
    public String nuevoUsuario(Model model) {

        Usuario usuario = new Usuario();
        usuario.setActivo(true);

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolRepository.findAll());

        return "admin/usuarioForm";
    }

    @GetMapping("/admin/usuarios/editar/{id}")
    public String editarUsuario(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "usuario",
                usuarioRepository.findById(id).orElseThrow());

        model.addAttribute(
                "roles",
                rolRepository.findAll());

        return "admin/usuarioForm";
    }

    @PostMapping("/admin/usuarios/guardar")
    public String guardarUsuario(
            @ModelAttribute Usuario usuario) {

        boolean usuarioNuevo = usuario.getId() == null;

        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }

        Usuario usuarioGuardado =
                usuarioRepository.save(usuario);

        if (usuarioNuevo) {

            correoService.enviarCorreo(
                    usuarioGuardado.getEmail(),
                    "Registro en MediCare",
                    "Hola "
                    + usuarioGuardado.getNombre()
                    + ", su usuario fue registrado correctamente en MediCare."
            );
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/admin/usuarios/eliminar/{id}")
    public String eliminarUsuario(
            @PathVariable Long id) {

        usuarioRepository.deleteById(id);

        return "redirect:/admin/usuarios";
    }

    // =========================
    // ROLES
    // =========================

    @GetMapping("/admin/roles")
    public String roles(Model model) {

        model.addAttribute(
                "roles",
                rolRepository.findAll());

        return "admin/roles";
    }

    @GetMapping("/admin/roles/nuevo")
    public String nuevoRol(Model model) {

        model.addAttribute(
                "rol",
                new Rol());

        return "admin/rolForm";
    }

    @GetMapping("/admin/roles/editar/{id}")
    public String editarRol(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "rol",
                rolRepository.findById(id).orElseThrow());

        return "admin/rolForm";
    }

    @PostMapping("/admin/roles/guardar")
    public String guardarRol(
            @ModelAttribute Rol rol) {

        rolRepository.save(rol);

        return "redirect:/admin/roles";
    }

    @GetMapping("/admin/roles/eliminar/{id}")
    public String eliminarRol(
            @PathVariable Long id) {

        rolRepository.deleteById(id);

        return "redirect:/admin/roles";
    }

    // =========================
    // CITAS
    // =========================

    @GetMapping("/admin/citas")
    public String citas(Model model) {

        model.addAttribute(
                "citas",
                citaMedicaRepository.findAll());

        return "admin/citas";
    }

    @GetMapping("/admin/citas/nueva")
    public String nuevaCita(Model model) {

        CitaMedica cita = new CitaMedica();
        cita.setEstado("PROGRAMADA");

        cargarDatosCita(model);

        model.addAttribute("cita", cita);

        return "admin/citaForm";
    }

    @GetMapping("/admin/citas/editar/{id}")
    public String editarCita(
            @PathVariable Long id,
            Model model) {

        cargarDatosCita(model);

        model.addAttribute(
                "cita",
                citaMedicaRepository.findById(id).orElseThrow());

        return "admin/citaForm";
    }

    @PostMapping("/admin/citas/guardar")
    public String guardarCita(
            @ModelAttribute CitaMedica cita) {

        citaMedicaRepository.save(cita);

        return "redirect:/admin/citas";
    }

    @GetMapping("/admin/citas/eliminar/{id}")
    public String eliminarCita(
            @PathVariable Long id) {

        citaMedicaRepository.deleteById(id);

        return "redirect:/admin/citas";
    }

    private void cargarDatosCita(Model model) {

        model.addAttribute(
                "pacientes",
                usuarioRepository.findByRolNombre("PACIENTE"));

        model.addAttribute(
                "especialidades",
                Especialidad.values());

        model.addAttribute(
                "estados",
                new String[]{
                    "PROGRAMADA",
                    "COMPLETADA",
                    "CANCELADA"
                });
    }
}