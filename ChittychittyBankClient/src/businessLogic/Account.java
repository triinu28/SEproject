package businessLogic;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;

import exceptions.AccountBalanceNegativeException;


@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Account {
	@XmlID
	private String cardNr;
	private String PIN;
	private float balance;
	private Customer customer;
	private Office office;
	
	public Account(String cardNr, String pIN, float balance, Customer customer, Office office) {
		super();
		this.cardNr = cardNr;
		PIN = pIN;
		this.balance = balance;
		this.customer = customer;
		this.office = office;
	}
	
	public String getCardNr() {
		return cardNr;
	}
	
	public float getBalance() {
		return balance;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public Office getOffice() {
		return office;
	}
	
	public String getPIN() {
		return PIN;
	}
	
	public boolean validate(String cardNr, String PIN) {
		return this.cardNr.equals(cardNr) && this.PIN.equals(PIN);
	}
	
	public void changeBalance(float change) throws AccountBalanceNegativeException {
		if (balance+change<0)
			throw new AccountBalanceNegativeException();
		balance += change;
	}
	
	public boolean checkCustomer(String custName, String DNIcardNr){
		return customer.getName().equals(custName) && customer.getDNIcardNr().equals(DNIcardNr);
	}
	
}
