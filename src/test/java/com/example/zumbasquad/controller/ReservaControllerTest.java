package com.example.zumbasquad.controller;

import com.example.zumbasquad.config.JwtAuthenticationFilter;
import com.example.zumbasquad.auth.JwtService;
import com.example.zumbasquad.enums.EnumPapel;
import com.example.zumbasquad.model.Produto;
import com.example.zumbasquad.model.Reserva;
import com.example.zumbasquad.model.Usuario;
import com.example.zumbasquad.repository.IUsuarioRepository;
import com.example.zumbasquad.service.ReservaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @MockBean
    IUsuarioRepository usuarioRepository;

    private List<Reserva> reservas;
    private Produto produto;
    private Usuario usuario;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        this.produto = new Produto();
        produto.setId(1L);

        this.usuario = new Usuario();
        usuario.setId(1L);
        usuario.setPapel(EnumPapel.USER);

        this.reservas = new ArrayList<>();
        this.reservas.add(new Reserva(1L, null, null, null, produto, usuario));
        this.reservas.add(new Reserva(2L, null, null, null, produto, usuario));

        this.objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    @Disabled
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
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void deveBuscarReservasPeloIdDoProduto() throws Exception{
        List<Reserva> reservasFiltradas = reservas.stream().filter(reserva -> reserva.getProduto().getId().equals(1L)).toList();

        given(service.getReservaByProdutoId(1L)).willReturn(reservasFiltradas);

        this.mockMvc
                .perform(get("/reservas/por_produto/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(2)));
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void deveBuscarReservasPeloIdDoUsuario() throws Exception{
        List<Reserva> reservasFiltradas = reservas.stream().filter(reserva -> reserva.getUsuario().getId().equals(1L)).toList();

        given(service.getReservaByUsuarioId(1L)).willReturn(reservasFiltradas);

        this.mockMvc
                .perform(get("/reservas/usuario/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(2)));
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void deveExcluirReservaComSucesso() throws Exception {
        // Given
        Long reservaId = 1L;

        // When
        doNothing().when(service).delete(reservaId);

        // Then
        mockMvc.perform(delete("/reservas/" + reservaId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).delete(reservaId);
    }
}
