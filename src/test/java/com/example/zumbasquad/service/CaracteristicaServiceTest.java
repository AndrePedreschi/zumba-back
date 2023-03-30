package com.example.zumbasquad.service;

import com.example.zumbasquad.model.Caracteristica;
import com.example.zumbasquad.repository.ICaracteristicaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CaracteristicaServiceTest {

    @Mock
    private ICaracteristicaRepository repository;

    @InjectMocks
    private CaracteristicaService service;

    @Test
    void deveSalvarNovaCaracteristica(){
        final Caracteristica caracteristica = new Caracteristica(1L, "nome", "icone", null);

        given(repository.save(caracteristica)).willAnswer(invocation -> invocation.getArgument(0));

        Caracteristica caracteristicaSalva = service.add(caracteristica);

        assertThat(caracteristicaSalva).isNotNull();

        verify(repository).save(any(Caracteristica.class));
    }

    @Test
    void deveBuscarTodasCaracteristicas(){
        List<Caracteristica> caracteristicas = new ArrayList<>();
        caracteristicas.add(new Caracteristica(1L, "nome", "icone", null));
        caracteristicas.add(new Caracteristica(2L, "nome2", "icone2", null));

        given(repository.findAll()).willReturn(caracteristicas);

        List<Caracteristica> caracteristicasEsperadas = service.getAll();

        assertEquals(caracteristicasEsperadas, caracteristicas);
    }

    @Test
    void deveBuscarCaracteristicaPorIdExistente(){
        final Long id = 1L;
        final Caracteristica caracteristica = new Caracteristica(1L, "nome", "icone", null);

        given(repository.findById(id)).willReturn(Optional.of(caracteristica));

        final Optional<Caracteristica> caracteristicaEsperada = Optional.ofNullable(service.getById(id));

        assertThat(caracteristicaEsperada).isNotNull();
    }
}
