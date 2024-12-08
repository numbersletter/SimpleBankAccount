package mybank;

public class WithdrawalAmountException extends RuntimeException {
  public WithdrawalAmountException() {
    super("Not enough balance");
  }
}
