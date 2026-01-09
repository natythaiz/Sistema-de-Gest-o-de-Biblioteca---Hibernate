package entities;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "loans")
public class Loan {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private LocalDate dataEmprestimo;
	private LocalDate dataDevolucaoPrevista;
	private LocalDate dataDevolucaoReal;
	@ManyToOne 
    @JoinColumn(name = "book_id") 
	private Book livro;
	@ManyToOne 
    @JoinColumn(name = "user_id") 
	private User user;
	
	public Loan() {}

	public Loan(Book livro, User user) {
		this.dataEmprestimo = LocalDate.now();
		this.livro = livro;
		this.user = user;
		this.dataDevolucaoPrevista = calcularDataDevolucao(user);
	}

	private LocalDate calcularDataDevolucao(User user2) {
//		esta lógica está associada ao fato de que cada usuário já
//		possui seu valor padrão de empréstimo 
		int dias = user.getTipo().getPrazoEmprestimo(); 
	    return LocalDate.now().plusDays(dias);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(LocalDate dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}

	public LocalDate getDataDevolucaoPrevista() {
		return dataDevolucaoPrevista;
	}

	public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
		this.dataDevolucaoPrevista = dataDevolucaoPrevista;
	}

	public LocalDate getDataDevolucaoReal() {
		return dataDevolucaoReal;
	}

	public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
		this.dataDevolucaoReal = dataDevolucaoReal;
	}

	public Book getLivro() {
		return livro;
	}

	public void setLivro(Book livro) {
		this.livro = livro;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Loan other = (Loan) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Loan [id=" + id + ", dataEmprestimo=" + dataEmprestimo + ", dataDevolucaoPrevista="
				+ dataDevolucaoPrevista + ", dataDevolucaoReal=" + dataDevolucaoReal + ", livro=" + livro + ", user="
				+ user + "]";
	}
}
