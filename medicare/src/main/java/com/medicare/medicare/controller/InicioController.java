/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.medicare.medicare.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    @GetMapping("/inicio")
    public String inicio(Authentication authentication) {

        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean esMedico = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MEDICO"));

        if (esAdmin) {
            return "redirect:/admin/inicio";
        }

        if (esMedico) {
            return "redirect:/medico/inicio";
        }

        return "redirect:/paciente/inicio";
    }

    @GetMapping("/admin/inicio")
    public String inicioAdmin() {
        return "admin/inicio";
    }

    @GetMapping("/medico/inicio")
    public String inicioMedico() {
        return "medico/inicio";
    }

    @GetMapping("/paciente/inicio")
    public String inicioPaciente() {
        return "paciente/inicio";
    }
}
