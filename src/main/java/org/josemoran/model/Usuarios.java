package org.josemoran.model;

/**
 *
 * @author josel
 */
public class Usuarios {
    private int idUser;
    private String nombre, apellido, correo, contrasena, telefono, direccion; // Mantenemos 'direccion'

    public Usuarios() {
        // Constructor vacío, útil para ciertos frameworks o deserialización
    }

    // CONSTRUCTOR COMPLETO (7 parámetros) - Útil para UsuariosController
    public Usuarios(int idUser, String nombre, String apellido, String correo, String contrasena, String telefono, String direccion) {
        this.idUser = idUser;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // NUEVO CONSTRUCTOR (6 parámetros) - Útil para FacturasController
    // Este constructor es el que FacturasController usará ahora
    public Usuarios(int idUser, String nombre, String apellido, String correo, String contrasena, String telefono) {
        this.idUser = idUser;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = null; // Inicializamos 'direccion' a null o a un valor por defecto si no se proporciona
    }

    // ELIMINA ESTE CONSTRUCTOR (el que tenía el "UnsupportedOperationException")
    /*
    public Usuarios(int aInt, String string, String string0, String string1, String string2, String string3) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    */

    // --- Getters y Setters (mantener todos, incluida la direccion) ---
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return idUser + " || " + nombre + " " + apellido;
    }
}