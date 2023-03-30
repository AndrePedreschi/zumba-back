package com.example.zumbasquad.service;

import com.example.zumbasquad.model.Produto;
import com.example.zumbasquad.model.Reserva;
import com.example.zumbasquad.repository.IReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private IReservaRepository repository;

    @InjectMocks
    private ReservaService service;

    private Reserva reserva;

    @BeforeEach
    void setup(){
        LocalTime hora = LocalTime.of(7, 0,0,0);
        LocalDate dataInicial = LocalDate.of(2023, 6, 3);
        LocalDate dataFinal = LocalDate.of(2023, 6, 6);
        this.reserva = new Reserva(1L, hora, dataInicial, dataFinal, null, null);
    }

    @Test
    void deveSalvarNovaReserva(){
        given(repository.save(reserva)).willAnswer(invocation -> invocation.getArgument(0));

        Reserva reservaSalva = service.add(reserva);

        assertThat(reservaSalva).isNotNull();

        verify(repository).save(any(Reserva.class));
    }

    @Test
    void deveBuscarReservaPorIdDoProduto(){
        final Long id = 1L;

        Produto produto = new Produto();
        produto.setId(1L);

        reserva.setProduto(produto);

        given(repository.findByProdutoId(id)).willReturn(Collections.singletonList(reserva));

        List<Reserva> reservasEsperadas = service.getReservaByProdutoId(reserva.getProduto().getId());

        assertEquals(reservasEsperadas, Collections.singletonList(reserva));
    }
}
