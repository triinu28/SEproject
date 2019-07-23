package dataAccess;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import domain.Account;
import domain.Currency;
import domain.Customer;
import domain.Employee;
import domain.Office;
import exceptions.*;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess  {
	protected static EntityManager  db;
	protected static EntityManagerFactory emf;


	ConfigXML c;

	public DataAccess(boolean initializeMode)  {
		
		c=ConfigXML.getInstance();
		
		System.out.println("Creating DataAccess instance => isDatabaseLocal: "+c.isDatabaseLocal()+" getDatabBaseOpenMode: "+c.getDataBaseOpenMode());

		String fileName=c.getDbFilename();
		if (initializeMode)
			fileName=fileName+";drop";
		
		if (c.isDatabaseLocal()) {
			  emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			  db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);

			  db = emf.createEntityManager();
    	   }
	}

	public DataAccess()  {	
		 new DataAccess(false);
	}
	
	
	/**
	 * This is the data access method that initializes the database with some offices, employees and currencys.
	 * This method is invoked by the business logic (constructor of BLFacadeImplementation) when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	public void initializeDB(){
		db.getTransaction().begin();
		try {
			List<Office> offices = new ArrayList<Office>();
			
			offices.add(new Office ("Atlanta", 3));
			offices.add(new Office ("Atlanta airoport", 2));
			offices.add(new Office ("Tallinn", 1));
			
			List<Currency> currencys = new ArrayList<Currency>();
			
			currencys.add(new Currency("USD", (float) 1.16579));
			currencys.add(new Currency("GBP", (float) 0.87656));
			currencys.add(new Currency("CAD", (float) 1.51218));
			currencys.add(new Currency("CHF", (float) 1.15343));
			currencys.add(new Currency("AUD", (float) 1.54238));
			currencys.add(new Currency("IRN", (float) 78.9885));
			currencys.add(new Currency("TND", (float) 2.98385));
			currencys.add(new Currency("AED", (float) 4.28139));
			currencys.add(new Currency("JPY", (float) 127.596));
			
			Random rand = new Random();
			for (Currency c : currencys) {
				for (Office o : offices) {
					o.setCurrency(c, rand.nextInt(10000)+100);
				}
			}
			
			List<Employee> employees = new ArrayList<Employee>();
			
			employees.add(new Employee("Liisa", "123", offices.get(0)));
			employees.add(new Employee("Laura", "1234", offices.get(1)));
			employees.add(new Employee("Kadi", "12345", offices.get(2)));
			employees.add(new Employee("Mark", "123456", offices.get(0)));
			employees.add(new Employee("Markus", "1234567", offices.get(1)));
			employees.add(new Employee("Mari", "12345678", offices.get(2)));
			
			Customer c1 = new Customer("Oskar Mets", "12345", 22);
			Account a1 = new Account("234567891", "1234", 10000, c1, offices.get(0));
			c1.addAccount(a1);
			offices.get(0).addAccount(a1);
			Account a2 = new Account("234567890", "1234", 10000, c1, offices.get(1));
			c1.addAccount(a2);
			offices.get(1).addAccount(a2);
			
			Customer c2 = new Customer("Mari Mets", "123456", 21);
			Account a3 = new Account("123456789", "1234", 10000, c2, offices.get(0));
			c1.addAccount(a3);
			offices.get(0).addAccount(a3);
			Account a4 = new Account("023456789", "1234", 10000, c2, offices.get(2));
			c1.addAccount(a4);
			offices.get(2).addAccount(a4);
			
			for (Currency c : currencys) {
				db.persist(c);
			}
			
			for (Office o : offices) {
				db.persist(o);
			}
			
			for (Employee e : employees) {
				db.persist(e);
			}	
			
			db.persist(c1);
			db.persist(a1);
			db.persist(a2);
			
			db.persist(c2);
			db.persist(a3);
			db.persist(a4);
			
			db.getTransaction().commit();
			System.out.println("Db initialized");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public Customer addCustomer(String name, String DNIcardNr, int age) throws  CustomerAlreadyRegistredException {
		try {
			this.findCustomer(name, DNIcardNr);
			throw new CustomerAlreadyRegistredException();
		}
		catch(CustomerNotFoundException e) {
			db.getTransaction().begin();
			Customer cust = new Customer(name, DNIcardNr, age);
			//db.persist(q);
			db.persist(cust); // db.persist(q) not required when CascadeType.PERSIST is added in questions property of Event class
							// @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
			db.getTransaction().commit();
			return cust;
		}
		
	}
	
	public Account addAccount(float balance, String custName, String DNIcardNr, Office office) {
		Random rand = new Random();
		Office o = findOffice(office.getLocation());
		Customer customer=null;
		try {
			customer = this.findCustomer(custName, DNIcardNr);
		}
		catch (CustomerNotFoundException e){} 
		db.getTransaction().begin();
		Account a = new Account(Integer.toString(rand.nextInt(999999999)), Integer.toString(rand.nextInt(9999)), balance, customer, o);
		db.persist(a);
		customer.addAccount(a);
		o.addAccount(a);
		db.merge(customer);
		db.merge(customer);
		db.getTransaction().commit();
		return a;
		
	}
	
	public Employee getEmployee(String username, String password) throws EmployeeNotFoundException{
		Employee employee = null;
		try{
			try{
				TypedQuery<Employee> query = db.createQuery("SELECT emp FROM Employee emp WHERE emp.username= ?1 AND emp.password = ?2",Employee.class);
				query.setParameter("1", username);
				query.setParameter("2", password);
			    List<Employee> employees = query.getResultList();
			    employee = employees.get(0);
			}
			catch(IndexOutOfBoundsException ex)
			{
				throw new PersistenceException();
			}
		    
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
			throw new EmployeeNotFoundException();
		}
		return employee;
		
	}
	
	public Customer findCustomer(String custName, String DNIcardNr) throws CustomerNotFoundException {
		Customer customer = null;
		try
		{
			try
			{
				TypedQuery<Customer> query = db.createQuery("SELECT c FROM Customer c WHERE c.name= ?1 AND c.DNIcardNr = ?2",Customer.class);
				query.setParameter("1", custName);
				query.setParameter("2", DNIcardNr);
			    List<Customer> customers = query.getResultList();
			    customer = customers.get(0);
			}
			catch(IndexOutOfBoundsException ex)
			{
				throw new PersistenceException();
			}
		    
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
			throw new CustomerNotFoundException();
		}
		return customer;
	}
	
	public Account findAccount(String accountCardNr, String PIN) throws AccountNotFoundException {
		Account account = null;
		try
		{
			try
			{
				TypedQuery<Account> query = db.createQuery("SELECT a FROM Account a WHERE a.cardNr= ?1 AND a.PIN = ?2",Account.class);
				query.setParameter("1", accountCardNr);
				query.setParameter("2", PIN);
			    List<Account> accounts = query.getResultList();
			    account = accounts.get(0);
			}
			catch(IndexOutOfBoundsException ex)
			{
				throw new PersistenceException();
			}
		    
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
			throw new AccountNotFoundException();
		}
		return account;
	}
	
	public void changeCurrency(Office office, Currency currency, float amount, Account account, float amountEUR) throws NotEnoughCurrencyException, AccountBalanceNegativeException{
		try{
			Office o = findOffice(office.getLocation());
			Currency c=null;
			for (Currency i : o.getCurrencys()) {
				if (i.getName().equals(currency.getName()))
					c=i;
			}
			Account a = this.findAccount(account.getCardNr(), account.getPIN());
			
			db.getTransaction().begin();
			o.changeCurrency(c, amount);
			a.changeBalance(amountEUR);
			db.getTransaction().commit();
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
		}
		
		catch(AccountNotFoundException e){}
	}
	
	public float changeBalance(Account account, float amount) throws AccountBalanceNegativeException {
		try {
			Account a = this.findAccount(account.getCardNr(), account.getPIN());
			db.getTransaction().begin();
			a.changeBalance(amount);
			db.getTransaction().commit();
			return a.getBalance();
		} catch (AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public Office findOffice(String officeLocation){
		Office office = null;
		try
		{
			try
			{
				TypedQuery<Office> query = db.createQuery("SELECT o FROM Office o WHERE o.location= ?1",Office.class);
				query.setParameter("1", officeLocation);
			    List<Office> offices = query.getResultList();
			    office = offices.get(0);
			}
			catch(IndexOutOfBoundsException ex)
			{
				throw new PersistenceException();
			}
		    
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
		}
		return office;
	}

	public Currency[] getCurrencys(String officeLocation){
		Office o = findOffice(officeLocation);
		return o.getCurrencys();
	}

	public void close(){
		db.close();
		System.out.println("DataBase closed");
	}

	public void printDB(){
		try{
			TypedQuery<Account> query = db.createQuery("SELECT a FROM Account a",Account.class);
			List<Account> accounts = query.getResultList();
			System.out.println("List of accounts:");
			for (Account a : accounts) {
				System.out.println("Card nr: " + a.getCardNr()+ ", PIN: "+a.getPIN()+ ", Balance: "+a.getBalance()+", Customer: "+a.getCustomer());
			}
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
		}
		
		
		try{
			TypedQuery<Currency> query = db.createQuery("SELECT c FROM Currency c",Currency.class);
			List<Currency> currencys = query.getResultList();
			System.out.println("List of currencys:");
			for (Currency c : currencys) {
				System.out.println("Name: " + c.getName()+ ", ValueEUR: "+c.getValueEur());
			}
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
		}
		
		try{
			TypedQuery<Customer> query = db.createQuery("SELECT c FROM Customer c",Customer.class);
			List<Customer> customers = query.getResultList();
			System.out.println("List of customers:");
			for (Customer c : customers) {
				System.out.println("Name: " + c.getName()+ ", DNIcardNr: "+c.getDNIcardNr());
			}
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
		}
		
		try{
			TypedQuery<Employee> query = db.createQuery("SELECT e FROM Employee e",Employee.class);
			List<Employee> employees = query.getResultList();
			System.out.println("List of employees:");
			for (Employee e : employees) {
				System.out.println("Username: " + e.getUsername()+ ", Password: "+e.getPassword());
			}
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
		}
		
		try{
			TypedQuery<Office> query = db.createQuery("SELECT o FROM Office o",Office.class);
			List<Office> offices = query.getResultList();
			System.out.println("List of offices:");
			for (Office o : offices) {
				System.out.println("Location: " + o.getLocation()+ ", ForexRate: "+o.getForexRate());
				Currency[] currencys = o.getCurrencys();
				for (Currency c : currencys) {
					System.out.println("Name: " + c.getName()+ ", InStock: "+o.getCurrency(c));
				}
			}
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
		}
	}
	
	
	public void emptyDB(){
		db.clear();
		try{
			TypedQuery<Account> query = db.createQuery("SELECT a FROM Account a",Account.class);
			List<Account> accounts = query.getResultList();
			System.out.println("List of accounts:");
			for (Account a : accounts) {
				db.getTransaction().begin();
				db.remove(a);
				db.getTransaction().commit();
			}
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
		}
		
		
		try{
			TypedQuery<Currency> query = db.createQuery("SELECT c FROM Currency c",Currency.class);
			List<Currency> currencys = query.getResultList();
			System.out.println("List of currencys:");
			for (Currency c : currencys) {
				db.getTransaction().begin();
				db.remove(c);
				db.getTransaction().commit();
			}
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
		}
		
		try{
			TypedQuery<Customer> query = db.createQuery("SELECT c FROM Customer c",Customer.class);
			List<Customer> customers = query.getResultList();
			System.out.println("List of customers:");
			for (Customer c : customers) {
				db.getTransaction().begin();
				db.remove(c);
				db.getTransaction().commit();
			}
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
		}
		
		try{
			TypedQuery<Employee> query = db.createQuery("SELECT e FROM Employee e",Employee.class);
			List<Employee> employees = query.getResultList();
			System.out.println("List of employees:");
			for (Employee e : employees) {
				db.getTransaction().begin();
				db.remove(e);
				db.getTransaction().commit();
			}
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
		}
		
		try{
			TypedQuery<Office> query = db.createQuery("SELECT o FROM Office o",Office.class);
			List<Office> offices = query.getResultList();
			System.out.println("List of offices:");
			for (Office o : offices) {
				db.getTransaction().begin();
				db.remove(o);
				db.getTransaction().commit();
			}
		}
		catch(PersistenceException ex)
		{
			System.out.print(ex + "\n");
		}
	}
}