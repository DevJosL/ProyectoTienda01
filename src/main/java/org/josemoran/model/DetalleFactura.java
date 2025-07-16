package org.josemoran.model;

public class DetalleFactura {

    private int idDetalle;
    private int idFactura;
    private int idCarro;
    private int cantidad;
    private double subtotal;

    public DetalleFactura(int idDetalle, int idFactura, int idCarro, int cantidad, double subtotal) {
        this.idDetalle = idDetalle;
        this.idFactura = idFactura;
        this.idCarro = idCarro;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdCarro() {
        return idCarro;
    }

    public void setIdCarro(int idCarro) {
        this.idCarro = idCarro;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "DetalleFactura{"
                + "idDetalle=" + idDetalle
                + ", idFactura=" + idFactura
                + ", idCarro=" + idCarro
                + ", cantidad=" + cantidad
                + ", subtotal=" + subtotal
                + '}';
    }
}
