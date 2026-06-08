package com.olivias.panaderia.controller;

import com.olivias.panaderia.dto.ProductoDTO;
import com.olivias.panaderia.model.Producto;
import com.olivias.panaderia.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController 
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired 
    private ProductoService productoService;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    /// Endpoint para filtrar por categoría
    @GetMapping("/filtrar")
    public ResponseEntity<List<Producto>> filtrarPorSalud(@RequestParam String salud) {
        return ResponseEntity.ok(productoService.obtenerPorCategoria(salud));
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(productoDTO.getNombre());
        nuevoProducto.setDescripcion(productoDTO.getDescripcion());
        nuevoProducto.setPrecio(productoDTO.getPrecio());
        nuevoProducto.setCategoriaSalud(productoDTO.getCategoriaSalud());
     
        Producto guardado = productoService.guardarProducto(nuevoProducto);
        return new ResponseEntity<>(guardado, HttpStatus.CREATED);
    }

    /// Método PUT 
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoDTO productoDTO) {
        Producto datosNuevos = new Producto();
        datosNuevos.setNombre(productoDTO.getNombre());
        datosNuevos.setDescripcion(productoDTO.getDescripcion());
        datosNuevos.setPrecio(productoDTO.getPrecio());
        datosNuevos.setCategoriaSalud(productoDTO.getCategoriaSalud());

        Producto actualizado = productoService.actualizarProducto(id, datosNuevos);
        return ResponseEntity.ok(actualizado);
    }

    /// Metodo para eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
    try {
        productoService.eliminarProducto(id);
        
    /// Retornamos una respuesta JSON limpia y estructurada
        java.util.Map<String, String> respuesta = new java.util.HashMap<>();
        respuesta.put("mensaje", "Producto con ID " + id + " eliminado con éxito.");
        
        return ResponseEntity.ok(respuesta);
    } catch (RuntimeException e) {
        /// Si el id no existe, el GlobalExceptionHandler o este catch atraparán el error
        java.util.Map<String, String> errorResponse = new java.util.HashMap<>();
        errorResponse.put("error", "Error al eliminar");
        errorResponse.put("detalle", e.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}

    @GetMapping("/remoto/verificar") 
    public ResponseEntity<String> verificarRestriccionRemota(@RequestParam String ingrediente) {
        String urlExterna = "https://api.duckduckgo.com/?q=" + ingrediente + "&format=json";
        try {
            String respuesta = restTemplate.getForObject(urlExterna, String.class);
            System.out.println("Datos remotos recibidos con éxito: " + respuesta.substring(0, Math.min(respuesta.length(), 50)) + "...");
            return ResponseEntity.ok("Validación externa completada para: " + ingrediente + ". Conexión remota exitosa.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Error en la conexión con el servidor remoto.");
        }
    }
}