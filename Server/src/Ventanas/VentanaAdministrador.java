package Ventanas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.GridLayout;

public class VentanaAdministrador extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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

	/**
	 * Create the frame.
	 */
	public VentanaAdministrador() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 463);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelSuperior = new JPanel();
		contentPane.add(panelSuperior, BorderLayout.NORTH);
		
		JLabel lblSeleccioneUnaOpcion = new JLabel("Seleccione una opcion:");
		panelSuperior.add(lblSeleccioneUnaOpcion);
		
		JPanel panelCentral = new JPanel();
		contentPane.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new BorderLayout(0, 0));
		
		JButton btnSalir = new JButton("SALIR");
		panelCentral.add(btnSalir, BorderLayout.SOUTH);
		
		JPanel panelBotonesCentral = new JPanel();
		panelCentral.add(panelBotonesCentral, BorderLayout.CENTER);
		panelBotonesCentral.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnNewButton = new JButton("New button");
		panelBotonesCentral.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("New button");
		panelBotonesCentral.add(btnNewButton_1);
	}

}
