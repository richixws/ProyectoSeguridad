package pe.gob.bcrp.repository;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.gob.bcrp.entities.Sistema;
import pe.gob.bcrp.repositories.ISistemaRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SistemaRepositoryTest {

    @Autowired
    private ISistemaRepository sistemaRepository;

    private Sistema sistema;

    @BeforeEach
    public void setUp() {
       // sistema =new Sistema();
    }
}
