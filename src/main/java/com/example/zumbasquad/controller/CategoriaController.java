package com.example.zumbasquad.controller;

import com.example.zumbasquad.exceptions.BadRequestException;
import com.example.zumbasquad.exceptions.ResourceNotFoundException;
import com.example.zumbasquad.model.Categoria;
import com.example.zumbasquad.service.CategoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaService service;

    final static Logger log = LoggerFactory.getLogger(CategoriaController.class);

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria cadastrarCategoria(@RequestBody Categoria categoria) throws BadRequestException {
        try {
            log.info("Cadastrado nova categoria com sucesso.");
            return service.add(categoria);
        } catch (Exception e) {
            log.error("Não foi possível cadastrar a categoria com base nas informações recebidas.");
            throw  new BadRequestException("Não foi possível cadastrar a categoria.");
        }
    }

    @GetMapping
    public List<Categoria> buscarTodos() { return service.getAll(); }

    @PutMapping("/atualizar")
    public ResponseEntity<Categoria> alterarCategoria(@RequestBody Categoria categoria) throws BadRequestException {
        try {
            service.getById(categoria.getId());
            service.update(categoria);
            log.info("A categoria com id: " + categoria.getId() + " foi atualizada com sucesso.");
            return ResponseEntity.ok(service.update(categoria));
        } catch (Exception e) {
            log.error("Não foi possível atualizar a categoria.");
            throw new BadRequestException("Não foi possível atualizar os dados da categoria");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirCategoria(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            service.remove(id);
            log.info("A categoria de id: " + id + " foi localizada no banco de dados e removida com sucesso.");
            return ResponseEntity.ok("Categoria excluída.");
        } catch (Exception e) {
            log.error("Não foi encontrado a categoria de id " + id + " para efetuar a exclusão.");
            throw new ResourceNotFoundException("Não foi possível excluir a categoria de id: " + id);
        }
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<String> processErrorBadRequest(BadRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
