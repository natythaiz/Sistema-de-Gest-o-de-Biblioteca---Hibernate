package services;

import java.time.LocalDate;

import dao.BookDAO;
import dao.LoanDAO;
import entities.Book;
import entities.Loan;
import entities.User;
import entities.enumeradores.Status;

public class LoanService {
	private LoanDAO loanDao = new LoanDAO();
    private BookDAO bookDao = new BookDAO();

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
        if(qtd_Loan >= 4) {
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
        Book livro = emprestimo.getLivro();
        livro.setStatus(Status.DISPONIVEL);
        bookDao.updateBook(livro);

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
}
