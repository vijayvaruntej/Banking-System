import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void deposit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Amount : ");
        Double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Pin : ");
        String pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts where account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_number);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Rs. " + amount + " debited successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance!");
                    }
                } else {
                    System.out.println("Invalid pin.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);
    }

    // Credit Money

    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Amount : ");
        Double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Pin : ");
        String pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts where account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    //double current_balance = resultSet.getDouble("balance");
                    String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_number);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Rs. " + amount + " Credited successfully");
                        //System.out.println("After credit your total balance is ");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Invalid pin.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Receiver Account Number: ");
        long receiver_account_number = scanner.nextLong();
        if(!genuineReceiver(receiver_account_number)){
            System.out.println("Receiver Account number not found.");
            return;
        }
        System.out.println("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Pin : ");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (sender_account_number != 0 && receiver_account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ? ");
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);
                        debitPreparedStatement.setDouble(1, amount);
                        debitPreparedStatement.setLong(2, sender_account_number);

                        String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                        PreparedStatement creditPreparedStatemtent = connection.prepareStatement(credit_query);
                        creditPreparedStatemtent.setDouble(1, amount);
                        creditPreparedStatemtent.setLong(2, receiver_account_number);

                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = creditPreparedStatemtent.executeUpdate();
                        if (rowsAffected1 == rowsAffected2 && rowsAffected2 >0) {
                            System.out.println("Transaction successful !");
                            System.out.println("Rs. " + amount + " Transferred Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance !");
                    }
                } else {
                    System.out.println("Invalid Security Pin !");

                }
            } else {
                System.out.println("Invalid account number ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    //Get Balance
    public void getBalance(long account_number) {
        scanner.nextLine();
        System.out.println("Enter Security pin : ");
        String pin = scanner.nextLine();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance from Accounts WHERE account_number = ? and security_pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                System.out.println("Total Balance in your account is : "+ balance);
            } else {
                System.out.println("Invalid Pin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean genuineReceiver(long receiver_account_number){
        try{
            String query = "SELECT account_number FROM Accounts WHERE account_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, receiver_account_number);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next() && resultSet.getLong("account_number") == receiver_account_number) {
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
