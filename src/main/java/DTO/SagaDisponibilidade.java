package DTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SagaDisponibilidade {
	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private String nome;
	private LocalDate dataDisponivel;
	
	public SagaDisponibilidade() {}

	public SagaDisponibilidade(String nome, LocalDate dataDisponivel) {
		this.nome = nome;
		this.dataDisponivel = dataDisponivel;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataDisponivel() {
		return dataDisponivel;
	}

	public void setDataDisponivel(LocalDate dataDisponivel) {
		this.dataDisponivel = dataDisponivel;
	}

	@Override
	public String toString() {
		String str = "";
		if(dataDisponivel.isBefore(LocalDate.now())) {
			str = " (entrega atrasada)";
		} else if(dataDisponivel.isEqual(LocalDate.now())) {
			str = " (pronto para retirada)";
		} 
		return "Saga: " + nome + ", data em que todos os livros estarão disponíveis: " + dataDisponivel.format(fmt) + str;
	}
}
