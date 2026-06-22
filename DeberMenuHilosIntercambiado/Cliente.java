import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    private static Scanner teclado = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        Socket cliente = null; // Declaracion en null de nuevo objeto Socket
        BufferedReader entrada = null; // Declaracion en null de nuevo objeto para el buffer de entrada
        PrintWriter salida = null; // Declaracion en null de nuevo objeto para el buffer de salida

        // CAPTURA LA IP DEL SERVIDOR POR PARTE DEL CLIENTE
        BufferedReader entradaIp = new BufferedReader(new InputStreamReader(System.in)); // Buffer de entrada para captura la IP por teclado
        String ipServer; // Almacena la IP del servidor

        System.out.print("Ingrese la ip del servidor: ");
        ipServer = entradaIp.readLine(); // Ingreso por teclado por parte del usuario de la IP y asignacion al objeto tipo String antes creado

        try{
            cliente = new Socket(ipServer, 4445); // intento de establecer conexion con la ip del servidor y el puerto
            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream())); // Inicio de buffer de entrada
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream())), true); // Inicio de buffer de salida
        } catch(IOException e) { // Captura la excepcion cuando el servidor no esta disponible
            System.out.println("No se pudo establecer la conexion.");
            System.exit(-1); // Finaliza el programa de forma anormal
        }

        System.out.println("Conexion establecida");
        
        String lineaEnviar;
        String lineaLlegada;

        lineaLlegada = entrada.readLine(); // Servidor envia: "Enviame tu nombre: "
        System.out.println(lineaLlegada); // Impresion del envio del servidor

        lineaEnviar = teclado.nextLine(); // Ingreso por teclado del nombre
        salida.println(lineaEnviar); // Envio de nombre al servidor

        lineaLlegada = entrada.readLine();  //Servidor Envia: Que hilo me atiende
        System.out.println(lineaLlegada);

        int control = 0;
        while(control == 0) {
            String menu;
            String[] menuSplit;
            String aux;

            try {
                menu = entrada.readLine();
                menuSplit = menu.split("/");
                for (int i = 0; i < menuSplit.length; i++) {
                    System.out.println(menuSplit[i]);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            System.out.print("Elija una opcion: ");
            lineaEnviar = teclado.nextLine();
            salida.println(lineaEnviar);
            try {
                control = Integer.parseInt(entrada.readLine());
            } catch(IOException e) {
                System.out.println(e.getMessage());
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
