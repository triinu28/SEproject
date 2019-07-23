package presentation;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import domain.Account;
import domain.Customer;
import exceptions.CustomerAlreadyRegistredException;
import exceptions.CustomerNotFoundException;
import net.miginfocom.swing.MigLayout;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OpenAccount extends JFrame {

	private JPanel newCustContentPane;
	private JPanel existingCustContentPane;
	
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnNewCustomer;
	private JRadioButton rdbtnExistingCustomer;
	private JLabel lblCustomerName;
	private JTextField customerNameField;
	private JLabel lblDniCardNr;
	private JTextField DNIcardField;
	private JLabel lblCustomerAge;
	private JTextField customerAgeField;
	private JButton btnAddCustomer;
	private JButton btnCancel;
	private JLabel lblAccountBalance;
	private JTextField accountBalanceField;
	private JButton btnOpenAccount;
	private JTextArea outputMessage;
	
	private final ButtonGroup buttonGroup2 = new ButtonGroup();
	private JRadioButton rdbtnNewCustomer2;
	private JRadioButton rdbtnExistingCustomer2;
	private JLabel lblCustomerName2;
	private JTextField customerNameField2;
	private JLabel lblDniCardNr2;
	private JTextField DNIcardField2;
	private JButton btnFindCustomer2;
	private JButton btnCancel2;
	private JLabel lblAccountBalance2;
	private JTextField accountBalanceField2;
	private JButton btnOpenAccount2;
	private JTextArea outputMessage2;

	public OpenAccount(BLFacade businessLogic) {
		setTitle("Open Account");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		newCustContentPane = new JPanel();
		newCustContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(newCustContentPane);
		newCustContentPane.setLayout(new MigLayout("", "[][][][grow]", "[][][][][][][][grow]"));
		
		existingCustContentPane = new JPanel();
		existingCustContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		existingCustContentPane.setLayout(new MigLayout("", "[][][grow]", "[][][][][][][][grow]"));
		
		//New customer pane creation
		
		lblCustomerName = new JLabel("Customer name:");
		newCustContentPane.add(lblCustomerName, "cell 0 1");
		
		customerNameField = new JTextField();
		newCustContentPane.add(customerNameField, "cell 2 1 2 1,growx");
		customerNameField.setColumns(10);
		
		lblDniCardNr = new JLabel("DNI card nr:");
		newCustContentPane.add(lblDniCardNr, "cell 0 2");
		
		DNIcardField = new JTextField();
		newCustContentPane.add(DNIcardField, "cell 2 2 2 1,growx");
		DNIcardField.setColumns(10);
		
		lblCustomerAge = new JLabel("Customer age:");
		newCustContentPane.add(lblCustomerAge, "cell 0 3");
		
		customerAgeField = new JTextField();
		newCustContentPane.add(customerAgeField, "cell 2 3 2 1,growx");
		customerAgeField.setColumns(10);
		
		outputMessage = new JTextArea();
		newCustContentPane.add(outputMessage, "cell 0 7 4 1,grow");
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManageAccounts a = new ManageAccounts(businessLogic);
				a.setVisible(true);
				OpenAccount.this.dispose();
			}
		});
		newCustContentPane.add(btnCancel, "cell 2 6");
		
		btnAddCustomer = new JButton("Add customer");
		btnAddCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String custName = customerNameField.getText();
					String DNIcardNr = DNIcardField.getText();
					int age = Integer.parseInt(customerAgeField.getText());
					if (age<18 || age>120)
						throw new NumberFormatException();
					businessLogic.addCustomer(custName, DNIcardNr, age); 
					btnOpenAccount.setEnabled(true);
					btnAddCustomer.setEnabled(false);
					customerNameField.setEditable(false);
					DNIcardField.setEditable(false);
					customerAgeField.setEditable(false);
					rdbtnExistingCustomer.setEnabled(false);
				}
				
				catch (NumberFormatException ex) {
					outputMessage.setText("Age has to be an Integer between 18 and 120");
				} catch (CustomerAlreadyRegistredException e1) {
					outputMessage.setText("Customer already registred");
				}
			}
		});
		newCustContentPane.add(btnAddCustomer, "cell 3 4,alignx right");
		
		lblAccountBalance = new JLabel("Account balance:");
		newCustContentPane.add(lblAccountBalance, "cell 0 5");
		
		accountBalanceField = new JTextField();
		newCustContentPane.add(accountBalanceField, "cell 2 5 2 1,growx");
		accountBalanceField.setColumns(10);
		
		btnOpenAccount = new JButton("Open Account");
		btnOpenAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					float balance = Float.valueOf(accountBalanceField.getText());
					if (balance<1000) 
						throw new NumberFormatException();
					String custName = customerNameField.getText();
					String DNIcardNr = DNIcardField.getText();
					String message = businessLogic.openAccount(custName, DNIcardNr, balance); 
					ManageAccounts a = new ManageAccounts(businessLogic);
					a.setVisible(true);
					OpenAccount.this.dispose();
					TransactionSuccessful t = new TransactionSuccessful(message);
					t.setVisible(true);
				}
				
				catch (NumberFormatException ex) {
					outputMessage.setText("Balance has to be a float that is at least 1000€");
				}
			}
		});
		newCustContentPane.add(btnOpenAccount, "cell 3 6,alignx right");
		btnOpenAccount.setEnabled(false);
		
		rdbtnNewCustomer = new JRadioButton("New customer");
		rdbtnNewCustomer.setSelected(true);
		rdbtnNewCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OpenAccount.this.setContentPane(newCustContentPane);
				OpenAccount.this.validate();
			}
		});
		buttonGroup.add(rdbtnNewCustomer);
		newCustContentPane.add(rdbtnNewCustomer, "cell 0 0");
		
		rdbtnExistingCustomer = new JRadioButton("Existing customer");
		rdbtnExistingCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OpenAccount.this.setContentPane(existingCustContentPane);
				OpenAccount.this.validate();
				rdbtnExistingCustomer2.setSelected(true);
			}
		});
		buttonGroup.add(rdbtnExistingCustomer);
		newCustContentPane.add(rdbtnExistingCustomer, "cell 2 0 2 1");
		
		//Existing customer pane creation
		
		lblCustomerName2 = new JLabel("Customer name:");
		existingCustContentPane.add(lblCustomerName2, "cell 0 1");
		
		customerNameField2 = new JTextField();
		existingCustContentPane.add(customerNameField2, "cell 2 1 2 1,growx");
		customerNameField2.setColumns(10);
		
		lblDniCardNr2 = new JLabel("DNI card nr:");
		existingCustContentPane.add(lblDniCardNr2, "cell 0 2");
		
		DNIcardField2 = new JTextField();
		existingCustContentPane.add(DNIcardField2, "cell 2 2 2 1,growx");
		DNIcardField2.setColumns(10);
		
		outputMessage2 = new JTextArea();
		existingCustContentPane.add(outputMessage2, "cell 0 7 4 1,grow");
		
		btnCancel2 = new JButton("Cancel");
		btnCancel2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManageAccounts a = new ManageAccounts(businessLogic);
				a.setVisible(true);
				OpenAccount.this.dispose();
			}
		});
		existingCustContentPane.add(btnCancel2, "cell 2 6");
		
		btnFindCustomer2 = new JButton("Find customer");
		btnFindCustomer2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String custName = customerNameField2.getText();
					String DNIcardNr = DNIcardField2.getText();
					businessLogic.findCustomer(custName, DNIcardNr); 
					btnOpenAccount2.setEnabled(true);
					btnFindCustomer2.setEnabled(false);
					customerNameField2.setEditable(false);
					DNIcardField2.setEditable(false);
					rdbtnNewCustomer2.setEnabled(false);
				}
				
				catch (NumberFormatException ex) {
					outputMessage2.setText("DNIcardNr has to be an Integer");
				}
				catch (CustomerNotFoundException ex) {
					outputMessage2.setText("Customer not found.");
				}
			}
		});
		existingCustContentPane.add(btnFindCustomer2, "cell 3 4,alignx right");
		
		lblAccountBalance2 = new JLabel("Account balance:");
		existingCustContentPane.add(lblAccountBalance2, "cell 0 5");
		
		accountBalanceField2 = new JTextField();
		existingCustContentPane.add(accountBalanceField2, "cell 2 5 2 1,growx");
		accountBalanceField2.setColumns(10);
		
		btnOpenAccount2 = new JButton("Open Account");
		btnOpenAccount2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					float balance = Float.valueOf(accountBalanceField2.getText());
					if (balance<1000) 
						throw new NumberFormatException();
					String custName = customerNameField2.getText();
					String DNIcardNr = DNIcardField2.getText();
					String message = businessLogic.openAccount(custName, DNIcardNr, balance); 
					ManageAccounts a = new ManageAccounts(businessLogic);
					a.setVisible(true);
					OpenAccount.this.dispose();
					TransactionSuccessful t = new TransactionSuccessful(message);
					t.setVisible(true);
				}
				
				catch (NumberFormatException ex) {
					outputMessage2.setText("Balance has to be a float that is at least 1000");
				}
			}
		});
		existingCustContentPane.add(btnOpenAccount2, "cell 3 6,alignx right");
		btnOpenAccount2.setEnabled(false);
		
		rdbtnNewCustomer2 = new JRadioButton("New customer");
		rdbtnNewCustomer2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OpenAccount.this.setContentPane(newCustContentPane);
				OpenAccount.this.validate();
				rdbtnNewCustomer.setSelected(true);
			}
		});
		buttonGroup2.add(rdbtnNewCustomer2);
		existingCustContentPane.add(rdbtnNewCustomer2, "cell 0 0");
		
		rdbtnExistingCustomer2 = new JRadioButton("Existing customer");
		rdbtnExistingCustomer2.setSelected(true);
		rdbtnExistingCustomer2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OpenAccount.this.setContentPane(existingCustContentPane);
				OpenAccount.this.validate();
			}
		});
		buttonGroup2.add(rdbtnExistingCustomer2);
		existingCustContentPane.add(rdbtnExistingCustomer2, "cell 2 0 2 1");
		
		
	}

}
