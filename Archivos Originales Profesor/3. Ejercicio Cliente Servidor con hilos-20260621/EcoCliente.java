import java.net.*;
import java.io.*;
import java.util.Scanner;

public class EcoCliente {
    public static void main(String args[]) throws IOException{
        Socket o_SCK_cli=null;
        BufferedReader entrada=null;
        PrintWriter salida=null;

        try{
           // o_SCK_cli= new Socket(direccion_ip_servidor,puerto_servidor);
            //192.168.100.10
            //127.0.0.1
            //localhost
            o_SCK_cli= new Socket("localhost",4444);
            entrada= new BufferedReader(new InputStreamReader( o_SCK_cli.getInputStream()  )      );
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(o_SCK_cli.getOutputStream())),true );
         }catch (IOException e){
            System.out.println("No se pudo establecer la conexion");
            System.exit(-1);
        }
        String linea;
        String linea_llegada;
        Scanner entrada_teclado = new Scanner(System.in);


        linea_llegada = entrada.readLine();  //recibo mensaje del servidor
        System.out.println(linea_llegada);


        linea = entrada_teclado.nextLine();
        salida.println(linea); //envio nombre

        linea_llegada = entrada.readLine();  //que hilo me atiende
        System.out.println(linea_llegada);


       do {

            linea_llegada = entrada.readLine();  //
            System.out.println(linea_llegada);

        }while(!linea_llegada.equals("adios"));

        
      //  salida.close();
       // entrada.close();
       // o_SCK_cli.close();

    }
}