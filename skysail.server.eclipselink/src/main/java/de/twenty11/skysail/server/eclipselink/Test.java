package de.twenty11.skysail.server.eclipselink;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


public class Test {

		private static final String PERSISTENCE_UNIT_NAME = "todos";
		private static EntityManagerFactory factory;

		public static void main(String[] args) {
			Todo testtodo = new Todo();
			factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
			EntityManager em = factory.createEntityManager();
			// Read the existing entries and write to console
			Query q = em.createQuery("select t from Todo t");
			List<Todo> todoList = q.getResultList();
			for (Todo todo : todoList) {
				System.out.println(todo);
			}
			System.out.println("Size: " + todoList.size());

			// Create new todo
			em.getTransaction().begin();
			Todo todo = new Todo();
			todo.setSummary("This is a test");
			todo.setDescription("This is a test");
			em.persist(todo);
			em.getTransaction().commit();

			em.close();
		}
}
