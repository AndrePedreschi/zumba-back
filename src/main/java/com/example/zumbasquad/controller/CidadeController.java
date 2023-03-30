package com.example.zumbasquad.controller;

import com.example.zumbasquad.exceptions.BadRequestException;
import com.example.zumbasquad.model.Cidade;
import com.example.zumbasquad.service.CidadeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cidades")
@CrossOrigin(origins = "*")
public class CidadeController {
    private final CidadeService service;
    final static Logger log = LoggerFactory.getLogger(CidadeController.class);

    public CidadeController(CidadeService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cidade cadastrarCidade(@RequestBody Cidade cidade) throws BadRequestException {
        try {
            log.info("cadastrada nova cidade com sucesso.");
            return service.add(cidade);
        } catch (Exception e) {
            log.error("Não foi possível cadastrar a cidade com base nas informações recebidas.");
            throw new BadRequestException("Não foi possível cadastrar a cidade com base nas informações recebidas.");
        }
    }

    @GetMapping
    public List<Cidade> buscarTodos() {
        return service.getAll();
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<String> processErrorBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
