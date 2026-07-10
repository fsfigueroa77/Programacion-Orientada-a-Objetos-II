import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MainServicios {
    public static final String IPSERVIDOR = "localhost"; // CAMBIAR IP DEL SERVIDOR PARA CONEXION EN RED
    public static final int PUERTO = 4321;
    public static Scanner teclado = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        Socket cliente = null;
        BufferedReader entrada = null;
        PrintWriter salida = null;

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

        String lineaSalida = "servicios";

        salida.println(lineaSalida); // 1) ENVIO DE LINEA DE TIPO DE CLIENTE

        boolean acceso = false;
        while(!acceso) {
            System.out.println("Ingrese sus credenciales");

            System.out.print("Usuario: ");
            lineaEnviar = teclado.nextLine();
            salida.println(lineaEnviar); // 2) ENVIO DE LINEA DE USUARIO

            System.out.print("Contraseña: ");
            lineaEnviar = teclado.nextLine();
            salida.println(lineaEnviar); // 3) ENVIO DE LINEA DE CONTRASEÑA

            try {
                lineaLlegada = entrada.readLine(); // A) LECTURA DE LINEA DE ACCESO CONCEDIDO O DENEGADO
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
        String aux = null;
        
        while(!acceso) {
            impresionMenu();
            aux = teclado.nextLine();
            salida.println(aux); // ENVIO AL SERVIDOR OPCION ELEGIDA POR TECLADO

            switch(Integer.parseInt(aux)) {
                case 1: // CREAR CUENTA
                    menu1();
                    System.out.print("Numero de cedula del cliente: ");
                    salida.println(teclado.nextLine()); // ENVIO AL SERVIDOR NUMERO DE CEDULA CLIENTE
                    try {
                        lineaLlegada = entrada.readLine(); // RECIBO DEL SERVIDOR NUMERO DE CEDULA ENCONTRADO O NO
                        if(lineaLlegada.equals("Cliente encontrado.")) {
                            System.out.println(lineaLlegada);
                            System.out.print("Tipo de cuenta. [Ahorros] o [Corriente]: ");
                            salida.println(teclado.nextLine());
                            System.out.print("Ingrese la clave de 4 digitos: ");
                            salida.println(teclado.nextLine());
                        }
                        if(lineaLlegada.equals("Cliente no existe.")) {
                            System.out.println(lineaLlegada);
                        }
                    } catch(IOException e) {
                        System.out.println("No se pudo leer");
                    }
                    break;
                case 2: // CREAR USUARIO
                    menu2();
                    System.out.print("Ingrese numero de cedula: ");
                    salida.println(teclado.nextLine());
                    System.out.print("Ingrese los nombres: ");
                    salida.println(teclado.nextLine());
                    System.out.print("Ingrese los apellidos: ");
                    salida.println(teclado.nextLine());
                    System.out.print("Ingrese fecha de nacimiento(YYYY-MM-DD): ");
                    salida.println(teclado.nextLine());
                    System.out.print("Ingrese numero de celular: ");
                    salida.println(teclado.nextLine());
                    System.out.print("Ingrese correo electronico: ");
                    salida.println(teclado.nextLine());
                    System.out.print("Ingrese direccion: ");
                    salida.println(teclado.nextLine());
                    break;
                case 3: // CONSULTAR SALDO
                    menu3();
                    System.out.print("Ingrese el numero de cuenta: ");
                    salida.println(teclado.nextLine());
                    try {
                        System.out.println(entrada.readLine());
                    } catch(IOException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4: // REPORTE DE MOVIMIENTOS
                    menu4();
                    String numeroCuentaUsuario = null;
                    System.out.print("Ingrese el numero de cuenta: ");
                    numeroCuentaUsuario = teclado.nextLine();
                    salida.println(numeroCuentaUsuario); // ENVIO AL SERVIDOR DE CUENTA A CONSULTAR
                    try(FileWriter salidaArchivo = new FileWriter("Reporte " + numeroCuentaUsuario + ".txt")){
                        lineaLlegada = "hola xd";
                        while(!lineaLlegada.equals(" ")) {
                            lineaLlegada = entrada.readLine();
                            salidaArchivo.write(lineaLlegada + "\n");
                            System.out.println(lineaLlegada);
                        }
                    } catch(IOException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5: // DATOS GENERALES
                    menu5(); // Imprime el título
                    System.out.print("Ingrese el número de cédula del cliente: ");
                    salida.println(teclado.nextLine()); // Envía la cédula al servidor

                    // Lee el reporte línea por línea hasta recibir una línea vacía
                    try {
                        String linea;
                        while (true) {
                            linea = entrada.readLine();
                            if (linea == null || linea.isEmpty()) {
                                break; // Fin del reporte
                            }
                            System.out.println(linea);
                        }
                    } catch (IOException e) {
                        System.out.println("Error al recibir los datos: " + e.getMessage());
                    }
                    break;
                case 6: // SALIR
                    acceso = true;
                    break;
                default:
                    System.out.println("Opcion no valida");
            }

        }

    } // FIN DEL MAIN

    public static void tituloPrincipal() {
        System.out.println("=============================================================================");
        System.out.println("                            SERVICIOS AL CLIENTE                             ");
        System.out.println("=============================================================================");
    }

    public static void impresionMenu() {
        System.out.println("=============================================================================");
        System.out.println("                               MENU PRINCIPAL                                ");
        System.out.println("=============================================================================");
        System.out.println("1. Crear cuenta");
        System.out.println("2. Crear usuario");
        System.out.println("3. Consultar saldo");
        System.out.println("4. Reporte de movimientos");
        System.out.println("5. Datos generales");
        System.out.println("6. Salir");
        System.out.print("Elija su opcion: ");
    }

    public static void menu1() {
        System.out.println("=============================================================================");
        System.out.println("                               CREAR CUENTA                                  ");
        System.out.println("=============================================================================");
    }

    public static void menu2() {
        System.out.println("=============================================================================");
        System.out.println("                               CREAR USUARIO                                 ");
        System.out.println("=============================================================================");
    }

    public static void menu3() {
        System.out.println("=============================================================================");
        System.out.println("                              CONSULTAR SALDO                                ");
        System.out.println("=============================================================================");
    }

    public static void menu4() {
        System.out.println("=============================================================================");
        System.out.println("                           REPORTE DE MOVIMIENTOS                            ");
        System.out.println("=============================================================================");
    }

    public static void menu5() {
        System.out.println("=============================================================================");
        System.out.println("                              DATOS GENERALES                                ");
        System.out.println("=============================================================================");
    }

} // FIN DE LA CLASE
