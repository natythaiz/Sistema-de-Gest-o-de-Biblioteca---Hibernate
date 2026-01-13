package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entities.HibernateUtil;
import entities.User;
import entities.enumeradores.TipoUser;

public class UserDAO {
//	create
	public void saveUser(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            System.out.println("User saved successfully.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
	
//	read all
	public List<User> getAllUsers(){
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }
	}
	
//	update
	public void updateUser(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            System.out.println("User updated successfully.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
	
	public User findById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
            	return user;
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return null;
    }
	
//	delete
	public void deleteUser(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(user);
            tx.commit();
            System.out.println("User deleted successfully.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

	public List<User> findUserType(String upperCase) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	TipoUser tipoEnum = TipoUser.valueOf(upperCase);
            String hql = "FROM User u WHERE u.tipo = :string";
            
            List<User> list = session.createQuery(hql, User.class)
                                .setParameter("string", tipoEnum)
                                .list();

            return list;
        } catch (IllegalArgumentException e) {
            System.out.println("Não existe este tipo de usuário!");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            e.getStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
	}
}
