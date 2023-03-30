package com.example.zumbasquad.controller;

import com.example.zumbasquad.config.JwtAuthenticationFilter;
import com.example.zumbasquad.config.JwtService;
import com.example.zumbasquad.model.Produto;
import com.example.zumbasquad.model.Reserva;
import com.example.zumbasquad.service.ReservaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReservaController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService service;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtService jwtService;

    private List<Reserva> reservas;
    private Produto produto;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        this.produto = new Produto();
        produto.setId(1L);

        this.reservas = new ArrayList<>();
        this.reservas.add(new Reserva(1L, null, null, null, produto, null));
        this.reservas.add(new Reserva(2L, null, null, null, produto, null));

        this.objectMapper = new ObjectMapper();
    }

    @Test
    void deveCriarNovaReserva() throws Exception{
        given(service.add(any(Reserva.class))).willAnswer(invocation -> invocation.getArgument(0));

        this.mockMvc
                .perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(reservas.get(0))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.is(reservas.get(0).getId()), Long.class));
    }

    @Test
    void deveBuscarReservasPeloIdDoProduto() throws Exception{
        List<Reserva> reservasFiltradas = reservas.stream().filter(reserva -> reserva.getProduto().getId().equals(1L)).toList();

        given(service.getReservaByProdutoId(1L)).willReturn(reservasFiltradas);

        this.mockMvc
                .perform(get("/reservas/por_produto/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(2)));
    }

}
