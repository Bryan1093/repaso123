package uce.edu.web.api.supermaxi.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uce.edu.web.api.supermaxi.domain.Producto;
import uce.edu.web.api.supermaxi.infrastructure.ProductoRepository;

@ApplicationScoped
public class ProductoService {

    @Inject
    ProductoRepository repository;

    @Transactional
    public void ingresarProducto(Producto p) {
        Producto existente = repository.findByCodigoBarras(p.getCodigoBarras());
        if (existente != null) {
            existente.setStock(existente.getStock() + p.getStock());
        } else {
            repository.persist(p);
        }
    }

    public Producto buscarPorCodigo(String codigo) {
        return repository.findByCodigoBarras(codigo);
    }
}
