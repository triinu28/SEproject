package exceptions;

public class NotEnoughCurrencyException extends Exception
{
private static final long serialVersionUID = 1L;
	
	public NotEnoughCurrencyException()
	  {
	    super();
	  }
	
	public NotEnoughCurrencyException(String s)
	  {
	    super(s);
	  }
}
