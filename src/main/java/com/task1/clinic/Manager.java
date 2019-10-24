package com.task1.clinic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class
Manager {
    private EntityManagerFactory factory;
    private EntityManager entityManager;

    public Manager() {
        this.factory = Persistence.createEntityManagerFactory("clinic");
    }

    public void exit() {
        System.out.println("sono entrato");
        factory.close();
        System.out.println("sono uscito");
    }

    public void createDoctor(String firstName, String lastName){
        System.out.println("create doctor.");
        DoctorEntity stud = new DoctorEntity("giuseppe", "bellino");
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
