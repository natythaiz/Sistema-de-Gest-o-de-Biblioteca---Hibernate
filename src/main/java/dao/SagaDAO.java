package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entities.HibernateUtil;
import entities.Saga;

public class SagaDAO {
	
	public void save(Saga saga) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(saga);
            tx.commit();
            System.out.println("Saga saved successfully.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

	public Saga buscarSagaNome(String nomeSaga) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Saga s WHERE s.nome = :string ", Saga.class)
                    .setParameter("string", nomeSaga)
                    .uniqueResult();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
	}
}
