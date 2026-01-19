package services;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;

import DTO.SagaBook;
import dao.BookDAO;
import entities.Book;
import entities.enumeradores.Categoria;
import entities.enumeradores.Status;

public class BookService {
	private BookDAO bookDao = new BookDAO();
	private Scanner scanner = new Scanner(System.in);

    public void cadastrarLivro(Session session) {
    	System.out.print("Título: "); 
        String titulo = scanner.nextLine();
        System.out.print("Autor: "); 
        String autor = scanner.nextLine();
        System.out.print("ISBN: "); 
        int isbn = scanner.nextInt();
        
        Book book = new Book(titulo, autor, isbn, Categoria.FICCAO);
        book.setStatus(Status.DISPONIVEL);
        if (book.getTitulo() == null || book.getTitulo().isEmpty()) {
            throw new RuntimeException("O título do livro é obrigatório.");
        }
        if (book.getIsbn() <= 0) {
            throw new RuntimeException("ISBN inválido.");
        }
        Book livroExistente = bookDao.findByIsbn(isbn, session);
        if (livroExistente != null) {
            throw new RuntimeException("Falha ao cadastrar: Já existe um livro com o ISBN " + book.getIsbn());
        }
        
        bookDao.saveBook(book, session);
    }

	public void findBookString(Session session) {
		System.out.println("Digite o fragmento de palavra que deseja pesquisar: ");
		String string = scanner.nextLine();
		List<Book> bookList = bookDao.findBookString(string, session);
		if(bookList.isEmpty() || bookList == null) {
			System.out.println("Não foi encontrado livros com este trecho de string!");
		} else {
			System.out.println("Resultado da busca de " + string);
			for(Book obj: bookList) {
				System.out.println("#" + obj.getId() + " - " + obj.getTitulo() + "/" + obj.getAutor());
			}
		}
	}

	public void findBookCategory(Session session) {
		System.out.println("Qual categoria deseja buscar: ");
		String categoria = scanner.nextLine();
		List<Book> bookList = bookDao.findBookCategory(categoria.toUpperCase(), session);
		if(bookList == null || bookList.isEmpty()) {
			System.out.println("Não foi encontrado livros desta categoria!");
		} else {
			System.out.println("Livros de " + categoria);
			for(Book obj: bookList) {
				System.out.println("#" + obj.getId() + " - " + obj.getTitulo() + "/" + obj.getAutor());
			}
		}
	}

	public void listBookAndSaga(Session session) {
		List<SagaBook> sagaBookList = bookDao.getAllBooksSaga(session);
		for(SagaBook obj: sagaBookList) {
			System.out.println(obj);
		}
	}

	public void listBook(Session session) {
		List<Book> bookList = bookDao.getAllBooks(session);
        System.out.println("\n--- LISTA DE LIVROS ---");
        for (Book b : bookList) {
            System.out.println("ID: " + b.getId() + " | Título: " + b.getTitulo() + " | Status: " + b.getStatus());
        }
	}
}
