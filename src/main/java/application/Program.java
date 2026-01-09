package application;

import java.util.List;

import dao.BookDAO;
import entities.Book;
import entities.enumeradores.Categoria;

public class Program {

	public static void main(String[] args) {
		BookDAO bookDao = new BookDAO();
		
//		Book novoLivro = new Book("Dom Casmurro", "Machado de Assis", 12345, Categoria.DIDATICO);
//		bookDao.saveBook(novoLivro);
		
		List<Book> lit = bookDao.getAllBooks();
		for(Book book: lit) {
			System.out.println(book);
		}
		
		Book book = bookDao.findById(1);
		book.setCategoria(Categoria.BIOGRAFIA);
		bookDao.updateBook(book);
		
//		lit = bookDao.getAllBooks();
//		for(Book obj: lit) {
//			System.out.println(obj);
//		}
		
		book = bookDao.findById(3);
		bookDao.deleteBook(book);
		lit = bookDao.getAllBooks();
		for(Book obj: lit) {
			System.out.println(obj);
		}
	}

}
