package mybank;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Program loop and logic for the console-based banking program. The program allows for multiple account
 * creations, displaying, withdrawals, deposits, and some error handling about the user input.
 * This also functions as the entry point / main class file for the whole program.
 */
public class BankSystem
{
    private final HashMap<String, BankAccount> accounts;
    private boolean runningState;
    private final Scanner scanner;

    public static void main(String[] args)
    {
        BankSystem mainSystem = new BankSystem();
    }

    /**
     * Establishes program loop
     */
    public BankSystem()
    {
        runningState = true;
        accounts = new HashMap<>();
        scanner = new Scanner(System.in);           // using console input
        int choice;

        /*
         * begin main menu loop. any other choice will branch out of this loop but at its conclusion, return
         * to the main menu.
         */
        while(runningState)
        {
            try
            {
                printMainMenu();

                choice = obtainInt();

                switch(choice)
                {
                    case 1: createAccount(); break;
                    case 2: displayAccount(); break;
                    case 3: withdrawFromAccount(); break;
                    case 4: depositToAccount(); break;
                    case 5: displayAll(); break;
                    case 6: removeAccount(); break;
                    case 7: calculateAndDisplayInterest(); break;
                    case 8: exitProgram(); break;

                    default: throw new WrongChoiceException();
                }
            }
            catch(InputMismatchException inputExcept)
            {
                System.out.println("Unexpected input");
                scanner.nextLine(); // flush any remaining input
            }
            catch(NameAlreadyExists | WrongChoiceException | InvalidPasscodeException
                  | AccountDoesNotExistException | WrongPasscodeWhenFindingException | WithdrawalAmountException except)
            {
                System.out.println(except.getMessage());
            }
        }
    }

    /**
     * Method to instantiate a new BankAccount and add it to the hashmap of existing accounts.
     * Since the hashmap is indexed by name, only one bank account can occupy the same name. In reality, names
     * are not quite unique among many people so remedying this issue requires using an easily differentiable
     * key (like a numeric ID). 
     */
    public void createAccount()
    {
        String name, passcode;
        double startBalance;
        BankAccount newAccount;

        System.out.println("\n**Create New Account**");
        System.out.println("1. Create Standard Account");
        System.out.println("2. Create VIP Account");
        System.out.print("Enter your choice: ");
        int choice = obtainInt();
        if(choice == 1 || choice == 2)
        {
            newAccount = (choice == 1) ? new standardAccount() : new VIPAccount();
            System.out.print("Enter name: ");
            newAccount.setName(scanner.nextLine());

            System.out.print("Enter passcode: ");
            newAccount.setPasscode(scanner.nextLine());

            System.out.print("Starting balance: ");
            newAccount.setBalance(obtainDouble());

            // assuming no duplicate names at input
            accounts.put(newAccount.getName(), newAccount);
            System.out.println("Account created!!");
        }
        else
            throw new WrongChoiceException();
    }

    /**
     * Try to obtain a name and display its corresponding account stored in the HashMap. If the account
     * belonging to that name does not exist, show a corresponding message.
     */
    public void displayAccount()
    {
        // obtain name
        String name;

        System.out.print("Enter your name: ");
        name = scanner.nextLine();

        displayAccountHelper(name);
    }

    /**
     * Displays the account details that has the given name.
     * @param name of the account
     * @throws AccountDoesNotExistException if the given name is not bound to an account in the HashMap
     */
    private void displayAccountHelper(String name) throws AccountDoesNotExistException
    {
        BankAccount retrievedAccount;
        if((retrievedAccount = accounts.getOrDefault(name, null))  == null)
        {
            throw new AccountDoesNotExistException(name);
        }
        else
        {
            System.out.println("**Account Details**");
            System.out.println("Name: " + retrievedAccount.getName());
            System.out.println("Account Type: " + retrievedAccount.getAccountType());
            System.out.println("Balance: " + retrievedAccount.getBalanceString());
        }
    }

    /**
     * Attempts to withdraw a given amount from a name-passcode specified account that exists in the accounts
     * HashMap.
     */
    public void withdrawFromAccount() throws AccountDoesNotExistException, WrongPasscodeWhenFindingException
    {
        String name, passcode;
        double withdrawAmount;
        BankAccount retrievedAccount;

        System.out.println("\n**Transaction - Withdraw**");
        System.out.print("Enter your name: ");
        name = scanner.nextLine();
        if((retrievedAccount = accounts.getOrDefault(name, null))  == null)
        {
            throw new AccountDoesNotExistException(name);
        }
        else
        {
            System.out.print("Enter passcode: ");
            passcode = scanner.nextLine();
            if(!retrievedAccount.passcodeMatch(passcode))
            {
                throw new WrongPasscodeWhenFindingException();
            }
            else
            {
                System.out.print("Enter amount to withdraw: ");
                withdrawAmount = obtainDouble();
                retrievedAccount.withdraw(withdrawAmount);
                System.out.println("Name: " + retrievedAccount.getName());
                System.out.println("Balance: " + retrievedAccount.getBalanceString());
            }
        }
    }

    /**
     * Attempts to do the deposit process to the account.
     * @throws AccountDoesNotExistException
     */
    public void depositToAccount() throws AccountDoesNotExistException
    {
        String name;
        double depositAmt;
        BankAccount retrievedAccount;

        System.out.println("\n** Transaction - Deposit**");
        System.out.print("Enter your name: ");
        name = scanner.nextLine();
        if((retrievedAccount = accounts.getOrDefault(name, null))  == null)
        {
            throw new AccountDoesNotExistException(name);
        }
        else
        {
            System.out.print("Enter amount to deposit: ");
            depositAmt = obtainDouble();
            retrievedAccount.deposit(depositAmt);
            System.out.println("Name: " + retrievedAccount.getName());
            System.out.println("Balance: " + retrievedAccount.getBalanceString());
        }


    }

    /**
     * Display all standard accounts followed by VIP accounts stored in the bank hashmap.
     */
    public void displayAll()
    {
        List<String> standardAccs = new ArrayList<>();
        List<String> VIPAccs = new ArrayList<>();

        accounts.forEach((k, v) ->
        {
            if(v.getAccountType() == AccountTypes.STANDARD)
                standardAccs.add(k);
            else
                VIPAccs.add(k);
        });

        System.out.println("\nStandard Account Details");
        for(String name : standardAccs)
            displayAccountHelper(name);

        System.out.println("\nVIP Account Details");
        for(String name : VIPAccs)
            displayAccountHelper(name);
    }

    /**
     * Attempts the remove account transaction.
     * @throws AccountDoesNotExistException if the account (by the name) could not be found in the hashmap
     * @throws WrongPasscodeWhenFindingException if the given passcode does not match
     */
    public void removeAccount() throws AccountDoesNotExistException, WrongPasscodeWhenFindingException
    {
        String name, passcode;
        BankAccount retrievedAccount;

        System.out.println("\n**Transaction - Remove Account**");
        System.out.print("Enter your name: ");
        name = scanner.nextLine();

        if((retrievedAccount = accounts.getOrDefault(name, null)) == null)
        {
            throw new AccountDoesNotExistException(name);
        }
        else
        {
            System.out.print("Enter passcode: ");
            passcode = scanner.nextLine();
            if(!retrievedAccount.passcodeMatch(passcode))
            {
                throw new WrongPasscodeWhenFindingException();
            }
            else
            {
                accounts.remove(name);
                System.out.println("Account has been removed!!");
            }
        }
    }

    /**
     * Applies the process to display the interest of a specified account.
     * @throws AccountDoesNotExistException if the specified name does not belong to an account
     */
    public void calculateAndDisplayInterest() throws AccountDoesNotExistException
    {
        String name;
        int months;
        BankAccount retrievedAccount;
        DecimalFormat df = new DecimalFormat("0.##");

        System.out.println("\n**Transaction - Calculate Interest");
        System.out.print("Enter your name: ");
        name = scanner.nextLine();

        if((retrievedAccount = accounts.getOrDefault(name, null)) == null)
        {
            throw new AccountDoesNotExistException(name);
        }
        else
        {
            System.out.print("Enter the number of months: ");
            months = obtainInt();
            System.out.println("The expected interest is: " + df.format(retrievedAccount.calculateInterest(months)));
        }

    }

    /**
     * Exit procedure
     */
    public void exitProgram()
    {
        runningState = false;
    }

    /**
     * Prints the main menu of the program.
     */
    public void printMainMenu()
    {
        System.out.println("\n*** Menu ***");
        System.out.println("1. Create Account");
        System.out.println("2. Display");
        System.out.println("3. Withdraw");
        System.out.println("4. Deposit ");
        System.out.println("5. Display All");
        System.out.println("6. Remove Account");
        System.out.println("7. Calculate Interest");
        System.out.println("8. Exit\n");
        System.out.print("Enter your choice: ");
    }

    /**
     * Used for automatic new line consumption integer scanning.
     * @return integer in System.in, delimited by the new line character (ENTER)
     */
    private int obtainInt()
    {
        int res = scanner.nextInt();
        scanner.nextLine();
        return res;
    }

    /**
     * Used for automatic new line consumption double scanning.
     * @return double in System.in, delimited by the new line character (ENTER)
     */
    private double obtainDouble()
    {
        double res = scanner.nextDouble();
        scanner.nextLine();
        return res;
    }
}