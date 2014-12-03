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
		ResultSet rs = stat.executeQuery("select P.ID, V.ID_VARIABLE, V.FUNCION_PRINC, PV.ESTADO, ID_ACCION from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
				+ "WHERE (P.ID=PV.ID_PLACA AND V.ID_VARIABLE=PV.ID_VARIABLE AND A.ID_ACCION=PV.ID_ULTIMA_ACCION)");
		int numElem = 1;
		
		while (rs.next()) {	
			String s = "ELEM "+numElem+" "+rs.getString("ID")+"; "+rs.getString("ID_VARIABLE")+"; "+rs.getString("FUNCION_PRINC")+"; ";
			
			if(rs.getBoolean("ESTADO")){
				s = s+"ON; ";
			}else{
				s = s+"OFF; ";
			}
			s = s+rs.getString("ID_ACCION");
			System.out.println(s);
			aLObjetos.add(s);
			numElem++;
		}
		
		rs.close();
		stat.close();
			
		return aLObjetos; 
	}	
	

	public static ArrayList<String>buscarObjetos(String opcion, String patron) throws SQLException{
		ArrayList<String>aLObjetos=new ArrayList<String>();
		patron = patron.replace('*', '%');
		patron = patron.replace('?', '_');
		Statement stat = conn.createStatement();
		ResultSet rs = null;
		if(opcion.equalsIgnoreCase("placa")){
			rs = stat.executeQuery("select P.ID, V.ID_VARIABLE, V.FUNCION_PRINC, PV.ESTADO, ID_ACCION from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
					+ "WHERE (P.ID=PV.ID_PLACA AND V.ID_VARIABLE=PV.ID_VARIABLE AND A.ID_ACCION=PV.ID_ULTIMA_ACCION AND upper(P.ID) LIKE upper('"+patron+"'))");
		}else if(opcion.equalsIgnoreCase("variable")){
			rs = stat.executeQuery("select P.ID, V.ID_VARIABLE, V.FUNCION_PRINC, PV.ESTADO, ID_ACCION from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
					+ "WHERE (P.ID=PV.ID_PLACA AND V.ID_VARIABLE=PV.ID_VARIABLE AND A.ID_ACCION=PV.ID_ULTIMA_ACCION AND upper(V.ID_VARIABLE) LIKE upper('"+patron+"'))");
		}else if(opcion.equalsIgnoreCase("funcion principal")){
			rs = stat.executeQuery("select P.ID, V.ID_VARIABLE, V.FUNCION_PRINC, PV.ESTADO, ID_ACCION from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
					+ "WHERE (P.ID=PV.ID_PLACA AND V.ID_VARIABLE=PV.ID_VARIABLE AND A.ID_ACCION=PV.ID_ULTIMA_ACCION AND upper(V.FUNC_PRINC) LIKE upper('"+patron+"'))");
		}else if(opcion.equalsIgnoreCase("estado")){
			if((patron.equalsIgnoreCase("O_"))||(patron.equalsIgnoreCase("%N"))||(patron.equalsIgnoreCase("_N")||(patron.equalsIgnoreCase("ON")))){
				rs = stat.executeQuery("select P.ID, V.ID_VARIABLE, V.FUNCION_PRINC, PV.ESTADO, ID_ACCION from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
						+ "WHERE (P.ID=PV.ID_PLACA AND V.ID_VARIABLE=PV.ID_VARIABLE AND A.ID_ACCION=PV.ID_ULTIMA_ACCION AND PV.ESTADO=1");
			}else if((patron.equalsIgnoreCase("O_F"))||(patron.equalsIgnoreCase("OF_"))||(patron.equalsIgnoreCase("_FF"))||(patron.equalsIgnoreCase("%F"))
				||(patron.equalsIgnoreCase("%FF"))||(patron.equalsIgnoreCase("OF%"))||(patron.equalsIgnoreCase("OFF"))){
				rs = stat.executeQuery("select P.ID, V.ID_VARIABLE, V.FUNCION_PRINC, PV.ESTADO, ID_ACCION from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
						+ "WHERE (P.ID=PV.ID_PLACA AND V.ID_VARIABLE=PV.ID_VARIABLE AND A.ID_ACCION=PV.ID_ULTIMA_ACCION AND PV.ESTADO=0");
			}else if(patron.equalsIgnoreCase("O%")){
				rs = stat.executeQuery("select P.ID, V.ID_VARIABLE, V.FUNCION_PRINC, PV.ESTADO, ID_ACCION from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
						+ "WHERE (P.ID=PV.ID_PLACA AND V.ID_VARIABLE=PV.ID_VARIABLE AND A.ID_ACCION=PV.ID_ULTIMA_ACCION)");
			}
		}else if(opcion.equalsIgnoreCase("ultima accion")){
			rs = stat.executeQuery("select P.ID, V.ID_VARIABLE, V.FUNCION_PRINC, PV.ESTADO, ID_ACCION from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
					+ "WHERE (P.ID=PV.ID_PLACA AND V.ID_VARIABLE=PV.ID_VARIABLE AND A.ID_ACCION=PV.ID_ULTIMA_ACCION AND upper(A.ID_ACCION) LIKE upper('"+patron+"'))");
		}		
		int numElem = 1;
		//System.out.println("NumElem: "+numElem);
		while (rs.next()) {	
			String s = "ELEM "+numElem+" "+rs.getString("ID")+"; "+rs.getString("ID_VARIABLE")+"; "+rs.getString("FUNCION_PRINC")+"; ";
			
			if(rs.getBoolean("ESTADO")){
				s = s+"ON; ";
			}else{
				s = s+"OFF; ";
			}
			s = s+rs.getString("ID_ACCION");
			System.out.println(s);
			aLObjetos.add(s);
			numElem++;
		}
		rs.close();
		stat.close();
		
		return aLObjetos; 
	}
	
	public static ArrayList<String> obtenerListaAcciones(String Variable) throws SQLException{
		ArrayList<String>aLAcciones=new ArrayList<String>();
		
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select A.ID_ACCION from ACCION A, AC_VAR AV "
				+ "WHERE (A.ID_ACCION=AV.ID_ACCION)");
		
		while (rs.next()) {	
			String s = rs.getString("ID_ACCION");;
			
			//System.out.println(s);
			aLAcciones.add(s);
		}
		
		rs.close();
		stat.close();
			
		return aLAcciones; 
	}
	
	public static void main(String[]args){
		try {
			BaseDatos.connect();
			BaseDatos.estadoVariable("Placa2", "Temperatura");
			BaseDatos.apagarVariable("Placa2", "Temperatura");
			//BaseDatos.listaObjetos();
			BaseDatos.disconnect();
		} catch ( SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public static void encenderVariable(String placa, String variable) throws SQLException{
		
		Statement stat = conn.createStatement();
		stat.executeUpdate("UPDATE PL_VAR SET ESTADO=1 where ID_VARIABLE='"+variable+"' AND ID_PLACA='"+placa+"'");
		stat.close();
		
	}
	
	public static void apagarVariable(String placa, String variable) throws SQLException{
		
		Statement stat = conn.createStatement();
		stat.executeUpdate("UPDATE PL_VAR SET ESTADO=0 where ID_VARIABLE='"+variable+"' AND ID_PLACA='"
				+placa+"'");
		stat.close();
		
	}
	
	public static boolean estadoVariable(String placa, String variable) throws SQLException{
		
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select ESTADO from PL_VAR PV where ID_VARIABLE='"+variable+"' AND ID_PLACA='"+placa+"'");
		boolean estado = false;
		if(rs.next()){
			estado = rs.getBoolean("ESTADO");
		}else{
			throw new SQLException("Variable Not Found");
		}
		
		
		stat.close();
		return estado;
		
	}
	
	public static boolean estadoPlaca(String placa) throws SQLException{
		
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select ESTADO from PLACA P where ID_PLACA='"+placa+"'");
		boolean estado = false;
		if(rs.next()){
			estado = rs.getBoolean("ESTADO");
		}else{
			throw new SQLException("Placa Not Found");
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