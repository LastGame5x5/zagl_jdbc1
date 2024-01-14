package com.example.demo;

import java.sql.*;

public class DbFunctions {

        private static final String URL = "jdbc:postgresql://192.168.0.105:5432/postgres";
        private static final String USER = "postgres";
        private static final String PASSWORD = "postgres";
        private  static final String insertUserQuery = "INSERT INTO users (login, password, date) VALUES (?, ?, ?); " +
                "INSERT INTO user_emails (login, email) VALUES (?, ?)";
        private static final String selectQuery = "SELECT * FROM users INNER JOIN user_emails ON users.login = user_emails.login" +
                " WHERE users.login = '";

        public static User getUserByLogin(String login) {
            User user = null;
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement statement = connection.createStatement()) {
                String query = selectQuery + login + "'";
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    user = new User(resultSet.getString("login"),
                            resultSet.getString("password"),
                            resultSet.getDate("date").toLocalDate(),
                            resultSet.getString("email"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return user;
        }

        public static int insertUser(User user) {
            int rowsUpdated = 0;
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement userStatement = connection.prepareStatement(insertUserQuery)) {
                userStatement.setString(1, user.getLogin());
                userStatement.setString(2, user.getPassword());
                userStatement.setDate(3, java.sql.Date.valueOf(user.getDate()));
                userStatement.setString(4, user.getLogin());
                userStatement.setString(5, user.getEmail());
                rowsUpdated = userStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return rowsUpdated;
        }
    }

