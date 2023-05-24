package net.froihofer.util.jboss.entity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Data access object for {@link Customer} entity, encapsulating all
 * persistence-related functions for {@link Customer}, i.e., saving,
 * searching, and deleting.
 * @author Lorenz Froihofer
 */
public class CustomerDAO {
  @PersistenceContext private EntityManager entityManager;
  
  public Customer findById(long customerID) {
    return entityManager.find(Customer.class, customerID);
  }
  
  public void persist(Customer Customer) {
    entityManager.persist(Customer);
  }
  
  public void delete(Customer Customer) {
    entityManager.remove(Customer);
  }

  public void startTransaction(){entityManager.getTransaction().begin();}

  public void commitTransaction(){entityManager.getTransaction().commit();}

  public void flushForId(){entityManager.flush();}

  public EntityManager getEntityManager() {
    return entityManager;
  }
}
