package co.edu.uniandes.dse.parcialprueba.services;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;


import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MedicoService.class)
public class MedicoServiceTest {
    
    @Autowired
    private MedicoService medicoService;

    @Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

    private List<MedicoEntity> medicoList = new ArrayList<>();

    @BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

    private void clearData() {
		entityManager.getEntityManager().createQuery("delete from MedicoEntity");
    }

    private void insertData() {
        
        for(int i = 0; i < 3; i++) {
            MedicoEntity medicoEntity = factory.manufacturePojo(MedicoEntity.class);
            entityManager.persist(medicoEntity);
			medicoList.add(medicoEntity);
        }
    }

    @Test
    void testCreateMedico() throws IllegalOperationException{
        MedicoEntity newEntity = factory.manufacturePojo(MedicoEntity.class);
        newEntity.setRegistroMedico("RM1745");
        
        MedicoEntity result = medicoService.createMedico(newEntity);
        assertNotNull(result);

        MedicoEntity entity = entityManager.find(MedicoEntity.class, result.getId());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getApellido(), entity.getApellido());
        assertEquals(newEntity.getRegistroMedico(), entity.getRegistroMedico());
        assertEquals(newEntity.getId(), entity.getId());
    }

    @Test
    void testCreateMedicoInvalidRegistro() {
        assertThrows(IllegalOperationException.class, () -> {
			MedicoEntity newEntity = factory.manufacturePojo(MedicoEntity.class);
			newEntity.setRegistro("JM1745");
			medicoService.createMedico(newEntity);
        });
    }

}
