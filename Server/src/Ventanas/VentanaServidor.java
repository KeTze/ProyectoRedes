package Ventanas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import webServer.Server;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;



public class VentanaServidor extends JFrame {

	private JPanel contentPane;
	private JSpinner spinner;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaServidor frame = new VentanaServidor();
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
	public VentanaServidor() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 301, 312);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblServidorDeInvernadero = new JLabel("SERVIDOR DE INVERNADERO");
		lblServidorDeInvernadero.setBounds(30, 11, 230, 33);
		lblServidorDeInvernadero.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JButton btnEncender = new JButton("ENCENDER");
		btnEncender.setBounds(82, 50, 117, 23);
		
		JLabel lblNumMaxDe = new JLabel("Num. Max. de Usuarios");
		lblNumMaxDe.setBounds(52, 101, 132, 14);
		
		spinner = new JSpinner();
		spinner.setBounds(203, 98, 29, 20);
		spinner.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				Server.setMaxUsuarios((int) spinner.getValue());
			}
		});
		
		JLabel lblUsuariosActuales = new JLabel("Usuarios Actuales: 0");
		lblUsuariosActuales.setBounds(76, 131, 140, 14);
		
		JButton btnVerLista = new JButton("VER LISTA");
		btnVerLista.setBounds(86, 159, 98, 23);
		
		JButton btnAdministrar = new JButton("ADMINISTRAR");
		btnAdministrar.setBounds(79, 201, 120, 23);
		
		JButton btnSalir = new JButton("SALIR");
		btnSalir.setBounds(99, 239, 78, 23);
		contentPane.setLayout(null);
		contentPane.add(lblServidorDeInvernadero);
		contentPane.add(btnEncender);
		contentPane.add(lblNumMaxDe);
		contentPane.add(spinner);
		contentPane.add(lblUsuariosActuales);
		contentPane.add(btnVerLista);
		contentPane.add(btnAdministrar);
		contentPane.add(btnSalir);
	}
}
