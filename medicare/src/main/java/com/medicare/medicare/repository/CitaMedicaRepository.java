/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.medicare.medicare.repository;

import com.medicare.medicare.domain.CitaMedica;
import com.medicare.medicare.domain.Especialidad;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {

    @Query("SELECT c FROM CitaMedica c "
            + "WHERE c.paciente.id = :pacienteId "
            + "AND c.estado = :estado")
    List<CitaMedica> buscarPorPacienteYEstado(
            @Param("pacienteId") Long pacienteId,
            @Param("estado") String estado);

    @Query("SELECT COUNT(c) FROM CitaMedica c "
            + "WHERE c.especialidad = :especialidad "
            + "AND c.fechaHora BETWEEN :inicio AND :fin")
    Long contarPorEspecialidadYRango(
            @Param("especialidad") Especialidad especialidad,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
    
    boolean existsByPacienteIdAndFechaHora(
        Long pacienteId,
        LocalDateTime fechaHora);
    
    List<CitaMedica> findByPacienteIdOrderByFechaHoraDesc(Long pacienteId);
    
    List<CitaMedica> findByEstado(String estado);
}
