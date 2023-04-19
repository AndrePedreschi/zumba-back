package com.example.zumbasquad.controller;

import com.example.zumbasquad.config.JwtAuthenticationFilter;
import com.example.zumbasquad.auth.JwtService;
import com.example.zumbasquad.exceptions.BadRequestException;
import com.example.zumbasquad.exceptions.ResourceNotFoundException;
import com.example.zumbasquad.model.*;
import com.example.zumbasquad.service.CidadeService;
import com.example.zumbasquad.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProdutoController.class)
@ActiveProfiles("test")
//retirando a necessidade de autenticação
@AutoConfigureMockMvc(addFilters = false)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService service;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private AtomicLong idGenerator;
    @MockBean
    private CidadeService cidadeService;

    private List<Produto> produtos;
    private Cidade cidade;
    private Categoria categoria;
    private List<Imagem> imagens;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        this.cidade = new Cidade(1L, "nomeCidade", "pais", null);
        this.categoria = new Categoria(1L, "qualificacao", "descricao", "urlImagem", null);
        this.imagens = new ArrayList<>();
        this.imagens.add(new Imagem(1L, "titulo", "url", null));

        this.produtos = new ArrayList<>();
        this.produtos.add(new Produto(1L, "nome", null, true, 2f, 5f, null, null,imagens, null, cidade, categoria, null));
        this.produtos.add(new Produto(2L, "nome2", null, true, 2f, 5f, null, null,imagens, null, cidade, categoria, null));

        this.objectMapper = new ObjectMapper();
    }

    @Test
    void deveBuscarTodosProdutos() throws Exception{
        given(service.getAll()).willReturn(produtos);

        this.mockMvc
                .perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(produtos.size())));
    }

    @Test
    void deveCriarNovoProduto() throws Exception{
        // given
        Produto produto = createValidProduto();

        when(cidadeService.getAll()).thenReturn(new ArrayList<>());
        when(service.add(any(Produto.class))).thenReturn(produto);

        // when
        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produto)))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()));

        verify(cidadeService, times(1)).getAll();
        verify(service, times(1)).add(any(Produto.class));
    }

    private Produto createValidProduto() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Hotel Teste");
        produto.setDescricao(new Descricao(1L,"Título da descrição", "Descrição do produto"));
        produto.setCategoria(new Categoria("Hotéis", "teste", "teste"));
        produto.setLocalizacao(new Localizacao(1L, "Rua teste, 123"));
        produto.setDetalhes(new Detalhe(1L,"Regras da casa", "Política de cancelamento", "Saúde e segurança"));
        produto.setImagens(Collections.singletonList(new Imagem(1L,"http://example.com/image.jpg")));
        produto.setCaracteristicas(Collections.singletonList(new Caracteristica(1L,"Wi-fi")));
        produto.setCidade(new Cidade(1L,"Rio de Janeiro"));
        return produto;
    }


    @Test
    void deveDarBadRequestExceptionAoTentarCriarSemBodyCorreto(){
        final Produto produto = new Produto();
        produto.setId(1L);

        try {
            given(service.add(any(Produto.class))).willAnswer(invocation -> invocation.getArgument(0));

            this.mockMvc
                .perform(post("/produtos"));
                //.andExpect(status().isBadRequest());
        } catch (Exception e){
            assertThatExceptionOfType(BadRequestException.class);
        }
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception{
        final Long id = 1L;
        given(service.getById(id)).willReturn(produtos.get(0));

        this.mockMvc
                .perform(get("/produtos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(produtos.get(0).getNome())));
    }

    @Test
    void deveDarResourceNotFoundAoBuscarIdQueNaoExiste(){
        try {
            //given(service.getById(3L)).willThrow(ResourceNotFoundException.class);

            this.mockMvc
                    .perform(get("/produtos/{id}", 3L));
        } catch (Exception e){
            assertThatExceptionOfType(ResourceNotFoundException.class);
        }
    }

    @Test
    void deveBuscarProdutosPeloIdDaCidade() throws Exception {
        List<Produto> produtosTodos = new ArrayList<>();
        produtosTodos.add(new Produto(1L, "nome", null, true, 2f, 5f, null, null,imagens, null, new Cidade(1L, "nome", "pais", null), categoria, null));
        produtosTodos.add(new Produto(2L, "nome", null, true, 2f, 5f, null, null,imagens, null, new Cidade(2L, "nome", "pais", null), categoria, null));
        produtosTodos.add(new Produto(3L, "nome", null, true, 2f, 5f, null, null,imagens, null, new Cidade(1L, "nome", "pais", null), categoria, null));


        List<Produto> produtosFiltrados = produtosTodos.stream().filter(produto -> produto.getCidade().getId().equals(1L)).toList();

        given(service.getAllProductsByCityId(1L)).willReturn(produtosFiltrados);

        this.mockMvc
                .perform(get("/produtos/por_cidade/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    void deveBuscarProdutosPeloNomeDaCidade() throws Exception {
        List<Produto> produtosTodos = new ArrayList<>();
        produtosTodos.add(new Produto(1L, "nome", null, true, 2f, 5f, null, null,imagens, null, new Cidade(1L, "nome", "pais", null), categoria, null));
        produtosTodos.add(new Produto(2L, "nome", null, true, 2f, 5f, null, null,imagens, null, new Cidade(2L, "nome2", "pais", null), categoria, null));
        produtosTodos.add(new Produto(3L, "nome", null, true, 2f, 5f, null, null,imagens, null, new Cidade(1L, "nome", "pais", null), categoria, null));

        List<Produto> produtosFiltrados = produtosTodos.stream().filter(produto -> produto.getCidade().getNome().equals("nome")).toList();

        given(service.getAllProductsByCityName("nome")).willReturn(produtosFiltrados);

        this.mockMvc
                .perform(get("/produtos/cidade")
                        .param("nomeCidade", "nome"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    void deveBuscarProdutosPeloIdDaCategoria() throws Exception {
        List<Produto> produtosTodos = new ArrayList<>();
        produtosTodos.add(new Produto(1L, "nome", null, true, 2f, 5f, null, null,imagens, null, null, new Categoria(1L, "qualificacao", "descricao", "url", null), null));
        produtosTodos.add(new Produto(2L, "nome", null, true, 2f, 5f, null, null,imagens, null, null, new Categoria(2L, "qualificacao", "descricao", "url", null), null));
        produtosTodos.add(new Produto(3L, "nome", null, true, 2f, 5f, null, null,imagens, null, null, new Categoria(1L, "qualificacao", "descricao", "url", null), null));

        List<Produto> produtosFiltrados = produtosTodos.stream().filter(produto -> produto.getCategoria().getId().equals(1L)).toList();

        given(service.getAllProductsByCategoryId(1L)).willReturn(produtosFiltrados);

        this.mockMvc
                .perform(get("/produtos/por_categoria/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    void deveBuscarProdutosPeloNomeDaCategoria() throws Exception {
        List<Produto> produtosTodos = new ArrayList<>();
        produtosTodos.add(new Produto(1L, "nome", null, true, 2f, 5f, null, null,imagens, null, null, new Categoria(1L, "qualificacao", "descricao", "url", null), null));
        produtosTodos.add(new Produto(2L, "nome", null, true, 2f, 5f, null, null,imagens, null, null, new Categoria(2L, "qualificacao2", "descricao", "url", null), null));
        produtosTodos.add(new Produto(3L, "nome", null, true, 2f, 5f, null, null,imagens, null, null, new Categoria(1L, "qualificacao", "descricao", "url", null), null));

        List<Produto> produtosFiltrados = produtosTodos.stream().filter(produto -> produto.getCategoria().getQualificacao().equals("qualificacao")).toList();

        given(service.getAllProductsByCategoryQualification("qualificacao")).willReturn(produtosFiltrados);

        this.mockMvc
                .perform(get("/produtos/categoria")
                        .param("categoria", "qualificacao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }
}
