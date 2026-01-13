package application;

import java.util.Scanner;
import services.*;
import entities.*;
import entities.enumeradores.*;
import dao.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BibliotecaApp {
    // Instanciando os serviços
    private static BookService bookService = new BookService();
    private static UserService userService = new UserService();
    private static LoanService loanService = new LoanService();
    private static ReservationService resService = new ReservationService();
    
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
                    case 1: cadastrarLivro(); break;
                    case 2: cadastrarUsuario(); break;
                    case 3: realizarEmprestimo(); break;
                    case 4: devolverLivro(); break;
                    case 5: reservarLivro(); break;
                    case 6: confirmarReserva(); break;
                    case 7: listarLivros(); break;
                    case 8: listarEmprestimos(); break;
                    case 9: listaReservaLivro(); break;
                    case 10: emprestimosAtivosUser(); break;
                    case 11: buscaLivroString(); break;
                    case 12: emprestimoAtrasado(); break;
                    case 13: buscaLivroCategoria(); break;
                    case 14: buscaUserTipo(); break;
                    case 0: System.out.println("Saindo..."); break;
                    default: System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.err.println("ERRO: " + e.getMessage());
            }
        }
    }

	private static void exibirMenu() {
        System.out.println("\n========= SISTEMA DE BIBLIOTECA =========");
        System.out.println("1 - Cadastrar Livro");
        System.out.println("2 - Cadastrar Usuário");
        System.out.println("3 - Realizar Empréstimo");
        System.out.println("4 - Devolver Livro (Finalizar Empréstimo)");
        System.out.println("5 - Reservar Livro");
        System.out.println("6 - Confirmar Reserva (Tirar da Fila)");
        System.out.println("7 - Listar Livros");
        System.out.println("8 - Listar todos os Empréstimos de um usuário");
        System.out.println("9 - Mostrar lista de reserva de um livro");
        System.out.println("10 - Reservas ativas de um usuário");
        System.out.println("11 - Busca de livros (Título e Autor)");
        System.out.println("12 - Empréstimos atrasados");
        System.out.println("13 - Busca de livro por categoria");
        System.out.println("14 - Busca de usuário pelo tipo");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    // --- MÉTODOS DE AÇÃO ---

    private static void cadastrarLivro() {
        System.out.print("Título: "); String titulo = scanner.nextLine();
        System.out.print("Autor: "); String autor = scanner.nextLine();
        System.out.print("ISBN: "); int isbn = scanner.nextInt();
        
        Book novo = new Book(titulo, autor, isbn, Categoria.FICCAO);
        novo.setStatus(Status.DISPONIVEL);
        bookService.cadastrarLivro(novo);
    }

    private static void cadastrarUsuario() {
        System.out.print("Nome: "); String nome = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        
        User user = new User(nome, email, TipoUser.PROFESSOR);
        userDao.saveUser(user); 
    }

    private static void realizarEmprestimo() {
        System.out.print("ID do Usuário: "); int uId = scanner.nextInt();
        System.out.print("ID do Livro: "); int bId = scanner.nextInt();
        
        User u = userDao.findById(uId);
        Book b = bookDao.findById(bId);
        
        loanService.registrarEmprestimo(u, b);
    }

    private static void devolverLivro() {
        System.out.print("ID do Empréstimo a finalizar: ");
        int loanId = scanner.nextInt();
        
        Loan loan = loanDao.findById(loanId);
        if (loan != null) {
            loanService.finalizarEmprestimo(loan);
        } else {
            System.out.println("Empréstimo não encontrado.");
        }
    }

    private static void reservarLivro() {
        System.out.print("ID do Usuário: "); int uId = scanner.nextInt();
        System.out.print("ID do Livro: "); int bId = scanner.nextInt();
        
        User u = userDao.findById(uId);
        Book b = bookDao.findById(bId);
        
        resService.createReservation(u, b);
    }

    private static void confirmarReserva() {
        System.out.print("ID da Reserva a confirmar: ");
        int resId = scanner.nextInt();
        resService.confirmReservation(resId);
    }

    private static void listarLivros() {
        List<Book> livros = bookDao.getAllBooks();
        System.out.println("\n--- LISTA DE LIVROS ---");
        for (Book b : livros) {
            System.out.println("ID: " + b.getId() + " | Título: " + b.getTitulo() + " | Status: " + b.getStatus());
        }
    }
    
    private static void listarEmprestimos() {
    	System.out.print("ID do Usuário: "); 
    	int uId = scanner.nextInt();
    	User user = userDao.findById(uId);
    	List<Loan> result = loanDao.allLoanPerUser(user);
    	for(Loan l : result) {
    		System.out.println(" #" + l.getId() +" - "+ l.getLivro().getTitulo() + 
                    " | Data: " + l.getDataEmprestimo());
    	}
	}
    
    private static void listaReservaLivro() {
		System.out.println("ID do livro a pesquisar: ");
		int livroID = scanner.nextInt();
		resService.listReservationBook(livroID);
	}
    
    private static void emprestimosAtivosUser() {
    	System.out.println("ID do usuário para visualizar seu empréstimos ativos: ");
		int userID = scanner.nextInt();
		loanService.listLoanPerUser(userID);
	}

    private static void buscaLivroString() {
		System.out.println("Digite o fragmento de palavra que deseja pesquisar: ");
		String string = scanner.nextLine();
		bookService.findBookString(string);
	}
    
    private static void emprestimoAtrasado() {
		loanService.findLoanLated();
	}
    
    private static void buscaLivroCategoria() {
		System.out.println("Qual categoria deseja buscar: ");
		String categoria = scanner.nextLine();
		bookService.findBookCategory(categoria);
	}
    
    private static void buscaUserTipo() {
    	System.out.println("Qual tipo de usuário deseja buscar: ");
		String tipo = scanner.nextLine();
		userService.findUserType(tipo);
	}
}