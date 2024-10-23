package view;

import java.awt.EventQueue;

import javax.swing.JDialog;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JLabel;
//import java.awt.SystemColor;
import java.awt.Font;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Sobre extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sobre dialog = new Sobre();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public Sobre() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Sobre.class.getResource("/icons/CF.png")));
		setTitle("Sobre");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 366, 437);
		getContentPane().setLayout(null);
		
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnOK.setForeground(new Color(128, 128, 255));
		btnOK.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnOK.setBounds(136, 337, 89, 23);
		getContentPane().add(btnOK);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(Sobre.class.getResource("/icons/MIT Lic.png")));
		lblNewLabel.setBounds(78, 164, 186, 149);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Projeto CatalogoFace, para registro");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setForeground(new Color(128, 128, 255));
		lblNewLabel_1.setBounds(30, 30, 256, 14);
		getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("@author Hellysamar");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(30, 80, 161, 14);
		getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Sob a licença MIT");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		lblNewLabel_3.setForeground(new Color(128, 128, 255));
		lblNewLabel_3.setBounds(30, 105, 176, 14);
		getContentPane().add(lblNewLabel_3);
		
		JLabel lblNewLabel_1_1 = new JLabel("de fotos de usuários.");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1.setForeground(new Color(128, 128, 255));
		lblNewLabel_1_1.setBounds(30, 55, 225, 14);
		getContentPane().add(lblNewLabel_1_1);
		
		this.setLocationRelativeTo(null);

	}

}
