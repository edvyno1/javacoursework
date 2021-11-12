package courseSystem.hibernateControllers;

import courseSystem.ds.Course;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class CourseHibernate {

    private EntityManagerFactory emf = null;

    public CourseHibernate(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void createCourse(Course course) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(em.merge(course));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public void deleteCourse(Course course) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(course) ? course : em.merge(course));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Course> getAllCourses() {
        return getAllCourses(false, -1, -1);
    }

    public List<Course> getAllCourses(boolean all, int resMax, int resFirst) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(Course.class));
            Query q = em.createQuery(query);

            if (!all) {
                q.setMaxResults(resMax);
                q.setFirstResult(resFirst);
            }

            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
    public Course getCourseById(int id) {
        EntityManager em = null;
        Course course = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            course = em.getReference(Course.class, id);
            course.getId();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such course by given Id");
        }
        return course;
    }
}
