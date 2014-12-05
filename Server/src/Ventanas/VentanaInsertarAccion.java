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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class VentanaInsertarAccion extends JFrame{
	
	private JButton btnAnyadir;
	private String variable;
	private JComboBox comboBox;
	private static VentanaInsertarAccion window;
	private ArrayList<String> lAcciones= new ArrayList();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new VentanaInsertarAccion("Temperatura");
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
	public VentanaInsertarAccion(String variable) {
		super();
		this.variable = variable;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		setBounds(100, 100, 324, 171);
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
		
		JLabel lblLogin = new JLabel("Elige una acci\u00F3n a insertar");
		lblLogin.setForeground(Color.WHITE);
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		
		btnAnyadir = new JButton("A\u00D1ADIR");
		btnAnyadir.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		btnAnyadir.setForeground(Color.BLACK);
		btnAnyadir.setOpaque(false);
		btnAnyadir.setFont(new Font("Tahoma", Font.PLAIN, 15));
		try {
			BaseDatos.connect();
			lAcciones = BaseDatos.obtenerAcciones();
			BaseDatos.disconnect();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] s1=  new String [lAcciones.size()];
		for(int i = 0; i<lAcciones.size(); i++){
			String u = lAcciones.get(i);
			if(!u.equals("-")){
				s1[i] = u;
			}
			

		}
		/*
		 * for(int i = 0; i<lAcciones.size(); i++){
		String u = lAcciones.get(i);
		boolean repetido = false;
		for(int j = 0; j<i; i++){
			String s = (String) dtm.getValueAt(j, 0);
			if(s.equals(u)){
				repetido = true;
			}

		}
		if(!repetido){
			dtm.addRow(new String []{u});

	}
		 */
		
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(s1));
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblLogin, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnAnyadir, GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
							.addContainerGap())))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lblLogin, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnAnyadir, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(42, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
		
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent arg0) {
			}
			public void windowClosed(WindowEvent arg0) {
			}
			public void windowClosing(WindowEvent arg0) {
				dispose();
				EventQueue.invokeLater(new Runnable() {

					public void run() {
						try {
							VentanaAccionesVariable frame = new VentanaAccionesVariable(variable);
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			public void windowDeactivated(WindowEvent arg0) {
			}
			public void windowDeiconified(WindowEvent arg0) {
			}
			public void windowIconified(WindowEvent arg0) {
			}
			public void windowOpened(WindowEvent arg0) {
			}
		});
		
		eventos();
	}
	
	private void eventos() {
		btnAnyadir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				int i = comboBox.getSelectedIndex();
				try {
					BaseDatos.connect();
					boolean res = BaseDatos.anyadirAccionAVariable(variable, lAcciones.get(i));
					BaseDatos.disconnect();
					if(res){
						dispose();
						EventQueue.invokeLater(new Runnable() {

							public void run() {
								try {
									VentanaAccionesVariable frame = new VentanaAccionesVariable(variable);
									frame.setVisible(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}else{
						JOptionPane.showMessageDialog( null, "La variable ya contiene esa accion. Elige otra", "Error", JOptionPane.ERROR_MESSAGE );
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog( null, "La variable ya contiene esa accion. Elige otra", "Error", JOptionPane.ERROR_MESSAGE );
				}
				
				
			}
			
		});
		
	
	}
	
	
	
	private JFrame getFrame(){
		return this;
	}
}
