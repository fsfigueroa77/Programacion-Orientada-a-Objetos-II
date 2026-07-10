import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MainCajero {
    public static final String IPSERVIDOR = "localhost"; // CAMBIAR IP DEL SERVIDOR PARA CONEXION EN RED
    public static final int PUERTO = 4321;
    public static Scanner teclado = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        String aux;
        Socket cliente = null;
        BufferedReader entrada = null;
        PrintWriter salida = null;
        String cedula = null;

        try {
            cliente = new Socket(IPSERVIDOR, PUERTO);
            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            salida = new PrintWriter(new OutputStreamWriter(cliente.getOutputStream()), true);
        } catch(IOException e) {
            System.out.println("No se pudo establecer conexion con el servidor");
            System.exit(-1);
        }

        tituloPrincipal();

        String lineaEnviar = null;
        String lineaLlegada = null;

        String lineaSalida = "cajero";

        salida.println(lineaSalida);

        boolean acceso = false;
        while(!acceso) {
            System.out.println("Ingrese sus credenciales");

            System.out.print("Usuario: ");
            lineaEnviar = teclado.nextLine();
            cedula = lineaEnviar;
            salida.println(lineaEnviar);

            System.out.print("Contraseña: ");
            lineaEnviar = teclado.nextLine();
            salida.println(lineaEnviar);

            try {
                lineaLlegada = entrada.readLine(); // A LECTURA DE LINEA DE ACCESO CONCEDIDO O DENEGADO
            } catch (IOException e) {
                System.out.println("No se pudo leer");
            }

            if(lineaLlegada.equals("Acceso Concedido")) {
                acceso = true;
                System.out.println(lineaLlegada);
            } else {
                System.out.println(lineaLlegada);
            }
        } // FIN DEL CICLO DE VERIFICACION

        acceso = false;
        while(!acceso) {
            impresionMenu();
            aux = teclado.nextLine();
            salida.println(aux); // ENVIO AL SERVIDOR OPCION ELEGIDA POR TECLADO

            switch(Integer.parseInt(aux)) {
                case 1: // CONSULTAR SALDOS
                    boolean repetir = false;
                    menu1();
                    while(!repetir) {
                        try {
                            lineaLlegada = entrada.readLine();

                            if(!lineaLlegada.equals("false")) {
                                System.out.println(lineaLlegada);
                            } else {
                                repetir = true;
                            }
                        } catch(IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
                case 2: // CONSULTAR 5 ULTIMOS MOVIMIENTOS
                    menu2();
                    int contador = 0;
                    try(FileWriter salidaArchivo = new FileWriter("5 ultimos movimientos usuario " + cedula + ".txt")) {
                        while(contador < 5) {
                            lineaLlegada = entrada.readLine();
                            if(!lineaLlegada.equals("false")) {
                                System.out.println(lineaLlegada);
                                salidaArchivo.write(lineaLlegada + "\n");
                                contador++;
                            } else {
                                contador = 5;
                            }
                        }
                    } catch(IOException e) {
                        System.out.println("Falla");
                    }
                    break;
                case 3: // DEPOSITAR
                    menu3();
                    System.out.print("Ingrese el numero de cuenta: ");
                    salida.println(teclado.nextLine());
                    System.out.print("Ingrese la cantidad a despositar: ");
                    salida.println(teclado.nextLine());
                    System.out.print("Descripcion: ");
                    salida.println(teclado.nextLine());
                    break;
                case 4: // RETIRAR
                    menu4();
                    System.out.println(retirar(entrada, salida));
                    break;
                case 5: // TRANSFERIR
                    menu5();
                    System.out.println(transferir(entrada, salida));
                    break;
                case 6:
                    acceso = true;
                    break;
                default:
                    System.out.println("Opcion no valida");
            }

        }
        System.out.println("Adios");
    } // FIN DEL MAIN

    public static String transferir(BufferedReader entrada, PrintWriter salida) {
        String mensaje = null;
        String aux = null;
        System.out.print("Ingrese el numero de cuenta de destino: ");
        aux = teclado.nextLine();
        salida.println(aux);
        System.out.print("Ingrese el monto a transferir: ");
        aux = teclado.nextLine();
        salida.println(aux);

        try{
            mensaje = entrada.readLine();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        return mensaje;
    }

    public static String retirar(BufferedReader entrada, PrintWriter salida) {
        String aux = null;
        String mensaje = null;
        int opcion = 0;
        boolean acceso = false;
        while(!acceso) {
            mostrarMenuRetirar();
            aux = teclado.nextLine();
            opcion = Integer.parseInt(aux);
            switch(opcion) {
                case 1:
                    salida.println("10");
                    acceso = true;
                    break;
                case 2:
                    salida.println("20");
                    acceso = true;
                    break;
                case 3:
                    salida.println("50");
                    acceso = true;
                    break;
                case 4:
                    salida.println("100");
                    acceso = true;
                    break;
                case 5:
                    System.out.print("Ingrese la cantidad que desea retirar: ");
                    aux = teclado.nextLine();
                    salida.println(aux);
                    acceso = true;
                    break;
                case 6:
                    acceso = true;
                    break;
                default:
                    System.out.println("Opcion no valida");
            }

        }
        
        try{
            mensaje = entrada.readLine();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        
        return mensaje;
    }

    public static void mostrarMenuRetirar() {
        System.out.println("1.  10 USD");
        System.out.println("2.  20 USD");
        System.out.println("3.  50 USD");
        System.out.println("4. 100 USD");
        System.out.println("5. Otra cantidad");
        System.out.println("6. Salir");
        System.out.print("Elija su opcion: ");
    }

    public static boolean validarCredenciales(String lineaEnviar, PrintWriter salida, String lineaLlegada, BufferedReader entrada) {
        System.out.println("Ingrese sus credenciales");

        System.out.print("Usuario: ");
        lineaEnviar = teclado.nextLine();
        salida.println(lineaEnviar);

        System.out.print("Contraseña: ");
        lineaEnviar = teclado.nextLine();
        salida.println(lineaEnviar);

        try{
            lineaLlegada = entrada.readLine();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        if(lineaLlegada.equals("Acceso Concedido")) {
            System.out.println(lineaLlegada);
            return true;
        } else {
            System.out.println(lineaLlegada);
            return false;
        }

    }

    public static void tituloPrincipal() {
        System.out.println("=============================================================================");
        System.out.println("                              CAJERO AUTOMATICO                              ");
        System.out.println("=============================================================================");
    }

    public static void impresionMenu() {
        System.out.println("=============================================================================");
        System.out.println("                               MENU PRINCIPAL                                ");
        System.out.println("=============================================================================");
        System.out.println("1. Consultar Saldos");
        System.out.println("2. Consultar los 5 ultimos movimientos");
        System.out.println("3. Depositar");
        System.out.println("4. Retirar");
        System.out.println("5. Transferir");
        System.out.println("6. Salir");
        System.out.print("Elija su opcion: ");
    }

    public static void menu1() {
        System.out.println("=============================================================================");
        System.out.println("                              CONSULTAR SALDOS                               ");
        System.out.println("=============================================================================");
    }

    public static void menu2() {
        System.out.println("=============================================================================");
        System.out.println("                       CONSULTAR 5 ULTIMOS MOVIMIENTOS                       ");
        System.out.println("=============================================================================");
    }

    public static void menu3() {
        System.out.println("=============================================================================");
        System.out.println("                                  DEPOSITAR                                   ");
        System.out.println("=============================================================================");
    }

    public static void menu4() {
        System.out.println("=============================================================================");
        System.out.println("                                  RETIRAR                                    ");
        System.out.println("=============================================================================");
    }

    public static void menu5() {
        System.out.println("=============================================================================");
        System.out.println("                                 TRANSFERIR                                  ");
        System.out.println("=============================================================================");
    }

} // FIN DE LA CLASE