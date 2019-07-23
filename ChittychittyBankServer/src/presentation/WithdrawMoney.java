package presentation;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import exceptions.AccountBalanceNegativeException;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WithdrawMoney extends JFrame {
	private JPanel contentPane;
	private JTextField withdrawField;
	private JLabel lblWithdrawAmount;
	private JButton btnCancel;
	private JButton btnWithdraw;
	private JTextArea outputMessage;
	
	public WithdrawMoney(BLFacade businessLogic) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][][grow][]", "[][][grow]"));
		
		lblWithdrawAmount = new JLabel("Withdraw amount:");
		contentPane.add(lblWithdrawAmount, "cell 0 0");
		
		withdrawField = new JTextField();
		contentPane.add(withdrawField, "cell 2 0 2 1,growx");
		withdrawField.setColumns(10);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManageAccounts a = new ManageAccounts(businessLogic);
				a.setVisible(true);
				WithdrawMoney.this.dispose();
			}
		});
		contentPane.add(btnCancel, "cell 2 1");
		
		btnWithdraw = new JButton("Withdraw");
		btnWithdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					float amount = Float.valueOf(withdrawField.getText());
					if (amount<0) throw new NumberFormatException();
					String message = businessLogic.withdrawMoney(amount);
					ManageAccounts a = new ManageAccounts(businessLogic);
					a.setVisible(true);
					WithdrawMoney.this.dispose();
					TransactionSuccessful t = new TransactionSuccessful(message);
					t.setVisible(true);
				}
				
				catch (NumberFormatException ex) {
					outputMessage.setText("Amount has to be a positive float");
				} 
				
				catch (AccountBalanceNegativeException e1) {
					outputMessage.setText("Not enough money in the account.");
				}
			}
		});
		contentPane.add(btnWithdraw, "cell 3 1");
		
		outputMessage = new JTextArea();
		contentPane.add(outputMessage, "cell 0 2 4 1,grow");
	}

}
