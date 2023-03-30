package com.example.zumbasquad.service;

import com.example.zumbasquad.model.Categoria;
import com.example.zumbasquad.model.Produto;
import com.example.zumbasquad.repository.ICategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private ICategoriaRepository repository;

    @InjectMocks
    private CategoriaService service;

    @Test
    void deveSalvarNovaCategoriaSemProduto(){
        final Categoria categoria = new Categoria(1L, "teste", "descricao", "url", null);

        //given(repository.findById(categoria.getId())).willReturn(Optional.empty());
        given(repository.save(categoria)).willAnswer(invocation -> invocation.getArgument(0));

        Categoria categoriaSalva = service.add(categoria);

        assertThat(categoriaSalva).isNotNull();

        verify(repository).save(any(Categoria.class));
    }

    @Test
    void deveSalvarNovaCategoriaComProduto(){
        final Set<Produto> listaProdutos = new HashSet<>();
        listaProdutos.add(new Produto());
        listaProdutos.add(new Produto());

        final Categoria categoria = new Categoria(1L, "teste", "descricao", "url", listaProdutos);

        given(repository.save(categoria)).willAnswer(invocation -> invocation.getArgument(0));

        Categoria categoriaSalva = service.add(categoria);

        assertThat(categoriaSalva).isNotNull();

        verify(repository).save(any(Categoria.class));
    }

    @Test
    void deveAtualizarCategoria(){
        final Categoria categoria = new Categoria(1L, "teste", "descricao", "url", null);
        final Categoria categoriaAtualizada = new Categoria(1L, "teste atualizado", "descricao", "url", null);

        //doReturn(categoria).when(repository).findById(isA(Long.class));
        doAnswer(AdditionalAnswers.returnsFirstArg()).when(repository).saveAndFlush(isA(Categoria.class));

        Categoria categoriaNova = service.update(categoriaAtualizada);

        assertEquals(categoriaAtualizada, categoriaNova);
    }

    @Test
    void deveBuscarTodasCategorias(){
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(new Categoria(1L, "teste1", "desc1", "url1", null));
        categorias.add(new Categoria(2L, "teste2", "desc2", "url2", null));
        categorias.add(new Categoria(3L, "teste3", "desc3", "url3", null));

        given(repository.findAll()).willReturn(categorias);

        List<Categoria> categoriasEsperadas = service.getAll();

        assertEquals(categoriasEsperadas, categorias);
    }

    @Test
    void deveBuscarCategoriaPorIdExistente(){
        final Long id = 1L;
        final Categoria categoria = new Categoria(1L, "teste", "descricao", "url", null);

        given(repository.findById(id)).willReturn(Optional.of(categoria));

        final Optional<Categoria> categoriaEsperada = Optional.ofNullable(service.getById(id));

        assertThat(categoria).isNotNull();
    }

    @Test
    void deveExcluirCategoriaExistente(){
        final Long id = 1L;

        service.remove(id);

        verify(repository, times(1)).deleteById(id);
    }
}
