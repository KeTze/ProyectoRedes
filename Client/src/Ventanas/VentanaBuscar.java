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
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class VentanaBuscar extends JFrame{
	private JTextField txtaAbc;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaBuscar window = new VentanaBuscar();
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
	public VentanaBuscar() {
		super();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 400, 240);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setBackground(Color.DARK_GRAY);
		setResizable(false);
		setTitle("Control de Invernadero: Buscar Variables Control");
		
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
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Elegir opci\u00F3n", "Por placa", "Por variable", "Por funci\u00F3n principal", "Por estado", "Por \u00FAltima acci\u00F3n"}));
		comboBox.setBounds(100, 46, 245, 30);
		
		JLabel lblOpcion = new JLabel("Opci\u00F3n:");
		lblOpcion.setBounds(10, 54, 86, 14);
		lblOpcion.setForeground(Color.WHITE);
		
		JLabel lblPatron = new JLabel("Patr\u00F3n:");
		lblPatron.setBounds(10, 101, 36, 14);
		lblPatron.setForeground(Color.WHITE);
		
		txtaAbc = new JTextField();
		txtaAbc.setText("(a*, abc?)");
		txtaAbc.setToolTipText("");
		txtaAbc.setBounds(100, 94, 245, 28);
		txtaAbc.setColumns(10);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(34, 167, 81, 23);
		
		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.setBounds(130, 167, 81, 23);
		
		JButton btnMostrarTodo = new JButton("Mostrar todo");
		btnMostrarTodo.setBounds(243, 167, 116, 23);
		getContentPane().setLayout(null);
		getContentPane().add(lblPatron);
		getContentPane().add(lblOpcion);
		getContentPane().add(txtaAbc);
		getContentPane().add(comboBox);
		getContentPane().add(btnBuscar);
		getContentPane().add(btnBorrar);
		getContentPane().add(btnMostrarTodo);
		
		setLocationRelativeTo(null);
		
		eventos();
	}
	private void desactivarBotones(){
		
	}
	private void eventos() {
		
	
	}
	
	private JFrame getFrame(){
		return this;
	}
}