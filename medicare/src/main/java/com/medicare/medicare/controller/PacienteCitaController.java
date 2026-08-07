/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.medicare.medicare.controller;

import com.medicare.medicare.domain.CitaMedica;
import com.medicare.medicare.domain.Especialidad;
import com.medicare.medicare.domain.Usuario;
import com.medicare.medicare.exception.CitaMedicaException;
import com.medicare.medicare.repository.CitaMedicaRepository;
import com.medicare.medicare.repository.UsuarioRepository;
import com.medicare.medicare.service.CitaMedicaService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PacienteCitaController {

    private final CitaMedicaService citaMedicaService;
    private final CitaMedicaRepository citaMedicaRepository;
    private final UsuarioRepository usuarioRepository;

    public PacienteCitaController(
            CitaMedicaService citaMedicaService,
            CitaMedicaRepository citaMedicaRepository,
            UsuarioRepository usuarioRepository) {

        this.citaMedicaService = citaMedicaService;
        this.citaMedicaRepository = citaMedicaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/paciente/citas/nueva")
    public String nuevaCita(Model model) {

        model.addAttribute("cita", new CitaMedica());
        model.addAttribute("especialidades", Especialidad.values());

        return "paciente/nuevaCita";
    }

    @PostMapping("/paciente/citas/guardar")
    public String guardarCita(
            @ModelAttribute("cita") CitaMedica cita,
            Model model) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Usuario paciente = usuarioRepository.findByEmail(email)
                .orElseThrow();

        cita.setPaciente(paciente);
        cita.setEstado("PROGRAMADA");

        try {

            citaMedicaService.registrarCita(cita);

            return "redirect:/paciente/citas/historial";

        } catch (CitaMedicaException ex) {

            model.addAttribute("error", ex.getMessage());
            model.addAttribute("especialidades", Especialidad.values());

            return "paciente/nuevaCita";
        }
    }

    @GetMapping("/paciente/citas/historial")
    public String historial(Model model) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Usuario paciente = usuarioRepository.findByEmail(email)
                .orElseThrow();

        model.addAttribute(
                "citas",
                citaMedicaRepository
                        .findByPacienteIdOrderByFechaHoraDesc(
                                paciente.getId()));

        return "paciente/historial";
    }
}
