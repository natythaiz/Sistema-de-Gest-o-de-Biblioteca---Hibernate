package application;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entities.Book;
import entities.HibernateUtil;
import entities.enumeradores.Status;

public class Program {

	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction(); // ABRIR TRANSAÇÃO É OBRIGATÓRIO PARA SALVAR

            // Criando um dado de teste
            Book livro = new Book();
            livro.setTitulo("O Pequeno Principe");
            livro.setAutor("Antoine de Saint-Exupéry");
            livro.setIsbn(10);
            livro.setStatus(Status.DISPONIVEL);

            session.save(livro); 

            tx.commit(); 
            System.out.println("Tabela criada e Livro salvo com sucesso!");

        } catch (Exception e) {
            if (tx != null) tx.rollback(); 
            e.printStackTrace();
        } finally {
            session.close();
        }
	}

}
