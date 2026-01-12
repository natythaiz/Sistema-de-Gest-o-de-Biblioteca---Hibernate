package application;

import java.util.Scanner;
import services.*;
import entities.*;
import entities.enumeradores.*;
import dao.*;
import java.util.List;

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

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao = -1;

        while (opcao != 0) {
            exibirMenu();
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            try {
                switch (opcao) {
                    case 1: cadastrarLivro(); break;
                    case 2: cadastrarUsuario(); break;
                    case 3: realizarEmprestimo(); break;
                    case 4: devolverLivro(); break;
                    case 5: reservarLivro(); break;
                    case 6: confirmarReserva(); break;
                    case 7: listarLivros(); break;
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
}