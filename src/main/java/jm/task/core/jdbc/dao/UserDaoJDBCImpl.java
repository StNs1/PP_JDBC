package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection;

    public UserDaoJDBCImpl() {
    }

    public UserDaoJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    public void createUsersTable() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS `db_user`.`user` (\n" +
                    "  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(255) NOT NULL,\n" +
                    "  `lastname` VARCHAR(255) NOT NULL,\n" +
                    "  `age` INT NOT NULL,\n" +
                    "  PRIMARY KEY (`id`))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;");
            stmt.close();
        } catch (SQLException e) {

        }
    }

    public void dropUsersTable() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("DROP TABLE IF EXISTS db_user.user");
            stmt.close();
        } catch (SQLException e) {

        }
    }

    public boolean getUserByName(String name) {
        String query = "SELECT id FROM db_user.user WHERE name = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, name);
            long id = stmt.getResultSet().getLong(1);
            return false;
        } catch (SQLException e) {

        } catch (NullPointerException e) {
            return true;
        }
        return false;
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            if (getUserByName(name)) {
                String query = "INSERT INTO db_user.user(name, lastname, age) VALUES (?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, name);
                stmt.setString(2, lastName);
                stmt.setByte(3, age);
                stmt.execute();
                stmt.close();
            }
        } catch (SQLException e) {

        }
    }

    public void removeUserById(long id) {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM db_user.user WHERE id ="+id);
            stmt.close();
        } catch (SQLException e) {

        }
    }

    public List<User> getAllUsers() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("SELECT * FROM db_user.user");
            ResultSet resultSet = stmt.getResultSet();
            List<User> list = new ArrayList<>();
            User user;
            while (resultSet.next()) {
                user = new User(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getByte(4)
                );
                list.add(user);
            }
            stmt.close();
            return list;
        } catch (SQLException e) {

        }
        return null;
    }

    public void cleanUsersTable() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM db_user.user");
            stmt.close();
        } catch (SQLException e) {

        }
    }
}
