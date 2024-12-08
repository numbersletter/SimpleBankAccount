package mybank;

/**
 * Used for exceptions in creation of the account whereby the given passcode is in an invalid format.
 */
public class InvalidPasscodeException extends RuntimeException
{
    public InvalidPasscodeException()
    {
        super("Invalid passcode");
    }
}
