import com.sun.security.jgss.GSSUtil;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;
    public Accounts(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }
    public long open_account(String email){
        try {
            if (!account_exist(email)) {
                String open_account_query = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES(?,?,?,?,?)";
                scanner.nextLine();
                System.out.println("Full Name : ");
                String fullName = scanner.nextLine();
                System.out.println("Initial deposit amount : ");
                Double initial_deposit_amount = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("Enter 4 digit Security pin : ");
                String pin = scanner.nextLine();
                Long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, fullName);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4,initial_deposit_amount);
                preparedStatement.setString(5, pin);
                int affectedRows = preparedStatement.executeUpdate();
                if(affectedRows>0){
                    return account_number;
                }else{
                    throw new RuntimeException("Account Creation Failed!!");
                }
            }else{
                throw new RuntimeException("Account already exist.");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account Already Exist");
    }
    public long getAccount_number(String email){
        String query = "SELECT account_number from Accounts WHERE email=?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Doesn't Exist!");
    }
    private long generateAccountNumber(){
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number FROM Accounts ORDER BY account_number DESC LIMIT 1");
            if(resultSet.next()){
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number+1;
            }else{
                return 14300100;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 14300100;
    }
    public boolean account_exist(String email){
        String existQuery = "SELECT account_number from  Accounts WHERE email=?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(existQuery);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

}
