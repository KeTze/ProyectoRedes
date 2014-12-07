package Ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPasswordField;
import javax.swing.UnsupportedLookAndFeelException;

import util.BaseDatos;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public class VentanaAnyadirUsuario extends JFrame{
	
	private JButton btnAnyadir;
	private JPasswordField passwordField;
	private JTextField txtUsername;
	private static VentanaAnyadirUsuario window;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new VentanaAnyadirUsuario();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VentanaAnyadirUsuario() {
		super();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 324, 214);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		getContentPane().setBackground(Color.DARK_GRAY);
		setResizable(false);
		setTitle("Invernadero");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JLabel lblLogin = new JLabel("A\u00F1adir usuario");
		lblLogin.setForeground(Color.WHITE);
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		
		btnAnyadir = new JButton("A\u00D1ADIR");
		btnAnyadir.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		btnAnyadir.setForeground(Color.BLACK);
		btnAnyadir.setOpaque(false);
		btnAnyadir.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		passwordField.setEchoChar('*');
		passwordField.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField.setToolTipText("password");
		
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtUsername.setHorizontalAlignment(SwingConstants.CENTER);
		txtUsername.setToolTipText("username");
		txtUsername.setColumns(10);
		
		JLabel lblUser = new JLabel("User:");
		lblUser.setForeground(Color.WHITE);
		
		JLabel lblPass = new JLabel("Pass:");
		lblPass.setForeground(Color.WHITE);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblLogin, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPass)
							.addGap(30)
							.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(lblUser, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtUsername, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnAnyadir, GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
							.addContainerGap())))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lblLogin, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUser, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
					.addGap(12)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPass))
					.addGap(18)
					.addComponent(btnAnyadir, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addGap(54))
		);
		getContentPane().setLayout(groupLayout);
		
		setLocationRelativeTo(null);
		
		eventos();
	}

	private void eventos() {
		btnAnyadir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				btnAnyadir.setText("INSERTANDO...");
				btnAnyadir.setEnabled(false);
				
				txtUsername.setBackground(Color.WHITE);
				passwordField.setBackground(Color.WHITE);
				boolean fallo = false;
				if(txtUsername.getText().equals("")){
					txtUsername.setBackground(Color.YELLOW);
					fallo = true;
				}
				if(passwordField.getText().equals("")){
					passwordField.setBackground(Color.YELLOW);
					fallo = true;
				}
				if(!fallo){
					boolean res = false;
					try {
						BaseDatos.connect();
						res = BaseDatos.anyadirUsuario(txtUsername.getText(), passwordField.getText());
						BaseDatos.disconnect();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					if(res){
						dispose();
					}else{
						dispose();
						JOptionPane.showMessageDialog( null, "Ya existe un usuario con ese nombre", "Error", JOptionPane.ERROR_MESSAGE );
					}
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								VentanaUsuarios window1 = new VentanaUsuarios();
								window1.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
				
				btnAnyadir.setText("A�ADIR");
				btnAnyadir.setEnabled(true);
				
				
			}
			
		});
		
	
	}
	
	private JFrame getFrame(){
		return this;
	}
}
