package courseSystem.hibernateControllers;

import courseSystem.ds.File;
import courseSystem.ds.Folder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class FileHibernate {
    private EntityManagerFactory emf = null;

    public FileHibernate(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void createFile(File file) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(em.merge(file));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void deleteFile(File file) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(file) ? file : em.merge(file));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void updateFile(File file) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(file);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public File getFileByName(String fileName) {
        EntityManager em;
        List<File> result = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<File> cr = cb.createQuery(File.class);
            Root<File> root = cr.from(File.class);
            cr.select(root).where(cb.equal(root.get("name"), fileName));
            TypedQuery<File> query = em.createQuery(cr);
            query.setMaxResults(1);
            result = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such file by given name");
        }
        return result.get(0);
    }
}
