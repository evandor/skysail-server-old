package skysail.server.um.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.common.um.SkysailUser;

public class SkysailUserTest {

    private EntityManagerFactory emf;
    private SkysailUser user;

    @Before
    public void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("testPU");
        user = new SkysailUser();
        user.setLogin("login");
        user.setPassword("password");
    }

    @Test
    public void can_create_user() {
        createUser(user);
    }

    private void createUser(SkysailUser user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }
}
