package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.DBConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connection = DBConnectionProvider.getInstance().getConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "last_name VARCHAR(50), " +
                "age SMALLINT" +
                ");";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("Таблица успешно создана.");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании таблицы: " + e.getMessage(), e);
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("Таблица users успешно удалена, если она существовала.");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении таблицы: " + e.getMessage(), e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setByte(3, age);
            final int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    System.out.println("Пользователь " + name + " успешно добавлен с ID: " + id);
                }
            } else {
                System.out.println("Не удалось добавить пользователя.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении пользователя: " + e.getMessage(), e);
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Пользователь с ID " + id + " был удалён.");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                users.add(getUserFromResultSet(resultSet));
            }
            System.out.println("Данные успешно извлечены. Количество пользователей: " + users.size());

        } catch (SQLException e) {
            System.err.println("Ошибка при извлечении данных: " + e.getMessage());
            throw new RuntimeException(e);
        }

        if (!users.isEmpty()) {
            System.out.println("Извлеченные пользователи:");
            users.forEach(System.out::println);
        } else {
            System.out.println("Нет пользователей для отображения.");
        }
        return users;
    }

    public User getUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setLastName(rs.getString("last_name"));
        user.setAge(rs.getByte("age"));
        return user;
    }

    public void cleanUsersTable() {
        String sql = "DELETE FROM users";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int rowsAffected = ps.executeUpdate();
            System.out.println("Удалено пользователей: " + rowsAffected);
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении пользователей: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }
}