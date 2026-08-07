/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.medicare.medicare.controller;

import com.medicare.medicare.domain.CitaMedica;
import com.medicare.medicare.domain.Rol;
import com.medicare.medicare.domain.Usuario;
import com.medicare.medicare.repository.CitaMedicaRepository;
import com.medicare.medicare.repository.RolRepository;
import com.medicare.medicare.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final CitaMedicaRepository citaMedicaRepository;

    public AdminController(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            CitaMedicaRepository citaMedicaRepository) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.citaMedicaRepository = citaMedicaRepository;
    }

    @GetMapping("/admin/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "admin/usuarios";
    }

    @GetMapping("/admin/roles")
    public String roles(Model model) {
        model.addAttribute("roles", rolRepository.findAll());
        return "admin/roles";
    }

    @GetMapping("/admin/citas")
    public String citas(Model model) {
        model.addAttribute("citas", citaMedicaRepository.findAll());
        return "admin/citas";
    }

    @GetMapping("/admin/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/admin/citas/eliminar/{id}")
    public String eliminarCita(@PathVariable Long id) {
        citaMedicaRepository.deleteById(id);
        return "redirect:/admin/citas";
    }

    @GetMapping("/admin/roles/eliminar/{id}")
    public String eliminarRol(@PathVariable Long id) {
        rolRepository.deleteById(id);
        return "redirect:/admin/roles";
    }
}