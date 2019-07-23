package presentation;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField usernameField;
	private JTextField passwordField;
	private JButton btnLogin;
	private JButton btnCancel;
	private JTextArea outputMessage;

	public Login(BLFacade businessLogic) {
		
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][][grow][][][][grow][]", "[][][][][][][][grow]"));
		
		JLabel lblUsername = new JLabel("Username:");
		contentPane.add(lblUsername, "cell 0 0,alignx left,aligny center");
		
		usernameField = new JTextField();
		contentPane.add(usernameField, "cell 2 0 5 1,growx,aligny top");
		usernameField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		contentPane.add(lblPassword, "cell 0 2,alignx left,aligny center");
		
		passwordField = new JTextField();
		contentPane.add(passwordField, "cell 2 2 5 1,growx,aligny top");
		passwordField.setColumns(10);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login.this.dispose();
			}
		});
		contentPane.add(btnCancel, "cell 2 4,alignx left,aligny center");
		
		outputMessage = new JTextArea();
		contentPane.add(outputMessage, "cell 0 7 8 1,grow");
		
		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = passwordField.getText();
				if (businessLogic.authenticate(username, password)) {
					ManageAccounts a = new ManageAccounts(businessLogic);
					a.setVisible(true);
					Login.this.dispose();
				}
				else 
					outputMessage.setText("Wrong usermane or password");
				
			}
		});
		contentPane.add(btnLogin, "flowx,cell 6 4,alignx right,aligny center");
		
	}

}
