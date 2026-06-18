import java.net.*;
import java.io.*;

public class Servidor {
    public static final int PORT = 4444;
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = null;
        try {
            servidor = new ServerSocket(PORT);
        }catch(IOException e) {
            System.out.println("No se puede iniciar el puerto " + PORT);
            System.exit(-1);
        }

        System.out.println("El servidor esta escuchando.");
        System.out.println(servidor);

        Socket cliente = null;
        BufferedReader entrada = null;
        PrintWriter salida = null;

        try{
            cliente = servidor.accept();
            System.out.println("Conexion establecida");
            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream())), true);

        }catch(IOException e) {
            System.out.println("Error de conexion.");
        }

        int control = 0;

        while(control != -1) {
            String cadena = entrada.readLine();
            System.out.println("El cliente " + cliente + " escribe: " + cadena);
            
            if(cadena.equals("ping")) {
                salida.println("pong");
            } else if(cadena.equals("adios")){
                salida.println("hasta la vista");
                control = -1;
            } else {
                salida.println("...");
            }

        }

        System.out.println("Conexion termina");

        salida.close();
        entrada.close();
        servidor.close();
        cliente.close();

    } //FIN DEL MAIN
} //FIN DE LA CLASE

//7
//69