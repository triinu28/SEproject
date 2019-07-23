package presentation;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;

public class ManageAccounts extends JFrame {

	private JPanel contentPane;
	private JButton btnOpenAccount;
	private JButton btnDepositMoney;
	private JButton btnWithdrawMoney;
	private JButton btnBuyForeignCurrency;
	private JButton btnSellForeignCurrency;
	private JButton btnLogOut;

	public ManageAccounts(BLFacade businessLogic) {
		businessLogic.printDB();
		setTitle("Manage Accounts");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[420px]", "[29px][29px][29px][29px][29px][29px]"));
		
		btnOpenAccount = new JButton("Open Account");
		btnOpenAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OpenAccount a = new OpenAccount(businessLogic);
				a.setVisible(true);
				ManageAccounts.this.dispose();
			}
		});
		contentPane.add(btnOpenAccount, "cell 0 0,growx,aligny top");
		
		btnDepositMoney = new JButton("Deposit Money");
		btnDepositMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DepositMoney newFrame = new DepositMoney(businessLogic);
				ValidateCustomer a = new ValidateCustomer(businessLogic, newFrame);
				a.setVisible(true);
				ManageAccounts.this.dispose();
			}
		});
		contentPane.add(btnDepositMoney, "cell 0 1,growx,aligny top");
		
		btnWithdrawMoney = new JButton("Withdraw Money");
		btnWithdrawMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WithdrawMoney newFrame = new WithdrawMoney(businessLogic);
				ValidateCustomer a = new ValidateCustomer(businessLogic, newFrame);
				a.setVisible(true);
				ManageAccounts.this.dispose();
			}
		});
		contentPane.add(btnWithdrawMoney, "cell 0 2,growx,aligny top");
		
		btnBuyForeignCurrency = new JButton("Buy Foreign Currency");
		btnBuyForeignCurrency.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BuyForeignCurrency newFrame = new BuyForeignCurrency(businessLogic);
				ValidateCustomer a = new ValidateCustomer(businessLogic, newFrame);
				a.setVisible(true);
				ManageAccounts.this.dispose();
			}
		});
		contentPane.add(btnBuyForeignCurrency, "cell 0 3,growx,aligny top");
		
		btnSellForeignCurrency = new JButton("Sell Foreign Currency");
		btnSellForeignCurrency.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SellForeignCurrency newFrame = new SellForeignCurrency(businessLogic);
				ValidateCustomer a = new ValidateCustomer(businessLogic, newFrame);
				a.setVisible(true);
				ManageAccounts.this.dispose();
			}
		});
		contentPane.add(btnSellForeignCurrency, "cell 0 4,growx,aligny top");
		
		btnLogOut = new JButton("Log out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login a = new Login(businessLogic);
				a.setVisible(true);
				ManageAccounts.this.dispose();
			}
		});
		contentPane.add(btnLogOut, "cell 0 5,alignx right,aligny top");
	}
}
