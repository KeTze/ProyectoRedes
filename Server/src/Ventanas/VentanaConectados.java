package Ventanas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.BoxLayout;

import util.SocketManager;
import webServer.Request;
import webServer.Server;


/**
 * Clase que crea la ventana tienda y sus atributos
 *
 */
public class VentanaConectados extends JFrame implements FocusListener {
	private JTable table = new JTable();
	private MiModelo dtm;
	ArrayList<String> usuarios;
	private JScrollPane scrollPane;
	private JPanel contentPane;
	private JPanel contentPane_1;
	private JPanel panelCentral;
	private JButton btnSalir;
	private JTable table_1 ;
	public int filaS=0;
	private JButton btnExpulsar;
	private Server s;

	/**
	 * Main de prueba
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					VentanaConectados frame = new VentanaConectados(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Crea la ventana tienda
	 * @param usuario que utiliza el programa
	 */
	public VentanaConectados(Server server) {
		s = server;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 386, 542);
		contentPane_1 = new JPanel();
		contentPane_1.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane_1);
		contentPane_1.setLayout(new BorderLayout(0, 0));

		dtm = new MiModelo();
		
		dtm.setColumnIdentifiers(new String [] {"NOMBRE"});
		usuarios = server.getUsuarios();
		
		
		if(usuarios!=null){
			for(int i = 0; i<usuarios.size(); i++){
				//SocketManager s = usuarios.get(i);
				String s = usuarios.get(i);
				dtm.addRow(new String []{s});
			}
		}
		


		btnSalir = new JButton("SALIR");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});

		contentPane_1.add(btnSalir,BorderLayout.SOUTH);

		panelCentral = new JPanel();
		contentPane_1.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new BorderLayout(0, 0));
		table_1 = new JTable(dtm);
		table_1.addFocusListener(this);
		table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_1.setModel(dtm);
		scrollPane = new JScrollPane(table_1);
		panelCentral.add(scrollPane, BorderLayout.CENTER);

		JPanel panelLabel = new JPanel();
		panelCentral.add(panelLabel, BorderLayout.NORTH);

		JLabel lblSeleccion = new JLabel("Usuarios conectados:");
		lblSeleccion.setToolTipText("");
		panelLabel.add(lblSeleccion);

		JPanel panelBotones = new JPanel();
		panelCentral.add(panelBotones, BorderLayout.SOUTH);
		panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));

		JPanel panel_2 = new JPanel();
		panelBotones.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		btnExpulsar = new JButton("EXPULSAR");
		btnExpulsar.setEnabled(false);
		panel_2.add(btnExpulsar);
		btnExpulsar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table_1.getSelectedRow();
				String u = usuarios.get(i);
				s.desconectarUsuario(u);
				actualizarTabla();			
			}
		});

	}

	


	/**
	 * Clase creada para hacer no editable una tabla
	 *
	 */
	public class MiModelo extends DefaultTableModel
	{

		/**
		 * Metodo utilizado para no editar la tabla
		 */
		public boolean isCellEditable (int row, int column)
		{
			// Aquí devolvemos true o false según queramos que una celda
			// identificada por fila,columna (row,column), sea o no editable
			if (column >=0)
				return false;
			return true;
		}
	}

	public void actualizarTabla(){
		dtm = new MiModelo();
		
		dtm.setColumnIdentifiers(new String [] {"NOMBRE"});
		usuarios = s.getUsuarios();
		
		
		if(usuarios!=null){
			for(int i = 0; i<usuarios.size(); i++){
				//SocketManager s = usuarios.get(i);
				String s = usuarios.get(i);
				dtm.addRow(new String []{s});
			}
		}
		table_1.setModel(dtm);
	}

	/**
	 * Escuchador del foco
	 */
	@Override
	public void focusGained(FocusEvent arg0) {
		filaS= table_1.getSelectedRow();
		btnExpulsar.setEnabled(true);
	}

	/**
	 * Escuchador del foco
	 */
	@Override
	public void focusLost(FocusEvent arg0) {

	}
}



