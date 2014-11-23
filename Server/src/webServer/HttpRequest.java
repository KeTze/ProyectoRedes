package webServer;

import java.io.*;
import java.net.*;
import java.util.*;
import util.*;

final class HttpRequest implements Runnable {

  final static String CRLF = "\r\n";
  SocketManager sockManager;
  int estado;

  // Constructor
  public HttpRequest(SocketManager sockMan) throws Exception {
    sockManager = sockMan;
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

  private void processRequest() throws Exception {
    // Get the request line of the HTTP request message.
   while (true){
	 String requestLine = sockManager.Leer();
    System.out.println("RequestLine: " + requestLine);
    sockManager.Escribir(requestLine+ '\n');
    String[] rl = requestLine.split(" ");
    String comando = rl[0];
    
    switch (estado) {
		case 0:
			/*USER Markel 200 OK Bienvenido Markel.
			USER 400 ERR Falta el nombre de usuario.
			USER Fernando 401 ERR Usuario desconocido.
			*/
	    	if (comando.equals("USER")) {
	    		if(rl.length == 1){
	    			System.out.println("USER 400 ERR Falta el nombre de usuario.");
	    		}else{
	    			String user = rl[1];
	    			if(true /*user.existeUsuario*/){
	    				System.out.println("USER "+user+" 200 OK Bienvenido "+user+".");
	    			}else{
	    				System.out.println("USER "+user+" 401 ERR Usuario desconocido.");
	    			}
	    		}
	    	}else{
	    	}
    	break;
    	case 1:
    		/*PASS 94475 201 OK Bienvenido al sistema.
			PASS mqm 401 ERR La clave es incorrecta.
			PASS 402 ERR Falta la clave.
    		 */
    		if (comando.equals("PASS")) {
	    		if(rl.length == 1){
	    			System.out.println("PASS 402 ERR Falta la clave.");
	    		}else{
	    			String pass = rl[1];
	    			if(true /*pass.existePass*/){
	    				System.out.println("PASS "+pass+" 201 OK Bienvenido al sistema.");
	    			}else{
	    				System.out.println("PASS "+pass+" 401 ERR La clave es incorrecta.");
	    			}
	    		}
	    	}else{
	    	}
        break;
    	case 2:
        	if (comando.equals("LISTADO")) {
        	
        	}else if (comando.equals("BUSCAR")){
        		
        	}else if (comando.equals("ON")){
        		
			}else if (comando.equals("OFF")){
        		
			}else if (comando.equals("ACCION")){
        		
			}else if (comando.equals("OBTENER_FOTO")){
        		
			}else if (comando.equals("SALIR")){
        		
			}
        break;
        case 3:
			if (comando.equals("CONFIRMAR_ACCION")) {
		    	
			}else if (comando.equals("RECHAZAR_ACCION")){
        		
			}
		break;
    }
   }
/*
    // Display the request line.
    //System.out.println();
    //System.out.println(requestLine);

    // Extract the filename from the request line.
    StringTokenizer tokens = new StringTokenizer(requestLine);
    tokens.nextToken(); // skip over the method, which should be "GET"
    //System.out.println("Next Token: "+tokens.nextToken());
    String fileName = tokens.nextToken();

    // Prepend a "." so that file request is within the current directory.
    fileName = "archivos/" + fileName;

    // Open the requested file.
    FileInputStream fis = null;
    boolean fileExists = true;
    try {
      fis = new FileInputStream(fileName);
    }
    catch (FileNotFoundException e) {
      fileExists = false;
      System.out.println("No abre fichero");
    }

    System.out.println("Incoming!!!");
    System.out.println("1.............." + requestLine);

    // Get and display the header lines.
    String headerLine = null;
    while ( (headerLine = sockManager.Leer()).length() != 0) {
      System.out.println("2.............." + headerLine);
    }

    // Construct the response message.
    String statusLine = null;
    String contentTypeLine = null;
    String entityBody = null;
    if (fileExists) {
      statusLine = "HTTP/1.0 200 OK" + CRLF;
      contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
    }
    else {
      statusLine = "HTTP/1.0 404 Not Found" + CRLF;
      contentTypeLine = "Content-Type: text/html" + CRLF;
      //entityBody = "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>" + "<BODY>Not Found</BODY></HTML>";
      entityBody = "<HTML><HEAD><TITLE>Not Found</TITLE></HEAD><BODY><font size='6' face='Calibri'>El fichero no ha sido encontrado. Puedes ver c�mo puedes dar formato al texto poniendolo, por ejemplo, en <b>negrita</b> o <i>cursiva</i>.</font></BODY></HTML>";
    }

    // Send the status line.
    sockManager.Escribir(statusLine);

    // Send the content type line.
    sockManager.Escribir(contentTypeLine);

    // Send a blank line to indicate the end of the header lines.
    sockManager.Escribir(CRLF);

    // Send the entity body.
    if (fileExists) {
      sendBytes(fis);
      fis.close();
    }
    else {
      sockManager.Escribir(entityBody);
    }
*/

    // Close streams and socket.
    //sockManager.CerrarStreams();
    //sockManager.CerrarSocket();

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
}
