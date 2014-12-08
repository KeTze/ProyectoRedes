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
		}else if(opcion.equalsIgnoreCase("funcion_principal")){
			rs = stat.executeQuery("select P.ID, V.ID_VARIABLE, V.FUNCION_PRINC, PV.ESTADO, ID_ACCION from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
					+ "WHERE (P.ID=PV.ID_PLACA AND V.ID_VARIABLE=PV.ID_VARIABLE AND A.ID_ACCION=PV.ID_ULTIMA_ACCION AND upper(V.FUNCION_PRINC) LIKE upper('"+patron+"'))");
		}else if(opcion.equalsIgnoreCase("estado")){
			if((patron.equalsIgnoreCase("O_"))||(patron.equalsIgnoreCase("%N"))||(patron.equalsIgnoreCase("_N")||(patron.equalsIgnoreCase("ON")))){
				rs = stat.executeQuery("select P.ID, V.ID_VARIABLE, V.FUNCION_PRINC, PV.ESTADO, ID_ACCION from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
						+ "WHERE (P.ID=PV.ID_PLACA AND V.ID_VARIABLE=PV.ID_VARIABLE AND A.ID_ACCION=PV.ID_ULTIMA_ACCION AND PV.ESTADO=1)");
			}else if((patron.equalsIgnoreCase("O_F"))||(patron.equalsIgnoreCase("OF_"))||(patron.equalsIgnoreCase("_FF"))||(patron.equalsIgnoreCase("%F"))
				||(patron.equalsIgnoreCase("%FF"))||(patron.equalsIgnoreCase("OF%"))||(patron.equalsIgnoreCase("OFF"))){
				rs = stat.executeQuery("select P.ID, V.ID_VARIABLE, V.FUNCION_PRINC, PV.ESTADO, ID_ACCION from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
						+ "WHERE (P.ID=PV.ID_PLACA AND V.ID_VARIABLE=PV.ID_VARIABLE AND A.ID_ACCION=PV.ID_ULTIMA_ACCION AND PV.ESTADO=0)");
			}else if(patron.equalsIgnoreCase("O%")){
				rs = stat.executeQuery("select P.ID, V.ID_VARIABLE, V.FUNCION_PRINC, PV.ESTADO, ID_ACCION from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
						+ "WHERE (P.ID=PV.ID_PLACA AND V.ID_VARIABLE=PV.ID_VARIABLE AND A.ID_ACCION=PV.ID_ULTIMA_ACCION)");
			}
		}else if(opcion.equalsIgnoreCase("ultima_accion")){
			rs = stat.executeQuery("select P.ID, V.ID_VARIABLE, V.FUNCION_PRINC, PV.ESTADO, ID_ACCION from PLACA P, VARIABLE V, ACCION A, PL_VAR PV "
					+ "WHERE (P.ID=PV.ID_PLACA AND V.ID_VARIABLE=PV.ID_VARIABLE AND A.ID_ACCION=PV.ID_ULTIMA_ACCION AND upper(A.ID_ACCION) LIKE upper('"+patron+"'))");
		}		
		int numElem = 1;
		//System.out.println("NumElem: "+numElem);
		
		if(rs!=null){
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
		}
		rs.close();
		stat.close();
		
		return aLObjetos; 
	}
	
	public static ArrayList<String> obtenerListaAcciones(String Variable) throws SQLException{
		ArrayList<String>aLAcciones=new ArrayList<String>();
		
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select A.ID_ACCION from ACCION A, AC_VAR AV "
				+ "WHERE (A.ID_ACCION=AV.ID_ACCION) AND AV.ID_VARIABLE='"+Variable+"'");
		int numElem = 1;
		while (rs.next()) {	
			String s = "ACCION "+numElem+" "+rs.getString("ID_ACCION");
			numElem++;
			System.out.println(s);
			aLAcciones.add(s);
		}
		
		rs.close();
		stat.close();
			
		return aLAcciones; 
	}
	
	public static ArrayList<String> obtenerAcciones() throws SQLException{
		ArrayList<String>aLAcciones=new ArrayList<String>();
		
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select A.ID_ACCION from ACCION A");
		while (rs.next()) {	
			String s = rs.getString("ID_ACCION");
			aLAcciones.add(s);
		}
		
		rs.close();
		stat.close();
			
		return aLAcciones; 
	}
	
	public static void borrarAccionVariable(String variable, String accion) throws SQLException
	{
		Statement stmt = conn.createStatement();
		String query = "DELETE FROM AC_VAR WHERE ID_ACCION = '"+accion+"' AND ID_VARIABLE = '"+variable+"'";
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		stmt.close();
	}
	
	
	public static void main(String[]args){
		try {
			BaseDatos.connect();
			BaseDatos.obtenerListaAcciones("Temperatura");
			//BaseDatos.apagarVariable("Placa2", "Temperatura");
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
		ResultSet rs = stat.executeQuery("select ESTADO from PLACA P where ID='"+placa+"'");
		boolean estado = false;
		if(rs.next()){
			estado = rs.getBoolean("ESTADO");
		}else{
			throw new SQLException("Placa Not Found");
			
		}
		
		stat.close();
		return estado;
		
	}
	
	public static String obtenerParametro(String accion) throws SQLException{
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select PARAMETRO from ACCION where ID_ACCION='"+accion+"'");
		String parametro = null;
		if(rs.next()){
			parametro = rs.getString("PARAMETRO");
		}
		
		stat.close();
		return parametro;
	}
	
	public static void cambiarUltimaAccion(String placa, String variable, String accion) throws SQLException{
		Statement stat = conn.createStatement();
		stat.executeUpdate("UPDATE PL_VAR SET ID_ULTIMA_ACCION='"+accion+"' where ID_VARIABLE='"+variable+"' AND ID_PLACA='"+placa+"'");
		stat.close();
	}
	
	public static void cambiarUltimasAcciones(String variable, String accion) throws SQLException{
		Statement stat = conn.createStatement();
		stat.executeUpdate("UPDATE PL_VAR SET ID_ULTIMA_ACCION='"+accion+"' where ID_VARIABLE='"+variable+"'");
		stat.close();
	}
	
	public static String obtenerFoto(String placa) throws SQLException{
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select FOTO from PLACA where ID='"+placa+"'");
		String url = null;
		if(rs.next()){
			url = rs.getString("FOTO");
		}
		stat.close();
		return url;
	}
	
	public static boolean anyadirUsuario(String nombre, String pass) throws SQLException
	{
		Statement stmt = conn.createStatement();
		String query = "INSERT INTO USUARIO VALUES ('"+nombre+"','"+pass+"')";
		try {
			stmt.executeUpdate(query);
			stmt.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			stmt.close();
			return false;
		}
	}
	
	public static void borrarUsuario(String nombre) throws SQLException
	{
		Statement stmt = conn.createStatement();
		String query = "DELETE FROM USUARIO WHERE nombre = '"+nombre+"'";
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		stmt.close();
	}
	
	public static void cambiarNombreUsuario(String nombre) throws SQLException
	{
		Statement stmt = conn.createStatement();
		String query = "UPDATE USUARIO SET nombre = '"+nombre+"' WHERE nombre='"+nombre+"'";
		try
		{
			stmt.executeUpdate(query);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		stmt.close();
	}
	
	public static void cambiarPassUsuario(String nombre, String pass) throws SQLException
	{
		Statement stmt = conn.createStatement();
		String query = "UPDATE USUARIO SET pass = '"+pass+"' WHERE nombre='"+nombre+"'";
		try
		{
			stmt.executeUpdate(query);
			
		}catch(Exception e) {
			e.printStackTrace();
		}	
		stmt.close();
	}
	
	public static boolean anyadirAccionAVariable(String variable, String accion) throws SQLException{
		Statement stmt = conn.createStatement();
		String query = "INSERT INTO AC_VAR VALUES ('"+accion+"','"+variable+"')";
		try {
			stmt.executeUpdate(query);
			stmt.close();
			return true;
		} catch (SQLException e) {
			stmt.close();
			return false;
		}
		
	}
	
	public static void reiniciarVariable(String variable) throws SQLException{

		Statement stat = conn.createStatement();
		stat.executeUpdate("UPDATE PL_VAR SET ESTADO=0 where ID_VARIABLE='"+variable+"'");
		stat.close();

		Statement stmt = conn.createStatement();
		String query = "UPDATE PL_VAR SET ID_ULTIMA_ACCION='-' where ID_VARIABLE='"+variable+"'";
		try
		{
			stmt.executeUpdate(query);
			
		}catch(Exception e) {
			e.printStackTrace();
		}	
		stmt.close();
	}
	
	public static ArrayList<String> obtenerListaUsuarios() throws SQLException{
		ArrayList<String>aLUsuarios=new ArrayList<String>();
		
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select nombre from USUARIO");
		while (rs.next()) {
			aLUsuarios.add(rs.getString("nombre"));
		}
		
		rs.close();
		stat.close();
			
		return aLUsuarios; 
	}
	
	public static ArrayList<String> obtenerListaVariables() throws SQLException{
		ArrayList<String>aLVariables=new ArrayList<String>();
		
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select ID_VARIABLE from VARIABLE");
		while (rs.next()) {
			aLVariables.add(rs.getString("ID_VARIABLE"));
		}
		
		rs.close();
		stat.close();
			
		return aLVariables; 
	}
	
	
}