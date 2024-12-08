package mybank;

public class WrongChoiceException extends RuntimeException
{
    public WrongChoiceException()
    {
        super("Wrong choice");
    }
}
