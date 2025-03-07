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

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.BookAuthorService;
import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.services.MedicoEspecialidadService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MedicoEspecialidadService.class)
public class MedicoEspecialidadServiceTest {
    @Autowired
    private MedicoEspecialidadService medicoEspecialidadService;

    @Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

    private MedicoEntity medicoEntity;
    private List<EspecialidadEntity> especialidadList = new ArrayList<>();

    @BeforeEach
    void setUp() {
		clearData();
		insertData();
	}

    private void clearData() {
		entityManager.getEntityManager().createQuery("delete from MedicoEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from EspecialidadEntity").executeUpdate();
	}

    private void insertData() {
		medico = factory.manufacturePojo(MedicoEntity.class);
		entityManager.persist(medico);

		for (int i = 0; i < 3; i++) {
			Especialidad entity = factory.manufacturePojo(EspecialidadEntity.class);
			entityManager.persist(entity);
			especialidadList.add(entity);
			medico.getEspecialidades().add(entity);	
		}
	}

    @Test
    void testAddEspecialidad() throws EntityNotFoundException {
        MedicoEntity newMedico = factory.manufacturePojo(MedicoEntity.class);
        entityManager.persist(newMedico);

        EspecialidadEntity newEspecialidad = factory.manufacturePojo(EspecialidadEntity.class);
        entityManager.persist(newEspecialidad);

        EspecialidadEntity result = medicoEspecialidadService.addEspecialidad(newMedico.getId(), newEspecialidad.getId());

        assertNotNull(result);
        assertEquals(newEspecialidad.getId(), result.getId());
        assertEquals(newEspecialidad.getNombre(), result.getNombre());
        assertEquals(newEspecialidad.getDescripcion(), result.getDescripcion());
    }

    @Test
    void testAddEspecialidadInvalidMedico() {
        assertThrows(EntityNotFoundException.class, () -> {
            EspecialidadEntity newEspecialidad = factory.manufacturePojo(EspecialidadEntity.class);
            entityManager.persist(newEspecialidad);

            medicoEspecialidadService.addEspecialidad(0L, newEspecialidad.getId());
        });
    }

    @Test
    void testAddEspecialidadInvalidEspecialidad(){
        assertThrows(EntityNotFoundException.class, () -> {
            MedicoEntity newMedico = factory.manufacturePojo(MedicoEntity.class);
            entityManager.persist(newMedico);

            medicoEspecialidadService.addEspecialidad(newMedico.getId(), 0L);
        });
    }

}
