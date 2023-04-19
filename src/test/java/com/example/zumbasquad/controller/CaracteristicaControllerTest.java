package com.example.zumbasquad.controller;

import com.example.zumbasquad.config.JwtAuthenticationFilter;
import com.example.zumbasquad.auth.JwtService;
import com.example.zumbasquad.exceptions.BadRequestException;
import com.example.zumbasquad.model.Caracteristica;
import com.example.zumbasquad.service.CaracteristicaService;
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

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CaracteristicaController.class)
@ActiveProfiles("test")
//retirando a necessidade de autenticação
@AutoConfigureMockMvc(addFilters = false)
public class CaracteristicaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaracteristicaService service;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtService jwtService;

    private List<Caracteristica> caracteristicas;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        this.caracteristicas = new ArrayList<>();
        this.caracteristicas.add(new Caracteristica(1L, "nome", "icone", null));
        this.caracteristicas.add(new Caracteristica(2L, "nome2", "icone2", null));

        this.objectMapper = new ObjectMapper();
    }

    @Test
    void deveBuscarTodasCaracteristicas() throws Exception{
        given(service.getAll()).willReturn(caracteristicas);

        this.mockMvc
                .perform(get("/caracteristicas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(caracteristicas.size())));
    }

    @Test
    void deveCriarNovaCaracteristica() throws Exception{
        given(service.add(any(Caracteristica.class))).willAnswer(invocation -> invocation.getArgument(0));

        this.mockMvc
                .perform(post("/caracteristicas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(caracteristicas.get(0))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(caracteristicas.get(0).getNome())));

    }

    @Test
    void deveDarBadRequestExceptionAoTentarCriarSemBodyCorreto(){
        final Caracteristica caracteristica = new Caracteristica();
        caracteristica.setId(1L);

        try {
            given(service.add(any(Caracteristica.class))).willAnswer(invocation -> invocation.getArgument(0));

            this.mockMvc
                    .perform(post("/caracteristicas"));
        } catch (Exception e){
            assertThatExceptionOfType(BadRequestException.class);
            assertEquals("Não foi possível cadastrar a característica com base nas informações recebidas.", e.getMessage());
        }
    }
}
