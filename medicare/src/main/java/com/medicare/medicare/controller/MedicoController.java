/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.medicare.medicare.controller;

import com.medicare.medicare.domain.CitaMedica;
import com.medicare.medicare.repository.CitaMedicaRepository;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MedicoController {

    private final CitaMedicaRepository citaMedicaRepository;

    public MedicoController(CitaMedicaRepository citaMedicaRepository) {
        this.citaMedicaRepository = citaMedicaRepository;
    }

    @GetMapping("/medico/citas")
    public String citasProgramadas(Model model) {

        List<CitaMedica> citas =
                citaMedicaRepository.findByEstado("PROGRAMADA");

        model.addAttribute("citas", citas);

        return "medico/citas";
    }

    @GetMapping("/medico/citas/completar/{id}")
    public String completar(@PathVariable Long id) {

        CitaMedica cita = citaMedicaRepository.findById(id)
                .orElseThrow();

        cita.setEstado("COMPLETADA");
        citaMedicaRepository.save(cita);

        return "redirect:/medico/citas";
    }

    @GetMapping("/medico/citas/cancelar/{id}")
    public String cancelar(@PathVariable Long id) {

        CitaMedica cita = citaMedicaRepository.findById(id)
                .orElseThrow();

        cita.setEstado("CANCELADA");
        citaMedicaRepository.save(cita);

        return "redirect:/medico/citas";
    }
}
