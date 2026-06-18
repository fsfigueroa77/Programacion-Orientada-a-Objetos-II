import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) throws IOException {
        Socket cliente = null;
        BufferedReader entrada = null;
        PrintWriter salida = null;

        BufferedReader entradaIp = new BufferedReader(new InputStreamReader(System.in)); //es como un Scanner
        String ipServer;

        System.out.print("Ingrese la ip del servidor: ");
        ipServer = entradaIp.readLine();

        try{
            cliente = new Socket(ipServer, 4444);
            System.out.println("Conexion establecida");
            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream())), true);
        } catch(IOException e) {
            System.out.println("No se pudo establecer la conexion.");
            System.exit(-1);
        }

        String lineaEnviar;
        String lineaLlegada;
        Scanner teclado = new Scanner(System.in);

        int control = 1;

        while(control != -1) {
            System.out.print("Escriba aqui su mensaje: ");
            lineaEnviar = teclado.nextLine();
            salida.println(lineaEnviar);
            lineaLlegada = entrada.readLine();
            System.out.println("El servidor responde: " + lineaLlegada);

            if(lineaEnviar.equals("adios")) {
                control = -1;
            }
        }

        System.out.println("Conexion terminada");

        entrada.close();
        salida.close();
        teclado.close();
        entradaIp.close();
        cliente.close();

    } // FIN DEL MAIN
} // FIN DE LA CLASE Cliente