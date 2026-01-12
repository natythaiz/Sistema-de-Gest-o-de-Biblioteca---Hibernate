package application;

import dao.BookDAO;
import dao.LoanDAO;
import dao.UserDAO;
import entities.Loan;
import services.LoanService;

public class Program {

	public static void main(String[] args) {
		BookDAO bookDao = new BookDAO();
	    UserDAO userDao = new UserDAO();
	    LoanDAO loanDao = new LoanDAO();

	    LoanService loan = new LoanService();
	    loan.registrarEmprestimo(userDao.findById(2), bookDao.findById(4));
	    
//	    Loan emprestimo = loanDao.findById(5);
//	    loan.finalizarEmprestimo(emprestimo);
//	    loan.atualizarEmprestimo(emprestimo);
	}

}
