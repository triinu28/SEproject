package exceptions;

public class AccountBalanceNegativeException extends Exception
{
private static final long serialVersionUID = 1L;
	
	public AccountBalanceNegativeException()
	  {
	    super();
	  }
	
	public AccountBalanceNegativeException(String s)
	  {
	    super(s);
	  }
}