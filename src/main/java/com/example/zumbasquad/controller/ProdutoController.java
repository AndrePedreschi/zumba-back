package com.example.zumbasquad.controller;

import com.example.zumbasquad.exceptions.BadRequestException;
import com.example.zumbasquad.exceptions.ResourceNotFoundException;
import com.example.zumbasquad.model.Categoria;
import com.example.zumbasquad.model.Cidade;
import com.example.zumbasquad.model.Produto;
import com.example.zumbasquad.service.ProdutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {
    private final ProdutoService service;
    final static Logger log = LoggerFactory.getLogger(ProdutoController.class);

    public ProdutoController(ProdutoService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto cadastrarProduto(@RequestBody Produto produto) throws BadRequestException {
        try {
            log.info("Cadastrado novo produto com sucesso.");
            return  service.add(produto);
        } catch (Exception e) {
            log.error("Não foi possível cadastrar o produto com base nas informações recebidas.");
            throw new BadRequestException("Não foi possível cadastrar o produto com base nas informações recebidas.");
        }
    }

    @GetMapping
    public List<Produto> buscarTodos() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoId(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            service.getById(id);
            log.info("Encontrado o produto solicitado de id: " + id);
            return ResponseEntity.ok(service.getById(id));
        } catch (Exception e) {
            log.error("Não foi encontrado o produto solicitado de id: " + id);
            throw new ResourceNotFoundException("Não foi encontrado o produto solicitado de id: " + id);
        }
    }

    @GetMapping("/por_cidade/{id}")
    public List<Produto> buscarProdutosPorCidadeId(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            log.info("Encontrado a lista de produtos solicitado");
            return service.getAllProductsByCityId(id);
        } catch (Exception e) {
            log.error("Não foi encontrado a lista de produtos solicitado");
            throw new ResourceNotFoundException("Não foi encontrado a lista de produtos solicitado.");
        }
    }

    @GetMapping("/cidade")
    public List<Produto> buscarProdutosPorCidade(@RequestParam String nomeCidade) throws ResourceNotFoundException {
        try {
            log.info("Encontrado a lista de produtos solicitado");
            return service.getAllProductsByCityName(nomeCidade);
        } catch (Exception e) {
            log.error("Não foi encontrado a lista de produtos solicitado");
            throw new ResourceNotFoundException("Não foi encontrado a lista de produtos solicitado.");
        }
    }

    @GetMapping("/por_categoria/{id}")
    public List<Produto> buscarProdutosPorCategoriaId(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            log.info("Encontrado a lista de produtos solicitado");
            return service.getAllProductsByCategoryId(id);
        } catch (Exception e) {
            log.error("Não foi encontrado a lista de produtos solicitado");
            throw new ResourceNotFoundException("Não foi encontrado a lista de produtos solicitado.");
        }
    }

    @GetMapping("/categoria")
    public List<Produto> buscarProdutosPorCategoria(@RequestParam String categoria) throws ResourceNotFoundException {
        try {
            log.info("Encontrado a lista de produtos solicitado");
            return service.getAllProductsByCategoryQualification(categoria);
        } catch (Exception e) {
            log.error("Não foi encontrado a lista de produtos solicitado");
            throw new ResourceNotFoundException("Não foi encontrado a lista de produtos solicitado.");
        }
    }

    @GetMapping("/datas")
    public List<Produto> buscarProdutosPorDatas(@RequestParam LocalDate dataInicial, @RequestParam LocalDate dataFinal) throws ResourceNotFoundException{
        try {
            log.info("Encontrado a lista de produtos solicitado");
            return service.getAllProductsByDates(dataInicial, dataFinal);
        } catch (Exception e) {
            log.error("Não foi encontrado a lista de produtos solicitado");
            throw new ResourceNotFoundException("Não foi encontrado a lista de produtos solicitado.");
        }
    }

    @GetMapping("/search")
    public List<Produto> buscarProdutosPorDatasOuCidade(@RequestParam(required = false) LocalDate dataInicial,
                                                        @RequestParam(required = false) LocalDate dataFinal,
                                                        @RequestParam(required = false) String cidade) throws ResourceNotFoundException{
        try {
            log.info("Encontrado a lista de produtos solicitado");
            return service.getAllProductsByDatesOrCity(dataInicial, dataFinal, cidade);
        } catch (Exception e) {
            log.error("Não foi encontrado a lista de produtos solicitado");
            throw new ResourceNotFoundException("Não foi encontrado a lista de produtos solicitado.");
        }
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<String> processErrorBadRequest(BadRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
