package entities;
import java.io.FileInputStream;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory;

    static {
        try {
        	Properties prop = new Properties();
        	FileInputStream fis = new FileInputStream("db.properties"); 
            prop.load(fis);
        	Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            
        	configuration.setProperty("hibernate.connection.password", prop.getProperty("password"));
        	configuration.addAnnotatedClass(entities.Book.class);
            configuration.addAnnotatedClass(entities.User.class);
            configuration.addAnnotatedClass(entities.Loan.class);
            configuration.addAnnotatedClass(entities.Reservation.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
