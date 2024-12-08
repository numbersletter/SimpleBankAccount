package mybank;

public class WrongPasscodeWhenFindingException extends RuntimeException {
  /**
   * Thrown when the given user passcode does not match with any existing bank account (which is indexed by the
   * account name). This occurs in withdrawal or removal functions.
   */
  public WrongPasscodeWhenFindingException() {
    super("Wrong passcode");
  }
}
