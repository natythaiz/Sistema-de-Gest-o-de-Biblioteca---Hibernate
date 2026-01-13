package dao;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entities.Book;
import entities.HibernateUtil;
import entities.Loan;
import entities.Reservation;
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
	
//	count how many loans actives a user have
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
	
//	all loans per user
	public List<Loan> allLoanPerUser(User user) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM Loan l WHERE l.user = :user ";
            
            List<Loan> list = session.createQuery(hql, Loan.class)
                                .setParameter("user", user)
                                .list();

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            e.getStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
	}

	public List<Loan> findLoanPerUser(User user) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM Loan l WHERE l.user = :user AND l.dataDevolucaoReal IS NULL";
            
            List<Loan> list = session.createQuery(hql, Loan.class)
                                .setParameter("user", user)
                                .list();

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            e.getStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
	}

	public List<Loan> findLoanLated() {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM Loan l WHERE l.dataDevolucaoPrevista < l.dataDevolucaoReal OR (l.dataDevolucaoPrevista < :hoje AND l.dataDevolucaoReal IS NULL)";
            List<Loan> list = session.createQuery(hql, Loan.class)
            					.setParameter("hoje", LocalDate.now())
                                .list();

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            e.getStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
	}
}
