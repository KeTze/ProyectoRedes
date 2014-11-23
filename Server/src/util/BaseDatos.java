import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JOptionPane;

/**
 * Este método inicializa y gestiona la base de datos.
 */
public class BaseDatos {

	private static Connection conn; 
	
	/**
	 * Sirve para conectarse a la Base de Datos
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void connect() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");

		//Aqui colocar la url donde este localizada la base de datos que vayamos a utilizar
		conn = DriverManager.getConnection("jdbc:sqlite:ProyectoBD.s3db");	
	}

	/**
	 * Sirve para desconectarse de la Base de Datos
	 * @throws SQLException
	 */
	public static void disconnect() throws SQLException{
		conn.close();
	}

	public static void main(String[]args) throws RowNotFoundException{
		

		try {
			BaseDatos.connect();
			Usuario u = new Usuario();
			u.setDni("44444");
			BaseDatos.disconnect();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Registra un nuevo usuario en la Base de Datos
	 * @param u Usuario a registrar
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void registrarUsuario (Usuario u)throws SQLException, ClassNotFoundException{
		String tabla = "Usuarios";
		String columna = "dni,nombre,apellido,telefono,contrasenya,direccion,fecha_nacimiento,admin,saldo";
		int admin;
		if(u.isAdmin()){
			admin = 1;
		}else{
			admin = 0;
		}
		String valores = "'"+u.getDni()+"','"+u.getNombre()+"','"+u.getApellido()+"',"+u.getTelefono()+",'"+u.getPassword()+"','"+u.getDir()+"','"+u.getFechaNac()+"',"+admin+","+u.getSaldo();	

		connect();
		insertar(tabla, columna, valores);
		disconnect();
	}

	/**
	 * Inserta una nueva tabla, junto a sus columnas y sus respectivos valores en la Base de Datos
	 * @param tabla Tabla en la que se guarda
	 * @param columna Columna en la que se guarda
	 * @param valores Valores a guardar
	 * @throws SQLException
	 */
	public static void insertar(String tabla, String columna, String valores) throws SQLException{
		Statement stat = conn.createStatement();  
		stat.executeUpdate("INSERT INTO "+tabla+" ("+columna+")  VALUES ("+valores+")");
		stat.close();
	}


	/** Este metodo se encarga de comprobar si un dni y su contraseña son correctos
	 * @return Usuario devuelve todos los datos del usuario en caso afirmativo y null si no lo son.
	 */
	public static Usuario comprobarDniPass(String dni, String pass) throws SQLException, ClassNotFoundException{
		connect();
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select * from Usuarios");
		Usuario u = null;
		while (rs.next()) {	

			if(dni.equals(rs.getString("dni"))){
				if(pass.equals(rs.getString("contrasenya"))){
					u = new Usuario();
					u.setDni(rs.getString("dni"));
					u.setNombre(rs.getString("nombre"));
					u.setApellido(rs.getString("apellido"));
					u.setTelefono(rs.getInt("telefono"));
					u.setPassword(rs.getString("contrasenya"));
					u.setDir(rs.getString("direccion"));
					u.setFechaNac(rs.getDate("fecha_nacimiento"));
					u.setAdmin(rs.getBoolean("admin"));
					u.setSaldo(rs.getInt("saldo"));

					rs.close();
					stat.close();
					disconnect();
					return u;
				}
			}

		}

		rs.close();
		stat.close();
		disconnect();
		return u;
	}
	
	/**
	 * Elimina un libro de la base de datos
	 * @param ISBN
	 * @return el libro que se ha eliminado
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws RowNotFoundException
	 */
	public static Libro eliminarLibro(String ISBN) throws SQLException, ClassNotFoundException, RowNotFoundException{
		Libro l = BaseDatos.obtenerLibro(ISBN);
		if(l==null){
			throw new RowNotFoundException();
		}
		connect();
		Statement stat = conn.createStatement();
		stat.executeUpdate("DELETE FROM Libros WHERE isbn='"+ISBN+"'");	
		stat.close();
		disconnect();
		return l;
	}
	
	/**
	 * Cambia el precio de alquiler y/o de venta de un libro
	 * @param isbn
	 * @param precioV
	 * @param precioAl
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws RowNotFoundException
	 */
	public static void gestionarLibro(String isbn,int precioV,int precioAl) throws ClassNotFoundException, SQLException, RowNotFoundException{
		Libro l = BaseDatos.obtenerLibro(isbn);
		if(l==null){
			throw new RowNotFoundException();
		}
		connect();
		Statement stat = conn.createStatement();
		stat.executeUpdate("UPDATE Libros SET precio_venta="+precioV+" where isbn='"+isbn+"'");
		stat.executeUpdate("UPDATE Libros SET precio_al="+precioAl+" where isbn='"+isbn+"'");
		stat.close();
		disconnect();	
	}
	
	/**
	 * Sirve para devolver una lista de todos los usuarios del sistema
	 * @return Devuelve la lista de usuarios registrados
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static ArrayList<Usuario>obtenerListaUsuarios() throws ClassNotFoundException, SQLException{
		ArrayList<Usuario>aLUsuario=new ArrayList<Usuario>();
		connect();
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select * from Usuarios");
		Usuario u = null;
		
		while (rs.next()) {	
			u = new Usuario();
			u.setDni(rs.getString("dni"));
			u.setNombre(rs.getString("nombre"));
			u.setApellido(rs.getString("apellido"));
			u.setTelefono(rs.getInt("telefono"));
			u.setPassword(rs.getString("contrasenya"));
			u.setDir(rs.getString("direccion"));
			u.setFechaNac(rs.getDate("fecha_nacimiento"));
			u.setAdmin(rs.getBoolean("admin"));
			u.setSaldo(rs.getInt("saldo"));
			aLUsuario.add(u);
		}
		
		rs.close();
		stat.close();
		disconnect();	
		return aLUsuario; 
	}
	
	/**
	 * Sirve para obtener un usuario concreto del sistema
	 * @param dni del usuario a obtener
	 * @return Obtiene un usuario concreto
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static Usuario obtenerUsuario(String dni) throws SQLException, ClassNotFoundException{
		connect();
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select * from Usuarios where dni='"+dni+"'");
		Usuario u = null;
		while (rs.next()) {	

			u = new Usuario();
			u.setDni(rs.getString("dni"));
			u.setNombre(rs.getString("nombre"));
			u.setApellido(rs.getString("apellido"));
			u.setTelefono(rs.getInt("telefono"));
			u.setPassword(rs.getString("contrasenya"));
			u.setDir(rs.getString("direccion"));
			u.setFechaNac(rs.getDate("fecha_nacimiento"));
			u.setAdmin(rs.getBoolean("admin"));
			u.setSaldo(rs.getInt("saldo"));
		}
		
		rs.close();
		stat.close();
		disconnect();	
		return u;
	}
	
	/**
	 * Sirve para obtener una lista de todas las ventas y alquileres
	 * @return Devuelve todas las ventas y los alquileres en un ArrayList de String
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static  ArrayList<String>obtenerVentas() throws ClassNotFoundException, SQLException{
		connect();
		Statement stat = conn.createStatement();
		Statement stat1 = conn.createStatement();
		ResultSet rs = stat.executeQuery("select isbn from Ventas");
		ArrayList <String>aLVentas=new ArrayList<String>();
		aLVentas.add("Comprados");
		ResultSet rs2 = null;
		while(rs.next()){
			rs2 = stat1.executeQuery("select count(*) from Ventas where isbn="+rs.getString("isbn"));
			aLVentas.add("Compras:");
			aLVentas.add(rs.getString("isbn")+" - "+rs2.getInt(1));
			
		}
		stat1 = conn.createStatement();
		rs=stat.executeQuery("select * from Alquileres");
		aLVentas.add("Alquilados");
		while(rs.next()){
			ResultSet rs3 = stat1.executeQuery("select count(*) from Alquileres where isbn="+rs.getString("isbn"));
			aLVentas.add(rs.getString("isbn")+" - "+rs3.getInt(1));
			rs3.close();
		}
		rs2.close();
		rs.close();

		disconnect();
		return aLVentas;
	}

	/**
	 * Elimina un usuario concreto de la base de datos
	 * @param dni del usuario
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws RowNotFoundException
	 */
	public static void eliminarUsuario(String dni) throws SQLException, ClassNotFoundException, RowNotFoundException{
		Usuario u = BaseDatos.obtenerUsuario(dni);
		if(u==null){
			throw new RowNotFoundException();
		}
		connect();
		Statement stat = conn.createStatement();
		stat.executeUpdate("DELETE from Usuarios "+" where dni='"+dni+"'");	
		stat.close();
		disconnect();

	}
	
	/**
	 * Sirve para obtener todos los libros del sistema
	 * @return devuelve un ArrayList de Libros con todos los libros del sistema
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static ArrayList<Libro> obtenerLibros() throws SQLException, ClassNotFoundException{
		connect();
		ArrayList<Libro> aLLibros = new ArrayList();
		Libro l = new Libro();
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select * from Libros l ; ");
		while (rs.next()) {
			l = new Libro();
			l.setISBN(rs.getString("isbn"));
			l.setTitulo(rs.getString("nombre"));
			l.setGenero(rs.getString("genero"));
			l.setPrecio_venta(rs.getInt("precio_venta"));
			l.setPrecio_alquiler(rs.getInt("precio_al"));
			l.setRuta(rs.getString("ruta"));
			l.setDisponible(rs.getBoolean("disponible"));
			l.setFechaSalida(rs.getDate("fecha_salida"));
			l.setAut(rs.getString("autor"));
			aLLibros.add(l);
		}

		rs.close();
		stat.close();
		disconnect();
		return aLLibros;
	}

	/**
	 * Sirve para obtener un libro concreto de la base de datos
	 * @param ISBN del libro
	 * @return devuelve un objeto de tipo Libro 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Libro obtenerLibro(String ISBN) throws ClassNotFoundException, SQLException{
		connect();
		Libro l=null; 
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select * from Libros l ; ");
		boolean enc= false;
		while(rs.next()&&!enc){
			if(ISBN.equals(rs.getString("isbn"))){
				l = new Libro();
				l.setISBN(rs.getString("isbn"));
				l.setTitulo(rs.getString("nombre"));
				l.setGenero(rs.getString("genero"));
				l.setPrecio_venta(rs.getInt("precio_venta"));
				l.setPrecio_alquiler(rs.getInt("precio_al"));
				l.setRuta(rs.getString("ruta"));
				l.setDisponible(rs.getBoolean("disponible"));
				l.setFechaSalida(rs.getDate("fecha_salida")); 
				l.setAut(rs.getString("autor"));
			}
		}
		rs.close();
		stat.close();
		disconnect();
		return l;
	}

	/**
	 * Obtiene los libros alquilados por un usuario concreto
	 * @param usuario del sistema
	 * @return devuelve un ArrayList de Libros con los libros alquilados por el usuario
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static ArrayList<Libro> obtenerLibrosAlquilados(Usuario u) throws SQLException, ClassNotFoundException{
		connect();
		ArrayList<Libro> aLLibrosAlquilados = new ArrayList();
		Libro l = new Libro();
		Statement stat = conn.createStatement();
		//Obtenemos el dia de hoy
		Calendar c = Calendar.getInstance();
		String dia = Integer.toString(c.get(Calendar.DATE));
		String mes = Integer.toString(c.get(Calendar.MONTH)+1);
		String año = Integer.toString(c.get(Calendar.YEAR));
		ResultSet rs = stat.executeQuery("select l.isbn, l.nombre, l.genero, l.precio_venta, l.precio_al, autor, ruta, disponible, fecha_salida from Libros l, Alquileres a, Usuarios u where (l.isbn = a.isbn and u.dni = a.dni and fecha_fin>='"+dia+"-"+mes+"-"+año+"') and u.dni = "+u.getDni()+"; ");
		while (rs.next()) {
			l = new Libro();
			System.out.println("Libro encontrado");
			l.setISBN(rs.getString("isbn"));
			l.setTitulo(rs.getString("nombre"));
			l.setGenero(rs.getString("genero"));
			l.setPrecio_venta(rs.getInt("precio_venta"));
			l.setPrecio_alquiler(rs.getInt("precio_al"));
			l.setRuta(rs.getString("ruta"));
			l.setDisponible(rs.getBoolean("disponible"));
			l.setFechaSalida(rs.getDate("fecha_salida"));
			l.setAut(rs.getString("autor"));
			aLLibrosAlquilados.add(l);

		}

		rs.close();
		stat.close();
		disconnect();

		return aLLibrosAlquilados;
	}

	/**
	 * Obtiene los libros comprados por un usuario concreto
	 * @param usuario del sistema
	 * @return devuelve un ArrayList de Libros con los libros comprados por el usuario
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static ArrayList<Libro> obtenerLibrosComprados(Usuario u) throws SQLException, ClassNotFoundException{
		connect();
		ArrayList<Libro> aLLibro = new ArrayList();
		Libro l = new Libro();
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select l.isbn, l.nombre, l.genero, l.precio_venta, l.precio_al, autor, ruta, disponible, fecha_salida from Libros l, Ventas v, Usuarios u where (l.isbn = v.isbn and u.dni = v.dni) and u.dni = "+u.getDni()+"; ");
		while (rs.next()) {
			l = new Libro();
			l.setISBN(rs.getString("isbn"));
			l.setTitulo(rs.getString("nombre"));
			l.setGenero(rs.getString("genero"));
			l.setPrecio_venta(rs.getInt("precio_venta"));
			l.setPrecio_alquiler(rs.getInt("precio_al"));
			l.setRuta(rs.getString("ruta"));
			l.setDisponible(rs.getBoolean("disponible"));
			l.setFechaSalida(rs.getDate("fecha_salida"));
			l.setAut(rs.getString("autor"));
			aLLibro.add(l);

		}

		rs.close();
		stat.close();
		disconnect();

		return aLLibro;
	}
	
	/**
	 * Inserta un alquiler a un usuario concreto con un periodo de 7 dias
	 * @param dni
	 * @param ISBN
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void insertarAlquiler(String dni,String ISBN) throws SQLException, ClassNotFoundException{
		Calendar c = Calendar.getInstance();
		String diaInicio = Integer.toString(c.get(Calendar.DATE));
		String mesInicio = Integer.toString(c.get(Calendar.MONTH)+1);
		String añoInicio = Integer.toString(c.get(Calendar.YEAR));
		String diaFin;
		String mesFin;
		String añoFin;
		
		if((c.get(Calendar.DATE)+7)>30){
			diaFin=Integer.toString(c.get(Calendar.DATE)+7-30);
			añoFin=Integer.toString(c.get(Calendar.YEAR));
			mesFin=Integer.toString(c.get(Calendar.MONTH)+2);
		}
		else{
			diaFin=Integer.toString(c.get(Calendar.DATE)+7);
			añoFin=Integer.toString(c.get(Calendar.YEAR));
			mesFin=Integer.toString(c.get(Calendar.MONTH)+1);
		}

		String tabla="Alquileres";
		String columna="dni,isbn,fecha_alquiler,fecha_fin";
		String valores="'"+dni+"','"+ISBN+"','"+diaInicio+"-"+mesInicio+"-"+añoInicio+"','"+diaFin+"-"+mesFin+"-"+añoFin+"'";
		System.out.println(valores);
		connect();
		insertar(tabla, columna, valores);
		disconnect();
	}
	
	/**
	 * Reduce el saldo de un usuario en concreto
	 * @param dni del usuario del sistema
	 * @param precio a reducir
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void reducirSaldo(String dni,int precio) throws SQLException, ClassNotFoundException{
		connect();
		Statement stat = conn.createStatement();
		stat.executeUpdate("UPDATE Usuarios SET saldo=saldo-"+precio+" where dni='"+dni+"'");	
		stat.close();
		disconnect();
	}
	
	/**
	 * Aumenta el credito disponible de un usuario en concreto
	 * @param dni del usuario
	 * @param cantidad a aumentar
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void aumentarSaldo(String dni,int cantidad) throws SQLException, ClassNotFoundException{
		connect();
		Statement stat = conn.createStatement();
		stat.executeUpdate("UPDATE Usuarios SET saldo=saldo+"+cantidad+" where dni='"+dni+"'");		
		stat.close();	
		disconnect();
	}
	
	/**
	 * Inserta una compra en la base de datos del usuario concreto
	 * @param dni del usuario
	 * @param ISBN del libro comprado
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void insertarCompra(String dni,String ISBN) throws ClassNotFoundException, SQLException{
		String tabla = "Ventas";
		String columna = "dni,isbn";
	
		String valores = "'"+dni+"',"+ISBN;
		connect();
		try{
			insertar(tabla, columna, valores);}
		catch(Exception e){
			JOptionPane.showMessageDialog( null, "El libro seleccionado ya esta comprado ", "Error", JOptionPane.ERROR_MESSAGE );	  
		}
		disconnect();
	}

	/**
	 * Inserta un nuevo libro en la Base de datos 
	 * @param libro a insertar
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void insertarLibro(Libro lib) throws SQLException, ClassNotFoundException{
		connect();
		Statement stat = conn.createStatement();
		stat.executeUpdate("INSERT INTO Libros VALUES ('"+lib.getISBN()+"','"+lib.getTitulo()+"','"+lib.getGenero()+"',"+lib.getPrecio_venta()+","+lib.getPrecio_alquiler()+",'"+lib.getRuta()+"','"+lib.getDisponible()+"','"+lib.getAut()+"','"+lib.getFechaSalida()+"');");
		stat.close();
		disconnect();

	}

}