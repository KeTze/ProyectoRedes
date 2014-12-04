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
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.BorderLayout;

import javax.swing.JPanel;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JRadioButton;

public class VentanaAccion extends JFrame{
	private static VentanaAccion window;
	private String variable;
	private ArrayList<JRadioButton> lButtons;
	private String accion;
	private String parametro;
	private String placa;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new VentanaAccion("Temperatura", "Placa2");
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
	public VentanaAccion(String variable, String placa) {
		super();
		this.variable = variable;
		this.placa = placa;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 365, 270);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
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
		lButtons = new ArrayList<>();
		ButtonGroup bg = new ButtonGroup();
		try {
			lAcciones = TCPClient.obtenerAcciones(variable);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		for (int i = 0; i < lAcciones.size()-1; i++)
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
		btnSeleccionarAccin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				boolean enc=false;
				int i = 0;
				accion = null;
				while(!enc&&(i<lButtons.size())){
					if(lButtons.get(i).isSelected()){
						accion = lButtons.get(i).getText();
						enc=true;
					}
					i++;
				}
				try {
					boolean res = TCPClient.ejecutarAccion(placa, variable, accion);
					if(res){
						parametro = TCPClient.obtenerParametro(accion);
						if(parametro==null){
							int seleccion = JOptionPane.showOptionDialog(null, "¿Confirmar accion "+accion+"?", "Confirmacion", 
									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] { "CONFIRMAR", "RECHAZAR" }, "CONFIRMAR");

							if(seleccion==0){
								dispose();
								TCPClient.confirmarAccion(null);
								JOptionPane.showMessageDialog( null, accion+" realizado correctamente", "Correcto", JOptionPane.INFORMATION_MESSAGE );
								
							}else{
								dispose();
								TCPClient.rechazarAccion();
								
							}
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
							
						}else{
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										VentanaConfirmar window1 = new VentanaConfirmar(parametro, accion);
										window1.setVisible(true);
										dispose();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
						}
					}else{
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
					}
					
					
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
			}
		});
		panel_1.add(btnVolver);
		setResizable(false);
		setTitle("Seleccione Accion");
		
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
