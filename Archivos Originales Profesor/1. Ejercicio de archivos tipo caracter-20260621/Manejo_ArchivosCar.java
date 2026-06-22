import java.io.*;
import java.util.Scanner;

public class Manejo_ArchivosCar{

	public static void main(String[] args){

		Scanner entradaTeclado = new Scanner(System.in);
		String nombreArchivo;
		
		System.out.println("Ingresnombre del archivo");
		nombreArchivo=entradaTeclado.nextLine();


		ArchivoCaracter archivoejemplo = new ArchivoCaracter(nombreArchivo);

	//	System.out.println("Puede empezar a escribir");

	//	archivoejemplo.escribir();

		archivoejemplo.leer();
		

}

}


