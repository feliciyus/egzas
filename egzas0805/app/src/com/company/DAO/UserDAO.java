package com.company.DAO;


import com.company.Entity.User;
import com.company.Utils.BCryptPassword;
import org.hibernate.Session;
import com.company.Utils.HibernateUtil;

public class UserDAO {
    public UserDAO(){}

    public User findUserByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Object user = session.createQuery("select a from User a where a.userName = '" + username + "'").getSingleResult();
        session.getTransaction().commit();
        return (User) user;
    }


    public void register(String regUsername, String regPassword) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String password = BCryptPassword.hashPassword(regPassword);
        User user = new User(regUsername, password, User.ROLE_USER);
        session.save(user);
        session.getTransaction().commit();
        System.out.println("Register successful!");
    }

    public boolean login(String logUsername, String logPassword){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        User foundUser = findUserByUsername(logUsername);
        session.getTransaction().commit();
        if (logUsername.equals(foundUser.getUserName())){
            if (BCryptPassword.checkPassword(logPassword, foundUser.getUserPass())){
                System.out.println("Logged in");
                return true;
            } else {
                System.out.println("Wrong password :(");
                return false;
            }
        } else {
            System.out.println("Wrong Username :(");
            return false;
        }
    }

    public String getUserRole(String username){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        User user = findUserByUsername(username);
        session.getTransaction().commit();
        return user.getRole();
    }

    public void addUser(User user){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String password = BCryptPassword.hashPassword(user.getUserPass());
        User user1 = new User(user.getUserName(), password, user.getRole());
        session.save(user1);
        session.getTransaction().commit();
    }




}
