package uce.edu.web.api.supermaxi.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uce.edu.web.api.supermaxi.domain.DetalleVenta;
import uce.edu.web.api.supermaxi.domain.Producto;
import uce.edu.web.api.supermaxi.domain.Venta;
import uce.edu.web.api.supermaxi.infrastructure.ProductoRepository;
import uce.edu.web.api.supermaxi.infrastructure.VentaRepository;
import uce.edu.web.api.supermaxi.interfaces.DetalleVentaDTO;
import uce.edu.web.api.supermaxi.interfaces.VentaDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class VentaService {

    @Inject
    VentaRepository ventaRepository;

    @Inject
    ProductoRepository productoRepository;

    public List<Venta> buscarTodas() {
        return ventaRepository.listAll();
    }

    @Transactional
    public void realizarVenta(VentaDTO dto) {
        Venta venta = new Venta();
        venta.setNumeroVenta(dto.getNumeroVenta());
        venta.setCedulaCliente(dto.getCedula());

        List<DetalleVenta> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVentaDTO d : dto.getProductos()) {
            Producto p = productoRepository.findByCodigoBarras(d.getCodigoBarras());
            if (p == null) {
                throw new RuntimeException("Producto no encontrado: " + d.getCodigoBarras());
            }
            if (p.getStock() < d.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + p.getNombre());
            }

            p.setStock(p.getStock() - d.getCantidad());

            DetalleVenta detalle = new DetalleVenta();
            detalle.setCantidad(d.getCantidad());
            detalle.setPrecioUnitario(p.getPrecioUnitario());
            detalle.setProducto(p);
            detalle.setVenta(venta);

            BigDecimal subtotal = p.getPrecioUnitario().multiply(new BigDecimal(d.getCantidad()));
            detalle.setSubtotal(subtotal);
            detalles.add(detalle);

            total = total.add(subtotal);
        }

        venta.setTotalVenta(total);
        venta.setDetalles(detalles);

        ventaRepository.persist(venta);
    }
}
