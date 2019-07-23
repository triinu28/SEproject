package businessLogic;

import javax.jws.WebMethod;
import javax.jws.WebService;

import exceptions.AccountBalanceNegativeException;
import exceptions.CustomerAlreadyRegistredException;
import exceptions.CustomerNotFoundException;
import exceptions.NotEnoughCurrencyException;

@WebService
public interface BLFacade {
	@WebMethod
	public boolean authenticate(String username, String password);
	@WebMethod
	public boolean validateCustomer(String custName, String DNIcardNr, String accountCardNr, String PIN);
	@WebMethod
	public String depositMoney(float amount);
	@WebMethod
	public String withdrawMoney(float amount) throws AccountBalanceNegativeException;
	@WebMethod
	public String buyCurrency(Currency currency, float amuont) throws NotEnoughCurrencyException, AccountBalanceNegativeException;
	@WebMethod
	public String sellCurrency(Currency currency, float amuont);
	@WebMethod
	public Currency[] getCurrencys();
	@WebMethod
	public Customer addCustomer(String custName, String DNIcardNr, int age) throws CustomerAlreadyRegistredException;
	@WebMethod
	public String openAccount(String custName, String DNIcardNr, float balance);
	@WebMethod
	public Customer findCustomer(String custName, String DNIcardNr) throws CustomerNotFoundException;
	@WebMethod
	public void printDB();

}
