import java.io.*;
import java.util.Scanner;

public class Manejo_ArchivosCar{

	public static void main(String[] args){

		Scanner entradaTeclado = new Scanner(System.in); // Objeto para ingreso por teclado
		String nombreArchivo; // Variable que almacena el nombre del archivo
		// ArchivoCaracter archivoejemplo = new ArchivoCaracter(nombreArchivo);
		int op = 0;

		System.out.println("MENU MANEJO DE ARCHIVO");

		while(op != 3) {
			imprimirMenu();
			op = entradaTeclado.nextInt();

			entradaTeclado.nextLine();

			switch(op) {
				case 1:
					nombreArchivo = pedirNombre(entradaTeclado);
					ArchivoCaracter archivoLectura = new ArchivoCaracter(nombreArchivo);
					archivoLectura.leer();
					break;
				case 2:
					nombreArchivo = pedirNombre(entradaTeclado);
					ArchivoCaracter archivoEscritura = new ArchivoCaracter(nombreArchivo);
					System.out.println("Puede empezar a escribir");
					archivoEscritura.escribir();
					break;
				case 3:
					System.out.println("topamos xddd");
					break;
				default:
					System.out.println("\nOpcion no valida. Intente nuevamente.\n");
			}

		}

	}

	public static void imprimirMenu() {
		System.out.println("=============================================================");
		System.out.println("[1] Leer");
		System.out.println("[2] Escribir");
		System.out.println("[3] Salir");
		System.out.print("Ingrese su eleccion: ");
	}

	public static String pedirNombre(Scanner entradaTeclado) {
		System.out.println("Ingrese el nombre del archivo");
		String nombreArchivo = entradaTeclado.nextLine();

		return nombreArchivo;
	}

}


