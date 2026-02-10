package uce.edu.web.api.supermaxi.interfaces;

public class DetalleVentaDTO {
    private String codigoBarras;
    private Integer cantidad;

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
