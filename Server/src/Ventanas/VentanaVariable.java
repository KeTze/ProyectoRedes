package Ventanas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;

import util.BaseDatos;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;

public class VentanaVariable extends JFrame {

	private JPanel contentPane;
	private JComboBox comboBox;
	private JComboBox comboBox_1;
	private ArrayList<String>lVariables;
	private ArrayList<String>lAcciones;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaVariable frame = new VentanaVariable();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaVariable() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 439, 317);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		lVariables = null;
		
		comboBox_1 = new JComboBox();
		comboBox_1.setBounds(24, 161, 372, 26);
		contentPane.add(comboBox_1);
		
		try {
			BaseDatos.connect();
			lVariables = BaseDatos.obtenerListaVariables();
			BaseDatos.disconnect();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String[]s = new String [lVariables.size()];
		for(int i=0; i<lVariables.size(); i++){
			s[i] = lVariables.get(i);
		}
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(s));
		comboBox.setBounds(24, 36, 372, 26);
		contentPane.add(comboBox);
		comboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					BaseDatos.connect();
					lAcciones = BaseDatos.obtenerListaAcciones(lVariables.get(comboBox.getSelectedIndex()));
					BaseDatos.disconnect();
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				String[]s = new String [lAcciones.size()];
				for(int i=0; i<lAcciones.size(); i++){
					s[i] = lAcciones.get(i);
				}
				
				
				comboBox_1.setModel(new DefaultComboBoxModel(s));
				
				
				
			}
		});
		
		JLabel lblEligeUnaVariable = new JLabel("Elige una variable");
		lblEligeUnaVariable.setBounds(24, 11, 154, 14);
		contentPane.add(lblEligeUnaVariable);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(24, 73, 372, 26);
		contentPane.add(separator);
		
		JButton btnEditarAcciones = new JButton("EDITAR ACCIONES");
		btnEditarAcciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							VentanaAccionesVariable frame = new VentanaAccionesVariable(lVariables.get(comboBox.getSelectedIndex()));
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		btnEditarAcciones.setBounds(24, 90, 372, 23);
		contentPane.add(btnEditarAcciones);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(24, 124, 372, 26);
		contentPane.add(separator_1);
		
		JLabel lblCambiarUltimaAccion = new JLabel("Cambiar ultima accion a:");
		lblCambiarUltimaAccion.setBounds(24, 138, 154, 14);
		contentPane.add(lblCambiarUltimaAccion);
		
		
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(24, 198, 372, 14);
		contentPane.add(separator_2);
		
		JButton btnReiniciarVariable = new JButton("REINICIAR VARIABLE");
		btnReiniciarVariable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					BaseDatos.connect();
					BaseDatos.reiniciarVariable(lVariables.get(comboBox.getSelectedIndex()));
					BaseDatos.disconnect();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btnReiniciarVariable.setBounds(24, 210, 372, 26);
		contentPane.add(btnReiniciarVariable);
		
		JButton btnCerrar = new JButton("CERRAR");
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCerrar.setBounds(165, 247, 89, 23);
		contentPane.add(btnCerrar);
	}
}
