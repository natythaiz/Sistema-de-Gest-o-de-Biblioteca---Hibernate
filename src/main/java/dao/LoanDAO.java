package dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import entities.Book;
import entities.HibernateUtil;
import entities.Loan;
import entities.Reservation;
import entities.User;

public class LoanDAO {
//	create
	public void saveLoan(Loan emprestimo, Session session) {
		session.persist(emprestimo);
    }
	
//	read all
	public List<Loan> getAllLoans(Session session){
		return session.createQuery("from Loan", Loan.class).list();
	}
	
//	update
	public void updateLoan(Loan emprestimo, Session session) {
		session.merge(emprestimo);
    }
	
	public Loan findById(int id, Session session) {
		Loan loan = session.get(Loan.class, id);
		return loan;
    }
	
//	delete
	public void deleteLoan(Loan emprestimo, Session session) {
		session.remove(emprestimo);
    }
	
//	count how many loans actives a user have
	public int countLoanPerUser(User user, Session session) {
		String hql = "SELECT COUNT(l) FROM Loan l WHERE l.user = :user AND l.dataDevolucaoReal IS NULL";
        
        Long count = session.createQuery(hql, Long.class)
                            .setParameter("user", user)
                            .uniqueResult();
        return count != null ? count.intValue() : 0;
	}
	
//	all loans per user
	public List<Loan> allLoanPerUser(User user, Session session) {
		String hql = "FROM Loan l WHERE l.user = :user ";
        
        List<Loan> loanList = session.createQuery(hql, Loan.class)
                            .setParameter("user", user)
                            .list();

        return loanList;
	}

	public List<Loan> findLoanPerUser(User user, Session session) {
		String hql = "FROM Loan l WHERE l.user = :user AND l.dataDevolucaoReal IS NULL";
        
        List<Loan> loanList = session.createQuery(hql, Loan.class)
                            .setParameter("user", user)
                            .list();

        return loanList;
	}

	public List<Loan> findLoanLated(Session session) {
		String hql = "FROM Loan l WHERE l.dataDevolucaoPrevista < l.dataDevolucaoReal OR (l.dataDevolucaoPrevista < :hoje AND l.dataDevolucaoReal IS NULL)";
        List<Loan> loanList = session.createQuery(hql, Loan.class)
        					.setParameter("hoje", LocalDate.now())
                            .list();

        return loanList;
	}

	public Loan findLoanBook(Book obj, Session session) {
		Loan loan = new Loan();
		String sql = "SELECT * FROM loans l "
 			   + "WHERE l.book_id = :id AND dataDevolucaoReal IS NULL";
     
	 	loan = session.createNativeQuery(sql, Loan.class)
	             .setParameter("id", obj.getId())
	             .uniqueResult(); 
	
	 	return loan;
	}
}
