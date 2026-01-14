package services;

import java.util.Scanner;

import dao.BookDAO;
import dao.SagaDAO;
import entities.Book;
import entities.Saga;

public class SagaService {
	private SagaDAO sagaDao = new SagaDAO();
	private BookDAO bookDao = new BookDAO();
	private Scanner scanner = new Scanner(System.in);

	public void cadastrar() {
		System.out.print("Nome da saga: "); 
		String nome = scanner.nextLine();
		Saga saga = new Saga(nome);
		if (saga.getNome() == null) {
            throw new RuntimeException("O nome da saga é obrigatório.");
        }
		sagaDao.save(saga);
		return;
	}

	public void atualizarLivroSaga(int id) {
		Book book = bookDao.findById(id);
		if (book != null) {
           System.out.println("Digite o nome da saga que deseja adicionar ao livro " + book.getTitulo());
           String nomeSaga = scanner.nextLine();
           Saga saga = sagaDao.buscarSagaNome(nomeSaga);
           if(saga == null) {
        	   saga = new Saga(nomeSaga);
        	   sagaDao.save(saga);
           }
           book.setSaga(saga);
           bookDao.updateBook(book);
           System.out.println("Saga " + saga.getNome() + " adicionada ao livro " + book.getTitulo());
        } else {
            System.out.println("Livro não encontrado.");
        }
	}

}
