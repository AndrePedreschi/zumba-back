package com.example.zumbasquad.controller;

import com.example.zumbasquad.config.JwtAuthenticationFilter;
import com.example.zumbasquad.config.JwtService;
import com.example.zumbasquad.exceptions.BadRequestException;
import com.example.zumbasquad.model.Caracteristica;
import com.example.zumbasquad.model.Categoria;
import com.example.zumbasquad.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.error.ShouldBeBlank;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest(controllers = CategoriaController.class)
@ActiveProfiles("test")
//retirando a necessidade de autenticação
@AutoConfigureMockMvc(addFilters = false)
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService service;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtService jwtService;

    private List<Categoria> categorias;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        this.categorias = new ArrayList<>();
        this.categorias.add(new Categoria(1L, "teste1", "desc1", "url1", null));
        this.categorias.add(new Categoria(2L, "teste2", "desc2", "url2", null));
        this.categorias.add(new Categoria(3L, "teste3", "desc3", "url3", null));

        this.objectMapper = new ObjectMapper();
    }

    @Test
    void deveBuscarTodasCategorias() throws Exception{
        given(service.getAll()).willReturn(categorias);

        this.mockMvc
                .perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(categorias.size())));
    }

    @Test
    void deveCriarNovaCategoria() throws Exception{
        given(service.add(any(Categoria.class))).willAnswer((invocation) -> invocation.getArgument(0));

        Categoria categoria = new Categoria(null, "teste", "desc", "url", null);

        this.mockMvc
                .perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.qualificacao", is(categoria.getQualificacao())));
    }

    @Test
    void deveRetornarBadRequestAoCriarComBodyErrado() throws Exception {
        String categoria = new JSONObject()
                .put("id", 1L)
                .toString();

        this.mockMvc
                .perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isBadRequest());
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
//                .andExpect(result -> assertEquals("Não foi possível cadastrar a categoria.", result.getResolvedException().getMessage()));
    }

    @Test
    void deveExcluirCategoria() throws Exception{
        Long id = 1L;
        Categoria categoria = new Categoria(1L, "teste1", "desc1", "url1", null);

        given(service.getById(id)).willReturn(categoria);
        doNothing().when(service).remove(categoria.getId());

        this.mockMvc
                .perform(delete("/categorias/{id}", categoria.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void deveAtualizarCategoria() throws Exception{
        categorias.get(0).setQualificacao("teste update");

        given(service.update(categorias.get(0))).willReturn(categorias.get(0));

        mockMvc.
                perform(put("/categorias/atualizar")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categorias.get(0))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.qualificacao", is(categorias.get(0).getQualificacao())));
    }

}
