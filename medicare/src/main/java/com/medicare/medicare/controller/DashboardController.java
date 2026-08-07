/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.medicare.medicare.controller;

import com.medicare.medicare.domain.Especialidad;
import com.medicare.medicare.domain.Usuario;
import com.medicare.medicare.repository.CitaMedicaRepository;
import com.medicare.medicare.repository.UsuarioRepository;
import java.time.LocalDateTime;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final CitaMedicaRepository citaMedicaRepository;
    private final UsuarioRepository usuarioRepository;

    public DashboardController(
            CitaMedicaRepository citaMedicaRepository,
            UsuarioRepository usuarioRepository) {

        this.citaMedicaRepository = citaMedicaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/paciente/dashboard")
    public String dashboard(Model model) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Usuario paciente = usuarioRepository.findByEmail(email)
                .orElseThrow();

        model.addAttribute(
                "programadas",
                citaMedicaRepository.buscarPorPacienteYEstado(
                        paciente.getId(),
                        "PROGRAMADA"));

        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fin = inicio.plusMonths(3);

        model.addAttribute("cardiologia",
                citaMedicaRepository.contarPorEspecialidadYRango(
                        Especialidad.CARDIOLOGIA, inicio, fin));

        model.addAttribute("dermatologia",
                citaMedicaRepository.contarPorEspecialidadYRango(
                        Especialidad.DERMATOLOGIA, inicio, fin));

        model.addAttribute("pediatria",
                citaMedicaRepository.contarPorEspecialidadYRango(
                        Especialidad.PEDIATRIA, inicio, fin));

        model.addAttribute("neurologia",
                citaMedicaRepository.contarPorEspecialidadYRango(
                        Especialidad.NEUROLOGIA, inicio, fin));

        return "paciente/dashboard";
    }
}
