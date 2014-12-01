package tcpClient;
import util.*;

import java.net.*;
import java.io.*;

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
	 * @return Devuelve el resultado de la operación
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
	
	/*Faltan:
	 * LISTADO
	 * BUSCAR
	 * ON VARIABLE
	 * OFF VARIABLE
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
		sm.Escribir("SALIR "+'\n');
		String s = sm.Leer();
		System.out.println(s);
		if(s.equals("208 OK Adios")){
			return true;
		}else{
			return false;
		}
		
		
	}
	
    public static void main(String[] args) throws Exception {
        String sentence=""; //Variable dnd se almacena la frase introducida por el usuario
        String modifiedSentence=""; //Variable dnd se recibe la frase capitalizada
        try {
            //Se crea el socket, pasando el nombre del servidor y el puerto de conexión
            SocketManager sm = new SocketManager("127.0.0.1", 3000);
            //Se inicializan los streams de lectura y escritura del socket

            //Se declara un buffer de lectura del
            //dato escrito por el usuario por teclado
            //es necesario pq no es un buffer propio de los sockets
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            //Se almacena en "sentence" la linea introducida por teclado

            while (!sentence.equals("adios")) {
                System.out.print("String a enviar: ");
                sentence = inFromUser.readLine();
                //El método Escribir, pone en el socket lo introducido por teclado
                sm.Escribir(sentence + '\n');
                //El método Leer, lee del socket lo enviado por el Servidor
                modifiedSentence = sm.Leer();
                //Saca por consola la frase modificada enviada por el servidor
                System.out.println("Desde el servidor: " + modifiedSentence);
            }
            System.out.println("Fin de la práctica");
            sm.CerrarSocket();
        } catch (Exception e) {
			System.err.println("main: " + e);
			e.printStackTrace();
        }

    }
    }
