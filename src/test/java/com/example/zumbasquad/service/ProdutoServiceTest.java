package com.example.zumbasquad.service;

import com.example.zumbasquad.model.Categoria;
import com.example.zumbasquad.model.Cidade;
import com.example.zumbasquad.model.Produto;
import com.example.zumbasquad.repository.IProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
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
public class ProdutoServiceTest {

    @Mock
    private IProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    private Produto produto;
    private List<Produto> produtos = new ArrayList<>();
    private Cidade cidade;
    private Categoria categoria;

    @BeforeEach
    void setup(){
        this.produto = new Produto(1L, "nome", null, true, 2f, 5f, null, null,null , null, cidade, categoria, null);
        this.cidade = new Cidade(1L, "nome", "pais", null);
        this.categoria = new Categoria(1L, "qualificacao", "descricao", "url", null);
    }

    @Test
    void deveSalvarNovoProduto(){
        given(repository.save(produto)).willAnswer(invocation -> invocation.getArgument(0));

        Produto produtoSalvo = service.add(produto);

        assertThat(produtoSalvo).isNotNull();

        verify(repository).save(any(Produto.class));
    }

    @Test
    void deveBuscarTodosProdutos(){
        produtos.add(produto);
        produtos.add(produto);

        given(repository.findAll()).willReturn(produtos);

        List<Produto> produtosEsperados = service.getAll();

        assertEquals(produtosEsperados, produtos);
    }

    @Test
    void deveBuscarProdutoPorId(){
        final Long id = 1L;

        given(repository.findById(id)).willReturn(Optional.of(produto));

        final Optional<Produto> produtoEsperado = Optional.ofNullable(service.getById(id));

        assertThat(produto).isNotNull();
    }

    @Test
    void deveBuscarProdutosPorIdDaCidade(){
        final Long id = 1L;

        produtos.add(new Produto(1L, "nome", null, true, 2f, 5f, null, null, null, null, cidade, categoria, null));

        given(repository.findByCidadeId(id)).willReturn(produtos);

        List<Produto> produtosEsperados = service.getAllProductsByCityId(produtos.get(0).getCidade().getId());

        assertEquals(produtosEsperados, produtos);
    }

    @Test
    void deveBuscarProdutosPorNomeDaCidade(){
        final String nome = "nome";

        produtos.add(new Produto(1L, "nome", null, true, 2f, 5f, null, null,null, null, cidade, categoria, null));

        given(repository.findByCidadeNome(nome)).willReturn(produtos);

        List<Produto> produtosEsperados = service.getAllProductsByCityName(produtos.get(0).getCidade().getNome());

        assertEquals(produtosEsperados, produtos);
    }

    @Test
    void deveBuscarProdutosPorIdDaCategoria(){
        final Long id = 1L;

        produtos.add(new Produto(1L, "nome", null, true, 2f, 5f, null, null, null, null, cidade, categoria, null));

        given(repository.findByCategoriaId(id)).willReturn(produtos);

        List<Produto> produtosEsperados = service.getAllProductsByCategoryId(produtos.get(0).getCategoria().getId());

        assertEquals(produtosEsperados, produtos);
    }

    @Test
    void deveBuscarProdutosPorQualificacaoDaCategoria(){
        final String qualificacao = "qualificacao";

        produtos.add(new Produto(1L, "nome", null, true, 2f, 5f, null, null,null , null, cidade, categoria, null));

        given(repository.findByCategoriaQualificacao(qualificacao)).willReturn(produtos);

        List<Produto> produtosEsperados = service.getAllProductsByCategoryQualification(produtos.get(0).getCategoria().getQualificacao());

        assertEquals(produtosEsperados, produtos);
    }






}
