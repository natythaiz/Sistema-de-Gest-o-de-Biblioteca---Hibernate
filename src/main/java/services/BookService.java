package services;

import dao.BookDAO;
import entities.Book;

public class BookService {
	private BookDAO bookDao = new BookDAO();

    public void cadastrarLivro(Book book) {
        if (book.getTitulo() == null || book.getTitulo().isEmpty()) {
            throw new RuntimeException("O título do livro é obrigatório.");
        }
        if (book.getIsbn() <= 0) {
            throw new RuntimeException("ISBN inválido.");
        }
        
        Book livroExistente = bookDao.findByIsbn(book.getIsbn());
        if (livroExistente != null) {
            throw new RuntimeException("Falha ao cadastrar: Já existe um livro com o ISBN " + book.getIsbn());
        }
        
        // Se passar nas validações, chama o DAO
        bookDao.saveBook(book);
    }
}
