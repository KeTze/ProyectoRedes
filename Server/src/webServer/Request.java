package webServer;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;

import util.BaseDatos;
import util.SocketManager;

public final class Request implements Runnable {
  final static String CRLF = "\r\n";
  SocketManager sockManager;
  Server server;
  int estado;
  String user = null;

  // Constructor
  public Request(SocketManager sockMan, Server server) {
    sockManager = sockMan;
    this.server = server;
  }

  // Implement the run() method of the Runnable interface.
  public void run() {
    try {
      processRequest();
    }
    catch (Exception e) {
      System.out.println(e);
    }
  }
  
  private String id_variable;
  private String id_placa;	
  private String accion;
  private boolean parametro;
  private static boolean proceso;
  
  
  private void processRequest() throws Exception{
	 proceso = true;
   while (proceso){
	   System.out.println("Estado"+estado);
	   String requestLine = sockManager.Leer();
    System.out.println("RequestLine: " + requestLine);
    //sockManager.Escribir(requestLine+ '\n');
    String[] rl = requestLine.split(" ");
    String comando = rl[0];
   
    switch (estado) {
		case 0:
	    	if (comando.equals("USER")) {
	    		if(rl.length == 1){
	    			sockManager.Escribir("400 ERR Falta el nombre de usuario"+ CRLF);
	    		}else{
	    			user = rl[1];
	    			BaseDatos.connect();
	    			if(BaseDatos.existeUsuario(user)){
	    				estado++;
	    				sockManager.Escribir("200 OK Bienvenido "+user+" "+ CRLF);
	    			}else{
	    				sockManager.Escribir("401 ERR Usuario desconocido"+ CRLF);
	    			}
	    			BaseDatos.disconnect();
	    		}
	    	}else{
	    	}
    	break;
    	case 1:
    		if (comando.equals("PASS")) {
	    		if(rl.length == 1){
	    			sockManager.Escribir("402 ERR Falta la clave"+ CRLF);
	    		}else{
	    			String pass = rl[1];
	    			BaseDatos.connect();
	    			if(BaseDatos.comprobarPass(user, pass)){
	    				estado++;
	    				sockManager.Escribir("201 OK Bienvenido al sistema"+ CRLF);
	    				server.añadirUsuario(user, this);
	    				System.out.println("Usuario añadido al Array");
	    			}else{
	    				sockManager.Escribir("403 ERR La clave es incorrecta"+ CRLF);
	    				//estado--;
	    			}
	    			BaseDatos.disconnect();
	    		}
	    	}else{
	    	}
        break;
    	case 2:
        	if (comando.equals("LISTADO")) {
        		BaseDatos.connect();
        		ArrayList<String> listado = BaseDatos.listaObjetos();
        		BaseDatos.disconnect();
        		for(int i = 0; i < listado.size();i++)
        		{
        			sockManager.Escribir(listado.get(i)+'\n');
        		}
        		sockManager.Escribir("\n");
        		sockManager.Escribir("202 FINLISTA"+ CRLF);
        		
        		
        		
        	}else if (comando.equals("BUSCAR")){
        		if(rl.length>1){
        			BaseDatos.connect();
        			ArrayList<String> listado = BaseDatos.buscarObjetos(rl[1], rl[2]);
        			BaseDatos.disconnect();
            		for(int i = 0; i < listado.size();i++)
            		{
            			sockManager.Escribir(listado.get(i)+'\n');
            		}
            		sockManager.Escribir("\n");
        		}
        		sockManager.Escribir("202 FINLISTA"+ CRLF);
        		
        	}else if (comando.equals("ON")){
        		String id_placa = rl[1];
        		String id_variable = rl[2];
        		BaseDatos.connect();
        		boolean estadoVariable = BaseDatos.estadoVariable(id_placa, id_variable);        		
        		if(estadoVariable){
        			sockManager.Escribir("404 ERROR "+id_variable+" en estado ON"+ CRLF);
        		}else{
        			try{
        				BaseDatos.encenderVariable(id_placa, id_variable);
        				sockManager.Escribir("203 OK "+id_variable+" activada"+ CRLF);
        			}catch(Exception e){
        				if(e.getMessage().equals("Variable Not Found")){
        					sockManager.Escribir("403 ERROR "+id_variable+" no existe"+ CRLF);
        				}
        			}
        		}
    			
    			BaseDatos.disconnect();
    			
			}else if (comando.equals("OFF")){
				String id_placa = rl[1];
        		String id_variable = rl[2];
        		BaseDatos.connect();
        		boolean estadoVariable = BaseDatos.estadoVariable(id_placa, id_variable);
        		if(!estadoVariable){
        			sockManager.Escribir("406 ERROR "+id_variable+" en estado OFF"+ CRLF);
        		}else{
        			try{
        				BaseDatos.apagarVariable(id_placa, id_variable);
        				sockManager.Escribir("204 OK "+id_variable+" desactivadao"+ CRLF);
        			}catch(Exception e){
        				if(e.getMessage().equals("Variable Not Found")){
        					sockManager.Escribir("405 ERROR "+id_variable+" no existe"+ CRLF);
        				}
        			}
        		}
    			
    			BaseDatos.disconnect();
				
			}else if(comando.equals("ACCIONES")){
				if(rl.length==1){
					sockManager.Escribir("410 ERR Falta variable"+ CRLF);
				}else{
					BaseDatos.connect();
					ArrayList<String>aA = BaseDatos.obtenerListaAcciones(rl[1]);
					BaseDatos.disconnect();
					for(int i = 0; i < aA.size();i++)
	        		{
	        			sockManager.Escribir(aA.get(i)+'\n');
	        		}
	        		sockManager.Escribir("\n");
	        		sockManager.Escribir("202 FINLISTA"+ CRLF);
					
				}
				
			}else if(comando.equals("ACCION")){
				String id_placa = rl[1];
        		String id_variable = rl[2];
        		String accion = "";
				for(int i=3; i<rl.length; i++){
					accion = accion+rl[i];
					if(i+1!=rl.length){
						accion = accion+" ";
					}
				}
				
        		BaseDatos.connect();
        		try{
        			if(BaseDatos.estadoPlaca(id_placa)){
            			if(BaseDatos.estadoVariable(id_placa, id_variable)){
            				sockManager.Escribir("205 OK Esperando confirmacion"+ CRLF);
            				this.id_variable = id_variable;
            				this.id_placa = id_placa;
            				this.accion = accion;
            				estado++;
            			}else{
            				sockManager.Escribir("408 ERROR id_variable en estado OFF"+ CRLF);
            			}
            		}else{
            			sockManager.Escribir("408 ERROR placa en estado OFF"+ CRLF);
            		}
        		}catch(SQLException e){
        			if(e.getMessage().equals("Variable Not Found")){
        				sockManager.Escribir("407 ERROR id_variable no existe"+ CRLF);
        			}else if(e.getMessage().equals("Placa Not Found")){
        				sockManager.Escribir("407 ERROR id_placa no existe"+ CRLF);
        			}
        		}
        		BaseDatos.disconnect();
        		
			}else if (comando.equals("OBTENER_FOTO")){
				if(rl.length>1){
					String id_placa = rl[1];
					BaseDatos.connect();
					String url = BaseDatos.obtenerFoto(id_placa);
					BaseDatos.disconnect();
					if(url==null){
						sockManager.Escribir("403 ERR Identificador no existe"+ CRLF);
					}else{
						File f = new File(url);
						sockManager.Escribir("206 OK "+f.length()+" bytes transmitiendo"+CRLF);
						FileInputStream fis = new FileInputStream(f.getAbsolutePath());
						sendBytes(fis);
					}
					
				}
				
			}else if (comando.equals("SALIR")){
				sockManager.Escribir("208 OK Adios"+ CRLF);
        		server.desconectarUsuario(user);
        	    sockManager.CerrarStreams();
        	    sockManager.CerrarSocket();
        		proceso = false;
			}
        break;
        case 3:
			if (comando.equals("CONFIRMAR_ACCION")) {
				if(parametro){
					if(rl.length == 1){
						sockManager.Escribir("409 ERR Faltan datos"+ CRLF);
		    		}else{
		    			//String parametro = rl[1];
		    			sockManager.Escribir("206 OK Accion sobre el sensor confirmada"+ CRLF);
		    			BaseDatos.connect();
		    			BaseDatos.cambiarUltimaAccion(id_placa, id_variable, accion);
		    			BaseDatos.disconnect();
		    			estado--;
		    		}
				}else{
					sockManager.Escribir("206 OK Accion sobre el sensor confirmada"+ CRLF);
					BaseDatos.connect();
					BaseDatos.cambiarUltimaAccion(id_placa, id_variable, accion);
					BaseDatos.disconnect();
					estado--;
				}
				
				
			}else if (comando.equals("RECHAZAR_ACCION")){
        		sockManager.Escribir("207 OK Accion cancelada"+ CRLF);
        		estado--;
			}else if (comando.equals("PARAMETRO")){
				if(rl.length==1){
					sockManager.Escribir("411 ERROR Falta accion"+ CRLF);
				}else{
					String accion = "";
					for(int i=1; i<rl.length; i++){
						accion = accion+rl[i];
						if(i+1!=rl.length){
							accion = accion+" ";
						}
					}
					BaseDatos.connect();
					String parametro = BaseDatos.obtenerParametro(accion);
					BaseDatos.disconnect();
					if(parametro==null){
						this.parametro=false;
						sockManager.Escribir("209 OK No hay parametro"+ CRLF);
					}else{
						this.parametro=true;
						sockManager.Escribir("210 OK "+parametro+CRLF);
					}
				}
			}
		break;
    }
   }


  }

  private void sendBytes(FileInputStream fis) throws Exception {
    // Construct a 1K buffer to hold bytes on their way to the socket.
    byte[] buffer = new byte[1024];
    int bytes = 0;

    // Copy requested file into the socket's output stream.
    while ( (bytes = fis.read(buffer)) != -1) {
      sockManager.Escribir(buffer, bytes);
    }
  }

  private static String contentType(String fileName) {
    if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
      return "text/html";
    }
    if (fileName.endsWith(".ram") || fileName.endsWith(".ra")) {
      return "audio/x-pn-realaudio";
    }
    return "application/octet-stream";
  }
  public void desconectarSocket(){
	  try {
		sockManager.CerrarStreams();
		sockManager.CerrarSocket();
	} catch (IOException e) {
		e.printStackTrace();
	}
	  proceso = false;
  }
}
