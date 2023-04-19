package com.example.zumbasquad.controller;

import com.example.zumbasquad.config.JwtAuthenticationFilter;
import com.example.zumbasquad.auth.JwtService;
import com.example.zumbasquad.model.Cidade;
import com.example.zumbasquad.service.CidadeService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CidadeController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class CidadeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CidadeService service;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtService jwtService;

    private List<Cidade> cidades;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        this.cidades = new ArrayList<>();
        this.cidades.add(new Cidade(1L, "nome", "pais", null));
        this.cidades.add(new Cidade(2L, "nome2", "pais2", null));

        this.objectMapper = new ObjectMapper();
    }

    @Test
    void deveBuscarTodasCidades() throws Exception{
        given(service.getAll()).willReturn(cidades);

        this.mockMvc
                .perform(get("/cidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(cidades.size())));
    }

    @Test
    void deveCriarNovaCidade() throws Exception{
        given(service.add(any(Cidade.class))).willAnswer(invocation-> invocation.getArgument(0));

        this.mockMvc
                .perform(post("/cidades")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(cidades.get(0))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(cidades.get(0).getNome())));
    }
}
