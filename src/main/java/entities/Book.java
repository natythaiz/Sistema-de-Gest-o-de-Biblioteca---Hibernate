package entities;

import java.util.Objects;

import entities.enumeradores.Categoria;
import entities.enumeradores.Status;

public class Book {
	private int id;
	private String titulo;
	private String autor;
	private int isbn;
	private Categoria categoria;
	private Status status;
	
	public Book() {}
	
	public Book(String titulo, String autor, int isbn, Categoria categoria) {
		this.titulo = titulo;
		this.autor = autor;
		this.isbn = isbn;
		this.categoria = categoria;
		this.status = status.DISPONIVEL;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public int getIsbn() {
		return isbn;
	}

	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return isbn == other.isbn;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", titulo=" + titulo + ", autor=" + autor + ", isbn=" + isbn + ", categoria="
				+ categoria + ", status=" + status + "]";
	}
	
}
