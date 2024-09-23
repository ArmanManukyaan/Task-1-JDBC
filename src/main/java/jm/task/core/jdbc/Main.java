package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    private static UserService userService = new UserServiceImpl(false);

    public static void main(String[] args) {
//        userService.createUsersTable();
//        userService.dropUsersTable();
//      String[][] users = {
//              {"Иван", "Петров", "24"},
//              {"Анна", "Смирнова", "30"},
//              {"Сергей", "Кузнецов", "28"},
//              {"Ольга", "Агров", "22"}
//      };
//      Arrays.stream(users)
//              .forEach(user -> userService.saveUser(user[0], user[1], Byte.parseByte(user[2])));
//        userService.removeUserById(1);
//          userService.getAllUsers();
       userService.cleanUsersTable();
    }
}
