package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.DBConnectionProvider;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {
    }


    @Override
    public void createUsersTable() {
        try (Session session = DBConnectionProvider.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(50), " +
                    "last_name VARCHAR(50), " +
                    "age SMALLINT" +
                    ");";
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("Таблица успешно создана.");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании таблицы: " + e.getMessage(), e);
        }
    }


    @Override
    public void dropUsersTable() {
        try (Session session = DBConnectionProvider.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String sql = "DROP TABLE IF EXISTS users;";
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("Таблица успешно удалено");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении таблицы: " + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = DBConnectionProvider.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String sql = "INSERT INTO users(name, last_name, age) VALUES (?, ?, ?)";
            session.createSQLQuery(sql)
                    .setParameter(1, name)
                    .setParameter(2, lastName)
                    .setParameter(3, age)
                    .executeUpdate();
            transaction.commit();
            System.out.println("Пользователь " + name + " успешно добавлен с : " + lastName);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при добавлении пользователя: " + e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = DBConnectionProvider.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String sql = "DELETE FROM users WHERE id = :id";
            session.createSQLQuery(sql)
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
            System.out.println("Пользователь с ID " + id + " успешно удалён.");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        try (Session session = DBConnectionProvider.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            List<User> fromUser = session.createQuery("FROM User", User.class).list();
            users.addAll(fromUser);
            transaction.commit();
            System.out.println("Данные успешно извлечены. Количество пользователей: " + users.size());
        } catch (Exception e) {
            System.err.println("Ошибка при извлечении данных: " + e.getMessage());
            throw new RuntimeException(e);
        }
        users.forEach(System.out::println);
        return users;
    }


    @Override
    public void cleanUsersTable() {
        try (Session session = DBConnectionProvider.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String sql = "DELETE FROM users";
            int rowsAffected = session.createSQLQuery(sql).executeUpdate();
            System.out.println("Удалено пользователей: " + rowsAffected);
            transaction.commit();
        } catch (Exception e) {
            System.err.println("Ошибка при удалении пользователей: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }
}