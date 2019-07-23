package presentation;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TransactionSuccessful extends JFrame {

	private JPanel contentPane;
	private JTextArea messageArea;
	private JButton btnPrintReceipt;
	private JButton btnNoReceipt;
	

	public TransactionSuccessful(String message) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 100, 400, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][]", "[grow][]"));
		
		messageArea = new JTextArea();
		contentPane.add(messageArea, "cell 0 0 2 1,grow");
		messageArea.setText(message);
		
		btnPrintReceipt = new JButton("Print receipt");
		btnPrintReceipt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TransactionSuccessful.this.dispose();
			}
		});
		contentPane.add(btnPrintReceipt, "cell 0 1");
		
		btnNoReceipt = new JButton("No receipt");
		btnNoReceipt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TransactionSuccessful.this.dispose();
			}
		});
		contentPane.add(btnNoReceipt, "cell 1 1");
	}

}
