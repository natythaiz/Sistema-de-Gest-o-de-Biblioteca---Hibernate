package application;

import java.util.Scanner;

import org.hibernate.Session;

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
	
    private static void cadastrarLivro() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            bookService.cadastrarLivro(session);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void cadastrarUsuario() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            userService.cadastrarUsuario(session);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void realizarEmprestimo() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            loanService.createLoan(session);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void devolverLivro() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            loanService.finishLoan(session);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void reservarLivro() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            resService.createReservation(session);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void confirmarReserva() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            resService.confirmReservation(session);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void listarLivros() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            bookService.listBook(session);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    private static void listarEmprestimos() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            loanService.listLoan(session);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
	}
    
    private static void listaReservaLivro() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            resService.listReservationBook(session);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
	}
    
    private static void emprestimosAtivosUser() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            loanService.listLoanPerUser(session);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
	}

    private static void buscaLivroString() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            bookService.findBookString(session);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
	}
    
    private static void emprestimoAtrasado() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            loanService.findLoanLated(session);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
	}
    
    private static void buscaLivroCategoria() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            bookService.findBookCategory(session);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
	}
    
    private static void buscaUserTipo() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            userService.findUserType(session);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
	}
}