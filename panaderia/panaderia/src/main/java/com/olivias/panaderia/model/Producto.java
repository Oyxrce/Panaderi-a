package com.olivias.panaderia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La descripción de ingredientes y alérgenos es obligatoria")
    @Column(nullable = false)
    private String descripcion;

    @NotNull(message = "El precio no puede ser nulo")
    @Min(value = 1, message = "El precio debe ser un valor positivo mayor a 0")
    @Column(nullable = false)
    private Double precio;

    @NotBlank(message = "La categoría de salud es obligatoria (ej: Celiacos, Diabeticos)")
    @Column(name = "categoria_salud")
    private String categoriaSalud;
}