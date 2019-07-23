package presentation;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ValidateCustomer extends JFrame {

	private JPanel contentPane;
	private JLabel lblCustomerName;
	private JTextField customerNameField;
	private JLabel lblDniCardNr;
	private JTextField DNIcardField;
	private JLabel lblAccountCardNr;
	private JTextField accountCardField;
	private JLabel lblPin;
	private JTextField PINField;
	private JButton btnCancel;
	private JButton btnValidateCustomer;
	private JTextArea outputMessage;

	public ValidateCustomer(BLFacade businessLogic, JFrame newFrame) {
		setTitle("Validate customer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][][grow][]", "[][][][][][][][grow]"));
		
		lblCustomerName = new JLabel("Customer name:");
		contentPane.add(lblCustomerName, "cell 0 0");
		
		customerNameField = new JTextField();
		contentPane.add(customerNameField, "cell 2 0 2 1,growx");
		customerNameField.setColumns(10);
		
		lblDniCardNr = new JLabel("DNI card nr:");
		contentPane.add(lblDniCardNr, "cell 0 1");
		
		DNIcardField = new JTextField();
		contentPane.add(DNIcardField, "cell 2 1 2 1,growx");
		DNIcardField.setColumns(10);
		
		lblAccountCardNr = new JLabel("Account card nr:");
		contentPane.add(lblAccountCardNr, "cell 0 2");
		
		accountCardField = new JTextField();
		contentPane.add(accountCardField, "cell 2 2 2 1,growx");
		accountCardField.setColumns(10);
		
		lblPin = new JLabel("PIN:");
		contentPane.add(lblPin, "cell 0 3");
		
		PINField = new JTextField();
		contentPane.add(PINField, "cell 2 3 2 1,growx");
		PINField.setColumns(10);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManageAccounts a = new ManageAccounts(businessLogic);
				a.setVisible(true);
				ValidateCustomer.this.dispose();
			}
		});
		contentPane.add(btnCancel, "cell 2 5");
		
		outputMessage = new JTextArea();
		contentPane.add(outputMessage, "cell 0 7 4 1,grow");
		
		btnValidateCustomer = new JButton("Validate customer");
		btnValidateCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String custName = customerNameField.getText();
					String DNIcardNr = DNIcardField.getText();
					String accountCardNr = accountCardField.getText();
					String PIN = PINField.getText();
				
					if (businessLogic.validateCustomer(custName, DNIcardNr, accountCardNr, PIN)) {
						newFrame.setVisible(true);
						ValidateCustomer.this.dispose();
					}
					else
						outputMessage.setText("Wrong data, plese try again");
				}
				catch (NumberFormatException ex) {
					outputMessage.setText("Wrong data, plese try again");
				}
			}
		});
		contentPane.add(btnValidateCustomer, "cell 3 5");
	}

}
