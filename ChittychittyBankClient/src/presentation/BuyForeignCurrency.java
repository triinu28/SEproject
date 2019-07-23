package presentation;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import businessLogic.Currency;
import exceptions.AccountBalanceNegativeException;
import exceptions.NotEnoughCurrencyException;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BuyForeignCurrency extends JFrame {

	private JPanel contentPane;
	private JLabel lblCurrency;
	private JComboBox<Currency> currencyComboBox;
	private JLabel lblAmount;
	private JTextField amountField;
	private JButton btnCancel;
	private JButton btnBuyCurrency;
	private JTextArea outputMessage;

	public BuyForeignCurrency(BLFacade businessLogic) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][][grow][]", "[][][][grow]"));
		
		lblCurrency = new JLabel("Currency:");
		contentPane.add(lblCurrency, "cell 0 0");
		
		Currency[] array = businessLogic.getCurrencys();
		currencyComboBox = new JComboBox<Currency>(array);
		contentPane.add(currencyComboBox, "cell 2 0 2 1,growx");
		
		lblAmount = new JLabel("Amount:");
		contentPane.add(lblAmount, "cell 0 1");
		
		amountField = new JTextField();
		contentPane.add(amountField, "cell 2 1 2 1,growx");
		amountField.setColumns(10);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManageAccounts a = new ManageAccounts(businessLogic);
				a.setVisible(true);
				BuyForeignCurrency.this.dispose();
			}
		});
		contentPane.add(btnCancel, "cell 2 2");
		
		outputMessage = new JTextArea();
		contentPane.add(outputMessage, "cell 0 3 4 1,grow");
		
		btnBuyCurrency = new JButton("Buy currency");
		btnBuyCurrency.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Currency currency = (Currency) currencyComboBox.getSelectedItem();
					float amount = Integer.parseInt(amountField.getText());
					String message = businessLogic.buyCurrency(currency, amount);
					ManageAccounts a = new ManageAccounts(businessLogic);
					a.setVisible(true);
					BuyForeignCurrency.this.dispose();
					TransactionSuccessful t = new TransactionSuccessful(message);
					t.setVisible(true);
				}
				
				catch (NumberFormatException ex) {
					outputMessage.setText("Amount has to be an Integer");
				} catch (NotEnoughCurrencyException e1) {
					outputMessage.setText("There is not enough currency in stock");
				} catch (AccountBalanceNegativeException e1) {
					outputMessage.setText("The account does not have enough balance");
				}
			}
		});
		contentPane.add(btnBuyCurrency, "cell 3 2");
		
	}

}
