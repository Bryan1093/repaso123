package uce.edu.web.api.supermaxi.interfaces;

import java.util.List;

public class VentaDTO {
    private String numeroVenta;
    private String cedula;
    private List<DetalleVentaDTO> productos;

    public String getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public List<DetalleVentaDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<DetalleVentaDTO> productos) {
        this.productos = productos;
    }
}
