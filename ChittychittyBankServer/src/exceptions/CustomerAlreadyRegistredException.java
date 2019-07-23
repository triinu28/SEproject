package exceptions;

public class CustomerAlreadyRegistredException extends Exception{
private static final long serialVersionUID = 1L;
	
	public CustomerAlreadyRegistredException()
	  {
	    super();
	  }
	
	public CustomerAlreadyRegistredException(String s)
	  {
	    super(s);
	  }

}
