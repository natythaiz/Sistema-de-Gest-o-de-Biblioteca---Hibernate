package application;

import java.util.Scanner;

import dao.BookDAO;
import dao.LoanDAO;
import dao.ReservationDAO;
import dao.UserDAO;
import entities.Saga;
import services.BookService;
import services.LoanService;
import services.ReservationService;
import services.SagaService;
import services.UserService;

public class Program {
	private static BookService bookService = new BookService();
    private static UserService userService = new UserService();
    private static LoanService loanService = new LoanService();
    private static ReservationService resService = new ReservationService();
    private static SagaService sagaService = new SagaService();
    
    // DAOs para buscas rápidas (usados aqui apenas para facilitar a busca por ID)
    private static BookDAO bookDao = new BookDAO();
    private static UserDAO userDao = new UserDAO();
    private static LoanDAO loanDao = new LoanDAO();
    private static ReservationDAO reservationDao = new ReservationDAO();
	
    private static Scanner scanner = new Scanner(System.in);
    
	public static void main(String[] args) {
		int opcao = -1;

        while (opcao != 0) {
            exibirMenu();
            opcao = scanner.nextInt();
            scanner.nextLine(); 

            try {
                switch (opcao) {
                    case 1: sagaService.cadastrar(); break;
                    case 2: atualizarLivroSaga(); break;
                    case 3: listarSaga(); break;
                    case 4: listarLivroSaga(); break;
                    case 5: listarLivroVariasSagas(); break;
                    case 6: listarLivrosNomeSaga(); break;
                    case 7: listarSagasContLivros(); break;
                    case 8: dataEmprestimoPossivelSagas(); break;
                    case 0: System.out.println("Saindo..."); break;
                    default: System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.err.println("ERRO: " + e.getMessage());
            }
        }
	}
	
	private static void exibirMenu() {
        System.out.println("\n========= SISTEMA DE BIBLIOTECA (Versão sagas) =========");
        System.out.println("1 - Cadastrar Saga");
        System.out.println("2 - Inserir saga em livro persistido");
        System.out.println("3 - Listar sagas disponíveis");
        System.out.println("4 - Listar livro de uma saga específica");
        System.out.println("5 - Listar livros de várias sagas selecionadas (múltiplos parâmetros)");
        System.out.println("6 - Listar livros + nome nome da saga");
        System.out.println("7 - Listar sagas e quantos livros cada uma tem");
        System.out.println("8 - Listar n sagas e retornar a data de possível empréstimo da saga inteira");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

	private static void dataEmprestimoPossivelSagas() {
		// TODO Auto-generated method stub
		
	}

	private static void listarSagasContLivros() {
		// TODO Auto-generated method stub
		
	}

	private static void listarLivrosNomeSaga() {
		// TODO Auto-generated method stub
		
	}

	private static void listarLivroVariasSagas() {
		// TODO Auto-generated method stub
		
	}

	private static void listarLivroSaga() {
		// TODO Auto-generated method stub
		
	}

	private static void listarSaga() {
		// TODO Auto-generated method stub
		
	}

	private static void atualizarLivroSaga() {
		System.out.print("ID do livro para atualização: "); 
		int id = scanner.nextInt();
		sagaService.atualizarLivroSaga(id);
	}

}
