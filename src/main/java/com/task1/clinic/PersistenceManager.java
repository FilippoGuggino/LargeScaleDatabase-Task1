package com.task1.clinic;

import javax.persistence.*;
import java.util.List;

/**
 * This is a Singleton class, used to manage all interactions with JPA.
 * An instance of this class is able to generate an Entity Manager for JPA.
 * It also provides a set of public methods that make CRUD operations on the database
 * extremely simple to use.
 */
public class PersistenceManager implements AutoCloseable{
    private EntityManagerFactory factory;

    private EntityManager em;

    private static PersistenceManager singletonInstance = null;

    /**
     * Unique constructor for this class.
     */
    private PersistenceManager() {
        this.factory = Persistence.createEntityManagerFactory("clinic");
        this.em = factory.createEntityManager();
    }

    /**
     * get the only instance of this Singleton class.
     * @return the instance of this class
     */
    public static PersistenceManager getInstance() {
        if(singletonInstance == null)
            singletonInstance = new PersistenceManager();
        return singletonInstance;
    }


    /**
     * Insert the parameter <code>obj</code> into the database, making it persistent. At the
     * end leaves <code>obj</code> in a detached state.
     * @param obj the object to be made persistent. It must be an instance of an Entity class.
     */
    public void create(Object obj) {
        em.getTransaction().begin();
        em.persist(obj);
        em.getTransaction().commit();

        /*
        this EntityManager method cuts the relationship between the object and cache/database, so that
        changes to the object don't affect cache/database. To attach it back there's the merge()
        method of EntityManager class.
        */
        em.detach(obj);
    }

    /**
     * Prepare a JPQL query for the database and returns it before being performed.
     * @param query the string that contains the JPQL query to be evaluated.
     * @return a Query object to be used to perform the interrogation on the database.
     */
    public Query read(String query) {
        Query q = em.createQuery(query);
        return q;
    }

    /**
     * Prepare a JPQL query that reads a list of medicals and returns it before being performed.
     * @param query the string that contains the JPQL query to be evaluated.
     * @return a TypedQuery object to be used to perform the interrogation on the database.
     */
    public TypedQuery<Medical> readMedicals(String query) {
        TypedQuery<Medical> q = em.createQuery(query, Medical.class);
        return q;
    }

    /**
     * Synchronize the status of the parameter <code>obj</code> with its related row
     * in the database. At the end leaves <code>obj</code> in a detached state
     * @param obj the object to be synchronized with the database. It must be an instance of an Entity class.
     */
    public void update(Object obj) {
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
        em.detach(obj);
    }

    /**
     * Delete the related row of parameter <code>obj</code> from the database.
     * The parameter still stays in cache memory and in Java memory.
     * At the end leaves <code>obj</code> in a detached state.
     * @param obj the object to be deleted from the database. It must be an instance of an Entity class.
     */
    public void delete(Object obj) {
        em.getTransaction().begin();
        Object managed = em.merge(obj);
        em.remove(managed);
        em.getTransaction().commit();
    }


    /**
     * Close an application-managed manager.
     */
    public void close() {
        em.close();
        factory.close();
    }


    /**
     * Get the entity manager reference.
     * @return the entity manager
     */
    public EntityManager getEm() {
        return em;
    }


}



