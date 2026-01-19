package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.hibernate.Session;

import DTO.SagaDisponibilidade;
import dao.BookDAO;
import dao.LoanDAO;
import dao.ReservationDAO;
import dao.SagaDAO;
import entities.Book;
import entities.Loan;
import entities.Reservation;
import entities.Saga;
import entities.enumeradores.Status;

public class SagaService {
	private SagaDAO sagaDao = new SagaDAO();
	private BookDAO bookDao = new BookDAO();
	private ReservationDAO reservationDAO = new ReservationDAO();
	private LoanDAO loanDAO = new LoanDAO();
	private Scanner scanner = new Scanner(System.in);

	public void create(Session session) {
		scanner.nextLine();
		System.out.print("Nome da saga: "); 
		String name = scanner.nextLine();
		if(name.isEmpty() || name == null) {
			System.out.println("O nome da saga é um campo obrigatório!");
		} else {
			Saga saga = new Saga(name);
			if (saga.getNome() == null) {
	            throw new RuntimeException("O nome da saga é obrigatório.");
	        }
			sagaDao.save(saga, session);
		}
		return;
	}

	public void updateBookFromSaga(Session session) {
		System.out.print("ID do livro para atualização: "); 
		int id = scanner.nextInt();
		Book book = bookDao.findById(id, session);
		if (book != null) {
           System.out.println("Digite o nome da saga que deseja adicionar ao livro " + book.getTitulo());
           String nomeSaga = scanner.nextLine();
           Saga saga = sagaDao.findySagaByName(nomeSaga, session);
           if(saga == null) {
        	   saga = new Saga(nomeSaga);
        	   sagaDao.save(saga, session);
           }
           book.setSaga(saga);
           bookDao.updateBook(book, session);
           System.out.println("Saga " + saga.getNome() + " adicionada ao livro " + book.getTitulo());
        } else {
            System.out.println("Livro não encontrado.");
        }
	}

	public void listSaga(Session session) {
		List<Saga> sagas = sagaDao.getAll(session);
		if(sagas != null) {
			System.out.println("Total de sagas encontradas: " + sagas.size());
		    for (Saga obj : sagas) {
		        System.out.println("ID: " + obj.getId() + ", Nome: " + obj.getNome());
		    }
		} else {
			System.out.println("Erro ao encontrar as sagas!");
		}
	}

	public void listBookSaga(Session session) {
		scanner.nextLine();
		System.out.println("Digite o nome da saga:");
		String nomeSaga = scanner.nextLine();
		Saga saga = sagaDao.findySagaByName(nomeSaga, session);
		if(saga == null) {
     	   System.out.println("Saga não encontrada no banco de dados, digite corretamente o nome (use a lista como parâmetro)!");
     	   return;
        }
        List<Book> results = bookDao.findBookSaga(saga, session);
        if(results.isEmpty()) {
        	System.out.println("A saga '" + saga.getNome() + "' não possui livros cadastrados!");
        } else {
        	System.out.println("Livros da saga " + saga.getNome());
            for (Book obj : results) {
    	        System.out.println(obj.getTitulo() + " (" + obj.getAutor() + ")");
    	    }
        }
	}
	
	public List<Long> getManySaga(Session session) {
		System.out.println("Selecione o id das sagas que deseja pesquisar, e 0 para finalizar a busca: ");
		List<Long> sagasId = new ArrayList<Long>();
		Long opc = (long) -1;
		List<Saga> sagas = sagaDao.getAll(session);
		if(sagas != null) {
			System.out.println("Sagas disponíveis: ");
		    for (Saga obj : sagas) {
		        System.out.println("ID: " + obj.getId() + ", Nome: " + obj.getNome());
		    }
		    while(opc!=0) {
				System.out.println("Digite o id de busca (ou 0 para sair): ");
				Long id = scanner.nextLong();
				if(opc != 0) {
					sagasId.add(id);
				}
				opc = id;
			}
		} else {
			System.out.println("Erro ao encontrar as sagas!");
		}
		return sagasId;
	}

	public void listBookFromSaga(Session session) {
		List<Long> sagaList = getManySaga(session);
		List<Book> book = bookDao.findBooksManySagas(sagaList, session);
	    if(book.isEmpty()) {
	    	System.out.println("Não encontramos livros das sagas selecionadas!");
	    } else {
	    	System.out.println("Resultado encontrado: ");
		    for (Book obj : book) {
		        System.out.println("ID: " + obj.getId() + ", Titulo: " + obj.getTitulo() + " - saga: " + obj.getSaga().getNome());
		    }
	    }
	}

	public void countBookFromSaga(Session session) {
			Map<String, Long> sagaCountList = new HashMap<>();
			sagaCountList = sagaDao.countBookSaga(session);
			for (String nome : sagaCountList.keySet()) {
			    Long total = sagaCountList.get(nome);
			    
			    System.out.println(nome + ": " + total + " livros");
			}
	}

	public void listDateAvaliableLoan(Session session) {
		List<Long> sagaListID = getManySaga(session);
		List<Saga> sagaList = sagaDao.findSagasById(sagaListID, session);
		if(sagaList.isEmpty()) {
			System.out.println("Sem resultados de busca das sagas selecionadas!");
		} else {
			List<SagaDisponibilidade> sagaDisponibilidadeList = new ArrayList<>();
			String str = "";
			for(int i=0; i<sagaList.size(); i++) {
				List<Book> bookList = bookDao.findBookSaga(sagaList.get(i), session);
				if(bookList.isEmpty()) {
					str += "Não há livros cadastrados nesta saga! (" + sagaList.get(i).getNome() + ") \n";
					continue;
				}
		        List<LocalDate> dataList = findDate(bookList, session);
		        LocalDate dateMax = Collections.max(dataList);
		        SagaDisponibilidade sagaDisponibilidade = new SagaDisponibilidade(sagaList.get(i).getNome(), dateMax);
		        sagaDisponibilidadeList.add(sagaDisponibilidade);
			}
			System.out.print(str);
			for(SagaDisponibilidade obj: sagaDisponibilidadeList) {
				System.out.println(obj);
			}
		}
	}

	private List<LocalDate> findDate(List<Book> bookList, Session session) {
		List<LocalDate> dateList = new ArrayList<>();
		for(Book obj: bookList) {
			if(obj.getStatus() == Status.DISPONIVEL) {
				dateList.add(LocalDate.now());
			} else {
				List<Reservation> reservationList = reservationDAO.bookReservationList(obj, session);
				Loan loan = loanDAO.findLoanBook(obj, session);
				LocalDate dateReturn = loan.getDataDevolucaoPrevista();
				
				if(reservationList.isEmpty()) {
					dateList.add(dateReturn);
				} else {
					for(int i=0; i<reservationList.size(); i++) {
						int time = reservationList.get(i).getUser().getTipo().getPrazoEmprestimo();
						dateReturn = dateReturn.plusDays(time);
					}
					dateList.add(dateReturn);
				}
			}
		}
		return dateList;
	}
}
