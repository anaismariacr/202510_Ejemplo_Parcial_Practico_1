package co.edu.uniandes.dse.parcialprueba.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialprueba.repositories.EspecialidadRepository;
import co.edu.uniandes.dse.parcialprueba.repositories.MedicoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicoEspecialidadService {
    
    @Autowired
    MedicoRepository medicoRepo;

    @Autowired
    EspecialidadRepository especialidadRepo;

    public EspecialidadEntity addEspecialidad(Long medicoId, Long especialidadId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociar una especialidad al medico con id = {0}", medicoId);

        Optional<EspecialidadEntity> especialidad = especialidadRepo.findById(especialidadId);
        if(especialidad.isEmpty())
            throw new EntityNotFoundException("La especialidad con id no existe");
        
        Optional<MedicoEntity> medico = medicoRepo.findById(medicoId);
        if(medico.isEmpty())
            throw new EntityNotFoundException("El medico con id no existe");

        medico.get().getEspecialidades().add(especialidad.get());
        log.info("Termina proceso de asociar una especialidad al medico con id = {0}", medicoId);
        return especialidad.get();
    }


}
