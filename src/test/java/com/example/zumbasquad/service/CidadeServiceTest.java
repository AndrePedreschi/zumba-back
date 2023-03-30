package com.example.zumbasquad.service;

import com.example.zumbasquad.model.Cidade;
import com.example.zumbasquad.repository.ICidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CidadeServiceTest {

    @Mock
    private ICidadeRepository repository;

    @InjectMocks
    private CidadeService service;

    private Cidade cidade;

    @BeforeEach
    void setup(){
        this.cidade = new Cidade(1L, "nome", "pais", null);
    }

    @Test
    void deveSalvarNovaCidade(){
        given(repository.save(cidade)).willAnswer(invocation -> invocation.getArgument(0));

        Cidade cidadeSalva = service.add(cidade);

        assertThat(cidadeSalva).isNotNull();

        verify(repository).save(any(Cidade.class));
    }

    @Test
    void deveBuscarTodasCidades(){
        List<Cidade> cidades = new ArrayList<>();
        cidades.add(cidade);
        cidades.add(cidade);

        given(repository.findAll()).willReturn(cidades);

        List<Cidade> cidadesEsperadas = service.getAll();

        assertEquals(cidadesEsperadas, cidades);
    }
}
