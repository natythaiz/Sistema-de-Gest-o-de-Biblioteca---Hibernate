package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entities.HibernateUtil;
import entities.User;
import entities.enumeradores.TipoUser;

public class UserDAO {
//	create
	public void saveUser(User user, Session session) {
		session.persist(user);
    }
	
//	read all
	public List<User> getAllUsers(){
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }
	}
	
//	update
	public void updateUser(User user, Session session) {
		session.merge(user);
    }
	
	public User findById(int id, Session session) {
		User user = session.get(User.class, id);
		return user;
    }
	
//	delete
	public void deleteUser(User user, Session session) {
		session.remove(user);
    }

	public List<User> findUserType(String upperCase, Session session) {
		TipoUser tipoEnum = TipoUser.valueOf(upperCase);
        String hql = "FROM User u WHERE u.tipo = :string";
        
        List<User> list = session.createQuery(hql, User.class)
                            .setParameter("string", tipoEnum)
                            .list();

        return list;
	}
}
