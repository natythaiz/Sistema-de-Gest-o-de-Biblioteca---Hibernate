package services;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;

import dao.UserDAO;
import entities.User;
import entities.enumeradores.TipoUser;

public class UserService {
	private UserDAO userDao = new UserDAO();
	private Scanner scanner = new Scanner(System.in);
	
	public void findUserType(Session session) {
		System.out.println("Qual tipo de usuário deseja buscar: ");
		String tipo = scanner.nextLine();
		List<User> result = userDao.findUserType(tipo.toUpperCase(), session);
		if(result == null || result.isEmpty()) {
			System.out.println("Não foi encontrado usuários deste tipo!");
		} else {
			System.out.println("Usuário " + tipo);
			for(User obj: result) {
				System.out.println("#" + obj.getId() + " - " + obj.getNome());
			}
		}
	}

	public void cadastrarUsuario(Session session) {
		System.out.print("Nome: "); String nome = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        
        User user = new User(nome, email, TipoUser.PROFESSOR);
        userDao.saveUser(user, session); 
	}

}
