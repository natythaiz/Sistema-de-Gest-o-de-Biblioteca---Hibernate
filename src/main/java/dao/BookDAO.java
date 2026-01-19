package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import DTO.SagaBook;
import entities.Book;
import entities.HibernateUtil;
import entities.Saga;
import entities.enumeradores.Categoria;

public class BookDAO {
//	create
	public void saveBook(Book livro, Session session) {
		session.persist(livro);
    }
	
//	read all
	public List<Book> getAllBooks(Session session){
		return session.createQuery("from Book", Book.class).list();
	}
	
//	update
	public void updateBook(Book livro, Session session) {
		session.merge(livro);
    }
	
	public Book findById(int id, Session session) {
		Book book = session.get(Book.class, id);
		return book;
    }
	
//	delete
	public void deleteBook(Book book, Session session) {
		session.remove(book);
    }
	
	public Book findByIsbn(int isbn, Session session) {
		return session.createQuery("from Book where isbn = :isbn", Book.class)
                .setParameter("isbn", isbn)
                .uniqueResult();
	}

	public List<Book> findBookString(String string, Session session) {
		String hql = "FROM Book b WHERE b.titulo LIKE :string OR b.autor LIKE :string";
        
        String busca = "%" + string + "%";
        List<Book> bookList = session.createQuery(hql, Book.class)
                            .setParameter("string", busca)
                            .list();

        return bookList;
	}

	public List<Book> findBookCategory(String categoria, Session session) {
		Categoria categoriaEnum = Categoria.valueOf(categoria);
        String hql = "FROM Book b WHERE b.categoria = :string";
        
        List<Book> bookList = session.createQuery(hql, Book.class)
                            .setParameter("string", categoriaEnum)
                            .list();

        return bookList;
	}

	public List<Book> findBookSaga(Saga saga, Session session) {
		String sqlQuery = "SELECT b.* FROM books b "
        		+ "INNER JOIN saga s ON b.saga_id = s.id "
        		+ "WHERE s.nome = :saga"; 

        Query<Book> query = session.createNativeQuery(sqlQuery, Book.class)
        		.setParameter("saga", saga.getNome());

        List<Book> bookList = query.getResultList();
        return bookList;
	}
	
	public List<Book> findBooksManySagas(List<Long> sagasIds, Session session) {
        if (sagasIds == null || sagasIds.isEmpty()) {
	        return new ArrayList<>();
	    }
        List<Book> bookList = null;

        String sqlQuery = "SELECT b.* FROM books b "
        		+ "INNER JOIN saga s ON b.saga_id = s.id "
        		+ "WHERE s.id IN (:ids)"; 

        Query<Book> query = session.createNativeQuery(sqlQuery, Book.class)
        		.setParameter("ids", sagasIds);

        bookList = query.getResultList();
        
        return bookList;
	}

	public List<SagaBook> getAllBooksSaga(Session session) {
	    List<SagaBook> sagaBookList = new ArrayList<SagaBook>();
	    String sql = "SELECT b.id, b.titulo, b.autor, s.nome  FROM books b "
     		   + "LEFT JOIN saga s ON b.saga_id = s.id;";
	     List<Object[]> rows = session.createNativeQuery(sql, Object[].class).list();
	     for (Object[] row : rows) {
	     	int id = (int) row[0];
	         String titulo = (String) row[1];
	         String autor = (String) row[2];
	         String sagaNome = (String) row[3];
	         SagaBook sagaBook = new SagaBook(id, titulo, autor, sagaNome);
	         sagaBookList.add(sagaBook);
	     }
	    return sagaBookList;
	}
}
