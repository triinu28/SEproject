package presentation;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DepositMoney extends JFrame {
	private JPanel contentPane;
	private JTextField depositField;
	private JLabel lblDepositAmount;
	private JButton btnCancel;
	private JButton btnDeposit;
	private JTextArea outputMessage;
	
	public DepositMoney(BLFacade businessLogic) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][][grow][]", "[][][grow]"));
		
		lblDepositAmount = new JLabel("Deposit amount:");
		contentPane.add(lblDepositAmount, "cell 0 0");
		
		depositField = new JTextField();
		contentPane.add(depositField, "cell 2 0 2 1,growx");
		depositField.setColumns(10);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManageAccounts a = new ManageAccounts(businessLogic);
				a.setVisible(true);
				DepositMoney.this.dispose();
			}
		});
		contentPane.add(btnCancel, "cell 2 1");
		
		btnDeposit = new JButton("Deposit");
		btnDeposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					float amount = Float.valueOf(depositField.getText());
					if (amount<0) throw new NumberFormatException();
					String message = businessLogic.depositMoney(amount);
					ManageAccounts a = new ManageAccounts(businessLogic);
					a.setVisible(true);
					DepositMoney.this.dispose();
					TransactionSuccessful t = new TransactionSuccessful(message);
					t.setVisible(true);
				}
				catch (NumberFormatException ex) {
					outputMessage.setText("Amount has to be a positive float");
				}
			}
		});
		contentPane.add(btnDeposit, "cell 3 1");
		
		outputMessage = new JTextArea();
		contentPane.add(outputMessage, "cell 0 2 4 1,grow");
	}
}
