package Ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.ButtonGroup;
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
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JRadioButton;

public class VentanaAccion extends JFrame{
	private static VentanaAccion window;
	private String variable;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new VentanaAccion("Temperatura");
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
	public VentanaAccion(String variable) {
		super();
		this.variable = variable;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 365, 270);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setBackground(Color.DARK_GRAY);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JLabel lblElegirAccin = new JLabel("Elegir Acci\u00F3n");
		lblElegirAccin.setForeground(Color.WHITE);
		getContentPane().add(lblElegirAccin, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		//2 ArrayList. Uno con las acciones y otro con los JRadioButtons.
				
		ArrayList<String> lAcciones = new ArrayList<>();
		ArrayList<JRadioButton> lButtons = new ArrayList<>();
		ButtonGroup bg = new ButtonGroup();
		try {
			lAcciones = TCPClient.obtenerAcciones(variable);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		for (int i = 0; i < lAcciones.size(); i++)
		{
			lButtons.add(new JRadioButton(lAcciones.get(i)));
			lButtons.get(i).setBackground(Color.DARK_GRAY);
			lButtons.get(i).setForeground(Color.WHITE);
			bg.add(lButtons.get(i));
			panel.add(lButtons.get(i));
		}
		
		/*
		JRadioButton rdbtnHhh = new JRadioButton("hhh");
		rdbtnHhh.setBackground(Color.DARK_GRAY);
		rdbtnHhh.setForeground(Color.WHITE);
		panel.add(rdbtnHhh);
		*/
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnSeleccionarAccin = new JButton("Seleccionar Acci\u00F3n");
		panel_1.add(btnSeleccionarAccin);
		
		JButton btnVolver = new JButton("Volver");
		panel_1.add(btnVolver);
		setResizable(false);
		setTitle("Control de Invernadero: Login");
		
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
	private void desactivarBotones(){
		
	}
	private void eventos() {
		
	
	}
	
	private JFrame getFrame(){
		return this;
	}
}
