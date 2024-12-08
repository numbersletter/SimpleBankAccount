package mybank;

public class NameAlreadyExists extends RuntimeException
{
    /**
     * Thrown when the user attempts to register for a bank account with a name that already exists in the
     * map of accounts.
     */
    public NameAlreadyExists(String message)
    {
        super("Name '" + message + "' already exists.");
    }
}
