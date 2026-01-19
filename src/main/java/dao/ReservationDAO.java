package dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import entities.Book;
import entities.HibernateUtil;
import entities.Reservation;
import entities.enumeradores.StatusReservation;

public class ReservationDAO {
// 		Save
	public void save(Reservation reservation, Session session) {
		session.persist(reservation);
    }
	
//	read all
	public List<Reservation> getAllReservation(Session session){
		return session.createQuery("from Reservation", Reservation.class).list();
	}
	
//		Update
	public void update(Reservation res, Session session) {
		session.merge(res);
	}
//		Delete
	public void deleteReservation(Reservation reservation, Session session) {
		session.remove(reservation);
    }

    public Reservation findNextInLine(Book book, Session session) {
    	String hql = "FROM Reservation r WHERE r.book = :book " +
                "AND r.status = :status ORDER BY r.requestDate ASC";
   
    	return session.createQuery(hql, Reservation.class)
           .setParameter("book", book)
           .setParameter("status", StatusReservation.PENDING)
           .setMaxResults(1) // Garante que só vem o primeiro
           .uniqueResult();
    }

	public Reservation findById(int resId, Session session) {
		Reservation reservation = session.get(Reservation.class, resId);
		return reservation;
	}

	public List<Reservation> usersReservatitionByBook(Book book, Session session) {
		String hql = "FROM Reservation r WHERE r.book = :book AND r.status = StatusReservation.PENDING";
        
        List<Reservation> reservationList = session.createQuery(hql, Reservation.class)
                            .setParameter("book", book)
                            .list();

        return reservationList;
	}

	public List<Reservation> bookReservationList(Book obj, Session session) {
		List<Reservation> reservationList = new ArrayList<Reservation>();
		String sql = "SELECT * FROM reservation r "
    			+ "WHERE r.book_id = :id";
        
        NativeQuery<Reservation> query = session.createNativeQuery(sql, Reservation.class);
        
        query.setParameter("id", obj.getId());

        reservationList = query.getResultList();

        return reservationList;
	}
}
