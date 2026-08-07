package com.medicare.medicare.service;

import com.medicare.medicare.domain.CitaMedica;
import com.medicare.medicare.exception.CitaMedicaException;
import com.medicare.medicare.repository.CitaMedicaRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class CitaMedicaService {

    private final CitaMedicaRepository citaMedicaRepository;

    public CitaMedicaService(CitaMedicaRepository citaMedicaRepository) {
        this.citaMedicaRepository = citaMedicaRepository;
    }

    public CitaMedica registrarCita(CitaMedica cita) {

        if (!cita.getFechaHora().isAfter(LocalDateTime.now())) {
            throw new CitaMedicaException(
                    "La fecha y hora de la cita debe ser futura.");
        }

        boolean citaExistente
                = citaMedicaRepository.existsByPacienteIdAndFechaHora(
                        cita.getPaciente().getId(),
                        cita.getFechaHora());

        if (citaExistente) {
            throw new CitaMedicaException(
                    "El paciente ya tiene una cita agendada en esa fecha y hora.");
        }

        return citaMedicaRepository.save(cita);
    }
}