import java.io.*;
import java.net.*;

class ServidorHilos extends Thread{
    private static int cantHilos=0;
    private int numHilo;
    
    Socket o_SCK_CLI;
    
    BufferedReader entrada=null;
    PrintWriter salida=null;
    
    public ServidorHilos(Socket socketCliente){
        cantHilos++;
        this.numHilo=cantHilos;
        this.o_SCK_CLI=socketCliente;
        
    }
    
    @Override
    public void run(){
        try {
            entrada = new BufferedReader(new InputStreamReader(o_SCK_CLI.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(o_SCK_CLI.getOutputStream())), true);
            String nombre;

            salida.println("Enviame tu nombre: ");
            nombre = entrada.readLine();
            System.out.println("Se establecio conexion con " + nombre);

            salida.println("Hola " + nombre + " el hilo numero " + this.numHilo + " te atendera...");
            for (int i = 0; i < 12; i++) {
                this.esperarXsegundos(10);
                salida.println("Llevas conectado " + ((i + 1) * 10) + " segundos");
                //salida.println("Sigues conectado....");
            }

            salida.println("adios");
        }catch (IOException e){
            System.out.println("error");
        }
  
    }

    private void esperarXsegundos(int segundos) {
        try {
            Thread.sleep(segundos * 1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    
    
    
    


}
