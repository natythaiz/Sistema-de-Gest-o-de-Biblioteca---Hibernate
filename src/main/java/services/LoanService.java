package services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;

import dao.BookDAO;
import dao.LoanDAO;
import dao.ReservationDAO;
import dao.UserDAO;
import entities.Book;
import entities.Loan;
import entities.Reservation;
import entities.User;
import entities.enumeradores.Status;

public class LoanService {
	private LoanDAO loanDao = new LoanDAO();
	private UserDAO userDao = new UserDAO();
    private BookDAO bookDao = new BookDAO();
    private ReservationDAO resDao = new ReservationDAO();
    private Scanner scanner = new Scanner(System.in);

    public void registrarEmprestimo(User usuario, Book livro, Session session) {
        if (livro.getStatus() != Status.DISPONIVEL) {
            System.out.println("Erro: Livro não está disponível para empréstimo.");
            return;
        }

        if (usuario == null || livro == null) {
            System.out.println("Erro: Usuário ou Livro não identificado.");
            return;
        }
        int qtd_Loan = loanDao.countLoanPerUser(usuario, session);
        if(qtd_Loan >= 5) {
        	System.out.println("Limite excedido: O usuário já possui 5 empréstimos ativos!");
            return;
        }

        // Se ok, cria o objeto de empréstimo
        Loan loan = new Loan();
        loan.setUser(usuario);
        loan.setLivro(livro);
        loan.setDataEmprestimo(LocalDate.now());
        loan.setDataDevolucaoPrevista(loan.getDataEmprestimo().plusDays(usuario.getLimiteEmprestimo()));

        livro.setStatus(Status.EMPRESTADO);
        bookDao.updateBook(livro, session);

        loanDao.saveLoan(loan, session);
    }
    
    public void finalizarloan(Loan loan, Session session) {
        loan.setDataDevolucaoReal(LocalDate.now());
        
        Reservation proxima = resDao.findNextInLine(loan.getLivro(), session);
        
        if (proxima != null) {
        	loan.getLivro().setStatus(Status.RESERVADO); 
            System.out.println("--- NOTIFICAÇÃO ---");
            System.out.println("O livro ficou disponível, mas há uma RESERVA ativa!");
            System.out.println("Favor avisar: " + proxima.getUser().getNome());
        } else {
        	loan.getLivro().setStatus(Status.DISPONIVEL);
        }
        
        bookDao.updateBook(loan.getLivro(), session);
        loanDao.updateLoan(loan, session);
    }
    
    public void atualizarloan(Loan loan, Session session) {
        if(loan.getDataDevolucaoReal() == null) {
        	loan.setDataDevolucaoPrevista(LocalDate.now().plusDays(loan.getUser().getLimiteEmprestimo()));
            loanDao.updateLoan(loan, session);
        } else {
        	System.out.println("Este empréstimo foi finalizado, não é possível alterar! Solicite um novo.");
            return;
        }
    }

	public void listLoanPerUser(Session session) {
		System.out.println("ID do usuário para visualizar seu empréstimos ativos: ");
		int userID = scanner.nextInt();
		User user = userDao.findById(userID, session);
		List<Loan> result = loanDao.findLoanPerUser(user, session);
		if(result.isEmpty() || result == null) {
			System.out.println("Não foi encontrado empréstimos ativos para este usuário!");
		} else {
			System.out.println("Empréstimos ativos de " + result.getFirst().getUser().getNome());
			for(Loan obj: result) {
				System.out.println("#" + obj.getId() + " - " + obj.getLivro().getTitulo() + "(Data de requisição: " + obj.getDataEmprestimo() + ")");
			}
		}
	}

	public void findLoanLated(Session session) {
		List<Loan> result = loanDao.findLoanLated(session);
		if(result.isEmpty() || result == null) {
			System.out.println("Não há empréstimos atrasados!");
		} else {
			System.out.println("Empréstimos atrasados");
				for(Loan obj: result) {
					LocalDate fimParaCalculo;
			        String situacao;
	
			        if (obj.getDataDevolucaoReal() == null) {
			            situacao = "EM ABERTO";
			            fimParaCalculo = LocalDate.now(); 
			        } else {
			            situacao = "ENTREGUE COM ATRASO";
			            fimParaCalculo = obj.getDataDevolucaoReal(); 
			        }
	
			        long diasAtrasados = ChronoUnit.DAYS.between(obj.getDataDevolucaoPrevista(), fimParaCalculo);
	
			        System.out.println("# " + obj.getLivro().getTitulo() + 
			                           " | Usuário: " + obj.getUser().getNome() +
			                           " | Atraso: " + diasAtrasados + " dias (" + situacao + ")");
			  }
		}
	}

	public void createLoan(Session session) {
		System.out.print("ID do Usuário: "); 
        int uId = scanner.nextInt();
        System.out.print("ID do Livro: "); 
        int bId = scanner.nextInt();
        
        User user = userDao.findById(uId, session);
        Book book = bookDao.findById(bId, session);
        
        registrarEmprestimo(user, book, session);
	}

	public void finishLoan(Session session) {
		System.out.print("ID do Empréstimo a finalizar: ");
        int loanId = scanner.nextInt();
        
        Loan loan = loanDao.findById(loanId, session);
        if (loan != null) {
            finalizarloan(loan, session);
        } else {
            System.out.println("Empréstimo não encontrado.");
        }
	}

	public void listLoan(Session session) {
		System.out.print("ID do Usuário: "); 
    	int uId = scanner.nextInt();
    	User user = userDao.findById(uId, session);
    	List<Loan> result = loanDao.allLoanPerUser(user, session);
    	for(Loan l : result) {
    		System.out.println(" #" + l.getId() +" - "+ l.getLivro().getTitulo() + 
                    " | Data: " + l.getDataEmprestimo());
    	}
	}
}
