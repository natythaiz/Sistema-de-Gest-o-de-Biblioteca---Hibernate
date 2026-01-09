package entities;

import java.util.Objects;

import entities.enumeradores.TipoUser;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nome;
	private String email;
	@Enumerated(EnumType.STRING)
	private TipoUser tipo;
	private int limiteEmprestimo;
	
	public User() {}

	public User(String nome, String email, TipoUser tipo, int limiteEmprestimo) {
		super();
		this.nome = nome;
		this.email = email;
		this.tipo = tipo;
		this.limiteEmprestimo = limiteEmprestimo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public TipoUser getTipo() {
		return tipo;
	}

	public void setTipo(TipoUser tipo) {
		this.tipo = tipo;
	}

	public int getLimiteEmprestimo() {
		return limiteEmprestimo;
	}

	public void setLimiteEmprestimo(int limiteEmprestimo) {
		this.limiteEmprestimo = limiteEmprestimo;
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
		User other = (User) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", nome=" + nome + ", email=" + email + ", tipo=" + tipo + ", limiteEmprestimo="
				+ limiteEmprestimo + "]";
	}
	
}
