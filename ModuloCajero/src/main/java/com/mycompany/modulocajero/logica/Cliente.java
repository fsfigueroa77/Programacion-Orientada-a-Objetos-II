package com.mycompany.modulocajero.logica;

public class Cliente {
    private int id_cliente;
    private String nombre;
    private String apellido;
    private String cedula;
    private String Fecha_nac;
    private String telefono;

    public Cliente() {
    }

    public Cliente(int id_cliente, String nombre, String apellido, String cedula, String Fecha_nac, String telefono) {
        this.id_cliente = id_cliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.Fecha_nac = Fecha_nac;
        this.telefono = telefono;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getFecha_nac() {
        return Fecha_nac;
    }

    public void setFecha_nac(String Fecha_nac) {
        this.Fecha_nac = Fecha_nac;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
}
