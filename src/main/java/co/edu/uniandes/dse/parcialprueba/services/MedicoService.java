package co.edu.uniandes.dse.parcialprueba.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.repositories.MedicoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicoService {
    @Autowired
    MedicoRepository medicoRepo;


    @Transactional
    public MedicoEntity createMedico(MedicoEntity medicoEntity) throws IllegalArgumentException {
        log.info("Inicia proceso de creación de Medico");

        String str = medicoEntity.getRegistroMedico();
        String[] arrString = str.split("");
        if (arrString[0] != "R" || arrString[1] != "M"){
            throw new IllegalArgumentException("El registro médico debe empezar con RM");
        }

        log.info("Termina proceso de creación de Medico");
        return medicoRepo.save(medicoEntity);
    }

}
