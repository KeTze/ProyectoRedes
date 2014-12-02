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
	public static void connect() throws  SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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

	

	public static boolean existeUsuario(String n) throws SQLException
	 {
	  
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
	   
	   return enc;
	 }
	
	public static boolean comprobarPass(String n, String con) throws SQLException
	 {
	  
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
	/*public static void gestionarLibro(String isbn,int precioV,int precioAl) throws ClassNotFoundException, SQLException, RowNotFoundException{
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
	}*/
	
	/**
	 * Sirve para devolver una lista de todos los objetos del sistema
	 * @return Devuelve la lista de objetos registrados
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static ArrayList<String>listaObjetos() throws SQLException{
		ArrayList<String>aLObjetos=new ArrayList<String>();
		
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select P.ID, V.NOMBRE, V.FUNCION_PRINC, PV.ESTADO, NOMBRE_A from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
				+ "WHERE (P.ID=PV.ID_PLACA AND V.ID=PV.ID_VARIABLE) AND (A.ID=PV.ID_ULTIMA_ACCION AND V.ID=PV.ID_VARIABLE)");
		int numElem = 1;
		
		while (rs.next()) {	
			String s = "ELEM "+numElem+" "+rs.getString("ID")+"; "+rs.getString("NOMBRE")+"; "+rs.getString("FUNCION_PRINC")+"; ";
			
			if(rs.getBoolean("ESTADO")){
				s = s+"ON; ";
			}else{
				s = s+"OFF; ";
			}
			s = s+rs.getString("NOMBRE_A");
			//System.out.println(s);
			aLObjetos.add(s);
			numElem++;
		}
		
		rs.close();
		stat.close();
			
		return aLObjetos; 
	}
	
	public static void main(String[]args){
		try {
			BaseDatos.connect();
			BaseDatos.listaObjetos();
			BaseDatos.disconnect();
		} catch ( SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	

	public static ArrayList<String>buscarObjetos(String opcion, String patron) throws SQLException{
		ArrayList<String>aLObjetos=new ArrayList<String>();
		patron.replace('*', '%');
		patron.replace('?', '_');
		Statement stat = conn.createStatement();
		ResultSet rs = null;
		if(opcion.equalsIgnoreCase("placa")){
			rs = stat.executeQuery("select P.ID, V.NOMBRE, V.FUNCION_PRINC, PV.ESTADO, NOMBRE_A from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
					+ "WHERE (P.ID=PV.ID_PLACA AND V.ID=PV.ID_VARIABLE AND A.ID=PV.ID_ULTIMA_ACCION AND V.ID=PV.ID_VARIABLE AND P.ID ILIKE '"+patron+"')");
		}else if(opcion.equalsIgnoreCase("variable")){
			rs = stat.executeQuery("select P.ID, V.NOMBRE, V.FUNCION_PRINC, PV.ESTADO, NOMBRE_A from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
					+ "WHERE (P.ID=PV.ID_PLACA AND V.ID=PV.ID_VARIABLE AND A.ID=PV.ID_ULTIMA_ACCION AND V.ID=PV.ID_VARIABLE AND V.NOMBRE ILIKE '"+patron+"')");
		}else if(opcion.equalsIgnoreCase("funcion principal")){
			rs = stat.executeQuery("select P.ID, V.NOMBRE, V.FUNCION_PRINC, PV.ESTADO, NOMBRE_A from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
					+ "WHERE (P.ID=PV.ID_PLACA AND V.ID=PV.ID_VARIABLE AND A.ID=PV.ID_ULTIMA_ACCION AND V.ID=PV.ID_VARIABLE AND V.FUNC_PRINC ILIKE '"+patron+"')");
		}else if(opcion.equalsIgnoreCase("estado")){
			if((patron.equalsIgnoreCase("O_"))||(patron.equalsIgnoreCase("%N"))||(patron.equalsIgnoreCase("_N")||(patron.equalsIgnoreCase("ON")))){
				rs = stat.executeQuery("select P.ID, V.NOMBRE, V.FUNCION_PRINC, PV.ESTADO, NOMBRE_A from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
						+ "WHERE (P.ID=PV.ID_PLACA AND V.ID=PV.ID_VARIABLE AND A.ID=PV.ID_ULTIMA_ACCION AND V.ID=PV.ID_VARIABLE AND PV.ESTADO=1");
			}else if((patron.equalsIgnoreCase("O_F"))||(patron.equalsIgnoreCase("OF_"))||(patron.equalsIgnoreCase("_FF"))||(patron.equalsIgnoreCase("%F"))
				||(patron.equalsIgnoreCase("%FF"))||(patron.equalsIgnoreCase("OF%"))||(patron.equalsIgnoreCase("OFF"))){
				rs = stat.executeQuery("select P.ID, V.NOMBRE, V.FUNCION_PRINC, PV.ESTADO, NOMBRE_A from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
						+ "WHERE (P.ID=PV.ID_PLACA AND V.ID=PV.ID_VARIABLE AND A.ID=PV.ID_ULTIMA_ACCION AND V.ID=PV.ID_VARIABLE AND PV.ESTADO=0");
			}else if(patron.equalsIgnoreCase("O%")){
				rs = stat.executeQuery("select P.ID, V.NOMBRE, V.FUNCION_PRINC, PV.ESTADO, NOMBRE_A from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
						+ "WHERE (P.ID=PV.ID_PLACA AND V.ID=PV.ID_VARIABLE AND A.ID=PV.ID_ULTIMA_ACCION AND V.ID=PV.ID_VARIABLE)");
			}
		}else if(opcion.equalsIgnoreCase("ultima accion")){
			rs = stat.executeQuery("select P.ID, V.NOMBRE, V.FUNCION_PRINC, PV.ESTADO, NOMBRE_A from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
					+ "WHERE (P.ID=PV.ID_PLACA AND V.ID=PV.ID_VARIABLE AND A.ID=PV.ID_ULTIMA_ACCION AND V.ID=PV.ID_VARIABLE AND NOMBRE_A ILIKE '"+patron+"')");
		}		
		int numElem = 1;
		while (rs.next()) {	
			String s = "ELEM "+numElem+" "+rs.getString("ID")+"; "+rs.getString("NOMBRE")+"; "+rs.getString("FUNCION_PRINC")+"; ";
			if(rs.getBoolean("ESTADO")){
				s = s+"ON; ";
			}else{
				s = s+"OFF; ";
			}
			s = s+rs.getString("NOMBRE_A");
			aLObjetos.add(s);
		}
		
		rs.close();
		stat.close();
			
		return aLObjetos; 
	}
	

	public static void encenderVariable(String placa, String variable) throws SQLException{
		
		Statement stat2 = conn.createStatement();
		ResultSet rs = stat2.executeQuery("select ID_VARIABLE from VARIABLE V, PL_VAR, PLACA P where "
				+ "V.ID=PL.ID_VARIABLE AND PL.ID_PLACA=P.ID AND NOMBRE='"+variable+"' AND P.ID='"+placa+"'");
		int idVariable = 0;
		while(rs.next()){
			idVariable = rs.getInt("ID_VARIABLE");
		}
		stat2.close();
		Statement stat = conn.createStatement();
		stat.executeUpdate("UPDATE PL_VAR SET ESTADO=1 where ID_VARIABLE="+idVariable+" AND ID_PLACA='"
				+variable+"'");
		stat.close();
		
	}
	
	public static void apagarVariable(String placa, String variable) throws SQLException{
		
		Statement stat2 = conn.createStatement();
		ResultSet rs = stat2.executeQuery("select ID_VARIABLE from VARIABLE V, PL_VAR, PLACA P where "
				+ "V.ID=PL.ID_VARIABLE AND PL.ID_PLACA=P.ID AND NOMBRE='"+variable+"' AND P.ID='"+placa+"'");
		int idVariable = 0;
		while(rs.next()){
			idVariable = rs.getInt("ID_VARIABLE");
		}
		stat2.close();
		
		Statement stat = conn.createStatement();
		stat.executeUpdate("UPDATE PL_VAR SET ESTADO=0 where ID_VARIABLE="+idVariable+" AND ID_PLACA='"
				+variable+"'");
		stat.close();
		
	}
	
	public static boolean estadoVariable(String placa, String variable) throws SQLException{
		
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select ESTADO from VARIABLE V, PL_VAR, PLACA P where "
				+ "V.ID=PL.ID_VARIABLE AND PL.ID_PLACA=P.ID AND NOMBRE='"+variable+"' AND P.ID='"+placa+"'");
		boolean estado = false;
		
		if(rs.next()){
			estado = rs.getBoolean("ESTADO");
		}else{
			throw new SQLException("Not Found");
		}
		
		
		stat.close();
		return estado;
		
	}
	
	public static boolean estadoPlaca(String placa) throws SQLException{
		
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select ESTADO from PLACA P where ID='"+placa+"'");
		boolean estado = false;
		while(rs.next()){
			estado = rs.getBoolean("ESTADO");
		}
		stat.close();
		return estado;
		
	}
	
	public static void cambiarUltimaAccion(String placa, String variable, String accion) throws SQLException{
		
	}
	
	public String obtenerFoto(String placa) throws SQLException{
		return null;
	}
	
	public static void anyadirUsuario(String nombre, String pass) throws SQLException{
		
	}
	
	public static void borrarUsuario(String nombre) throws SQLException{
		
	}
	
	public static void cambiarNombreUsuario(String nombre, String pass) throws SQLException{
		
	}
	
	public static void cambiarPassUsuario(String nombre, String pass) throws SQLException{
		
	}
	
	public static void anyadirAccionAVariable(String placa, String variable, String accion) throws SQLException{
		
	}
	
	public static void reiniciarVariable(String placa, String variable, String accion) throws SQLException{
		
	}
	
	
	
}