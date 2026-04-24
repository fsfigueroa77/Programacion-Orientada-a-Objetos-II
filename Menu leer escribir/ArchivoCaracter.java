import java.io.*;
import java.util.Scanner;

class ArchivoCaracter{
	private String nombre;

	public ArchivoCaracter(String nombre){
		this.nombre=nombre;
	}

	public void escribir(){
		try{
			FileWriter salidaArc = new FileWriter(nombre);
			Scanner entradaTeclado = new Scanner(System.in);
			String lineaTexto;

			do {
				lineaTexto = entradaTeclado.nextLine();
				if(!lineaTexto.equals("-1"))
					salidaArc.write(lineaTexto+"\n");
			}while( !lineaTexto.equals("-1")  );

			salidaArc.close();

		}catch(IOException ioe){
			System.out.println("No se ha encontrado el archivo");

		}
	}

	public void leer(){
		try{
			FileReader entradaArc = new FileReader(nombre);
			int carAscii;

			carAscii = entradaArc.read();

			while(carAscii!=-1){
				char letra =  (char)carAscii;
				System.out.print(letra);
				carAscii = entradaArc.read();
			}

		} catch (IOException e) {
			System.out.println("No se ha encontrado el archivo");
		}

	}




}