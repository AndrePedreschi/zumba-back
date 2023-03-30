package com.example.zumbasquad.controller;

import com.example.zumbasquad.exceptions.BadRequestException;
import com.example.zumbasquad.model.Caracteristica;
import com.example.zumbasquad.model.Cidade;
import com.example.zumbasquad.service.CaracteristicaService;
import com.example.zumbasquad.service.CidadeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/caracteristicas")
@CrossOrigin(origins = "*")
public class CaracteristicaController {
    private final CaracteristicaService service;
    final static Logger log = LoggerFactory.getLogger(CidadeController.class);

    public CaracteristicaController(CaracteristicaService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Caracteristica cadastrarCaracteristica(@RequestBody Caracteristica caracteristica) throws BadRequestException {
        try {
            log.info("cadastrada nova característica com sucesso.");
            return service.add(caracteristica);
        } catch (Exception e) {
            log.error("Não foi possível cadastrar a característica com base nas informações recebidas.");
            throw new BadRequestException("Não foi possível cadastrar a característica com base nas informações recebidas.");
        }
    }

    @GetMapping
    public List<Caracteristica> buscarTodos() {
        return service.getAll();
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<String> processErrorBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
