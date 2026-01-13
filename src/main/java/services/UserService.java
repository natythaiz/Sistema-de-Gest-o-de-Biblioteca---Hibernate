package services;

import java.util.List;

import dao.UserDAO;
import entities.User;

public class UserService {
	private UserDAO userDao = new UserDAO();
	
	public void findUserType(String tipo) {
		List<User> result = userDao.findUserType(tipo.toUpperCase());
		if(result == null || result.isEmpty()) {
			System.out.println("Não foi encontrado usuários deste tipo!");
		} else {
			System.out.println("Usuário " + tipo);
			for(User obj: result) {
				System.out.println("#" + obj.getId() + " - " + obj.getNome());
			}
		}
	}

}
