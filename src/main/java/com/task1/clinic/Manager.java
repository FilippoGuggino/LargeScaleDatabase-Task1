package com.task1.clinic;

import javax.persistence.*;

/**
 * This is a Singleton class, used to manage all interactions with JPA.
 * an instance of this class is able to generate an Entity Manager for JPA.
 * It also provides a set of public methods that make CRUD operations on the database
 * extremely simple to use.
 */
public class Manager {
    private EntityManagerFactory factory;

    private EntityManager em;

    private static Manager singletonInstance = null;

    /**
     * Unique constructor for this class.
     */
    private Manager() {
        this.factory = Persistence.createEntityManagerFactory("clinic");
        this.em = factory.createEntityManager();
    }

    /**
     * get the only instance of this Singleton class.
     * @return the instance of this class
     */
    public static Manager getInstance() {
        if(singletonInstance == null)
            singletonInstance = new Manager();
        return singletonInstance;
    }

    //TODO: Remove this method
    public void exit() {
        System.out.println("sono entrato");
        factory.close();
        System.out.println("sono uscito");
    }

    //TODO: Remove this method
    public void createDoctor(String firstName, String lastName){
        System.out.println("create doctor.");
        DoctorEntity stud = new DoctorEntity("giuseppe", "bellino");
        try{
            em.getTransaction().begin();
            em.persist(stud);
            em.getTransaction().commit();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            em.close();
        }
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
        changes to the object doesn't affect cache/database. To attach it back there's the merge()
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
    public TypedQuery<MedicalEntity> readMedicals(String query) {
        TypedQuery<MedicalEntity> q = em.createNamedQuery(query, MedicalEntity.class);
        return q;
    }

    /**
     * Synchronize the status of the parameter <code>obj</code> with its related row
     * in the database. At the end leaves <code>obj<code/> in a detached state
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



/*
    public void create() {
        System.out.println("Creating a new book");

        Book book = new Book();
        book.setTitle("ciccio banana");
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(book);
            entityManager.getTransaction().commit();
            System.out.println("book created");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void read(int bookId) {
        System.out.println("Getting book");
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            Book book = entityManager.find(Book.class, bookId);

            System.out.println("Title: " + book.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void update(int bookId){
        Book book = new Book();
        book.setTitle("updated");
        book.setId(bookId);
        try{
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(book);
            entityManager.getTransaction().commit();
            System.out.println("updated");
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            entityManager.close();
        }
    }

    public void delete(int bookId){
        System.out.println("remove book");

        try{
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            Book ref = entityManager.getReference(Book.class, bookId);
            entityManager.remove(ref);
            entityManager.getTransaction().commit();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            entityManager.close();
        }
    }

    public void createStudent(String firstName, String lastName){
        System.out.println("create student.");
        Student stud = new Student();
        stud.setFirstName(firstName);
        stud.setLastName(lastName);
        try{
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(stud);
            entityManager.getTransaction().commit();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            entityManager.close();
        }
    }

    public void createLikeBook(int studId, int bookId){
        try{
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            Student stud = entityManager.find(Student.class, studId);
            Book book = entityManager.find(Book.class, bookId);
            entityManager.getTransaction().commit();
            entityManager.getTransaction().begin();
            LikedBook lik = new LikedBook(stud, book);
            entityManager.persist(lik);
            entityManager.getTransaction().commit();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            entityManager.close();
        }
    }

    public void joinBookStudents(Book book){
        try{
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        TypedQuery<Student> query = entityManager.createQuery("select s from student s inner join s.likes", Student.class);
        List<Student> st = query.getResultList();
        System.out.println(st.remove(0).getFirstName());
        System.out.println(st.remove(0).getFirstName());
        entityManager.getTransaction().commit();
        }catch(Exception e){
        e.printStackTrace();
        }finally {
        entityManager.close();
        }
        }
        }

        * */
