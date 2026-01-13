package services;

import java.util.List;

import dao.BookDAO;
import dao.ReservationDAO;
import entities.Book;
import entities.Reservation;
import entities.User;
import entities.enumeradores.Status;
import entities.enumeradores.StatusReservation;

public class ReservationService {
    private ReservationDAO resDao = new ReservationDAO();
    private BookDAO bookDao = new BookDAO();

    public void createReservation(User user, Book book) {
        if (book.getStatus() == Status.DISPONIVEL) {
            throw new RuntimeException("Livro está disponível. Faça o empréstimo direto!");
        }

        // O usuário já está na fila desse livro? (Evita duplicidade)
        // método no DAO para checar isso

        Reservation res = new Reservation(book, user);
        resDao.save(res); 
        System.out.println("Reserva confirmada para " + user.getNome() + " na fila de " + book.getTitulo());
    }

    public void cancelReservation(int resId) {
        Reservation res = resDao.findById(resId);
        if (res != null) {
            res.setStatus(StatusReservation.CANCELED);
            resDao.update(res);
        }
    }
    
    public void confirmReservation(int resId) {
        Reservation res = resDao.findById(resId);
        
        if (res == null) {
            throw new RuntimeException("Reserva não encontrada!");
        }
        if (res.getStatus() != StatusReservation.PENDING) {
            throw new RuntimeException("Esta reserva não está mais pendente.");
        }

        Book livro = res.getBook();
        User usuario = res.getUser();

        try {
            LoanService loanService = new LoanService();
            livro.setStatus(Status.DISPONIVEL);
            
            loanService.registrarEmprestimo(usuario, livro);

            res.setStatus(StatusReservation.COMPLETED);
            resDao.update(res);
            
            System.out.println("Reserva convertida em empréstimo com sucesso!");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter reserva em empréstimo: " + e.getMessage());
        }
    }

	public void listReservationBook(int livroID) {
		Book book = bookDao.findById(livroID);
		if(book == null) {
			System.out.println("Não foi encontrado um livro com este ID!");
			return;
		}
		List<Reservation> result = resDao.usersReservatitionByBook(book);
    	if(result.isEmpty() || result == null) {
    		System.out.println("Lista de retorno vazia, não há empréstimos deste livro!");
    	} else {
    		for(Reservation r : result) {
        		System.out.println(" #" + r.getId() +" - "+ r.getUser().getNome() + ". (" + r.getRequestDate()+ ")");
        	}
    	}
	}
}
