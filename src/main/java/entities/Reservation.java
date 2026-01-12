package entities;

import java.time.LocalDateTime;

import entities.enumeradores.StatusReservation;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservation")
public class Reservation {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne 
    @JoinColumn(name = "book_id")
	private Book book;
	@ManyToOne 
    @JoinColumn(name = "user_id") 
	private User user;
	private LocalDateTime requestDate;
	private StatusReservation status;
	
	public Reservation() {}

	public Reservation(Book book, User user) {
		this.book = book;
		this.user = user;
		this.requestDate = LocalDateTime.now();
		this.status = StatusReservation.PENDING;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDateTime requestDate) {
		this.requestDate = requestDate;
	}

	public StatusReservation getStatus() {
		return status;
	}

	public void setStatus(StatusReservation status) {
		this.status = status;
	}
}
