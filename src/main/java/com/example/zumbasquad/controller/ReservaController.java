package com.example.zumbasquad.controller;

import com.example.zumbasquad.exceptions.BadRequestException;
import com.example.zumbasquad.exceptions.ResourceNotFoundException;
import com.example.zumbasquad.model.Produto;
import com.example.zumbasquad.model.Reserva;
import com.example.zumbasquad.service.ProdutoService;
import com.example.zumbasquad.service.ReservaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {
    private final ReservaService service;
    final static Logger log = LoggerFactory.getLogger(ProdutoController.class);

    public ReservaController(ReservaService service) { this.service = service; }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reserva cadastrarReserva(@RequestBody Reserva reserva) throws BadRequestException {
        try {
            log.info("Cadastrado nova reserva com sucesso.");
            return service.add(reserva);
        } catch (Exception e) {
            log.info("Não foi possível cadastrar a reserva com base nas informações recebidas.");
            throw new BadRequestException("Não foi possível cadastrar a reserva com base nas informações recebidas.");
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/por_produto/{id}")
    public List<Reserva> buscarReservaPorProdutoId(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            log.info("Encontrado a lista de produtos solicitada.");
            return service.getReservaByProdutoId(id);
        } catch (Exception e) {
            log.info("Não foi encontrado a lista de reservas solicitada.");
            throw new ResourceNotFoundException("Não foi encontrado a lista de reservas solicitada.");
        }
    }
}
