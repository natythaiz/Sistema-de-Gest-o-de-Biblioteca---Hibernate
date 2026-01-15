package services;

import java.util.List;

import org.hibernate.Session;

import dao.BookDAO;
import entities.Book;
import entities.HibernateUtil;
import entities.Loan;
import entities.SagaBook;
import entities.User;

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

	public void findBookString(String string) {
		List<Book> result = bookDao.findBookString(string);
		if(result.isEmpty() || result == null) {
			System.out.println("Não foi encontrado livros com este trecho de string!");
		} else {
			System.out.println("Resultado da busca de " + string);
			for(Book obj: result) {
				System.out.println("#" + obj.getId() + " - " + obj.getTitulo() + "/" + obj.getAutor());
			}
		}
	}

	public void findBookCategory(String categoria) {
		List<Book> result = bookDao.findBookCategory(categoria.toUpperCase());
		if(result == null || result.isEmpty()) {
			System.out.println("Não foi encontrado livros desta categoria!");
		} else {
			System.out.println("Livros de " + categoria);
			for(Book obj: result) {
				System.out.println("#" + obj.getId() + " - " + obj.getTitulo() + "/" + obj.getAutor());
			}
		}
	}

	public void listarLivrosNomeSaga() {
		List<SagaBook> books = bookDao.getAllBooksSaga();
		for(SagaBook obj: books) {
			System.out.println("#" + obj.getId() + ": " + obj.getTitulo() + " (" + obj.getAutor() + ") - " + obj.getSagaNome());
		}
	}
}
