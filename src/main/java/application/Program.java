package application;

import java.time.LocalDate;
import java.util.List;

import dao.BookDAO;
import dao.LoanDAO;
import dao.UserDAO;
import entities.Book;
import entities.Loan;
import entities.User;
import entities.enumeradores.Status;

public class Program {

	public static void main(String[] args) {
		BookDAO bookDao = new BookDAO();
	    UserDAO userDao = new UserDAO();
	    LoanDAO loanDao = new LoanDAO();

	    Book book = bookDao.findById(1); 
	    User user = userDao.findById(1);

	    if (book != null && user != null) {
	        
//	        System.out.println("--- Criando Novo Empréstimo ---");
//	        Loan novoEmprestimo = new Loan();
//	        novoEmprestimo.setLivro(book);
//	        novoEmprestimo.setUser(user);
//	        novoEmprestimo.setDataEmprestimo(LocalDate.now()); // ou data atual
//	        
	        // Se o seu Book tem status, lembre-se de mudar para EMPRESTADO
//	        book.setStatus(Status.EMPRESTADO);
//	        bookDao.updateBook(book);
//	        Loan novoEmprestimo = loanDao.findById(1);
//	        loanDao.saveLoan(novoEmprestimo);

	        // --- TESTE: READ ALL ---
	        System.out.println("\n--- Lista de Empréstimos Ativos ---");
	        List<Loan> lista = loanDao.getAllLoans();
	        for (Loan l : lista) {
	            System.out.println("ID: " + l.getId() + " | Livro: " + l.getLivro().getTitulo() + " | Usuário: " + l.getUser().getNome());
	        }
	        
	        Loan novoEmprestimo = loanDao.findById(1);
	        
	        if (novoEmprestimo.getDataDevolucaoReal() == null) {
	        	novoEmprestimo.setDataDevolucaoReal(LocalDate.now());
	        	Book b = novoEmprestimo.getLivro();
	            b.setStatus(Status.DISPONIVEL);
	            bookDao.updateBook(b);
	            
	            loanDao.updateLoan(novoEmprestimo);
	        }
	        // --- TESTE: UPDATE (Devolução) ---
//	        System.out.println("\n--- Realizando Devolução (Update) ---");
//	        Loan emprestimoParaDevolver = loanDao.findById(novoEmprestimo.getId());
//	        if (emprestimoParaDevolver != null) {
//	            emprestimoParaDevolver.setDataDevolucaoReal(LocalDate.now());
//	            
//	            // Atualiza o livro para disponível novamente
//	            Book b = emprestimoParaDevolver.getLivro();
//	            b.setStatus(Status.DISPONIVEL);
//	            bookDao.updateBook(b);
//	            
//	            loanDao.updateLoan(emprestimoParaDevolver);
//	        }

	        // --- TESTE: DELETE ---
	        // System.out.println("\n--- Removendo Registro de Empréstimo ---");
	        // loanDao.deleteLoan(novoEmprestimo);

	    } else {
	        System.err.println("Erro: Certifique-se de que o Livro ID 1 e o Usuário ID 1 existam no banco antes de testar!");
	    }
	}

}
