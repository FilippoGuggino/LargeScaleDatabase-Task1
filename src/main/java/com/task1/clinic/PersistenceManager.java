package com.task1.clinic;

import javax.persistence.*;

import org.iq80.leveldb.*;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;

/**
 * This is a Singleton class, used to manage all interactions with JPA.
 * An instance of this class is able to generate an Entity Manager for JPA.
 * It also provides a set of public methods that make CRUD operations on the database
 * extremely simple to use.
 */
public class PersistenceManager implements AutoCloseable{
    private EntityManagerFactory em_factory;

    private EntityManager em;

    private DB cache_keyValue;

    private static PersistenceManager singletonInstance = null;

    /**
     * Unique constructor for this class.
     */
    private PersistenceManager() {
        this.em_factory = Persistence.createEntityManagerFactory("clinic");
        this.em = em_factory.createEntityManager();
    }

    /**
     * Get the only instance of this Singleton class.
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
     * Prepare a JPQL query for the database and get it returned before being performed.
     * @param query the string that contains the JPQL query to be evaluated.
     * @return a Query object to be used to perform the interrogation on the database.
     */
    public Query read(String query) {
        Query q = em.createQuery(query);
        return q;
    }

    /**
     * Prepare a JPQL query that reads a list of medicals and get it returned before being performed.
     * @param query the string that contains the JPQL query to be evaluated.
     * @return a TypedQuery object to be used to perform the interrogation on the database.
     */
    public TypedQuery<Medical> readMedicals(String query) {
        TypedQuery<Medical> q = em.createQuery(query, Medical.class);
        return q;
    }

    /**
     * Update the related row in the database
     * with the current status of the parameter <code>obj</code>.
     * At the end leaves <code>obj</code> in a detached state.
     * @param obj the object to be synchronized with the database. It must be an instance of an Entity class.
     */
    public void update(Object obj) {
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
        em.detach(obj);
    }

    /**
     * Delete the associated row of parameter <code>obj</code> from the database.
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

    private DB openCache() {
        try {
            Options options = new Options();
            //We use default options
            this.cache_keyValue = factory.open(new File("cache_keyValue"), options);
        } catch(IOException e) {
            System.out.println("Error while opening database main directory:");
            e.printStackTrace();
        }

        String date = asString(cache_keyValue.get(bytes("GLOBAL_CACHE_DATE")));
        if(date == null) {
            //TODO: init the cache
            return cache_keyValue;
        }
        Date current = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String current_str = df.format(current);
        if(date.equals(current_str)) {
            return cache_keyValue;
        }

        //TODO: update the cache
        return cache_keyValue;
    }

    private void closeCache() {
        try {
            this.cache_keyValue.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<Medical> getTodayMedicals(Doctor byDoctor, Patient byPatient) {
        DB cache = openCache();
        String keyStart = "medical:";
        List<Medical> result = new ArrayList<Medical>();
        Patient current_pat = null;
        Doctor current_doc = null;
        int counter = 0;
        boolean isAccepted = true;
        Medical current_med = null;
        try (DBIterator iterator = cache.iterator();) {
            iterator.seek(bytes(keyStart));
            while (iterator.hasNext()) {
                String key = asString(iterator.peekNext().getKey()); // key arrangement : medical:$medical_id:attr_name
                String[] keySplit = key.split(":"); // split the key
                String value = asString(iterator.peekNext().getValue());
                if(keySplit[2].equals("doctor")) {
                    if(byDoctor == null) {
                        String[] params = value.split(",");
                        current_doc = new Doctor(params[1], params[2]);
                        current_doc.setIdCode(Integer.parseInt(params[0]));
                    }
                    else if ( value.equals(byDoctor.toString())) {
                        current_doc = byDoctor;
                    }
                    else {
                        current_doc = null;
                        isAccepted = false;
                    }
                    counter++;

                }
                else if(keySplit[2].equals("patient")) {
                    if(byPatient == null) {
                        String[] params = value.split(",");
                        current_pat = new Patient(params[1], params[2]);
                        current_pat.setIdCode(Integer.parseInt(params[0]));
                    }
                    else if ( value.equals(byPatient.toString())) {
                        current_pat = byPatient;
                    }
                    else {
                        current_doc = null;
                        isAccepted = false;
                    }
                    counter++;
                }

                if(counter == 2) {
                    counter = 0;
                    if(!isAccepted) {
                        isAccepted = true;
                        continue;
                    }
                    current_med = new Medical(current_doc, current_pat, new Date());
                    result.add(current_med);
                    counter = 0;
                }


            }
        } catch(DBException | IOException e) {
            System.out.println("Exception occurred while reading the cache...");
            e.printStackTrace();
        }

        closeCache();
        return result;
    }

    /**
     * Close an application-managed manager.
     */
    public void close() {
        em.close();
        em_factory.close();
    }


    /**
     * Get the entity manager reference.
     * @return the entity manager
     */
    public EntityManager getEm() {
        return em;
    }


}



