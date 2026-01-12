package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entities.HibernateUtil;
import entities.Loan;
import entities.User;

public class LoanDAO {
//	create
	public void saveLoan(Loan emprestimo) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(emprestimo);
            tx.commit();
            System.out.println("Loan saved successfully.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
	
//	read all
	public List<Loan> getAllLoans(){
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Loan", Loan.class).list();
        }
	}
	
//	update
	public void updateLoan(Loan emprestimo) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(emprestimo);
            tx.commit();
            System.out.println("Loan updated successfully.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
	
	public Loan findById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	tx = session.beginTransaction();
            Loan loan = session.get(Loan.class, id);
            if (loan != null) {
            	return loan;
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return null;
    }
	
//	delete
	public void deleteLoan(Loan emprestimo) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(emprestimo);
            tx.commit();
            System.out.println("Loan deleted successfully.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
	
//	count how many loans a user have
	public int countLoanPerUser(User user) {
		Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            String hql = "SELECT COUNT(l) FROM Loan l WHERE l.user = :user AND l.dataDevolucaoReal IS NULL";
            
            Long count = session.createQuery(hql, Long.class)
                                .setParameter("user", user)
                                .uniqueResult();
            tx.commit();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return 0;
        }
	}
}
