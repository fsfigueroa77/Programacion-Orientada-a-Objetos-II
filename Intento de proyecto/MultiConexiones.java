import java.net.*;
import java.io.*;

public class MultiConexiones extends Thread {

    private static int cantHilos = 0;
    private String nombreUsuario;
    private int numHilo;

    Socket cliente;

    BufferedReader entrada = null;
    PrintWriter salida = null;

    public MultiConexiones(Socket cliente) {
        cantHilos++;
        this.numHilo = cantHilos;
        this.cliente = cliente;
    }

    @Override
    public void run() {
        try {
            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream())), true);

            boolean acceso = false;

            String usuario;
            String contrasenia;

            while (!acceso) {

                try {
                    usuario = entrada.readLine();
                    System.out.println("Usuario leido"); // PRUEBAS
                    contrasenia = entrada.readLine();
                    System.out.println("Contraseña leida"); // PRUEBAS

                    if (usuario.equals("hormonel") && contrasenia.equals("FIFAntino")) {
                        acceso = true;
                        salida.println("Acceso Concedido");
                    } else {
                        salida.println("Usuario o Contraseña no encontrados");
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }


            }
        } catch (IOException e) {
            System.out.println("error");
        }
    }
}
