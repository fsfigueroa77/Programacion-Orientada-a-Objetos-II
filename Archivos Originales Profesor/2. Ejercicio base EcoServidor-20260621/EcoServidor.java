import java.io.*;
import java.net.*;

public class EcoServidor {
    public static final int PORT=4444;

    public static void main(String args[]) throws IOException {
        //para servidor
        ServerSocket SCK_Serv=null;

        try {
            SCK_Serv=new ServerSocket(PORT);
        }catch (IOException e){
            System.out.println("No se pudo iniciar el servidor en el puerto "+PORT);
            System.exit(-1);

        }

        Socket SCK_cli=null;
        BufferedReader entrada=null;
        PrintWriter salida=null;

        System.out.println("Servidor esta escuchando: "+SCK_Serv);
        try {
            SCK_cli=SCK_Serv.accept();
            

            System.out.println("Conxion aceptada"+SCK_cli);
            entrada= new BufferedReader(new InputStreamReader( SCK_cli.getInputStream()  )      );
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(SCK_cli.getOutputStream())),true );
                
            while(true){
                String cadena=entrada.readLine();
                System.out.println("El cliente envio: "+cadena); // muestra en pantalla del servidor

                salida.println("Esto contesta el servidor: "+cadena);
            }

        }catch (IOException e){
            System.out.println("Error: "+e.getMessage());
        }

        salida.close();
        entrada.close();
        SCK_cli.close();
        SCK_Serv.close();
               


    }
}