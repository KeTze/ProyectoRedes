package util;

import java.util.ArrayList;

public class Conexiones {
	public static ArrayList<String>usuarios;
	public static int maxUsuarios;
	
	public static void iniciarlizar(){
		usuarios = new ArrayList<>();
		maxUsuarios = 0;
	}
}
