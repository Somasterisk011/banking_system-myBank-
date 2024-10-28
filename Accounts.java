package myBank;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    public long openAccount(String email){

        if (!accountExists(email)){
            String open_account_query="INSERT INTO accounts(account_number, full_name, email, balance, security_pin) VALUES(?,?,?,?,?)";
            scanner.nextLine();
            System.out.println("Full Name: ");
            String fullName=scanner.nextLine();
            System.out.println("Deposit initial balance: ");
            double balance=scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter security pin: ");
            String securityPin=scanner.nextLine();
            try{
                long accountNumber = generateAccountNumber();
                PreparedStatement preparedStatement=connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1, accountNumber);
                preparedStatement.setString(2, fullName);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, securityPin);
                int affectedRow=preparedStatement.executeUpdate();
                if (affectedRow>0){
                    return accountNumber;
                }else
                    throw new RuntimeException("Invalid Request!!! Operation Failed.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account Already Exists!!!");
    }
    public long generateAccountNumber(){
        try{
            Statement statement=connection.createStatement();
            String getAccountQuery="SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1";
            ResultSet resultSet= statement.executeQuery(getAccountQuery);

            if (resultSet.next()){
                long lastAccountNumber=resultSet.getLong("account_number");
                return lastAccountNumber+1;
            }else
                return 10000100;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 10000100;
    }
    public long getAccountNumber(String email){
        String getAccountNumberQuery="SELECT account_number from accounts WHERE email=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getAccountNumberQuery);
            preparedStatement.setString(1, email);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Account number doesn't exists!!!");
    }
    public boolean accountExists(String email){
        String accountExistQuery="SELECT account_number FROM accounts WHERE email=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(accountExistQuery);
            preparedStatement.setString(1, email);
            ResultSet resultSet= preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
