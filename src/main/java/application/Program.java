package application;

import dao.BookDAO;
import dao.LoanDAO;
import dao.UserDAO;
import entities.Book;
import entities.Loan;
import entities.User;
import services.BookService;
import services.LoanService;
import services.ReservationService;

public class Program {

	public static void main(String[] args) {
		BookDAO bookDao = new BookDAO();
	    UserDAO userDao = new UserDAO();
	    LoanDAO loanDao = new LoanDAO();

	    BookService bookService = new BookService();
	    LoanService loanService = new LoanService();
	    ReservationService resService = new ReservationService();
	    
	    Book livro = bookDao.findById(1);
	    User usuarioA = userDao.findById(1);
	    User usuarioB = userDao.findById(2);

	    System.out.println("--- INÍCIO DO TESTE ---");

	    // 2. Usuário A faz o empréstimo (Livro fica EMPRESTADO)
	    //loanService.registrarEmprestimo(usuarioA, livro);
	    
	    // 3. Usuário B tenta reservar o mesmo livro
	    // O Service deve permitir porque o status não é DISPONIVEL
	    //resService.createReservation(usuarioB, livro);

	    // 4. Usuário A devolve o livro
	    // Aqui o console deve imprimir a NOTIFICAÇÃO que o Usuário B está na fila
//	    Loan loanAtivo = loanDao.findById(7); // Crie um método pra achar o empréstimo atual
//	    loanService.finalizarEmprestimo(loanAtivo);

	    // 5. O Bibliotecário confirma a reserva para o Usuário B
	    // Busque o ID da reserva criada para o usuário B no banco
	    int idReserva = 1; // substitua pelo ID real gerado
	    resService.confirmReservation(idReserva);
	    
	    System.out.println("--- FIM DO TESTE ---");
	}

}
