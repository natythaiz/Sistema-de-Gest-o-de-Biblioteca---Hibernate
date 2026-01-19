package application;

import java.util.Scanner;

import org.hibernate.Session;

import entities.HibernateUtil;
import services.BookService;
import services.SagaService;

public class Program {
	private static BookService bookService = new BookService();
    private static SagaService sagaService = new SagaService();
	
    private static Scanner scanner = new Scanner(System.in);
    
	public static void main(String[] args) {
		int opcao = -1;

        while (opcao != 0) {
            exibirMenu();
            opcao = scanner.nextInt();
            scanner.nextLine(); 

            try {
                switch (opcao) {
                    case 1: createSaga(); break;
                    case 2: updateBookFromSaga(); break;
                    case 3: listSaga(); break;
                    case 4: listBookBySaga();; break;
                    case 5: listBookFromSaga(); break;
                    case 6: listBookAndSaga(); break;
                    case 7: countBookFromSaga(); break;
                    case 8: listDateAvaliableLoan(); break;
                    case 0: System.out.println("Saindo..."); break;
                    default: System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.err.println("ERRO: " + e.getMessage());
            }
        }
	}
	
	private static void createSaga() {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            sagaService.create(session);
            
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
	
	private static void countBookFromSaga() {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            sagaService.countBookFromSaga(session);
            
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
	
	private static void listBookAndSaga() {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            bookService.listBookAndSaga(session);
            
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
	
	private static void listBookBySaga() {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            sagaService.listBookSaga(session);
            
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
	
	private static void listBookFromSaga() {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            sagaService.listBookFromSaga(session);
            
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
	private static void listSaga() {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            sagaService.listSaga(session);
            
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
	private static void listDateAvaliableLoan() {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            sagaService.listDateAvaliableLoan(session);
            
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
	
	private static void updateBookFromSaga() {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            sagaService.updateBookFromSaga(session);
            
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
	
	private static void exibirMenu() {
        System.out.println("\n========= SISTEMA DE BIBLIOTECA (Versão sagas) =========");
        System.out.println("1 - Cadastrar Saga");
        System.out.println("2 - Inserir saga em livro persistido");
        System.out.println("3 - Listar sagas disponíveis");
        System.out.println("4 - Listar livros de uma saga específica");
        System.out.println("5 - Listar livros de várias sagas selecionadas (múltiplos parâmetros)");
        System.out.println("6 - Listar livros + nome nome da saga");
        System.out.println("7 - Listar sagas e quantos livros cada uma tem");
        System.out.println("8 - Listar n sagas e retornar a data de possível empréstimo da saga inteira");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }
}
