package services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public void cadastrar() {
		scanner.nextInt();
		System.out.print("Nome da saga: "); 
		String nome = scanner.nextLine();
		Saga saga = new Saga(nome);
		if (saga.getNome() == null) {
            throw new RuntimeException("O nome da saga é obrigatório.");
        }
		sagaDao.save(saga);
		return;
	}

	public void atualizarLivroSaga(int id) {
		Book book = bookDao.findById(id);
		if (book != null) {
           System.out.println("Digite o nome da saga que deseja adicionar ao livro " + book.getTitulo());
           String nomeSaga = scanner.nextLine();
           Saga saga = sagaDao.buscarSagaNome(nomeSaga);
           if(saga == null) {
        	   saga = new Saga(nomeSaga);
        	   sagaDao.save(saga);
           }
           book.setSaga(saga);
           bookDao.updateBook(book);
           System.out.println("Saga " + saga.getNome() + " adicionada ao livro " + book.getTitulo());
        } else {
            System.out.println("Livro não encontrado.");
        }
	}

	public void listarSaga() {
		List<Saga> sagas = sagaDao.getAll();
		if(sagas != null) {
			System.out.println("Total de sagas encontradas: " + sagas.size());
		    for (Saga obj : sagas) {
		        System.out.println("ID: " + obj.getId() + ", Nome: " + obj.getNome());
		    }
		} else {
			System.out.println("Erro ao encontrar as sagas!");
		}
	}

	public void listarLivroSaga() {
		scanner.nextLine();
		System.out.println("Digite o nome da saga:");
		String nomeSaga = scanner.nextLine();
		Saga saga = sagaDao.buscarSagaNome(nomeSaga);
		if(saga == null) {
     	   System.out.println("Saga não encontrada no banco de dados, digite corretamente o nome (use a lista como parâmetro)!");
     	   return;
        }
        List<Book> results = bookDao.findBookSaga(saga);
        if(results.isEmpty()) {
        	System.out.println("A saga '" + saga.getNome() + "' não possui livros cadastrados!");
        } else {
        	System.out.println("Livros da saga " + saga.getNome());
            for (Book obj : results) {
    	        System.out.println(obj.getTitulo() + " (" + obj.getAutor() + ")");
    	    }
        }
	}
	
	public List<Long> pegarVariasSagas() {
		System.out.println("Selecione o id das sagas que deseja pesquisar, e 0 para finalizar a busca: ");
		List<Long> sagasId = new ArrayList<Long>();
		Long opc = (long) -1;
		List<Saga> sagas = sagaDao.getAll();
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

	public void listarLivroVariasSagas() {
		List<Book> book = bookDao.findBooksManySagas(pegarVariasSagas());
	    if(book.isEmpty()) {
	    	System.out.println("Não encontramos livros das sagas selecionadas!");
	    } else {
	    	System.out.println("Resultado encontrado: ");
		    for (Book obj : book) {
		        System.out.println("ID: " + obj.getId() + ", Titulo: " + obj.getTitulo() + " - saga: " + obj.getSaga().getNome());
		    }
	    }
	}

	public void listarSagasContLivros() {
			Map<String, Long> resultado = new HashMap<>();
			resultado = sagaDao.countBookSaga();
			for (String nome : resultado.keySet()) {
			    Long total = resultado.get(nome);
			    
			    System.out.println(nome + ": " + total + " livros");
			}
	}

	public void dataEmprestimoPossivelSagas() {
		List<Saga> sagas = sagaDao.findSagasById(pegarVariasSagas());
		if(sagas.isEmpty()) {
			System.out.println("Você selecionou uma saga que não existe!");
		} else {
			List<SagaDisponibilidade> valores = new ArrayList<>();
			String str = "";
			for(int i=0; i<sagas.size(); i++) {
				List<Book> results = bookDao.findBookSaga(sagas.get(i));
				if(results.isEmpty()) {
					str += "Não há livros cadastrados nesta saga! (" + sagas.get(i).getNome() + ")";
					continue;
				}
		        List<LocalDate> data = findDate(results);
		        LocalDate dataMaxima = Collections.max(data);
		        SagaDisponibilidade novo = new SagaDisponibilidade(sagas.get(i).getNome(), dataMaxima);
		        valores.add(novo);
			}
			System.out.println(str);
			for(SagaDisponibilidade obj: valores) {
				System.out.println(obj);
			}
		}
	}

	private List<LocalDate> findDate(List<Book> results) {
		List<LocalDate> data = new ArrayList<LocalDate>();
		for(Book obj: results) {
			if(obj.getStatus() == Status.DISPONIVEL) {
				data.add(LocalDate.now());
			} else {
				List<Reservation> reservas = reservationDAO.isReservation(obj);
				Loan loan = loanDAO.findLoanBook(obj);
				LocalDate devolucao = loan.getDataDevolucaoPrevista();
				
//				1° opção: está emprestado e não está na lista de reserva
				if(reservas.isEmpty()) {
					data.add(devolucao);
				} else {
//					2° opcação está emprestado e está na lista de reserva 
//					ai tem que ser feito uma interação dos valores baseado no tipo de usuário
					for(int i=0; i<reservas.size(); i++) {
						int dia = reservas.get(i).getUser().getTipo().getPrazoEmprestimo();
						devolucao = devolucao.plusDays(dia);
					}
					data.add(devolucao);
				}
			}
		}
		return data;
	}
}
