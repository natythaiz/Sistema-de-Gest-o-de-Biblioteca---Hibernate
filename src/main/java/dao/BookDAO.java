package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import entities.Book;
import entities.HibernateUtil;
import entities.Loan;
import entities.Saga;
import entities.SagaBook;
import entities.enumeradores.Categoria;

public class BookDAO {
//	create
	public void saveBook(Book livro) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(livro);
            tx.commit();
            System.out.println("Book saved successfully.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
	
//	read all
	public List<Book> getAllBooks(){
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Book", Book.class).list();
        }
	}
	
//	update
	public void updateBook(Book livro) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(livro);
            tx.commit();
            System.out.println("Book updated successfully.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
	
	public Book findById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	tx = session.beginTransaction();
            Book book = session.get(Book.class, id);
            if (book != null) {
            	return book;
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return null;
    }
	
//	delete
	public void deleteBook(Book livro) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(livro);
            tx.commit();
            System.out.println("Book delted successfully.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
	
	public Book findByIsbn(int isbn) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        return session.createQuery("from Book where isbn = :isbn", Book.class)
	                      .setParameter("isbn", isbn)
	                      .uniqueResult(); // Retorna o livro ou null se não achar
	    }
	}

	public List<Book> findBookString(String string) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM Book b WHERE b.titulo LIKE :string OR b.autor LIKE :string";
            
            String busca = "%" + string + "%";
            List<Book> list = session.createQuery(hql, Book.class)
                                .setParameter("string", busca)
                                .list();

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            e.getStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
	}

	public List<Book> findBookCategory(String categoria) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	Categoria categoriaEnum = Categoria.valueOf(categoria);
            String hql = "FROM Book b WHERE b.categoria = :string";
            
            List<Book> list = session.createQuery(hql, Book.class)
                                .setParameter("string", categoriaEnum)
                                .list();

            return list;
        } catch (IllegalArgumentException e) {
            System.out.println("Não existe esta categoria de livro!");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            e.getStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
	}

	public List<Book> findBookSaga(Saga saga) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            String sqlQuery = "SELECT b.* FROM books b "
            		+ "INNER JOIN saga s ON b.saga_id = s.id "
            		+ "WHERE s.nome = :saga"; 

            Query<Book> query = session.createNativeQuery(sqlQuery, Book.class)
            		.setParameter("saga", saga.getNome());

            List<Book> results = query.getResultList();
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
	
	public List<Book> findBooksManySagas(List<Long> sagasIds) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        if (sagasIds == null || sagasIds.isEmpty()) {
	        return new ArrayList<>();
	    }
        List<Book> resultados = null;
        try {
            session.beginTransaction();

            String sqlQuery = "SELECT b.* FROM books b "
            		+ "INNER JOIN saga s ON b.saga_id = s.id "
            		+ "WHERE s.id IN (:ids)"; 

            Query<Book> query = session.createNativeQuery(sqlQuery, Book.class)
            		.setParameter("ids", sagasIds);

            resultados = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
        return resultados;
	}

	public List<SagaBook> getAllBooksSaga() {
		Session session = HibernateUtil.getSessionFactory().openSession();
	    List<SagaBook> resultado = new ArrayList<SagaBook>();
	    try {
	        session.beginTransaction();

	        String sql = "SELECT b.id, b.titulo, b.autor, s.nome  FROM books b "
	        		   + "RIGHT JOIN saga s ON b.saga_id = s.id;";
	        List<Object[]> rows = session.createNativeQuery(sql, Object[].class).list();
	        for (Object[] row : rows) {
	        	int id = (int) row[0];
	            String titulo = (String) row[1];
	            String autor = (String) row[2];
	            String sagaNome = (String) row[3];
	            SagaBook sagaBook = new SagaBook(id, titulo, autor, sagaNome);
	            resultado.add(sagaBook);
	        }
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
	    return resultado;
	}
}
