package entities.enumeradores;

public enum TipoUser {
	ALUNO(7),      // 7 dias de prazo
    PROFESSOR(30), // 30 dias
    COMUNIDADE(3); // 3 dias

    private final int prazoEmprestimo;

    TipoUser(int prazo) {
        this.prazoEmprestimo = prazo;
    }

    public int getPrazoEmprestimo() {
        return prazoEmprestimo;
    }
}
