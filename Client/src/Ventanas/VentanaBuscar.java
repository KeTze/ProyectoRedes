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
import tcpClient.Variable;

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
	private JComboBox comboBox;
	private ArrayList<Variable>lVariable = new ArrayList<>();

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
	
		
		comboBox = new JComboBox();
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
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(txtaAbc.equals("")||txtaAbc.equals("(a*, abc?)")){
					txtaAbc.setBackground(Color.YELLOW);
				}else{
					if(comboBox.getSelectedItem().equals("Por placa")){

						try {
							lVariable = TCPClient.obtenerBusqueda("placa", txtaAbc.getText());
						} catch (IOException e1) {
							JOptionPane.showMessageDialog( null, "Has sido desconectado del servidor. Hasta otra!", "Error", JOptionPane.ERROR_MESSAGE );
							System.exit(0);
						}
							
					}

					if(comboBox.getSelectedItem().equals("Por variable")){
						
						try {
							lVariable = TCPClient.obtenerBusqueda("variable", txtaAbc.getText());
						} catch (IOException e2) {
							JOptionPane.showMessageDialog( null, "Has sido desconectado del servidor. Hasta otra!", "Error", JOptionPane.ERROR_MESSAGE );
							System.exit(0);
						}
						
					}

					if(comboBox.getSelectedItem().equals("Por funcion principal")){
						
						try {
							lVariable = TCPClient.obtenerBusqueda("funcion_principal", txtaAbc.getText());
						} catch (IOException e3) {
							JOptionPane.showMessageDialog( null, "Has sido desconectado del servidor. Hasta otra!", "Error", JOptionPane.ERROR_MESSAGE );
							System.exit(0);
						}
						
					}

					

					if(comboBox.getSelectedItem().equals("Por estado")){

						try {
							lVariable = TCPClient.obtenerBusqueda("estado", txtaAbc.getText());
						} catch (IOException e4) {
							JOptionPane.showMessageDialog( null, "Has sido desconectado del servidor. Hasta otra!", "Error", JOptionPane.ERROR_MESSAGE );
							System.exit(0);
						}
						
					}

					if(comboBox.getSelectedItem().equals("Por ultima accion")){
						
						try {
							lVariable = TCPClient.obtenerBusqueda("ultima_accion", txtaAbc.getText());
						} catch (IOException e5) {
							JOptionPane.showMessageDialog( null, "Has sido desconectado del servidor. Hasta otra!", "Error", JOptionPane.ERROR_MESSAGE );
							System.exit(0);
						}
					}
					if(lVariable.size()==0){
						JOptionPane.showMessageDialog( null, "No se encontraron resultados a su busqueda", "Error", JOptionPane.ERROR_MESSAGE );
					}else{
						JOptionPane.showMessageDialog( null, "Se encontraron "+lVariable.size()+" coincidencias para su busqueda", "Busqueda", JOptionPane.INFORMATION_MESSAGE );
						dispose();
						EventQueue.invokeLater(new Runnable() {

							public void run() {
								try {
									VentanaListado frame = new VentanaListado(lVariable);
									frame.setVisible(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			}
		});
		btnBuscar.setBounds(34, 167, 81, 23);
		
		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtaAbc.setText("");
			}
		});
		btnBorrar.setBounds(130, 167, 81, 23);
		
		JButton btnMostrarTodo = new JButton("Mostrar todo");
		btnMostrarTodo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				EventQueue.invokeLater(new Runnable() {

					public void run() {
						try {
							VentanaListado frame = new VentanaListado(null);
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
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
		
	}
	
	private JFrame getFrame(){
		return this;
	}
}
