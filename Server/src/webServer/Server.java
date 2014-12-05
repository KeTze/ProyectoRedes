package webServer;
import java.io.* ;
import java.net.* ;
import java.util.* ;

import util.*;

public class Server
{
	
	private int maxUsuarios;
	private ArrayList<String>usuarios;
	private HashMap<String, Request>sockets;
	private boolean encendido;
	private ServerSocket wellcomeSocket;
	public Server() {
		//usuariosSockets = new ArrayList<>();
		maxUsuarios = 0;
		sockets = new HashMap<>();
		usuarios = new ArrayList<>();
		encendido = true;
	}
	
	
	public void ejecutar(){

		// Set the port number.
		int port = 3000; //(new Integer(argv[0])).intValue();
	
		
		System.out.println("Servidor funcionando...");
		
		try {
			wellcomeSocket = new ServerSocket(port);
			while (encendido)
			{
				//System.out.println("NumUsuarios: "+Conexiones.usuarios.size());
				//Socket conn = sock.accept();
				//System.out.println(maxUsuarios);
				
				SocketManager sockManager = new SocketManager(wellcomeSocket.accept());
					
				if((usuarios.size()+1)<=maxUsuarios){
					System.out.println("Nuevo usuario en el servidor");
					Request request = new Request(sockManager, this);

					Thread thre = new Thread(request);

					thre.start();
					
				}else{
					System.out.println("Nuevo usuario... Max Usuarios Alcanzado. No se conecta");
					sockManager.CerrarSocket();
				}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String argv[])
	{
		new Server();
	}
	
	public ArrayList<String> getUsuarios(){
		return usuarios;
	}
	
	public void desconectarUsuario(String user){
		boolean enc = false;
		int i=0;
		while(!enc){
			String s = usuarios.get(i);
			
			if(s.equals(user)){
				enc = true;
			}else{
				i++;
			}
		}
		usuarios.remove(i);
		sockets.get("user").desconectarSocket();
		sockets.remove(user);
	}
	
	public void setMaxUsuarios(int i){
		maxUsuarios = i;
	}
	
	public void añadirUsuario(String user, Request r){
		usuarios.add(user);
		sockets.put(user, r);
	}
}