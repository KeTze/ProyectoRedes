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
import javax.swing.UnsupportedLookAndFeelException;

import tcpClient.TCPClient;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class VentanaConfirmar extends JFrame{
	private static VentanaConfirmar window;
	private JTextField textField;
	private JButton btnConfirmar;
	private JButton btnRechazar;
	private String parametro;
	private String accion;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new VentanaConfirmar("Parametro", "Accion");
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
	public VentanaConfirmar(String parametro, String accion) {
		super();
		this.parametro = parametro;
		this.accion = accion;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 365, 195);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		getContentPane().setBackground(Color.DARK_GRAY);
		
		btnConfirmar = new JButton("Confirmar");
		btnRechazar = new JButton("Rechazar");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblVariable = new JLabel(parametro);
		lblVariable.setForeground(Color.WHITE);
		
		JLabel lblParaccion = new JLabel("Parametro accion "+accion);
		lblParaccion.setForeground(Color.WHITE);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(50)
					.addComponent(btnConfirmar, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnRechazar, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(98, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(23)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblParaccion)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblVariable)
							.addPreferredGap(ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
							.addGap(36))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(27)
					.addComponent(lblParaccion)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblVariable)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
					.addGap(36)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnRechazar)
						.addComponent(btnConfirmar))
					.addGap(71))
		);
		getContentPane().setLayout(groupLayout);
		setResizable(false);
		setTitle("Control de Invernadero: Confirmar");
		
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
		
		setLocationRelativeTo(null);
		
		eventos();
	}

	private void eventos() {
		btnConfirmar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				btnConfirmar.setEnabled(false);
				btnRechazar.setEnabled(false);
				Thread t = new Thread(new Runnable(){
					public void run() {
						try {
							boolean correcto = TCPClient.confirmarAccion(textField.getText());
							if (correcto)
							{
								dispose();
								EventQueue.invokeLater(new Runnable() {
									public void run() {
										try {
											VentanaListado window1 = new VentanaListado(null);
											window1.setVisible(true);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								});
							}else{
								textField.setBackground(Color.YELLOW);
								JOptionPane.showMessageDialog( null, "Faltan el parametro", "Error", JOptionPane.ERROR_MESSAGE );
								btnConfirmar.setEnabled(true);
								btnRechazar.setEnabled(true);
							}
						} catch (Exception e) {
							JOptionPane.showMessageDialog( null, "Has sido desconectado del servidor. Hasta otra!", "Error", JOptionPane.ERROR_MESSAGE );
							System.exit(0);
						}
					}
				});
				t.start();
			}
		});
		
		btnRechazar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				btnConfirmar.setEnabled(false);
				btnRechazar.setEnabled(false);
				Thread t = new Thread(new Runnable(){
					public void run() {
						try {
							TCPClient.rechazarAccion();
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										VentanaListado window1 = new VentanaListado(null);
										window1.setVisible(true);
										dispose();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							dispose();
						} catch (Exception e) {
							JOptionPane.showMessageDialog( null, "Has sido desconectado del servidor. Hasta otra!", "Error", JOptionPane.ERROR_MESSAGE );
							System.exit(0);
						}
					}
				});
				t.start();
			}
		});
	}
	
	private JFrame getFrame(){
		return this;
	}
}
