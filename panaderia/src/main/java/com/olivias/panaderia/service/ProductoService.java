package com.olivias.panaderia.service;

import com.olivias.panaderia.model.Producto;
import com.olivias.panaderia.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    @Autowired 
    private ProductoRepository productoRepository;

    /// Método Get
    public List<Producto> obtenerTodos() {
        log.info("Obteniendo la lista completa de productos para la vitrina.");
        return productoRepository.findAll();
    }

    public Producto guardarProducto(Producto producto) {
        log.info("Guardando un nuevo producto en el catálogo: {}", producto.getNombre());
        return productoRepository.save(producto);
    }

    public Optional<Producto> buscarPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);
        return productoRepository.findById(id);
    }


    // Metodo DELETE
  public void eliminarProducto(Long id) {
    /// Verificamos primero si el producto existe antes de borrar para evitar caídas
    if (productoRepository.existsById(id)) {
        org.slf4j.LoggerFactory.getLogger(ProductoService.class)
            .warn("Eliminando de forma permanente el producto con ID: {}", id);
        productoRepository.deleteById(id);
    } else {
        throw new RuntimeException("No se puede eliminar: No existe ningún producto con el ID " + id);
    }
}

    /// Para filtrar productos para diabéticos o alérgicos
    public List<Producto> obtenerPorCategoria(String categoria) {
        log.info("Filtrando catálogo por la categoría de salud: {}", categoria);
        return productoRepository.findByCategoriaSaludIgnoreCase(categoria);
    }

    /// CRUD Completo (Update)
    public Producto actualizarProducto(Long id, Producto datosNuevos) {
        log.info("Iniciando actualización del producto con ID: {}", id);
        return productoRepository.findById(id).map(productoExistente -> {
            productoExistente.setNombre(datosNuevos.getNombre());
            productoExistente.setDescripcion(datosNuevos.getDescripcion());
            productoExistente.setPrecio(datosNuevos.getPrecio());
            productoExistente.setCategoriaSalud(datosNuevos.getCategoriaSalud());
            return productoRepository.save(productoExistente);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado con el ID: " + id));
    }
}