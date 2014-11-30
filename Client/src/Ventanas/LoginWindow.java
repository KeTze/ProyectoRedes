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

import tcpClient.TCPClient;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class LoginWindow extends JFrame{
	
	private JButton btnLogin;
	private JPasswordField passwordField;
	private JTextField txtUsername;
	private JTextField txtServidor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow window = new LoginWindow();
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
	public LoginWindow() {
		super();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 324, 253);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setBackground(Color.DARK_GRAY);
		setResizable(false);
		setTitle("Control de Invernadero");
		
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
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setForeground(Color.WHITE);
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		
		btnLogin = new JButton("Iniciar Sesión");
		btnLogin.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		btnLogin.setForeground(Color.BLACK);
		btnLogin.setOpaque(false);
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
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
		
		txtServidor = new JTextField();
		txtServidor.setToolTipText("username");
		txtServidor.setHorizontalAlignment(SwingConstants.CENTER);
		txtServidor.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtServidor.setColumns(10);
		
		JLabel lblServidor = new JLabel("IPServer:");
		lblServidor.setForeground(Color.WHITE);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblLogin, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblServidor, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtServidor, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPass)
							.addGap(31)
							.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnLogin, GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(lblUser, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtUsername, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
							.addContainerGap())))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lblLogin, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(txtServidor, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblServidor, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUser, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPass))
					.addGap(19)
					.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
		
		setLocationRelativeTo(null);
		
		eventos();
	}
	private void desactivarBotones(){
		
	}
	private void eventos() {
		btnLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				btnLogin.setText("CONECTANDO...");
				btnLogin.setEnabled(false);
				
				txtServidor.setBackground(Color.WHITE);
				txtUsername.setBackground(Color.WHITE);
				passwordField.setBackground(Color.WHITE);
				if(txtServidor.getText().equals("")){
					txtServidor.setBackground(Color.YELLOW);
				}else{
					
					Thread t = new Thread(new Runnable(){
						public void run() {
							try {
								
								TCPClient.conectar(txtServidor.getText(), 3000);
								String resultado = TCPClient.iniciarSesion(txtUsername.getText(), passwordField.getText());
								System.out.println(resultado);
								if(resultado.substring(0, 3).equals("201")){
									//Abrir ventana principal
									JOptionPane.showMessageDialog( null, "Correcto!", "Error", JOptionPane.ERROR_MESSAGE );
								}else if(resultado.substring(0, 3).equals("400")){
									txtUsername.setBackground(Color.YELLOW);
									JOptionPane.showMessageDialog( null, "Falta el nombre de usuario", "Error", JOptionPane.ERROR_MESSAGE );
								}else if(resultado.substring(0, 3).equals("401")){
									txtUsername.setBackground(Color.RED);
									JOptionPane.showMessageDialog( null, "Usuario no registrado. Intentelo de nuevo", "Error", JOptionPane.ERROR_MESSAGE );
								}else if(resultado.substring(0, 3).equals("402")){
									passwordField.setBackground(Color.YELLOW);
									JOptionPane.showMessageDialog( null, "Falta la clave de usuario", "Error", JOptionPane.ERROR_MESSAGE );
								}else if(resultado.substring(0, 3).equals("403")){
									passwordField.setBackground(Color.RED);
									JOptionPane.showMessageDialog( null, "La clave de usuario que ha introducido es incorrecta", "Error", JOptionPane.ERROR_MESSAGE );
								}
							} catch (IOException e) {
								txtServidor.setBackground(Color.RED);
								JOptionPane.showMessageDialog( null, "La IP del Servidor que ha introducido es incorrecta o el servidor no se encuentra disponible", "Error", JOptionPane.ERROR_MESSAGE );
							}
						}
					});
					t.start();
					
				}
				
				btnLogin.setEnabled(true);
				btnLogin.setText("Iniciar Sesión");
			}
		});
		
	
	}
	
	private JFrame getFrame(){
		return this;
	}
}
