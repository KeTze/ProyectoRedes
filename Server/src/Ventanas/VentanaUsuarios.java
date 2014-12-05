package Ventanas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
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




import util.BaseDatos;

/**
 * Clase que crea la ventana tienda y sus atributos
 *
 */
public class VentanaUsuarios extends JFrame implements FocusListener {
	private JTable table = new JTable();
	private MiModelo dtm;
	private ArrayList<String> lUsuarios= new ArrayList();
	private JScrollPane scrollPane;
	private JPanel contentPane;
	private JPanel contentPane_1;
	private JPanel panelCentral;
	private JButton btnSalir;
	private JTable table_1 ;
	public int filaS=0;
	private JButton btnBorrar;
	private JButton btnAnyadir;
	private JButton btnModificar;

	/**
	 * Main de prueba
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					VentanaUsuarios frame = new VentanaUsuarios();
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
	public VentanaUsuarios() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 386, 542);
		contentPane_1 = new JPanel();
		this.setResizable(false);
		contentPane_1.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane_1);
		contentPane_1.setLayout(new BorderLayout(0, 0));

		
		
		try {
			BaseDatos.connect();
			lUsuarios = BaseDatos.obtenerListaUsuarios();
			BaseDatos.disconnect();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		dtm = new MiModelo();
		
		dtm.setColumnIdentifiers(new String [] {"NOMBRE"});

		for(int i = 0; i<lUsuarios.size(); i++){
			String u = lUsuarios.get(i);
			dtm.addRow(new String []{u});

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

		JLabel lblSeleccion = new JLabel("Seleccione un usuario y pulse la opci\u00F3n deseada");
		lblSeleccion.setToolTipText("");
		panelLabel.add(lblSeleccion);

		JPanel panelBotones = new JPanel();
		panelCentral.add(panelBotones, BorderLayout.SOUTH);
		panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		panelBotones.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		btnAnyadir = new JButton("A\u00F1adir Usuario");
		btnAnyadir.setEnabled(false);
		panel.add(btnAnyadir);
		btnAnyadir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						
						try {
							VentanaAnyadirUsuario window = new VentanaAnyadirUsuario();
							window.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		JPanel panel_1 = new JPanel();
		panelBotones.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		btnBorrar = new JButton("Borrar Usuario");
		btnBorrar.setEnabled(false);
		panel_1.add(btnBorrar);
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table_1.getSelectedRow();
				
				try {
					BaseDatos.connect();
					BaseDatos.borrarUsuario(lUsuarios.get(i));
					BaseDatos.disconnect();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				actualizar();
				
			}
		});

		JPanel panel_2 = new JPanel();
		panelBotones.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		btnModificar = new JButton("Modificar Usuario");
		btnModificar.setEnabled(false);
		panel_2.add(btnModificar);
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final int i = table_1.getSelectedRow();
				dispose();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						
						try {
							VentanaModificarUsuario window = new VentanaModificarUsuario(lUsuarios.get(i));
							window.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});			

			}
		});

		
		
		
	}

	public void actualizar(){
		try {
			BaseDatos.connect();
			lUsuarios = BaseDatos.obtenerListaUsuarios();
			BaseDatos.disconnect();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		dtm = new MiModelo();
		
		dtm.setColumnIdentifiers(new String [] {"NOMBRE"});

		for(int i = 0; i<lUsuarios.size(); i++){
			String u = lUsuarios.get(i);
			dtm.addRow(new String []{u});

		}
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

	/**
	 * Escuchador del foco
	 */
	@Override
	public void focusGained(FocusEvent arg0) {
		filaS= table_1.getSelectedRow();
		btnAnyadir.setEnabled(true);
		btnBorrar.setEnabled(true);
		btnModificar.setEnabled(true);
	}

	/**
	 * Escuchador del foco
	 */
	@Override
	public void focusLost(FocusEvent arg0) {

	}
}



