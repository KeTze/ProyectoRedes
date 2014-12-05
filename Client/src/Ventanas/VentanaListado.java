package Ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.BoxLayout;

import tcpClient.TCPClient;
import tcpClient.Variable;

import java.io.IOException;

/**
 * Clase que crea la ventana tienda y sus atributos
 *
 */
public class VentanaListado extends JFrame implements FocusListener {

	private JTextField txtBusqueda;
	private JTable table = new JTable();
	private MiModelo dtm;
	private JButton btnBuscar;
	private JComboBox comboBox;
	private ArrayList<Variable> lVariable= new ArrayList();
	private JScrollPane scrollPane;
	private JPanel contentPane;
	private JPanel contentPane_1;
	private JPanel panelCentral;
	private JPanel panelBusqueda;
	private JButton btnSalir;
	private JTable table_1 ;
	public int filaS=0;
	private JButton btnActivarDesactivar;
	private JButton btnObtenerFoto;
	private JButton btnEjecutarAccion;

	/**
	 * Main de prueba
	 */
	public static void main(String[] args) {
		
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

	/**
	 * Crea la ventana tienda
	 * @param usuario que utiliza el programa
	 */
	public VentanaListado(ArrayList<Variable> lVariable1) {
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 862, 542);
		contentPane_1 = new JPanel();
		contentPane_1.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane_1);
		contentPane_1.setLayout(new BorderLayout(0, 0));

		
		//Obtener variables
		if(lVariable1==null){
			try {
				TCPClient.conectar("127.0.0.1", 3000);	//BORRAAAAAAR! ESTO ES SOLO PARA PROBAR
				TCPClient.iniciarSesion("user", "pass");  //BORRAAAAAAR! ESTO ES SOLO PARA PROBAR
				this.lVariable = TCPClient.obtenerListado();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog( null, "Has sido desconectado del servidor. Hasta otra!", "Error", JOptionPane.ERROR_MESSAGE );
				System.exit(0);
			}
		}else{
			this.lVariable = lVariable1;
		}
		
		
		
		panelBusqueda = new JPanel();
		dtm = new MiModelo();
		
		dtm.setColumnIdentifiers(new String [] {"PLACA","VARIABLE","FUNCION PRINC", "ESTADO","ULTIMA ACCION"});

		for(int i = 0; i<lVariable.size(); i++){
			Variable v = lVariable.get(i);
			dtm.addRow(new String []{v.getPlaca(),v.getNombre(), v.getFuncPrincipal(), v.getEstado(),v.getUltimaAccion()});

		}

		contentPane_1.add(panelBusqueda, BorderLayout.NORTH);
		panelBusqueda.setLayout(new BorderLayout(0, 0));

		txtBusqueda = new JTextField();
		panelBusqueda.add(txtBusqueda, BorderLayout.WEST);
		txtBusqueda.setColumns(50);

		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Tipo de Busqueda", "Por placa", "Por variable", "Por funcion principal", "Por estado", "Por ultima accion"}));
		comboBox.setToolTipText("");
		panelBusqueda.add(comboBox);

		btnBuscar = new JButton("BUSCAR");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscar();
			}
		});

		btnBuscar.setEnabled(true);
		panelBusqueda.add(btnBuscar, BorderLayout.EAST);


		btnSalir = new JButton("SALIR");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					TCPClient.salir();
					dispose();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					TCPClient.desconectar();
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
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

		JLabel lblSeleccion = new JLabel("Seleccione un componente y pulse la opci\u00F3n deseada");
		lblSeleccion.setToolTipText("");
		panelLabel.add(lblSeleccion);

		JPanel panelBotones = new JPanel();
		panelCentral.add(panelBotones, BorderLayout.SOUTH);
		panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		panelBotones.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		btnObtenerFoto = new JButton("Obtener Foto");
		btnObtenerFoto.setEnabled(false);
		panel.add(btnObtenerFoto);
		btnObtenerFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//OBTENER FOTO**********************************************************************************
			}
		});

		JPanel panel_1 = new JPanel();
		panelBotones.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		btnActivarDesactivar = new JButton("Activar/Desactivar");
		btnActivarDesactivar.setEnabled(false);
		panel_1.add(btnActivarDesactivar);
		btnActivarDesactivar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table_1.getSelectedRow();
				Variable v = lVariable.get(i);
				try {
					if(v.getEstado().equals("ON")){
						TCPClient.apagarVariable(v.getPlaca(), v.getNombre());
						
					}else{
						TCPClient.encenderVariable(v.getPlaca(), v.getNombre());
						
					}
					actualizarTabla();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog( null, "Has sido desconectado del servidor. Hasta otra!", "Error", JOptionPane.ERROR_MESSAGE );
					System.exit(0);
				}
			}
		});

		JPanel panel_2 = new JPanel();
		panelBotones.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		btnEjecutarAccion = new JButton("Ejecutar Accion");
		btnEjecutarAccion.setEnabled(false);
		panel_2.add(btnEjecutarAccion);
		btnEjecutarAccion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					int i = table_1.getSelectedRow();
					Variable v = lVariable.get(i);
					public void run() {
						try {
							VentanaAccion frame = new VentanaAccion(v.getNombre(), v.getPlaca());
							frame.setVisible(true);
							dispose();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			}
		});

		/**
		 * Listeners
		 */
		addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent arg0) {
			}
			public void windowClosed(WindowEvent arg0) {
			}
			public void windowClosing(WindowEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							TCPClient.salir();
							TCPClient.desconectar();
							dispose();
							
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
		dtm.setColumnIdentifiers(new String [] {"PLACA","VARIABLE","FUNCION PRINC", "ESTADO","ULTIMA ACCION"});
		txtBusqueda.setBackground(Color.WHITE);
		table.setModel(dtm); 
		try {
			lVariable = TCPClient.obtenerListado();
		} catch (IOException e) {
			JOptionPane.showMessageDialog( null, "Has sido desconectado del servidor. Hasta otra!", "Error", JOptionPane.ERROR_MESSAGE );
			System.exit(0);
		}
		
		for(int i = 0; i<lVariable.size(); i++){
			Variable v = lVariable.get(i);
			dtm.addRow(new String []{v.getPlaca(),v.getNombre(), v.getFuncPrincipal(), v.getEstado(),v.getUltimaAccion()});
			
		}
		table_1.setModel(dtm);
	}
	
	
	/**
	 * Metodo para buscar un libro dentro de la tabla.
	 * Puede ser por medio de varios atributos
	 */
	public void buscar(){
		btnBuscar.setEnabled(false);
		dtm = new MiModelo();
		dtm.setColumnIdentifiers(new String [] {"PLACA","VARIABLE","FUNCION PRINC", "ESTADO","ULTIMA ACCION"});
		txtBusqueda.setBackground(Color.WHITE);
		table.setModel(dtm); 
		if(comboBox.getSelectedItem().equals("Tipo de Busqueda")){
			try {
				lVariable = TCPClient.obtenerListado();
			} catch (IOException e) {
				JOptionPane.showMessageDialog( null, "Error al obtener el listado de objetos", "Error", JOptionPane.ERROR_MESSAGE );
			}
			
			for(int i = 0; i<lVariable.size(); i++){
				Variable v = lVariable.get(i);
				dtm.addRow(new String []{v.getPlaca(),v.getNombre(), v.getFuncPrincipal(), v.getEstado(),v.getUltimaAccion()});
				
			}
			table_1.setModel(dtm);
		}else{
			if(txtBusqueda.getText().equals("")){
				txtBusqueda.setBackground(Color.YELLOW);
			}else{
				if(comboBox.getSelectedItem().equals("Por placa")){

					try {
						lVariable = TCPClient.obtenerBusqueda("placa", txtBusqueda.getText());
					} catch (IOException e) {
						JOptionPane.showMessageDialog( null, "Error al realizar la busqueda", "Error", JOptionPane.ERROR_MESSAGE );
					}
						
				}

				if(comboBox.getSelectedItem().equals("Por variable")){
					
					try {
						lVariable = TCPClient.obtenerBusqueda("variable", txtBusqueda.getText());
					} catch (IOException e) {
						JOptionPane.showMessageDialog( null, "Error al realizar la busqueda", "Error", JOptionPane.ERROR_MESSAGE );
					}
					
				}

				if(comboBox.getSelectedItem().equals("Por funcion principal")){
					
					try {
						lVariable = TCPClient.obtenerBusqueda("funcion_principal", txtBusqueda.getText());
					} catch (IOException e) {
						JOptionPane.showMessageDialog( null, "Error al realizar la busqueda", "Error", JOptionPane.ERROR_MESSAGE );
					}
					
				}

				

				if(comboBox.getSelectedItem().equals("Por estado")){

					try {
						lVariable = TCPClient.obtenerBusqueda("estado", txtBusqueda.getText());
					} catch (IOException e) {
						JOptionPane.showMessageDialog( null, "Error al realizar la busqueda", "Error", JOptionPane.ERROR_MESSAGE );
					}
					
				}

				if(comboBox.getSelectedItem().equals("Por ultima accion")){
					
					try {
						lVariable = TCPClient.obtenerBusqueda("ultima_accion", txtBusqueda.getText());
					} catch (IOException e) {
						JOptionPane.showMessageDialog( null, "Error al realizar la busqueda", "Error", JOptionPane.ERROR_MESSAGE );
					}
				}
				if(lVariable.size()==0){
					JOptionPane.showMessageDialog( null, "No se encontraron resultados a su busqueda", "Error", JOptionPane.ERROR_MESSAGE );
				}else{
					JOptionPane.showMessageDialog( null, "Se encontraron "+lVariable.size()+" coincidencias para su busqueda", "Busqueda", JOptionPane.INFORMATION_MESSAGE );
				}
				for(int i = 0; i<lVariable.size(); i++){
					Variable v = lVariable.get(i);
					dtm.addRow(new String []{v.getPlaca(),v.getNombre(), v.getFuncPrincipal(), v.getEstado(),v.getUltimaAccion()});
					
				}
				table_1.setModel(dtm);
			}
		}
		btnBuscar.setEnabled(true);
	}

	/**
	 * Escuchador del foco
	 */
	@Override
	public void focusGained(FocusEvent arg0) {
		filaS= table_1.getSelectedRow();
		btnObtenerFoto.setEnabled(true);;
		btnActivarDesactivar.setEnabled(true);;
		btnEjecutarAccion.setEnabled(true);;
	}

	/**
	 * Escuchador del foco
	 */
	@Override
	public void focusLost(FocusEvent arg0) {

	}
}



