package myBank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private  Scanner scanner;
    public User(Connection connection, Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }
    public void register() throws SQLException {
        scanner.nextLine();
        System.out.println("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.println("Email: ");
        String email = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        if (user_exist(email)){
            System.out.println("User already exist for this email id!!");
            return;
        }else {
            String registerQuery="INSERT INTO user(full_name, email, password) VALUES(?,?,?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(registerQuery);
                preparedStatement.setString(1, fullName);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0)
                    System.out.println("User Registered Successfully:)");
                else
                    System.out.println("User Registration Unsuccessful!!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public String login(){
        scanner.nextLine();
        System.out.println("Email: ");
        String email=scanner.nextLine();
        System.out.println("Password: ");
        String password=scanner.nextLine();
        String loginQuery="SELECT * FROM user WHERE email=? AND password=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(loginQuery);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                return email;
            }else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean user_exist(String email){
        String query="SELECT * FROM user WHERE email=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next())
                return true;
            else
                return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
