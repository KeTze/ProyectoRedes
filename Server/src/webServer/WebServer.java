package webServer;
import java.io.* ;
import java.net.* ;
import java.util.* ;

import util.*;

public final class WebServer
{
	
	public static void main(String argv[])
	{
		// Set the port number.
		int port = 3000; //(new Integer(argv[0])).intValue();

		ServerSocket wellcomeSocket;
		System.out.println("Servidor funcionando...");
		try {
			wellcomeSocket = new ServerSocket(port);
			while (true)
			{
				//Socket conn = sock.accept();
				
				SocketManager sockManager = new SocketManager(wellcomeSocket.accept());
				
				System.out.println("Hola");
				HttpRequest request = new HttpRequest(sockManager);

				Thread thre = new Thread(request);

				thre.start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
