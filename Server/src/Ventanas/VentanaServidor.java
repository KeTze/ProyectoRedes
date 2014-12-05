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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;



public class VentanaServidor extends JFrame {

	private JPanel contentPane;
	private JSpinner spinner;
	private Server s;
	private JButton btnEncender;
	private JLabel lblUsuariosActuales;
	private JButton btnVerLista;
	private JLabel lblNumMaxDe;
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
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 301, 312);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblServidorDeInvernadero = new JLabel("SERVIDOR DE INVERNADERO");
		lblServidorDeInvernadero.setBounds(30, 11, 230, 33);
		lblServidorDeInvernadero.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		btnEncender = new JButton("ENCENDER");
		
		btnEncender.setBounds(82, 50, 117, 23);
		
		lblNumMaxDe = new JLabel("Num. Max. de Usuarios");
		lblNumMaxDe.setEnabled(false);
		lblNumMaxDe.setBounds(52, 101, 132, 14);
		
		spinner = new JSpinner();
		spinner.setEnabled(false);
		spinner.setBounds(203, 98, 29, 20);
		spinner.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				s.setMaxUsuarios(((int) spinner.getValue()));
			}
		});
		
		lblUsuariosActuales = new JLabel("Usuarios actuales: 0");
		lblUsuariosActuales.setEnabled(false);
		lblUsuariosActuales.setBounds(76, 131, 140, 14);
		
		
		btnEncender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				s = new Server();
				Thread t = new Thread(new Runnable(){
					public void run() {
						s.ejecutar();
					}
				});
				t.start();
				btnEncender.setText("ENCENDIDO");
				btnEncender.setEnabled(false);
				lblUsuariosActuales.setEnabled(true);
				btnVerLista.setEnabled(true);
				lblNumMaxDe.setEnabled(true);
				spinner.setEnabled(true);
				
				Thread t1 = new Thread(new Runnable(){
					public void run() {
						while(true){
							lblUsuariosActuales.setText("Usuarios Actuales: "+s.getUsuarios().size());
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
							}
						}
						
					}
				});
				t1.start();
			}
		});
		
		btnVerLista = new JButton("VER LISTA");
		btnVerLista.setEnabled(false);
		btnVerLista.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {

					public void run() {
						try {
							VentanaConectados frame = new VentanaConectados(s);
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		btnVerLista.setBounds(86, 159, 98, 23);
		
		JButton btnAdministrar = new JButton("ADMINISTRAR");
		btnAdministrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							VentanaAdministrador frame = new VentanaAdministrador();
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		btnAdministrar.setBounds(79, 201, 120, 23);
		
		JButton btnSalir = new JButton("SALIR");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
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
