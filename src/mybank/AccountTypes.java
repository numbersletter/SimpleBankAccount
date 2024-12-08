package mybank;

/**
 * Defines the two account types that are used in this banking application. This is mostly a helper class
 * to easily access / display the account type.
 */
public enum AccountTypes
{
    STANDARD("Standard"),
    VIP("VIP");

    private final String description;

    AccountTypes(String standard)
    {
        this.description = standard;
    }

    @Override
    public String toString()
    {
        return description;
    }
}