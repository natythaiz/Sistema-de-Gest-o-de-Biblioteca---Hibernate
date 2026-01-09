package entities;

import java.time.LocalDate;
import java.util.Objects;

public class Loan {
	private int id;
	private LocalDate dataEmprestimo;
	private LocalDate dataDevolucaoPrevista;
	private LocalDate dataDevolucaoReal;
	private Book livro;
	private User user;
	
	public Loan() {}

	public Loan(LocalDate dataEmprestimo, Book livro, User user) {
		super();
		this.dataEmprestimo = dataEmprestimo;
		this.livro = livro;
		this.user = user;
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
