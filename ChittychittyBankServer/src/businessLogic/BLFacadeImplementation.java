package businessLogic;

import java.util.List;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Account;
import domain.Currency;
import domain.Customer;
import domain.Employee;
import domain.Office;
import exceptions.AccountBalanceNegativeException;
import exceptions.AccountNotFoundException;
import exceptions.CustomerAlreadyRegistredException;
import exceptions.CustomerNotFoundException;
import exceptions.EmployeeNotFoundException;
import exceptions.NotEnoughCurrencyException;

@WebService(name="BLFacade")
public class BLFacadeImplementation implements BLFacade {
	private Office office;
	private Account currentAccount;
	
	public BLFacadeImplementation() {
		super();
		System.out.println("Creating BLFacadeImplementation instance");
		ConfigXML c=ConfigXML.getInstance();
		
		if (c.getDataBaseOpenMode().equals("initialize")) {
			DataAccess dbManager=new DataAccess(c.getDataBaseOpenMode().equals("initialize"));
			dbManager.initializeDB();
			dbManager.close();
		}	
	}

	@WebMethod
	public boolean authenticate(String username, String password) {
		try{
			DataAccess dbManager=new DataAccess();
			Employee e = dbManager.getEmployee(username, password);
			Office o = e.getOffice();
			office = dbManager.findOffice(o.getLocation());
			dbManager.close();
			return true;
		}
		catch (EmployeeNotFoundException e) {
			return false;
		}
	}
	
	@WebMethod
	public boolean validateCustomer(String custName, String DNIcardNr, String accountCardNr, String PIN){
		try {
			DataAccess dbManager=new DataAccess();
			currentAccount = dbManager.findAccount(accountCardNr, PIN);
			boolean b = currentAccount.checkCustomer(custName, DNIcardNr);
			dbManager.close();
			return b;
		}
		
		catch (AccountNotFoundException e) {
			return false;
		}
	}
	
	@WebMethod
	public String depositMoney(float amount){
		float newBalance=0;
		try {
			DataAccess dbManager=new DataAccess();
			newBalance = dbManager.changeBalance(currentAccount, amount);
			dbManager.close();
		}
		catch (AccountBalanceNegativeException e) {
			
		}
		return "Transaction was successful.\n"+
			Float.toString(amount)+"€ were deposited to account.\n"
					+ "New balance:"+newBalance;
	}
	
	@WebMethod
	public String withdrawMoney(float amount) throws AccountBalanceNegativeException{
		DataAccess dbManager=new DataAccess();
		float newBalance = dbManager.changeBalance(currentAccount, amount*-1);
		dbManager.close();
		return "Transaction was successful.\n"+
			Float.toString(amount)+"€ were withdraw from account.\n"
					+ "New balance:"+newBalance;
	}
	
	@WebMethod
	public String buyCurrency(Currency currency, float amount) throws NotEnoughCurrencyException, AccountBalanceNegativeException {
		float amountEUR = amount*(1+office.getForexRate()/100)*currency.getValueEur();
		DataAccess dbManager=new DataAccess();
		dbManager.changeCurrency(office, currency, amount*-1, currentAccount, amountEUR*-1);
		dbManager.close();
		return "Customer recives "+amount+currency+". \n"+ amountEUR+"€ is removed from the account.";
	}
	
	@WebMethod
	public String sellCurrency(Currency currency, float amount) {
		try {
			float amountEUR = amount*(1-office.getForexRate()/100)*currency.getValueEur();
			DataAccess dbManager=new DataAccess();
			dbManager.changeCurrency(office, currency, amount, currentAccount, amountEUR);
			dbManager.close();
			return "Customer gives "+amount+currency+". \n"+ amountEUR+"€ is transferred to the account.";
		}
		catch (NotEnoughCurrencyException e) {}
		catch (AccountBalanceNegativeException e) {}
		return null;
	}
	
	@WebMethod
	public Currency[] getCurrencys() {
		DataAccess dbManager=new DataAccess();
		Currency[] c = dbManager.getCurrencys(office.getLocation());
		dbManager.close();
		return c;
	}

	@WebMethod
	public Customer addCustomer(String custName, String DNIcardNr, int age) throws CustomerAlreadyRegistredException {
		DataAccess dbManager=new DataAccess();
		Customer c = dbManager.addCustomer(custName, DNIcardNr, age);
		dbManager.close();
		return c;
	}
	
	@WebMethod
	public String openAccount(String custName, String DNIcardNr, float balance) {
		DataAccess dbManager=new DataAccess();
		Account a = dbManager.addAccount(balance, custName, DNIcardNr, office);
		dbManager.close();
		return "Transaction was successful.\n"+
				a.getCustomer().toString()+" opened a new account with balance "+Float.toString(balance)+"\n"+
				"Account number: "+a.getCardNr()+"\n Account PIN:" +a.getPIN();
	}
	
	@WebMethod
	public Customer findCustomer(String custName, String DNIcardNr) throws CustomerNotFoundException{
		DataAccess dbManager=new DataAccess();
		Customer c = dbManager.findCustomer(custName, DNIcardNr);
		dbManager.close();
		return c;
	}
	
	public void printDB(){
		DataAccess dbManager=new DataAccess();
		dbManager.printDB();
		dbManager.close();
	}
	
}
