package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
		conn = DriverManager.getConnection("jdbc:sqlite:redes.s3db");	
	}

	/**
	 * Sirve para desconectarse de la Base de Datos
	 * @throws SQLException
	 */
	public static void disconnect() throws SQLException{
		conn.close();
	}

	public static void main(String[]args){
		

		try {
			BaseDatos.connect();

			BaseDatos.disconnect();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean existeUsuario(String n) throws ClassNotFoundException, SQLException
	 {
	  connect();
	  Statement stat = conn.createStatement();
	  ResultSet rs = stat.executeQuery("SELECT nombre FROM USUARIO where nombre='"+n+"'");
	  boolean enc=false;

	   while(rs.next() && !enc)
	   {
		   String nick = rs.getString("nombre");
		   if(nick.equalsIgnoreCase(n))
			   enc=true;
	   }
	   rs.close();
	   stat.close();
	   disconnect();
	   return enc;
	 }
	
	public boolean comprobarPass(String n, String con) throws ClassNotFoundException, SQLException
	 {
	  connect();
	  Statement stat = conn.createStatement();
	  ResultSet rs = stat.executeQuery("SELECT pass FROM USUARIO where nombre='"+n+"'");
	  boolean enc=false;

	   while(rs.next() && !enc)
	   {
		   String contraseña = rs.getString("pass");
		   if(contraseña.equalsIgnoreCase(con))
			   enc=true;
	   }
	   rs.close();
	   stat.close();
	   disconnect();
	   return enc;
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
	 * Sirve para devolver una lista de todos los objetos del sistema
	 * @return Devuelve la lista de objetos registrados
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static ArrayList<String>listaObjetos() throws ClassNotFoundException, SQLException{
		ArrayList<String>aLObjetos=new ArrayList<String>();
		connect();
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select P.ID, V.NOMBRE, V.FUNCION_PRINC, PV.ESTADO, NOM_ACCION AS A.NOMBRE from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
				+ "WHERE (P.ID=PV.ID_VARIABLE AND V.ID=PV.ID_PLACA AND A.ID=V.ID_ULTIMA_ACCION AND V.ID=AV.ID_VARIABLE)");
		int numElem = 0;
		while (rs.next()) {	
			String s = "ELEM "+numElem+" "+rs.getString("ID")+"; "+rs.getString("NOMBRE")+"; "+rs.getString("FUNCION_PRINC")+"; ";
			if(rs.getBoolean("ESTADO")){
				s = s+"ON; ";
			}else{
				s = s+"OFF; ";
			}
			s = s+rs.getString("NOM_ACCION");
			aLObjetos.add(s);
		}
		
		rs.close();
		stat.close();
		disconnect();	
		return aLObjetos; 
	}

	
}