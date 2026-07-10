import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MultiClientes extends Thread {
    private Socket cliente;
    private BufferedReader entrada;
    private PrintWriter salida;
    private String usuarioBD = "temp1";
    private String contraseniaBD = "12345";
    private String ipBD = "localhost"; // CAMBIAR IP DE LA BASE DE DATOS PARA CONEXION EN RED
    private String puertoBD = "3306";
    private String nombreBD = "bancoxd";
    private Connection c = null;
    private Statement s = null;
    private Statement ss = null;
    private ResultSet rs = null;
    private ResultSet rss = null;
    private int selectorCliente;
    private String usuario = null;
    private String contrasenia = null;
    private String usuarioConsulta = null;
    private String contraseniaConsulta = null;
    private String consulta = null;
    private String numeroCuenta = null;
    private String cedula = null;
    private String numeroCuentaCajero = null;
    private String cedulaCajero = null;

    public MultiClientes(Socket cliente, int selectorCliente) { // CONSTRUCTOR
        this.cliente = cliente;
        this.selectorCliente = selectorCliente;
    }

    @Override
    public void run() {
        boolean acceso = false;

        try{ // INTENTO DE CONEXION A LA BASE DE DATOS
            c = DriverManager.getConnection("jdbc:mysql://" + ipBD + ":" + puertoBD + "/" + nombreBD + "", usuarioBD, contraseniaBD);
        } catch (SQLException e) {
            System.out.println("No se puedo establecer conexion con la base de datos");
            System.exit(-1);
        }

        try{ // INTENTO DE ESTABLECER CONEXION DE FLUJO ENTRE CLIENTE Y SERVIDOR
            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream())), true);
        } catch(IOException e) {
            System.out.println("No se pudo realizar la conexion");
            System.exit(-1);
        }

        while (!acceso) { // LOG IN
            try {
                usuario = entrada.readLine(); // 2 RECEPCION DE LINEA DE CLIENTE
                System.out.println("Usuario leido: " + usuario + " del tipo " + selectorCliente); // PRUEBAS
                contrasenia = entrada.readLine(); // 3 RECEPCION DE LINEA DE CONTRASEÑA
                System.out.println("Contraseña leida: " + contrasenia + " del tipo " + selectorCliente); // PRUEBAS
            } catch(IOException e) {
                System.out.println("No se pudo recibir el usuario y la contraseña");
            }

            acceso = validacionAcceso(usuario, contrasenia);

            if(!acceso) {
                salida.println("Usuario o Contraseña no encontrados");
            } else {
                salida.println("Acceso Concedido");
            }
        }

        /* =================================== SERVICIO AL CLIENTE =================================== */
        if(selectorCliente == 1) {
            acceso = false;
            String aux = null;

            while(!acceso) {
                try {
                    aux = entrada.readLine();
                } catch(IOException e) {
                    System.out.println("No se pudo leer");
                }

                switch(Integer.parseInt(aux)) {
                    case 1: // CREAR CUENTA
                        cedula = null;
                        int idTipoCuenta = 0;
                        numeroCuenta = null;
                        int idCliente = 0;
                        String clave = null;
                        String query = null;

                        try{
                            cedula = entrada.readLine(); // LECTURA DE NUMERO DE CEDULA ENVIADO DESDE EL CLIENTE
                        } catch(IOException e) {
                            System.out.println("No se pudo leer");
                        }
                        try {
                            // CONSULTAR SI EL CLIENTE EXISTE EN LA BASE DE DATOS
                            s = c.createStatement();
                            rs = s.executeQuery("select * from cliente where cedula = '" + cedula + "';");
                            ss = c.createStatement();
                            rss = ss.executeQuery("select numero from cuenta");

                            if(rs.next()) {
                                // SI LO ENCUENTRA AVISA AL CLIENTE
                                salida.println("Cliente encontrado.");
                                idCliente = rs.getInt("id");
                                System.out.println(idCliente);
                                // LECTURA DE DATOS DE LA CUENTA NUEVA
                                try {
                                    String tipo = entrada.readLine();
                                    if(tipo.equals("Ahorros")) {
                                        idTipoCuenta = 1;
                                    }
                                    if(tipo.equals("Corriente")) {
                                        idTipoCuenta = 2;
                                    }
                                    System.out.println(idTipoCuenta);
                                    numeroCuenta = generadorNumCuenta(rss);
                                    System.out.println(numeroCuenta);
                                    clave = entrada.readLine();
                                    System.out.println(clave);
                                } catch(IOException e) {
                                    System.out.println("No se pudo hacer");
                                }
                                query = "insert into cuenta(id_cliente, id_tipo_cuenta, numero, clave) value(" +
                                        idCliente + ", " + idTipoCuenta + ", '" + numeroCuenta + "', '" + clave + "');";
                                System.out.println(query);
                                s.executeUpdate(query);

                            } else {
                                salida.println("Cliente no existe.");
                            }

                        } catch(SQLException e) {
                            System.out.println("No se pudo realizar cambios en la base de datos");
                        }
                        break;
                    case 2: // CREAR USUARIO
                        cedula = null;
                        String nombres = null;
                        String apellidos = null;
                        String fechaNac = null;
                        String celular = null;
                        String correo = null;
                        String direccion = null;
                        try {
                            cedula = entrada.readLine();
                            nombres = entrada.readLine();
                            apellidos = entrada.readLine();
                            fechaNac = entrada.readLine();
                            celular = entrada.readLine();
                            correo = entrada.readLine();
                            direccion = entrada.readLine();
                        } catch(IOException e) {
                            System.out.println("No se pudo leer");
                        }
                        try {
                            s = c.createStatement();
                            s.executeUpdate("insert into cliente(cedula, nombres, apellidos, fecha_nac, telefono, email, direccion) values('"+cedula+"', '"+nombres+"', '"+apellidos+"', '"+fechaNac+"', '"+celular+"', '"+correo+"', '"+direccion+"');");
                        } catch(SQLException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 3: // CONSULTAR SALDO
                        consultarSaldo(entrada, salida);
                        break;
                    case 4: // REPORTE DE MOVIMIENTOS
                        consultaMovimientos(entrada, salida);
                        break;
                    case 5: // DATOS GENERALES
                        datosGenerales(entrada, salida);
                        break;
                    case 6: // SALIR
                        acceso = true;
                        break;
                }
            }
        }
        /* =========================================================================================== */

        /* ========================================= CAJERO ========================================== */
        if(selectorCliente == 2) {
            // cedula = null;
            // numeroCuenta = null;
            String monto = null;

            acceso = false;
            String aux = null;

            while(!acceso) {
                try {
                    aux = entrada.readLine();
                } catch (IOException e) {
                    System.out.println("Problema con la variable de acceso al bucle del menu");
                }
                switch(Integer.parseInt(aux)) {
                    case 1: // CONSULTAR SALDOS
                        cedula = usuario;
                        String mostrarEnPantalla = null;
                        // La cuenta de [Ahorros / Corriente] numero [XXXXXXXXXX] tiene: [XX.XX] USD
                        // select cliente.cedula, cuenta.numero, cuenta.saldo_actual, tipo_cuenta.nombre from cliente join cuenta on cliente.id = cuenta.id_cliente join tipo_cuenta on cuenta.id_tipo_cuenta = tipo_cuenta.id where cliente.cedula = '0912345678';
                        try {
                            double saldo = 0.0;
                            s = c.createStatement();
                            System.out.println(cedula);
                            rs = s.executeQuery("select cliente.cedula, cuenta.numero, cuenta.saldo_actual, tipo_cuenta.nombre from cliente join cuenta on cliente.id = cuenta.id_cliente join tipo_cuenta on cuenta.id_tipo_cuenta = tipo_cuenta.id where cliente.cedula = '" + cedula + "';");
                            while(rs.next()) {
                                mostrarEnPantalla = "La cuenta de " + rs.getString("nombre") + " numero " + rs.getString("numero") + " tiene: " + rs.getDouble("saldo_actual") + " USD";
                                salida.println(mostrarEnPantalla);
                                System.out.println(mostrarEnPantalla);
                            }
                            if(rs.next() == false) {
                                salida.println("false");
                            }
                        } catch(SQLException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 2: // CONSULTAR 5 ULTIMOS MOVIMIENTOS
                        cincoMovimientos(usuario, salida);
                        break;
                    case 3: // DEPOSITAR
                        try {
                            double saldoAntes = 0.0;
                            double saldoDespues = 0.0;
                            String descripcion = "";
                            int idCuenta = 0;
                            numeroCuenta = entrada.readLine();
                            monto = entrada.readLine();
                            descripcion = entrada.readLine();
                            s = c.createStatement();
                            rs = s.executeQuery("select * from cuenta where numero = '" + numeroCuenta + "';");

                            if(rs.next()) {
                                idCuenta = rs.getInt("id");
                                saldoAntes = rs.getDouble("saldo_actual");
                                saldoDespues = saldoAntes + (Double.parseDouble(monto));
                                s = c.createStatement();
                                s.executeUpdate("insert into movimiento(id_cuenta, id_tipo_movimiento, monto, " +
                                        "saldo_antes, saldo_despues, descripcion) values(" + idCuenta + ", " + 1 +
                                        ", " + monto + ", " + saldoAntes + ", " + saldoDespues + ", '" + descripcion + "')");
                                s = c.createStatement();
                                s.executeUpdate("update cuenta set saldo_actual = " + saldoDespues + " where id = " + idCuenta + ";");

                            } else {
                                System.out.println("Numero de cuenta no encontrado.");
                            }
                        } catch(IOException | SQLException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 4: // RETIRAR
                        retirar(entrada, salida);
                        break;
                    case 5: // CAJERO
                        transferir(entrada, salida);
                        break;
                    case 6:
                        acceso = true;
                        break;
                } // FIN DEL SWITCH
            } // FIN DEL WHILE

        } // FIN DEL IF

        try {
            entrada.close();
            salida.close();
            c.close();
            cliente.close();
            if(selectorCliente == 1) {
                System.out.println("Modulo de servicio al cliente desconectado");
            }
            if(selectorCliente == 2) {
                System.out.println("Modulo de cajero automatico desconectado");
            }
        } catch(IOException | SQLException e) {
            System.out.println(e.getMessage());
        }
        
    } // FIN DEL METODO RUN

    public void consultarSaldo(BufferedReader entrada, PrintWriter salida) {
        String numeroCuenta = null;
        String query = null ;
        try(Statement s = c.createStatement()) {
            numeroCuenta = entrada.readLine();
            query = "select * from cuenta where numero = '" + numeroCuenta + "';";
            ResultSet rs = s.executeQuery(query);
            while(rs.next()) {
                salida.println("El saldo de la cuenta " + numeroCuenta + " es " + rs.getDouble("saldo_actual") + " USD");
            }
        } catch(IOException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void transferir(BufferedReader entrada, PrintWriter salida) {
        String aux = null;
        String cuentaDestino = null;
        double valorTransferencia = 0.0;
        double saldoDespues = 0.0;
        double saldo = 0.0;
        double saldoDespuesDestino = 0.0;
        double saldoDestino = 0.0;
        int idCuentaDestino = 0;
        String query = "select * from cuenta where numero = '" + numeroCuentaCajero + "';";
        String query2 = null;

        try {
            aux = entrada.readLine();
            cuentaDestino = aux;
            query2 = "select * from cuenta where numero = '" + cuentaDestino + "';";
            aux = entrada.readLine();
            valorTransferencia = Double.parseDouble(aux);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        try(Statement s = c.createStatement(); Statement ss = c.createStatement(); Statement sss = c.createStatement(); Statement ssss = c.createStatement(); Statement sssss = c.createStatement(); Statement ssssss = c.createStatement()) {
            ResultSet rs = s.executeQuery(query);
            ResultSet rssss = ssss.executeQuery(query2);
            while(rssss.next()) {
                idCuentaDestino = rssss.getInt("id");
                saldoDestino = rssss.getDouble("saldo_actual");
            }
            while(rs.next()) {
                saldo = rs.getDouble("saldo_actual");
                if(saldo >= valorTransferencia) {
                    saldoDespues = saldo - valorTransferencia;
                    sss.executeUpdate("update cuenta set saldo_actual = " + saldoDespues + " where numero = '" + numeroCuentaCajero + "';");
                    ss.executeUpdate("insert into movimiento (id_cuenta, id_tipo_movimiento, id_cuenta_destino, monto, saldo_antes, saldo_despues) values(" + rs.getInt("id") + ", " + 3 + ", " + idCuentaDestino + ", " + valorTransferencia + ", " + saldo + ", " + saldoDespues + ");");
                    saldoDespuesDestino = saldoDestino + valorTransferencia;
                    sssss.executeUpdate("insert into movimiento (id_cuenta, id_tipo_movimiento, monto, saldo_antes, saldo_despues) values(" + idCuentaDestino + ", " + 3 + ", " + valorTransferencia + ", " + saldoDestino + ", " + saldoDespuesDestino + ");");
                    ssssss.executeUpdate("update cuenta set saldo_actual = " + saldoDespuesDestino + " where numero = '" + cuentaDestino + "';");
                    salida.println("Transferencia exitosa.");
                } else {
                    salida.println("Fondos insuficientes.");
                }
            }
        } catch(SQLException e) {
            System.out.println("no vale");
        }
    }

    public void retirar(BufferedReader entrada, PrintWriter salida) {
        String aux = null;
        double saldoDespues = 0.0;
        double saldo = 0.0;
        double valorRetiro = 0.0;
        int idCuenta = 0;
        String query = "select * from cuenta where numero = '" + numeroCuentaCajero + "';";

        try{
            aux = entrada.readLine();
            System.out.println(aux);
            valorRetiro = Double.parseDouble(aux);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        try(Statement s = c.createStatement(); Statement ss = c.createStatement(); Statement sss = c.createStatement()) {
            ResultSet rs = s.executeQuery(query);
            while(rs.next()) {
                saldo = rs.getDouble("saldo_actual");
                if(saldo >= valorRetiro) {
                    saldoDespues = saldo - valorRetiro;
                    sss.executeUpdate("update cuenta set saldo_actual = " + saldoDespues + " where numero = '" + numeroCuentaCajero + "';");
                    ss.executeUpdate("insert into movimiento (id_cuenta, id_tipo_movimiento, monto, saldo_antes, saldo_despues) values(" + rs.getInt("id") + ", " + 2 + ", " + valorRetiro + ", " + saldo + ", " + saldoDespues + ");");
                    salida.println("Retire su dinero.");
                } else {
                    salida.println("Fondos insuficientes.");
                }
            }
        } catch(SQLException e) {
            System.out.println("no vale");
        }
    }
    
    public String generadorNumCuenta(ResultSet rss) throws SQLException {
        // 1. Guardar TODOS los números de cuentas existentes en una lista
        List<String> numerosExistentes = new ArrayList<>();
        while (rss.next()) {
            numerosExistentes.add(rss.getString("numero"));
        }

        String numeroCuenta;
        boolean existe;

        // 2. Generar números hasta encontrar uno que no esté en la lista
        do {
            // Genera un número de 8 dígitos (de 00000000 a 99999999)
            int parteAleatoria = (int) (Math.random() * 100_000_000);
            // Le anteponemos "10" y formateamos para que siempre tenga 10 dígitos totales
            numeroCuenta = "10" + String.format("%08d", parteAleatoria);

            // 3. Verificar si ese número ya existe en la lista
            existe = false;
            for (String num : numerosExistentes) {
                if (num.equals(numeroCuenta)) {
                    existe = true;  // Si lo encuentra, salimos del for
                    break;
                }
            }
        } while (existe); // Si existe, repetimos el proceso

        return numeroCuenta;
    }

    public boolean validacionAcceso(String user, String pass) {
        String userConsulta = "";
        String passConsulta = "";

        try(Statement s = c.createStatement()) {
            if(selectorCliente == 1) {
                ResultSet rs = s.executeQuery("select * from empleado where usuario = '" + user + "';");
                if(rs.next()) { // SI ENCUENTRA COINCIDENCIA
                    userConsulta = rs.getString("usuario");
                    passConsulta = rs.getString("contrasenia");
                }
            }
            if(selectorCliente == 2) {
                ResultSet rs = s.executeQuery("select cliente.cedula, cuenta.clave, cuenta.numero from cliente join cuenta ON cliente.id = cuenta.id_cliente;");
                while(rs.next()) {
                    if(pass.equals(rs.getString("clave"))){
                        userConsulta = rs.getString("cedula");
                        passConsulta = pass;
                        numeroCuentaCajero = rs.getString("numero");
                    }
                }
            }
        } catch(SQLException e) {
            System.out.println("Error");
        }
        if(user.equals(userConsulta) && pass.equals(passConsulta)) {
            return true;
        } else {
            return false;
        }
    }

    public void cincoMovimientos(String usuario, PrintWriter salida) {
        int contador = 0;
        String query = "select cuenta.numero, tipo_movimiento.nombre, movimiento.monto, movimiento.saldo_antes, movimiento.saldo_despues, movimiento.fecha_movimiento from cliente join cuenta on cliente.id = cuenta.id_cliente join movimiento on cuenta.id = movimiento.id_cuenta join tipo_movimiento on tipo_movimiento.id = movimiento.id_tipo_movimiento where cuenta.numero = '" + numeroCuentaCajero + "' order by movimiento.fecha_movimiento desc limit 5;";
        System.out.println(usuario);
        String mensajeSalida = "";
        try(Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(query);
            while(contador < 5) {
                if(rs.next() != false) {
                    mensajeSalida = "Tipo: " + rs.getString("nombre") + " | Monto: " + rs.getDouble("monto") + " | Saldo anterior: " + rs.getDouble("saldo_antes") + " | Saldo actual: " + rs.getDouble("saldo_despues") + " | Fecha: " + rs.getTimestamp("fecha_movimiento");
                    salida.println(mensajeSalida);
                    System.out.println(mensajeSalida);
                    contador ++;
                } else {
                    salida.println("false");
                    contador = 5;
                }
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void consultaMovimientos(BufferedReader entrada, PrintWriter salida) {
        String numeroCuenta = null;
        String query = null ;
        String mensajeSalida = null;

        try(Statement s = c.createStatement()) {
            numeroCuenta = entrada.readLine(); // RECEPCION DESDE SERVICIO AL CLIENTE DEL NUMERO DE CUENTA A CONSULTAR
            System.out.println(numeroCuenta);
            query = "select cuenta.numero, tipo_movimiento.nombre, movimiento.monto, movimiento.saldo_antes, movimiento.saldo_despues, movimiento.fecha_movimiento from cliente join cuenta on cliente.id = cuenta.id_cliente join movimiento on cuenta.id = movimiento.id_cuenta join tipo_movimiento on tipo_movimiento.id = movimiento.id_tipo_movimiento where cuenta.numero = '" + numeroCuenta + "' order by movimiento.fecha_movimiento desc;";
            ResultSet rs = s.executeQuery(query);
            while(rs.next()) {
                mensajeSalida = "Tipo: " + rs.getString("nombre") + " | Monto: " + rs.getDouble("monto") + " | Saldo anterior: " + rs.getDouble("saldo_antes") + " | Saldo actual: " + rs.getDouble("saldo_despues") + " | Fecha: " + rs.getTimestamp("fecha_movimiento");
                salida.println(mensajeSalida);
                System.out.println(mensajeSalida);
            }
            salida.println(" ");
        } catch(IOException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void datosGenerales(BufferedReader entrada, PrintWriter salida) {
        String cedula = null;
        try {
            cedula = entrada.readLine(); // Lee la cédula enviada por el cliente
        } catch (IOException e) {
            System.out.println("Error al leer la cédula: " + e.getMessage());
            return;
        }

        String queryCliente = "SELECT * FROM cliente WHERE cedula = '" + cedula + "'";
        String queryCuentas = "SELECT c.numero, tc.nombre AS tipo_cuenta, c.saldo_actual, c.estado " +
                "FROM cuenta c " +
                "JOIN tipo_cuenta tc ON c.id_tipo_cuenta = tc.id " +
                "WHERE c.id_cliente = (SELECT id FROM cliente WHERE cedula = '" + cedula + "')";

        try (Statement stmt = c.createStatement()) {
            // 1. Consultar datos del cliente
            ResultSet rsCliente = stmt.executeQuery(queryCliente);
            if (!rsCliente.next()) {
                salida.println("Cliente no encontrado.");
                salida.println(""); // línea vacía para indicar fin
                return;
            }

            // Enviar datos personales en una línea (separados por '|' para fácil parseo en cliente)
            String datosPersonales = "Cédula: " + rsCliente.getString("cedula") +
                    " | Nombres: " + rsCliente.getString("nombres") +
                    " " + rsCliente.getString("apellidos") +
                    " | Teléfono: " + rsCliente.getString("telefono") +
                    " | Email: " + rsCliente.getString("email") +
                    " | Dirección: " + rsCliente.getString("direccion") +
                    " | Fecha registro: " + rsCliente.getTimestamp("fecha_registro") +
                    " | Estado: " + rsCliente.getString("estado");
            salida.println(datosPersonales);

            // 2. Consultar cuentas del cliente
            ResultSet rsCuentas = stmt.executeQuery(queryCuentas);
            boolean tieneCuentas = false;
            while (rsCuentas.next()) {
                tieneCuentas = true;
                String lineaCuenta = "  Cuenta Nro: " + rsCuentas.getString("numero") +
                        " | Tipo: " + rsCuentas.getString("tipo_cuenta") +
                        " | Saldo: " + rsCuentas.getDouble("saldo_actual") + " USD" +
                        " | Estado: " + rsCuentas.getString("estado");
                salida.println(lineaCuenta);
            }
            if (!tieneCuentas) {
                salida.println("  El cliente no tiene cuentas registradas.");
            }

            // Línea vacía para indicar fin del reporte
            salida.println("");

        } catch (SQLException e) {
            System.out.println("Error en datosGenerales: " + e.getMessage());
            salida.println("Error al obtener los datos.");
            salida.println("");
        }
    }

} // FIN DE LA CLASE
