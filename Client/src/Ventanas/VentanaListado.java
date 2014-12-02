package Ventanas;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;


import javax.swing.BoxLayout;

import tcpClient.Variable;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
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
	private JButton btnActivarDesactivar;
	private JButton btnObtenerFoto;
	private int filaS=0;
	private JButton btnEjecutarAccion;

	/**
	 * Main de prueba
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					VentanaListado frame = new VentanaListado();
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
	public VentanaListado() {
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 862, 542);
		contentPane_1 = new JPanel();
		contentPane_1.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane_1);
		contentPane_1.setLayout(new BorderLayout(0, 0));

		
		//Obtener variables**************************************
		
		
		
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
				buscar();}
		});

		btnBuscar.setEnabled(true);
		panelBusqueda.add(btnBuscar, BorderLayout.EAST);


		btnSalir = new JButton("SALIR");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							dispose();
							
							VentanaCarga vC = new VentanaCarga();
							vC.setVisible1(true);
							
							
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										VentanaPrincipal w1 = new VentanaPrincipal(usuarioActual);
										w1.setVisible(true);

									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});


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

				int seleccion = JOptionPane.showOptionDialog(null, "¿Desea comprar el libro "+lVariable.get(filaS).getTitulo()+"?", "Compra", 
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] { "Si", "No" }, "Si");

				if(seleccion==0){

					Runnable r = new Runnable (){
						public void run() {

							try {
								comprar( filaS);
							} catch (ClassNotFoundException | SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}							

						}
					};
					Thread t = new Thread(r);
					t.start();

				}
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
				VentanaRegalar v=new VentanaRegalar(usuarioActual,lVariable.get(filaS),filaS);
				v.setVisible(true);
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
				int seleccion = JOptionPane.showOptionDialog(null, "¿Desea Alquilar el libro "+lVariable.get(filaS).getTitulo()+"?", "Alquilar", 
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] { "Si", "No" }, "Si");
				if(seleccion==0){

					try {
						alquilar( filaS);
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

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
							dispose();
							VentanaPrincipal window1 = new VentanaPrincipal(usuarioActual);

							window1.setVisible(true);
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
	 * Metodo para regalar un libro seleccionado en la tabla a otra persona mediante email
	 * @param fila donde se encuentra el libro en la tabla
	 * @param email del usuario que va a recibir el libro
	 * @param nombre del usuario que va a recibir el libro
	 * @throws Exception
	 */
	public void regalar(int fila,String email,String nombre) throws Exception{	
		String ISBN=dtm.getValueAt(fila, 0).toString();
		Libro l=new Libro();
		l=BaseDatos.obtenerLibro(ISBN);
		if(usuarioActual.getSaldo()>=l.getPrecio_venta()){
			EnvioMail.regalarLibro(email,nombre,usuarioActual.getNombre()+" "+usuarioActual.getApellido(),l);
			BaseDatos.reducirSaldo(usuarioActual.getDni(), l.getPrecio_venta());
			usuarioActual.setSaldo(usuarioActual.getSaldo()-(l.getPrecio_venta()));
		}else{
			JOptionPane.showMessageDialog( null, "Saldo insuficiente"+usuarioActual.getSaldo(), "Error", JOptionPane.ERROR_MESSAGE );	
		}
	}
	
	/**
	 * Metodo para alquilar un libro seleccionado de la tabla
	 * @param fila donde se encuentra el libro en la tabla
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void alquilar(int fila) throws ClassNotFoundException, SQLException{
		String ISBN=dtm.getValueAt(fila, 0).toString();
		Libro l=new Libro();
		l=BaseDatos.obtenerLibro(ISBN);
		if(usuarioActual.getSaldo()>=l.getPrecio_alquiler()){
			BaseDatos.reducirSaldo(usuarioActual.getDni(), l.getPrecio_alquiler());
			usuarioActual.setSaldo(usuarioActual.getSaldo()-(l.getPrecio_alquiler()));
			BaseDatos.insertarAlquiler(usuarioActual.getDni(), l.getISBN());
		}
		else{
			JOptionPane.showMessageDialog( null, "Saldo insuficiente (Saldo actual: "+usuarioActual.getSaldo()+")", "Error", JOptionPane.ERROR_MESSAGE );	
		}
	}
	
	/**
	 * Metodo para comprar un libro seleccionado de la tabla
	 * @param fila donde se encuentra el libro en la tabla
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void comprar(int fila) throws ClassNotFoundException, SQLException{
		String ISBN=dtm.getValueAt(fila, 0).toString();
		Libro l=new Libro();
		l=BaseDatos.obtenerLibro(ISBN);
		if(usuarioActual.getSaldo()>=l.getPrecio_venta()){
			BaseDatos.reducirSaldo(usuarioActual.getDni(), l.getPrecio_venta());
			usuarioActual.setSaldo(usuarioActual.getSaldo()-(l.getPrecio_venta()));
			BaseDatos.insertarCompra(usuarioActual.getDni(), l.getISBN());
			usuarioActual.setSaldo(usuarioActual.getSaldo()-l.getPrecio_venta());
		}
		else{
			JOptionPane.showMessageDialog( null, "Saldo insuficiente (Saldo actual: "+usuarioActual.getSaldo()+")", "Error", JOptionPane.ERROR_MESSAGE );	
		}
	}

	/**
	 * Ventana para regalar un libro
	 */
	class VentanaRegalar extends JFrame implements FocusListener{

		private JPanel contentPane;
		private JTextField txtNombre;
		private JTextField txtEmail;
		private JButton btnAceptar;

		public VentanaRegalar(Usuario u,Libro l,final int fila) {
			setResizable(false);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 317, 181);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));
			setContentPane(contentPane);

			JLabel lblPregunta = new JLabel("¿A quien desea enviar el libro "+l.getTitulo()+"?");
			contentPane.add(lblPregunta, BorderLayout.NORTH);

			JPanel panel = new JPanel();
			contentPane.add(panel, BorderLayout.SOUTH);
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

			JPanel jpAceptar = new JPanel();
			panel.add(jpAceptar);
			jpAceptar.setLayout(new BorderLayout(0, 0));

			btnAceptar = new JButton("Aceptar");
			btnAceptar.setEnabled(false);
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						dispose();
						Runnable r = new Runnable (){
							public void run() {
								try {
									regalar(fila,txtEmail.getText(),txtNombre.getText());
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						};
						Thread t = new Thread(r);
						t.start();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			jpAceptar.add(btnAceptar);

			JPanel jpCancelar = new JPanel();
			panel.add(jpCancelar);

			JButton btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			jpCancelar.setLayout(new BorderLayout(0, 0));
			jpCancelar.add(btnCancelar);

			JPanel panel_1 = new JPanel();
			contentPane.add(panel_1, BorderLayout.CENTER);
			panel_1.setLayout(null);

			JLabel lblNewLabel = new JLabel("Nombre:");
			lblNewLabel.setBounds(26, 25, 68, 14);
			panel_1.add(lblNewLabel);

			JLabel lblNewLabel_1 = new JLabel("Email:");
			lblNewLabel_1.setBounds(26, 62, 68, 14);
			panel_1.add(lblNewLabel_1);

			txtNombre = new JTextField();
			txtNombre.setBounds(91, 22, 164, 20);
			panel_1.add(txtNombre);
			txtNombre.setColumns(10);

			txtEmail = new JTextField();
			txtEmail.addFocusListener(this);
			txtEmail.setBounds(91, 59, 164, 20);
			panel_1.add(txtEmail);
			txtEmail.setColumns(10);
		}

		/**
		 * Escuchador del foco
		 */
		@Override
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub

		}

		/**
		 * Escuchador del foco
		 */
		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			btnAceptar.setEnabled(true);
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
	 * Metodo para buscar un libro dentro de la tabla.
	 * Puede ser por medio de varios atributos
	 */
	public void buscar(){
		dtm = new MiModelo();
		dtm.setColumnIdentifiers(new String [] { "ISBN", "TITULO", "AUTOR", "GENERO","COMPRA","ALQUILAR"});
		Libro l=new Libro();
		table.setModel(dtm); 
		if(comboBox.getSelectedItem().equals("Por autor")){

			for(int i=0; i<lVariable.size(); i++){
				l = lVariable.get(i);
				if(txtBusqueda.getText().equals(l.getAut()) || txtBusqueda.getText().equals(l.getAut()) || txtBusqueda.getText().equals(l.getAut()+" "+l.getAut())){
					dtm.addRow(new String []{l.getISBN(),l.getTitulo(), l.getAut(), l.getGenero(),""+l.getPrecio_venta(),""+l.getPrecio_alquiler()});
				}
			}


			table_1.setModel(dtm);	
		}

		if(comboBox.getSelectedItem().equals("Por ISBN")){
			for(int i=0; i<lVariable.size(); i++){
				l = lVariable.get(i);
				if(txtBusqueda.getText().equals(l.getISBN())){
					dtm.addRow(new String []{l.getISBN(),l.getTitulo(), l.getAut(), l.getGenero(),""+l.getPrecio_venta(),""+l.getPrecio_alquiler()});		
				}

			}
			table_1.setModel(dtm);
		}

		if(comboBox.getSelectedItem().equals("Por titulo")){
			for(int i=0; i<lVariable.size(); i++){
				l = lVariable.get(i);
				if(txtBusqueda.getText().equals(l.getTitulo())){
					dtm.addRow(new String []{l.getISBN(),l.getTitulo(), l.getAut(), l.getGenero(),""+l.getPrecio_venta(),""+l.getPrecio_alquiler()});
				}

			}
			table_1.setModel(dtm);
		}

		if(comboBox.getSelectedItem().equals("Tipo de Busqueda")){
			for(int i=0; i<lVariable.size(); i++){
				l = lVariable.get(i);

				dtm.addRow(new String []{l.getISBN(),l.getTitulo(), l.getAut(), l.getGenero(),""+l.getPrecio_venta(),""+l.getPrecio_alquiler()});

			}
			table_1.setModel(dtm);
		}

		if(comboBox.getSelectedItem().equals("Por Genero")){
			for(int i=0; i<lVariable.size(); i++){
				l = lVariable.get(i);
				if(txtBusqueda.getText().equals(l.getGenero())){
					dtm.addRow(new String []{l.getISBN(),l.getTitulo(), l.getAut(), l.getGenero(),""+l.getPrecio_venta(),""+l.getPrecio_alquiler()});

				}

			}
			table_1.setModel(dtm);
		}

		if(comboBox.getSelectedItem().equals("Precio menor que")){
			for(int i=0; i<lVariable.size(); i++){
				l = lVariable.get(i);
				if(Integer.parseInt(txtBusqueda.getText())>=(l.getPrecio_venta())){
					dtm.addRow(new String []{l.getISBN(),l.getTitulo(), l.getAut(), l.getGenero(),""+l.getPrecio_venta(),""+l.getPrecio_alquiler()});

				}

			}
			table_1.setModel(dtm);
		}
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



