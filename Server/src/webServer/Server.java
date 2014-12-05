package webServer;
import java.io.* ;
import java.net.* ;
import java.util.* ;

import util.*;

public class Server
{
	private static ArrayList<String>usuariosSockets;
	
	private static int maxUsuarios;
	
	
	public Server() {
		usuariosSockets = new ArrayList<>();
		maxUsuarios = 0;
		ejecutar();
	}
	
	public void ejecutar(){

		// Set the port number.
		int port = 3000; //(new Integer(argv[0])).intValue();
	
		ServerSocket wellcomeSocket;
		System.out.println("Servidor funcionando...");
		
		try {
			wellcomeSocket = new ServerSocket(port);
			while (true)
			{
				System.out.println("NumUsuarios: "+usuariosSockets.size());
				//Socket conn = sock.accept();
				System.out.println(maxUsuarios);
				if((usuariosSockets.size()+1)!=maxUsuarios){
					SocketManager sockManager = new SocketManager(wellcomeSocket.accept());
					
					
					System.out.println("Nuevo usuario en el servidor");
					Request request = new Request(sockManager);

					Thread thre = new Thread(request);

					thre.start();
					
				}else{
					System.out.println("Nuevo usuario... Max Usuarios Alcanzado. No se conecta");
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
	
	public static ArrayList <String> getUsuarios(){
		System.out.println(usuariosSockets.size());
		return usuariosSockets;
	}
	
	public static void desconectarUsuario(String usuario){
		try {//Cambiar
			usuariosSockets.get(0).CerrarStreams();
			usuariosSockets.get(0).CerrarSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
		usuariosSockets.remove(usuario);
		//usuariosSockets.remove(usuario);
	}
	
	public static void añadirUsuario(String sM){
		usuariosSockets.add(sM);
		System.out.println("Usuario añadido");
	}
	
	public static void setMaxUsuarios(int num){
		System.out.println(num);
		maxUsuarios = num;
	}
}