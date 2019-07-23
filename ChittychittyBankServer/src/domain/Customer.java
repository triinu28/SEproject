package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;

import exceptions.AccountNotFoundException;


@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Customer {
	private String name;
	@XmlID
	private String DNIcardNr;
	private int age;
	private List<Account> accounts;
	
	public Customer(String name, String dNIcardNr, int age, List<Account> accounts) {
		super();
		this.name = name;
		this.age = age;
		DNIcardNr = dNIcardNr;
		this.accounts = accounts;
	}
	
	public Customer(String name, String dNIcardNr, int age) {
		super();
		this.name = name;
		this.age = age;
		DNIcardNr = dNIcardNr;
		accounts = new ArrayList<Account>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getDNIcardNr() {
		return DNIcardNr;
	}
	
	public void setDNIcardNr(String dNIcardNr) {
		DNIcardNr = dNIcardNr;
	}
	
	public Account validateAccount(String cardNr, String PIN) throws AccountNotFoundException {
		for (Account a : accounts) {
			if (a.validate(cardNr, PIN))
				return a;
		}
		throw new AccountNotFoundException();
	}
	
	public void addAccount(Account a) {
		accounts.add(a);
	}

	@Override
	public String toString() {
		return name;
	}
	
}
