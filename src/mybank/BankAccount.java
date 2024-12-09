package mybank;

public interface BankAccount
{
    double rateStandard = 0.005;
    double rateVIP = 0.01;

    void deposit(double amount);
    String withdraw(double amount);

    double getBalance();
    String getBalanceString();
    void setBalance(double value);

    String getName();
    void setName(String name);

    void setPasscode(String code);
    boolean passcodeMatch(String passcodeToCheck);

    AccountTypes getAccountType();
    void setAccountType(AccountTypes accountType);

    double calculateInterest(int months);
}

abstract class AbstractBankAccount implements BankAccount
{
    private double balance;
    private String name;
    // the pass code must be a 4-digit number
    private String passcode;
    private AccountTypes accountType;

    /**
     * This is the default constructor to be called by the subclasses (accountType depends on the given subclass).
     * @param accountType of the account (given by the specific subclass)
     */
    public AbstractBankAccount(AccountTypes accountType)
    {
        setName("");
        setPasscode("0000");
        setBalance(0);
        setAccountType(accountType);
    }

    /**
     * Constructs a specified account.
     * @param name of the account
     * @param passcode of the account
     * @param balance initial balance of the account
     * @param accountType type of the account (STANDARD or VIP)
     */
    public AbstractBankAccount(String name, String passcode, double balance, AccountTypes accountType)
    {
        setName(name);
        setPasscode(passcode);
        setBalance(balance);
        setAccountType(accountType);
    }

    /**
     * Add a given amount of money to the balance. Checks for integer bounds (even though no one has that much
     * money).
     * @param amount of money to add to balance
     * @throws ArithmeticException if the value results in balance double overflow
     */
    @Override
    public void deposit(double amount) throws ArithmeticException
    {
        if((Double.compare(amount, Double.MAX_VALUE - balance)) > 0 | amount < 0)
            throw new ArithmeticException("Error depositing");
        else
            balance += amount;
    }

    /**
     * Attempts to withdraw a given amount from balance and returns the remaining balance as a string.
     * @param amount to withdraw
     * @return remaining balance as a string
     * @throws WithdrawalAmountException when the given amount is greater than the balance.
     */
    @Override
    public String withdraw(double amount) throws WithdrawalAmountException
    {
        if(Double.compare(amount, balance) > 0)
            throw new WithdrawalAmountException();
        else
        {
            balance -= amount;
            return getBalanceString();
        }

    }

    /**
     * Attempts to set the passcode to the given 4-digit contained in the string code.
     * @param code the passcode as a string
     * @throws InvalidPasscodeException if the passcode does not adhere to the 4-digit format
     */
    @Override
    public void setPasscode(String code) throws InvalidPasscodeException
    {
        // handle invalid passcodes using Integer parsing
        try
        {
            if(code.length() != 4)
                throw new NumberFormatException();

            int codeAsInt = Integer.parseInt(code);
            if(codeAsInt < 0 || codeAsInt > 9999)
                throw new NumberFormatException();
            else
            {
                this.passcode = code;
            }

        } catch (NumberFormatException e) {
            throw new InvalidPasscodeException();
        }
    }

    /**
     * Returns the passcode to verify account access when withdrawing and removing.
     * @return the passcode
     */
    private String getPasscode()
    {
        return this.passcode;
    }

    /**
     * Public method to verify the given passcode so that the passcode can't be obtained from the caller after
     * initialization.
     * @param passcodeToCheck the user given passcode to verify access
     * @return true if the given passcode matches
     */
    public boolean passcodeMatch(String passcodeToCheck)
    {
        return passcodeToCheck.equals(getPasscode());
    }

    /**
     * Sets the balance of the account
     * @param value amount to set the balance
     */
    @Override
    public void setBalance(double value)
    {
        this.balance = value;
    }

    /**
     * Obtain the balance of the account
     * @return balance of the account
     */
    @Override
    public double getBalance()
    {
        return balance;
    }

    /**
     * Cleaner string format of the string with rounding to the nearest cent.
     * @return prettier balance
     */
    public String getBalanceString()
    {
        return Double.toString(Math.round(getBalance() * 100.0) / 100.0);
    }

    /**
     * Set the name of the account
     * @param name of the account
     */
    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Obtain the name of the account
     * @return name of the account
     */
    @Override
    public String getName()
    {
        return name;
    }

    /**
     * Returns the account type enum.
     * @return AccountTypes.STANDARD of AccountTypes.VIP depending on the subclass.
     */
    public AccountTypes getAccountType()
    {
        return accountType;
    }

    /**
     * Sets the account type to the given AccountTypes enum. This is expected to be done by the implementing
     * subclass of this abstract class (in their constructors, ideally).
     * @param accountType type of the account (STANDARD or VIP)
     */
    public void setAccountType(AccountTypes accountType)
    {
        this.accountType = accountType;
    }
}

class standardAccount extends AbstractBankAccount
{
    public standardAccount()
    {
        super(AccountTypes.STANDARD);
    }
    public standardAccount(String name, String passcode, double balance)
    {
        super(name, passcode, balance, AccountTypes.STANDARD);
    }

    /**
     * Apply the standard account interest calculation for the given duration of collection (in months).
     * @param months of interest building
     * @return interest amount after the given number of months
     */
    @Override
    public double calculateInterest(int months)
    {
        return Math.round((getBalance() * rateStandard * months * 100.0)) / 100.0;
    }

}

class VIPAccount extends AbstractBankAccount
{
    public VIPAccount()
    {
        super(AccountTypes.VIP);
    }
    public VIPAccount(String name, String passcode, double balance)
    {
        super(name, passcode, balance, AccountTypes.VIP);
    }

    /**
     * Apply the VIP account interest calculation for the given duration of collection (in months).
     * @param months of interest building
     * @return interest amount after the given number of months
     */
    @Override
    public double calculateInterest(int months)
    {
        return Math.round( (getBalance() * ((Math.pow((1+rateVIP), months)) - 1) * 100.0)) / 100.0;
    }

}