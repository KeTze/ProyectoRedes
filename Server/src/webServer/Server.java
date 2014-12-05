package webServer;
import java.io.* ;
import java.net.* ;
import java.util.* ;

import util.*;

public class Server
{
	
	
	
	public Server() {
		//usuariosSockets = new ArrayList<>();
		Conexiones.iniciarlizar();
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
				System.out.println("NumUsuarios: "+Conexiones.usuarios.size());
				//Socket conn = sock.accept();
				//System.out.println(maxUsuarios);
				
				SocketManager sockManager = new SocketManager(wellcomeSocket.accept());
					
				if((Conexiones.usuarios.size()+1)<=Conexiones.maxUsuarios){
					System.out.println("Nuevo usuario en el servidor, Anterior usuario"+Conexiones.usuarios.get(0));
					Request request = new Request(sockManager);

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
	
	public static String devolverUsuario(int i){
		return Conexiones.usuarios.get(i);
	}
}