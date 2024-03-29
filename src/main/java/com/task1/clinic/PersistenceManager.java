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

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    private DB cache_keyValue;

    private static PersistenceManager singletonInstance = null;

    //list of attributes of patients and doctors in cache format
    private static final String[] keyFinals= {"idCode", "firstName", "lastName"};

    /**
     * Unique constructor for this class.
     */
    private PersistenceManager() {
        this.em_factory = Persistence.createEntityManagerFactory("clinic");
        this.em = null;
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
        em = em_factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(obj);
        em.getTransaction().commit();

        /*
        this EntityManager method cuts the relationship between the object and cache/database, so that
        changes to the object don't affect cache/database. To attach it back there's the merge()
        method of EntityManager class.
        */
        em.detach(obj);
        em.close();
    }

    /**
     * Prepare a JPQL query for the database and get it returned before being performed.
     * @param query the string that contains the JPQL query to be evaluated.
     * @return a Query object to be used to perform the interrogation on the database.
     */
    public Query prepareQuery(String query) {
        em = em_factory.createEntityManager();
        Query q = em.createQuery(query);
        return q;
    }

    /**
     * Prepare a JPQL query that reads a list of medicals and get it returned before being performed.
     * @param query the string that contains the JPQL query to be evaluated.
     * @return a TypedQuery object to be used to perform the interrogation on the database.
     */
    public TypedQuery<Medical> prepareMedicalQuery(String query) {
        em = em_factory.createEntityManager();
        TypedQuery<Medical> q = em.createQuery(query, Medical.class);
        return q;
    }

    public <T> List<T> executeTypedQuery(TypedQuery<T> query) {
        if(em == null || !em.isOpen()) {
            System.err.println("Error: Entity Manager has been closed!");
            return null;
        }
        List<T> res = query.getResultList();
        em.close();
        return res;
    }

    public  List executeQuery(Query query) {
        if(em == null || !em.isOpen()) {
            System.err.println("Error: Entity Manager has been closed!");
            return null;
        }
        List res = query.getResultList();
        em.close();
        return res;
    }

    /**
     * Update the related row in the database
     * with the current status of the parameter <code>obj</code>.
     * At the end leaves <code>obj</code> in a detached state.
     * @param obj the object to be synchronized with the database. It must be an instance of an Entity class.
     */
    public void update(Object obj) {
        em = em_factory.createEntityManager();
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
        em.detach(obj);
        em.close();
    }

    /**
     * Delete the associated row of parameter <code>obj</code> from the database.
     * The parameter still stays in cache memory and in Java memory.
     * At the end leaves <code>obj</code> in a detached state.
     * @param obj the object to be deleted from the database. It must be an instance of an Entity class.
     */
    public void delete(Object obj) {
        em = em_factory.createEntityManager();
        em.getTransaction().begin();
        Object managed = em.merge(obj);
        em.remove(managed);
        em.getTransaction().commit();
        em.close();
    }


    /**
     * Initialize the cache with all medicals for current date.
     */
    private void initCache() {
        System.out.println("Initializing the cache...");
        Date current = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String current_str = df.format(current);
        String query = "SELECT m\n" +
                "FROM Medical m\n" +
                "JOIN FETCH m.doctor JOIN FETCH m.patient\n" +
                "WHERE m.date = :currentDate\n" +
                "AND m.approved = true\n";
        TypedQuery<Medical> preparedQuery = prepareMedicalQuery(query);
        preparedQuery.setParameter("currentDate",current);
        List<Medical> initList = executeTypedQuery(preparedQuery);
        WriteBatch batch = cache_keyValue.createWriteBatch();
        String[] docValues;
        String[] patValues;
        for(int i = 0; i < initList.size(); ++i){
            Medical med = initList.get(i);
            Doctor doc = med.getDoctor();
            Patient pat = med.getPatient();
            String keyDoc = "medical"+":"+med.getIdCode()+":"+"doctor:";
            String keyPat = "medical"+":"+med.getIdCode()+":"+"patient:";
            docValues = new String[]{String.valueOf(doc.getIdCode()), doc.getFirstName(), doc.getLastName()};
            patValues = new String[]{String.valueOf(pat.getIdCode()), pat.getFirstName(), pat.getLastName()};
            for(int j = 0; j<3; ++j) {
                batch.put(bytes(keyDoc + keyFinals[j]), bytes(docValues[j]));
                batch.put(bytes(keyPat + keyFinals[j]), bytes(patValues[j]));
            }
        }
        cache_keyValue.write(batch);
        cache_keyValue.put(bytes("GLOBAL_CACHE_DATE"),bytes(current_str));
        System.out.println("cache initialized");
    }

    /**
     * Open the cache for reads and writes.
     * Check if cache is updated, and if not update it.
     * must be called at the beginning of each method
     * that uses the cache.
     * @return the database reference
     */
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
        Date current = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String current_str = df.format(current);
        if(date == null) {
            initCache();
            return cache_keyValue;
        }
        if(date.equals(current_str)) {
            return cache_keyValue;
        }

        //deletes everything and initialize the cache
        WriteBatch deleteBatch =cache_keyValue.createWriteBatch();
        DBIterator iterator = cache_keyValue.iterator();
        iterator.seekToFirst();
        //all deletes in a batch
        while(iterator.hasNext()){
            byte[] key = iterator.peekNext().getKey();
            deleteBatch.delete(key);
            iterator.next();
        }
        //deletes all stored info in the cache
        cache_keyValue.write(deleteBatch);
        //deletes GLOBAL_CACHE_DATE
        cache_keyValue.delete(bytes("GLOBAL_CACHE_DATE"));

        //initialize the cache
        initCache();
        return cache_keyValue;
    }

    /**
     * Close the cache after a read/write operation.
     * Must be called at the end of each method that uses the cache.
     */
    private void closeCache() {
        try {
            this.cache_keyValue.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Get the list of medicals planned for the current date retrieving them from the cache.
     * If any of the parameters is set to null then it's not used to filter the result set.
     * @param byDoctor the doctor involved in the medicals
     * @param byPatient the patient involved in the medicals
     * @return the filtered list of medicals
     */
    public List<Medical> getTodayMedicals(Doctor byDoctor, Patient byPatient) {
        DB cache = openCache();
        String keyStart = "medical:";
        List<Medical> result = new ArrayList<>();
        Patient current_pat = null;
        Doctor current_doc = null;
        int attr_counter = 0;
        int doc_counter = 0;
        int pat_counter = 0;
        boolean isAccepted = true;
        Medical current_med;
        try (DBIterator iterator = cache.iterator()) {
            iterator.seek(bytes(keyStart));
            while (iterator.hasNext()) {
                String key = asString(iterator.peekNext().getKey()); // key arrangement : medical:$medical_id:attr_name
                String[] keySplit = key.split(":"); // split the key
                String value = asString(iterator.peekNext().getValue());
                if(keySplit[2].equals("doctor")) {
                    if(isAccepted) { //if medical has to be discarded we don't waste time
                        if (byDoctor == null) {
                            if (current_doc == null)
                                current_doc = new Doctor();
                            current_doc.setByString(keySplit[3], value);
                        }
                        else {
                            if (current_doc == null)
                                current_doc = byDoctor;

                            //if the doctor is not the same as the one in byDoctor variable
                            if (keySplit[3].equals("idCode") && Integer.parseInt(value) != current_doc.getIdCode()) {
                                current_doc = null;
                                isAccepted = false;
                            }
                        }
                    }
                    doc_counter++;
                }
                else if(keySplit[2].equals("patient")) {
                    if(isAccepted) { //if medical has to be discarded we don't waste time
                        if (byPatient == null) {
                            if (current_pat == null)
                                current_pat = new Patient();
                            current_pat.setByString(keySplit[3], value);
                        }
                        else {
                            if (current_pat == null)
                                current_pat = byPatient;

                            //if the patient is not the same as the one in byPatient variable
                            if (keySplit[3].equals("idCode") && Integer.parseInt(value) != current_pat.getIdCode()) {
                                current_pat = null;
                                isAccepted = false;
                            }
                        }
                    }
                    pat_counter++;
                }

                if(pat_counter == 3 && doc_counter == 3) {
                    pat_counter = doc_counter = 0;
                    current_med = new Medical(current_doc, current_pat, new Date());
                    current_doc = null;
                    current_pat = null;
                    if(!isAccepted) {
                        isAccepted = true;
                        iterator.next();
                        continue;
                    }
                    current_med.setIdCode(Integer.parseInt(keySplit[1]));
                    result.add(current_med);
                }

                iterator.next();
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
        em_factory.close();
    }


    /**
     * Get the entity manager reference, it must be closed after the usage.
     * @return the entity manager
     */
    public EntityManager getEm() {
        if(em == null || !em.isOpen())
            em = em_factory.createEntityManager();
        return em;
    }

    /**
     * Remove from the levelDB cache the specified Medical.
     * The delete operation is done inside of a batch.
     * @param med the Medical to be deleted from the cache.
     */

    public void removeFromCache(Medical med){
        if (med == null){
            return;
        }
        DB cache = openCache();
        String keyDoc = "medical"+":"+med.getIdCode()+":"+"doctor:";
        String keyPat = "medical"+":"+med.getIdCode()+":"+"patient:";
        WriteBatch deleteBatch = cache.createWriteBatch();
        for(int i = 0; i<3; ++i) {
            deleteBatch.delete(bytes(keyDoc + keyFinals[i]));
            deleteBatch.delete(bytes(keyPat + keyFinals[i]));
        }
        cache.write(deleteBatch);
        closeCache();
    }

    /**
     * Add the specified Medical to the levelDB cache.
     * @param med the Medical to be added to the cache.
     */

    public void addToCache(Medical med){
        if (med == null){
            return;
        }
        DB cache = openCache();
        String keyDoc = "medical"+":"+med.getIdCode()+":"+"doctor:";
        Doctor doc = med.getDoctor();
        String keyPat = "medical"+":"+med.getIdCode()+":"+"patient:";
        Patient pat = med.getPatient();
        String[] docValues = {String.valueOf(doc.getIdCode()), doc.getFirstName(), doc.getLastName()};
        String[] patValues = {String.valueOf(pat.getIdCode()), pat.getFirstName(), pat.getLastName()};
        WriteBatch addBatch = cache.createWriteBatch();
        for(int i = 0; i<3; ++i) {
            addBatch.put(bytes(keyDoc + keyFinals[i]), bytes(docValues[i]));
            addBatch.put(bytes(keyPat + keyFinals[i]), bytes(patValues[i]));
        }
        cache.write(addBatch);
        closeCache();
    }


}




