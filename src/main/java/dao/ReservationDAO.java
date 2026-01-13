package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entities.Book;
import entities.HibernateUtil;
import entities.Reservation;
import entities.enumeradores.StatusReservation;

public class ReservationDAO {
// 		Save
	public void save(Reservation reservation) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(reservation);
            tx.commit();
            System.out.println("Reservation saved successfully.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
	
//	read all
	public List<Reservation> getAllReservation(){
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Reservation", Reservation.class).list();
        }
	}
	
//		Update
	public void update(Reservation res) {
		Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(res);
            tx.commit();
            System.out.println("Reservation updated successfully.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
	}
//		Delete
	public void deleteReservation(Reservation reservation) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(reservation);
            tx.commit();
            System.out.println("Reservation deleted successfully.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Reservation findNextInLine(Book book) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // HQL: Pega o PENDING mais antigo (ASC) para aquele livro
            String hql = "FROM Reservation r WHERE r.book = :book " +
                         "AND r.status = :status ORDER BY r.requestDate ASC";
            
            return session.createQuery(hql, Reservation.class)
                    .setParameter("book", book)
                    .setParameter("status", StatusReservation.PENDING)
                    .setMaxResults(1) // Garante que só vem o primeiro
                    .uniqueResult();
        }
    }

	public Reservation findById(int resId) {
		Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	tx = session.beginTransaction();
        	Reservation reservation = session.get(Reservation.class, resId);
            if (reservation != null) {
            	return reservation;
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return null;
	}

	public List<Reservation> usersReservatitionByBook(Book book) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM Reservation r WHERE r.book = :book AND r.status = StatusReservation.PENDING";
            
            List<Reservation> list = session.createQuery(hql, Reservation.class)
                                .setParameter("book", book)
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
