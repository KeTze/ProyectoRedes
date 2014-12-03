package tcpClient;
import util.*;

import java.util.ArrayList;
import java.io.*;

import javax.swing.JOptionPane;

/**
 * <p>Title: practica1</p>
 *
 * <p>Description: Introduccion a los sockets</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: ESIDE</p>
 *
 * @author Susana Pérez
 * @version 1.0
 */
public class TCPClient {
	private static SocketManager sm;
	public static void conectar(String ip, int puerto) throws IOException{
		
		sm = new SocketManager(ip, puerto);
		
	}
	
	public static void desconectar() throws IOException{
		sm.CerrarSocket();
	}
	
	/** Metodo que se encarga del inicio de sesion.
	 * Primero comprueba el USER y a continuación si este es correcto el PASS
	 * @param nombre Nombre de usuario
	 * @param pass Contraseña
	 * @return Devuelve el resultado de la operación (Error de USER o Resultado de PASS)
	 * @throws IOException
	 */
	public static String iniciarSesion(String nombre, String pass) throws IOException{
		sm.Escribir("USER "+nombre+'\n');
		String s = sm.Leer();
		System.out.println(s);
		if(s.charAt(0)=='4'){	//Si ha habido error en el USER devuelve el error
			return s;
		}else{
			sm.Escribir("PASS "+pass+'\n');
			String s2 = sm.Leer();
			//System.out.println(s2);
			return s2;
		}
		
	}
	/** Devuelve una lista con todos los objetos de la BBDD del servidor
	 * @return Lista de variables
	 * @throws IOException 
	 */
	public static ArrayList<Variable> obtenerListado() throws IOException{
		ArrayList <Variable> aV = new ArrayList<>();
		sm.Escribir("LISTADO"+'\n');
		
		String linea = sm.Leer();
		System.out.println(linea);
		while(!linea.equals("202 FINLISTA")){
			if(linea!="\n"){
				Variable v = new Variable();
				String[] respuesta = linea.split("; ");
				if(respuesta.length==5){
					
					String [] primerHueco = respuesta[0].split(" ");
					String s = "";
					//Buscamos todo lo q haya desde ELEM ** hasta ;
					for(int i=2; i<primerHueco.length;i++){
						s = s + primerHueco[i];
					}
					v.setPlaca(s);
					v.setNombre(respuesta[1]);
					v.setFuncPrincipal(respuesta[2]);			
					v.setEstado(respuesta[3]);
					v.setUltimaAccion(respuesta[4]);
					aV.add(v);
				}

			}
			linea = sm.Leer();
			System.out.println(linea);
		}
		
		
		return aV;
	}
	
	/**Devuelve una lista con todos los objetos de la BBDD del servidor
	 * que se corresponden con la opcion y el patrón 
	 * @param opcion 
	 * @param patron
	 * @return Lista de variables
	 * @throws IOException
	 */
	public static ArrayList<Variable> obtenerBusqueda(String opcion, String patron) throws IOException{
		ArrayList <Variable> aV = new ArrayList<>();
		sm.Escribir("BUSCAR "+opcion+" "+patron+'\n');
		
		String linea = sm.Leer();
		System.out.println(linea);
		while(!linea.equals("202 FINLISTA")){
			if(linea!="\n"){
				Variable v = new Variable();
				String[] respuesta = linea.split("; ");
				if(respuesta.length==5){
					
					String [] primerHueco = respuesta[0].split(" ");
					String s = "";
					//Buscamos todo lo q haya desde ELEM ** hasta ;
					for(int i=2; i<primerHueco.length;i++){
						s = s + primerHueco[i];
					}
					v.setPlaca(s);
					v.setNombre(respuesta[1]);
					v.setFuncPrincipal(respuesta[2]);			
					v.setEstado(respuesta[3]);
					v.setUltimaAccion(respuesta[4]);
					aV.add(v);
				}

			}
			linea = sm.Leer();
			System.out.println(linea);
		}
		
		
		return aV;
	}
	
	public static boolean encenderVariable(String placa, String variable) throws IOException{
		sm.Escribir("ON "+placa+" "+variable+'\n');
		String respuesta = sm.Leer();
		System.out.println(respuesta);
		if(respuesta.substring(0, 8).equals("403 ERROR")){
			JOptionPane.showMessageDialog( null, "Variable no existe", "Error", JOptionPane.ERROR_MESSAGE );
			return false;
		}else if(respuesta.substring(0, 8).equals("404 ERROR")){
			//No se va a dar nunca en nuestro proyecto
			JOptionPane.showMessageDialog( null, "Variable en estado ON", "Error", JOptionPane.ERROR_MESSAGE );
			return false;
		}else if(respuesta.substring(0, 6).equals("203 OK")){
			return true;
		}
		return false;
	}
	
	public static boolean apagarVariable(String placa, String variable) throws IOException{
		sm.Escribir("OFF "+placa+" "+variable+'\n');
		String respuesta = sm.Leer();
		System.out.println(respuesta);
		if(respuesta.substring(0, 8).equals("405 ERROR")){
			JOptionPane.showMessageDialog( null, "Variable no existe", "Error", JOptionPane.ERROR_MESSAGE );
			return false;
		}else if(respuesta.substring(0, 8).equals("406 ERROR")){
			//No se va a dar nunca en nuestro proyecto
			JOptionPane.showMessageDialog( null, "Variable en estado OFF", "Error", JOptionPane.ERROR_MESSAGE );
			return false;
		}else if(respuesta.substring(0, 6).equals("203 OK")){
			return true;
		}
		return false;
	}
	
	/*Faltan:
	 * ACCION 
	 */
	
	/** Confirma una accion a realizar sobre la variable
	 * @param parametro Parametro a enviar o null si no hay que enviar nada
	 * @return true si ha sido correcta la confirmacion - false si faltan datos
	 * @throws IOException
	 */
	public static boolean confirmarAccion(String parametro) throws IOException{
		if(parametro==null){
			sm.Escribir("CONFIRMAR_ACCION "+'\n');
		}else{
			sm.Escribir("CONFIRMAR_ACCION "+parametro+" "+'\n');
		}
		String s = sm.Leer();
		System.out.println(s);
		if(s.equals("206 OK Accion sobre el sensor confirmada")){
			return true;
		}else if(s.equals("409 ERR Faltan datos")){
			return false;
		}
		return false;
	}
	
	/**Rechaza la accion solicitada
	 * @return true Si ha sido correcto - false Si no ha llegado confirmacion
	 * @throws IOException
	 */
	public static boolean rechazarAccion() throws IOException{
		sm.Escribir("RECHAZAR_ACCION "+'\n');
		String s = sm.Leer();
		System.out.println(s);
		if(s.equals("207 OK Accion cancelada")){
			return true;
		}else{
			return false;
		}
	}
	
	//FALTA OBTENER_FOTO************************************************************
	
	/**Cierra la conexion con el servidor
	 * @return true Si ha sido correcto - false Si no ha llegado confirmacion
	 * @throws IOException
	 */
	public static boolean salir() throws IOException{
		sm.Escribir("SALIR"+'\n');
		String s = sm.Leer();
		System.out.println(s);
		if(s.equals("208 OK Adios")){
			return true;
		}else{
			return false;
		}
		
		
	}
	
	
	//BANCO DE PRUEBAS
    public static void main(String[] args) throws Exception {
       try {
            //Se crea el socket, pasando el nombre del servidor y el puerto de conexión
            SocketManager sm = new SocketManager("127.0.0.1", 3000);
            sm.Escribir("USER user"+'\n');
            sm.Escribir("PASS pass"+'\n');
            sm.Escribir("BUSCAR variable temperatu*"+'\n');
            //sm.Escribir("LISTADO"+'\n');
           // ArrayList <Variable> aV = new ArrayList<>();
    		
    		String linea = sm.Leer();
    		System.out.println(linea);
    		while(!linea.equals("202 FINLISTA")){
    			if(linea!="\n"){
    				
    			}
    			
    			linea = sm.Leer();
    			System.out.println(linea);
    		}
            
            
            
            
            
            System.out.println("Fin de la prueba");
            sm.CerrarSocket();
        } catch (Exception e) {
			System.err.println("main: " + e);
			e.printStackTrace();
        }

    }
    }
