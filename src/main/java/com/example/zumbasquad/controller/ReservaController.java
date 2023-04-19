package com.example.zumbasquad.controller;

import com.example.zumbasquad.enums.EnumPapel;
import com.example.zumbasquad.exceptions.BadRequestException;
import com.example.zumbasquad.exceptions.ResourceNotFoundException;
import com.example.zumbasquad.model.Reserva;
import com.example.zumbasquad.repository.IUsuarioRepository;
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

    public ReservaController(ReservaService service, IUsuarioRepository usuarioRepository) { this.service = service;
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody()
    public Reserva cadastrarReserva(@RequestBody Reserva reserva) throws BadRequestException {
        try {
            if (reserva.getUsuario().getPapel() == null){
                reserva.getUsuario().setPapel(EnumPapel.USER);
            }
            log.info("Cadastrado nova reserva com sucesso.");
            return service.add(reserva);
        } catch (Exception e) {
            log.info("Não foi possível cadastrar a reserva com base nas informações recebidas.");
            throw new BadRequestException("Não foi possível cadastrar a reserva com base nas informações recebidas.");
        }
    }

//    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @GetMapping("/por_produto/{id}")
    public List<Reserva> buscarReservaPorProdutoId(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            log.info("Encontrado a lista de reservas solicitada.");
            return service.getReservaByProdutoId(id);
        } catch (Exception e) {
            log.info("Não foi encontrado a lista de reservas solicitada.");
            throw new ResourceNotFoundException("Não foi encontrado a lista de reservas solicitada.");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @GetMapping("/usuario/{id}")
    public List<Reserva> buscarReservaPorUsuarioId(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            log.info("Encontrado a lista de reservas solicitada.");
            return service.getReservaByUsuarioId(id);
        } catch (Exception e) {
            log.info("Não foi encontrado a lista de reservas solicitada.");
            throw new ResourceNotFoundException("Não foi encontrado a lista de reservas solicitada.");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirReserva(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            service.delete(id);
            log.info("A reserva de id: " + id + " foi localizada no banco de dados e removida com sucesso.");
            return ResponseEntity.ok("Reserva excluída.");
        } catch (Exception e) {
            log.error("Não foi encontrado a reserva de id " + id + " para efetuar a exclusão.");
            throw new ResourceNotFoundException("Não foi possível excluir a reserva de id: " + id);
        }
    }
}
