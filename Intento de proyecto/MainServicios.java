import java.util.Scanner;
import java.net.*;
import java.io.*;

public class MainServicios {
    private static Scanner teclado = new Scanner(System.in);
    public static final String ipServer = "localhost"; // EDITAR IP DEL SERVIDOR
    public static final int port = 4444;

    public static void main(String[] args) throws IOException {
        Socket cliente = null;
        BufferedReader entrada = null;
        PrintWriter salida = null;

        try{
            cliente = new Socket(ipServer, port);
            entrada = new BufferedReader(new InputStreamReader(
                    cliente.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    cliente.getOutputStream())), true);
        }catch(IOException e) {
            System.out.println("El servidor no esta disponible.");
            System.exit(-1);
        }

        System.out.println("SERVICIO AL CLIENTE");


        String lineaEnviar;
        String lineaLlegada;


        boolean acceso = false;
        while(!acceso) {
            System.out.println("Ingrese sus credenciales");

            System.out.print("Usuario: ");
            lineaEnviar = teclado.nextLine();
            salida.println(lineaEnviar);

            System.out.print("Contraseña: ");
            lineaEnviar = teclado.nextLine();
            salida.println(lineaEnviar);

            lineaLlegada = entrada.readLine();

            if(lineaLlegada.equals("Acceso Concedido")) {
                acceso = true;
                System.out.println(lineaLlegada);
            } else {
                System.out.println(lineaLlegada);
            }
        }






    }
}
