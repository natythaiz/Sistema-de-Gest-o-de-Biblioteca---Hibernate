package DTO;

public class SagaBook {
	private int id;
	private String titulo;
	private String autor;
	private String sagaNome;
	
	public SagaBook() {}

	public SagaBook(int id, String titulo, String autor, String sagaNome) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.autor = autor;
		this.sagaNome = sagaNome;
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

	public String getSagaNome() {
		return sagaNome;
	}

	public void setSagaNome(String sagaNome) {
		this.sagaNome = sagaNome;
	}

	@Override
	public String toString() {
		String str = "";
		if(sagaNome == null) {
			str = ", sem saga cadastrada!";
		} else {
			str = ", saga: " + sagaNome + ".";
		}
		return "Book id #" + id + ", " + titulo + ", (" + autor + ")" + str;
	}
	
	
}
