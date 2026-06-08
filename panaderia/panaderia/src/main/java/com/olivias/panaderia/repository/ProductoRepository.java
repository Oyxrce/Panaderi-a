package com.olivias.panaderia.repository;

import com.olivias.panaderia.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Permite filtrar en Laragon de manera automática por "Celiacos" o "Diabeticos"
    List<Producto> findByCategoriaSaludIgnoreCase(String categoriaSalud);
}