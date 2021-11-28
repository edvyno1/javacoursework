package courseSystem.hibernateControllers;

import courseSystem.ds.Course;
import courseSystem.ds.Folder;
import courseSystem.ds.Folder;
import javafx.scene.control.TextField;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

public class FolderHibernate {
    private EntityManagerFactory emf = null;

    public FolderHibernate(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void createFolder(Folder folder) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(em.merge(folder));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void updateFolder(Folder folder) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(folder);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public void deleteFolder(Folder folder) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(folder) ? folder : em.merge(folder));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void deleteFolder(int id) {
        EntityManager em = null;
        Folder folder;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            folder = em.getReference(Folder.class, id);
            em.remove(folder);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    public Folder getFolderByTitle(String folderTitle) {
        System.out.println(folderTitle);
        EntityManager em;
        List<Folder> result = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Folder> cr = cb.createQuery(Folder.class);
            Root<Folder> root = cr.from(Folder.class);
            cr.select(root).where(cb.equal(root.get("title"), folderTitle));
            TypedQuery<Folder> query = em.createQuery(cr);
            query.setMaxResults(1);
            result = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such folder by title");
        }
        return result.get(0);
    }
    public List<Folder> getAllFolders() {
        EntityManager em = getEntityManager();
        List<Folder> result = null;
        try {
            CriteriaQuery query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(Folder.class));
            Query q = em.createQuery(query);

            result = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return result;
    }

    public Folder getFolderById(int id) {
        EntityManager em;
        Folder folder = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            folder = em.getReference(Folder.class, id);
            folder.getId();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such folder by given Id");
        }
        return folder;
    }
}