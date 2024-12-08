package mybank;

public class AccountDoesNotExistException extends RuntimeException
{
    /**
     * Thrown when the given name is not registered to any stored bank accounts.
     * @param name of the account
     */
    public AccountDoesNotExistException(String name)
    {
        super("Name: " + name + " does not exist.");
    }
}
