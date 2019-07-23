package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.persistence.Entity;

import exceptions.NotEnoughCurrencyException;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Office implements Serializable {
	@XmlID
	private String location;
	private Map<Currency, Float> currencys;
	private float forexRate;
	private List<Account> accounts;

	public Office(String location, Map<Currency, Float> currencys, float forexRate, List<Account> accounts) {
		super();
		this.location = location;
		this.currencys = currencys;
		this.forexRate = forexRate;
		this.accounts = accounts;
	}
	
	public Office(String location, Map<Currency, Float> currencys, float forexRate) {
		super();
		this.location = location;
		this.currencys = currencys;
		this.forexRate = forexRate;
		accounts = new ArrayList<Account>();
	}

	public Office(String location, float forexRate) {
		super();
		this.location = location;
		this.forexRate = forexRate;
		currencys = new HashMap<Currency, Float>();
		accounts = new ArrayList<Account>();
	}

	public float getCurrency(Currency c) {
		return currencys.get(c);
	}
	
	public void setCurrency(Currency currency, float currencyValue) {
		currencys.put(currency, currencyValue);
	}
	
	public Currency[] getCurrencys() {
		Set<Currency> keySet = currencys.keySet();
		Currency[] array = keySet.stream().toArray(Currency[]::new);
		return array;
	}
	
	public String getLocation() {
		return location;
	}
	
	public float getForexRate() {
		return forexRate;
	}

	public void setForexRate(float forexRate) {
		this.forexRate = forexRate;
	}

	public Account getAccount(String cardNr, String PIN) {
		Account account = null;
		for (Account a: accounts) {
			if (a.getCardNr()==cardNr || a.getPIN()==PIN) {
				account = a;
				break;
			}
		}
		return account;
	}

	public void addAccount(Account account) {
		accounts.add(account);
	}

	public void changeCurrency(Currency currency, float change) throws NotEnoughCurrencyException {
		float inStock = currencys.get(currency);
		if (inStock+change<0) 
			throw new NotEnoughCurrencyException();
		this.setCurrency(currency, inStock+change);
	}
	
}
