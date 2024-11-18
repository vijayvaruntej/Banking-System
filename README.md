# Banking System

A simple **Banking System** built using **Java**, **JDBC**, and **MySQL** to simulate essential banking operations such as account management, user management, and transaction handling.

## Features

1. **Accounts Management**
   - Open new accounts.
   - Generate unique account numbers.
   - Verify if an account exists.

2. **User Management**
   - Register new users.
   - User login functionality.
   - Check if a user exists in the system.

3. **Account Manager**
   - Credit money into accounts.
   - Debit money from accounts.
   - Transfer funds between accounts.
   - Check account balances.

4. **Banking App**
   - Provides the main menu interface.
   - Handles database connections using JDBC.
   - Sets up driver configurations and application instances.

## Technologies Used

- **Java**: Core programming language for the implementation.
- **JDBC**: To connect and interact with the MySQL database.
- **MySQL**: Database for storing user and account information.

## Project Structure

- **Accounts**  
  Contains methods for handling account-related operations:
  - `open_account()`
  - `get_account_number()`
  - `generate_account_number()`
  - `account_exists()`

- **User**  
  Handles user registration and authentication:
  - `register()`
  - `login()`
  - `user_exist()`

- **Account Manager**  
  Manages account transactions and balance checks:
  - `credit_money()`
  - `debit_money()`
  - `transfer_money()`
  - `check_balance()`

- **Banking App**  
  Initializes the application and connects to the database:
  - `main menu`
  - `create instances`
  - `setup connection with db`
  - `load drivers`

## Installation and Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/vijayvaruntej/banking-system.git
   cd banking-system
