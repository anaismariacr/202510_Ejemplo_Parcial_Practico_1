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
import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(EspecialidadService.class)
public class EspecialidadServiceTest {
    
    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

    private List<EspecialidadEntity> especialidadList = new ArrayList<>();

    @BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

    private void clearData() {
		entityManager.getEntityManager().createQuery("delete from EspecialidadEntity");
    }

    private void insertData() {
        
        for(int i = 0; i < 3; i++) {
            EspecialidadEntity especialidadEntityEntity = factory.manufacturePojo(EspecialidadEntity.class);
            entityManager.persist(especialidadEntity);
			especialidadList.add(especialidadEntity);
        }
    }

    @Test
    void testCreateEspecialidad() throws IllegalOperationException{
        EspecialidadEntity newEntity = factory.manufacturePojo(EspecialidadEntity.class);
        newEntity.setDescripcion("esto tiene mas de diez caracteres");
        
        EspecialidadEntity result = especialidadService.createEspecialidad(newEntity);
        assertNotNull(result);

        MedicoEntity entity = entityManager.find(MedicoEntity.class, result.getId());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getDescripcion(), entity.getDescripcion());
        assertEquals(newEntity.getId(), entity.getId());
    }

    @Test
    void testCreateEspecialidadInvalidDescripcion() {
        assertThrows(IllegalOperationException.class, () -> {
			EspecialidadEntity newEntity = factory.manufacturePojo(EspecialidadEntity.class);
			newEntity.setDescripcion("nosufi");
			especialidadService.createEspecialidad(newEntity);
        });
    }

}