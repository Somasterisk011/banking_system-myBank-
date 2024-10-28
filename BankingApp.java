package myBank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    private static final String url="jdbc:mysql://localhost:3306/banking_system";
    private static final String username="root";
    private static final String pass="mysql@Som777";

    public static void main(String[] args) throws SQLException {
        String email;
        long accountNumber;

        try {
            Connection connection = DriverManager.getConnection(url, username, pass);
            Scanner scanner=new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager=new AccountManager(connection, scanner);

            while (true){
                System.out.println("* * * Welcome to the Trusty Banking System * * *");
                System.out.println();
                System.out.println("1. Register\n2. Login\n3. Exit");
                System.out.println("Enter Your Choice: ");
                byte choice=scanner.nextByte();
                switch (choice){
                    case 1: user.register();
                    break;
                    case 2: email=user.login();
                    if (email!=null){
                        System.out.println();
                        System.out.println("User Logged In!!");
                        if (!accounts.accountExists(email)){
                            System.out.println();
                            System.out.println("1. open Acoount\n2. Exit");
                            System.out.println("Enter option: ");
                            if (scanner.nextByte()==1){
                                accountNumber=accounts.openAccount(email);
                                System.out.println("Account opened successfully.\nYour Account number: "+accountNumber);
                            }else
                                break;
                        }else {
                            accountNumber=accounts.getAccountNumber(email);
                            byte choice2=0;
                            while (choice2!=5){
                                System.out.println();
                                System.out.println("1. DEPOSIT BALANCE\n2. WITHDRAW BALANCE\n3. BALANCE ENQUIRY\n4. TRANSFER MONEY\n5. LOG OUT");
                                System.out.println("Enter option: ");
                                choice2=scanner.nextByte();
                                switch (choice2){
                                    case 1:
                                        accountManager.depositMoney(accountNumber);
                                        break;
                                    case 2:
                                        accountManager.withdrawMoney(accountNumber);
                                        break;
                                    case 4:
                                        accountManager.transferMoney(accountNumber);
                                        break;
                                    case 3:
                                        accountManager.getBalance(accountNumber);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }
                        }


                    }else {
                        System.out.println("Invalid email or password!!");

                    }
                    case 3: System.out.println("Thank you for using Trusty Bank! see you again.");
                        return;
                    default:System.out.println("Invalid Option!!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
