package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import entities.HibernateUtil;
import entities.Loan;
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
	
	public List<Saga> getAll(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            String sqlQuery = "SELECT * FROM saga"; 

            Query<Saga> query = session.createNativeQuery(sqlQuery, Saga.class);

            List<Saga> results = query.getResultList();
            session.getTransaction().commit();
            return results;
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
	}
	
	public List<Saga> findManySagas(List<Long> sagasIds) {
	    if (sagasIds == null || sagasIds.isEmpty()) {
	        return new ArrayList<>();
	    }
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    List<Saga> resultados = null;
	    try {
	        session.beginTransaction();

	        String sql = "SELECT * FROM saga s WHERE s.id IN (:ids)";
	        
	        NativeQuery<Saga> query = session.createNativeQuery(sql, Saga.class);
	        
	        query.setParameter("ids", sagasIds);

	        resultados = query.getResultList();

	        session.getTransaction().commit();

	    } catch (Exception e) {
	        if (session.getTransaction() != null && session.getTransaction().isActive()) {
	        	session.getTransaction().rollback();
	        }
	        e.printStackTrace(); 
	    } finally {
	        if (session != null && session.isOpen()) {
	            session.close();
	        }
	    }
	    return resultados;
	}

	public Map<String, Long> countBookSaga() {
		Session session = HibernateUtil.getSessionFactory().openSession();
	    Map<String, Long> resultado = new HashMap<>();
	    try {
	        session.beginTransaction();

	        String sql = "SELECT s.nome, COUNT(b.id) FROM books b "
	        		+ "RIGHT JOIN saga s ON b.saga_id = s.id "
	        		+ "GROUP BY s.nome;";
	        
	        List<Object[]> rows = session.createNativeQuery(sql, Object[].class).list();
	        
	        for (Object[] row : rows) {
	            String nomeSaga = (String) row[0];
	            Long quantidade = ((Number) row[1]).longValue(); 
	            resultado.put(nomeSaga, quantidade);
	        }

	        session.getTransaction().commit();

	    } catch (Exception e) {
	        if (session.getTransaction() != null && session.getTransaction().isActive()) {
	        	session.getTransaction().rollback();
	        }
	        e.printStackTrace(); 
	        return (Map<String, Long>) java.util.Collections.emptyList();
	    } finally {
	        if (session != null && session.isOpen()) {
	            session.close();
	        }
	    }
	    return resultado;
	}

	public List<Saga> findSagasById(List<Long> pegarVariasSagas) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Saga> sagas = new ArrayList<>();
        try {
        	session.beginTransaction();

	        String sql = "SELECT * FROM saga s WHERE s.id IN (:ids)";
	        
	        NativeQuery<Saga> query = session.createNativeQuery(sql, Saga.class);
	        
	        query.setParameter("ids", pegarVariasSagas);

	        sagas = query.getResultList();

	        session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            e.getStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return sagas;
	}
}
