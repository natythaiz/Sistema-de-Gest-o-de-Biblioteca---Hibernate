package services;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;

import dao.BookDAO;
import dao.ReservationDAO;
import dao.UserDAO;
import entities.Book;
import entities.Reservation;
import entities.User;
import entities.enumeradores.Status;
import entities.enumeradores.StatusReservation;

public class ReservationService {
    private ReservationDAO resDao = new ReservationDAO();
    private BookDAO bookDao = new BookDAO();
    private UserDAO userDao = new UserDAO();
    private Scanner scanner = new Scanner(System.in);

    public void createReservation(Session session) {
    	System.out.print("ID do Usuário: "); 
    	int uId = scanner.nextInt();
        System.out.print("ID do Livro: "); 
        int bId = scanner.nextInt();
        
        User user = userDao.findById(uId, session);
        Book book = bookDao.findById(bId, session);
        if (book.getStatus() == Status.DISPONIVEL) {
            throw new RuntimeException("Livro está disponível. Faça o empréstimo direto!");
        }

        Reservation res = new Reservation(book, user);
        resDao.save(res, session); 
        System.out.println("Reserva confirmada para " + user.getNome() + " na fila de " + book.getTitulo());
    }

    public void cancelReservation(int resId, Session session) {
        Reservation res = resDao.findById(resId, session);
        if (res != null) {
            res.setStatus(StatusReservation.CANCELED);
            resDao.update(res, session);
        }
    }
    
    public void confirmReservation(Session session) {
    	System.out.print("ID da Reserva a confirmar: ");
        int resId = scanner.nextInt();
        Reservation res = resDao.findById(resId, session);
        
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
            
            loanService.registrarEmprestimo(usuario, livro, session);

            res.setStatus(StatusReservation.COMPLETED);
            resDao.update(res, session);
            
            System.out.println("Reserva convertida em empréstimo com sucesso!");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter reserva em empréstimo: " + e.getMessage());
        }
    }

	public void listReservationBook(Session session) {
		System.out.println("ID do livro a pesquisar: ");
		int livroID = scanner.nextInt();
		Book book = bookDao.findById(livroID, session);
		if(book == null) {
			System.out.println("Não foi encontrado um livro com este ID!");
			return;
		}
		List<Reservation> result = resDao.usersReservatitionByBook(book, session);
    	if(result.isEmpty() || result == null) {
    		System.out.println("Lista de retorno vazia, não há empréstimos deste livro!");
    	} else {
    		for(Reservation r : result) {
        		System.out.println(" #" + r.getId() +" - "+ r.getUser().getNome() + ". (" + r.getRequestDate()+ ")");
        	}
    	}
	}
}
