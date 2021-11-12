package cs.hibernateControllers;

import cs.ds.User;
import javafx.scene.control.TextField;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserHibernate {
    private EntityManagerFactory emf;

    public UserHibernate(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void createUser(User user) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(em.merge(user));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public User getUserByLogin(TextField login) {
        EntityManager em;
        List<User> result = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);
            cr.select(root).where(cb.equal(root.get("login"), login.getText()));
            TypedQuery<User> query = em.createQuery(cr);
            query.setMaxResults(1);
            result = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such user by given username");
        }
        return result.get(0);
    }
}
