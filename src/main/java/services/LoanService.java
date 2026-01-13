package services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

    public void registrarEmprestimo(User usuario, Book livro) {
        if (livro.getStatus() != Status.DISPONIVEL) {
            System.out.println("Erro: Livro não está disponível para empréstimo.");
            return;
        }

        if (usuario == null || livro == null) {
            System.out.println("Erro: Usuário ou Livro não identificado.");
            return;
        }
        int qtd_Loan = loanDao.countLoanPerUser(usuario);
        if(qtd_Loan >= 5) {
        	System.out.println("Limite excedido: O usuário já possui 5 empréstimos ativos!");
            return;
        }

        // Se ok, cria o objeto de empréstimo
        Loan emprestimo = new Loan();
        emprestimo.setUser(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucaoPrevista(emprestimo.getDataEmprestimo().plusDays(usuario.getLimiteEmprestimo()));

        livro.setStatus(Status.EMPRESTADO);
        bookDao.updateBook(livro);

        loanDao.saveLoan(emprestimo);
    }
    
    public void finalizarEmprestimo(Loan emprestimo) {
        emprestimo.setDataDevolucaoReal(LocalDate.now());
        
        Reservation proxima = resDao.findNextInLine(emprestimo.getLivro());
        
        if (proxima != null) {
        	emprestimo.getLivro().setStatus(Status.RESERVADO); 
            System.out.println("--- NOTIFICAÇÃO ---");
            System.out.println("O livro ficou disponível, mas há uma RESERVA ativa!");
            System.out.println("Favor avisar: " + proxima.getUser().getNome());
        } else {
        	emprestimo.getLivro().setStatus(Status.DISPONIVEL);
        }
        
        bookDao.updateBook(emprestimo.getLivro());
        loanDao.updateLoan(emprestimo);
    }
    
    public void atualizarEmprestimo(Loan emprestimo) {
        if(emprestimo.getDataDevolucaoReal() == null) {
        	emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(emprestimo.getUser().getLimiteEmprestimo()));
            loanDao.updateLoan(emprestimo);
        } else {
        	System.out.println("Este empréstimo foi finalizado, não é possível alterar! Solicite um novo.");
            return;
        }
    }

	public void listLoanPerUser(int userID) {
		User user = userDao.findById(userID);
		List<Loan> result = loanDao.findLoanPerUser(user);
		if(result.isEmpty() || result == null) {
			System.out.println("Não foi encontrado empréstimos ativos para este usuário!");
		} else {
			System.out.println("Empréstimos ativos de " + result.getFirst().getUser().getNome());
			for(Loan obj: result) {
				System.out.println("#" + obj.getId() + " - " + obj.getLivro().getTitulo() + "(Data de requisição: " + obj.getDataEmprestimo() + ")");
			}
		}
	}

	public void findLoanLated() {
		List<Loan> result = loanDao.findLoanLated();
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
}
