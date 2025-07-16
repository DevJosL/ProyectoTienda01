package org.josemoran.model;

import java.sql.Date;

public class Factura {
    private int idFactura;
    private Date fecha;
    private int idUser;
    private String direccion;
    
    public Factura(int idFactura, Date fecha, int idUser) {
        this.idFactura = idFactura;
        this.fecha = fecha;
        this.idUser = idUser;
        this.direccion = direccion;
    }

    public Factura(int aInt, Date date, int aInt0, String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Factura{" + "idFactura=" + idFactura + ", fecha=" + fecha + ", idUser=" + idUser + '}';
    }
}
