package ch.bfh.eadj.control;

import javax.persistence.EntityManager;
import java.util.List;

public abstract class AbstractRepository<T> {

    private final Class<T> entityClass;

    public AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(entity);
        getEntityManager().flush(); //TODO scheint nicht zu funktionieren trotz flush
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Liefert Proxy Referenz des Entities zurück
     * Verwenden wenn z.b nur PK auf FK gesetzt werden muss um eine Beziehung zu erstellen
     * @param id
     * @return
     */
    public T getReference(Object id) {
        return getEntityManager().getReference(entityClass, id);
    }

    public List<T> getAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}