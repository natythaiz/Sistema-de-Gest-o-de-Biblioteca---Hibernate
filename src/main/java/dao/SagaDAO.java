package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import entities.Saga;

public class SagaDAO {
	public void save(Saga saga, Session session) {
		session.persist(saga);
    }

	public Saga findySagaByName(String nomeSaga, Session session) {
		return session.createQuery("FROM Saga s WHERE s.nome = :string ", Saga.class)
                .setParameter("string", nomeSaga)
                .uniqueResult();
	}
	
	public List<Saga> getAll(Session session){
        String sqlQuery = "SELECT * FROM saga"; 

        Query<Saga> query = session.createNativeQuery(sqlQuery, Saga.class);

        List<Saga> sagaList = query.getResultList();
        return sagaList;
	}
	
	public List<Saga> findManySagas(List<Long> sagasIds, Session session) {
	    if (sagasIds == null || sagasIds.isEmpty()) {
	        return new ArrayList<>();
	    }
	  
	    List<Saga> sagaList = new ArrayList<>();
	    String sql = "SELECT * FROM saga s WHERE s.id IN (:ids)";
        
        NativeQuery<Saga> query = session.createNativeQuery(sql, Saga.class);
        
        query.setParameter("ids", sagasIds);

        sagaList = query.getResultList();
	    return sagaList;
	}

	public Map<String, Long> countBookSaga(Session session) {
	    Map<String, Long> sagaCountList = new HashMap<>();
	    String sql = "SELECT s.nome, COUNT(b.id) FROM books b "
        		+ "RIGHT JOIN saga s ON b.saga_id = s.id "
        		+ "GROUP BY s.nome;";
        
        List<Object[]> rows = session.createNativeQuery(sql, Object[].class).list();
        
        for (Object[] row : rows) {
            String nomeSaga = (String) row[0];
            Long quantidade = ((Number) row[1]).longValue(); 
            sagaCountList.put(nomeSaga, quantidade);
        }
	    return sagaCountList;
	}

	public List<Saga> findSagasById(List<Long> pegarVariasSagas, Session session) {
		List<Saga> sagaList = new ArrayList<>();
		String sql = "SELECT * FROM saga s WHERE s.id IN (:ids)";
        
        NativeQuery<Saga> query = session.createNativeQuery(sql, Saga.class);
        
        query.setParameter("ids", pegarVariasSagas);

        sagaList = query.getResultList();
        return sagaList;
	}
}
