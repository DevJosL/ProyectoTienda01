
package org.josemoran.model;

/**
 *
 * @author Angel Rodriguez
 */
public class Carros {
    private int idCarro;
    private String marca;
    private String modelo;
    private String color;
    private String descripcion;
    private int año;
    private double precio;
    private int idCategoria;
    private int idProveedor;

    public Carros(int idCarro, String marca, String modelo, String color,
                  String descripcion, int año, double precio,
                  int idCategoria, int idProveedor) {
        this.idCarro = idCarro;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.descripcion = descripcion;
        this.año = año;
        this.precio = precio;
        this.idCategoria = idCategoria;
        this.idProveedor = idProveedor;
    }

    public int getIdCarro() {
        return idCarro;
    }

    public void setIdCarro(int idCarro) {
        this.idCarro = idCarro;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    
    
    @Override
    public String toString() {
        return "Carros{" + "idCarro=" + idCarro + ", marca=" + marca + ", modelo=" + modelo + ", idCategoria=" + idCategoria + ", idProveedor=" + idProveedor + '}';
    }
    
    
}
