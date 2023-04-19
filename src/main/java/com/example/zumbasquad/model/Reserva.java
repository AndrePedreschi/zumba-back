package com.example.zumbasquad.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="HH:mm:ss")
    private LocalTime hora;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate dataInicial;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate dataFinal;
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
//    @ManyToOne
//    @JoinColumn(name = "cliente_id")
//    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Reserva(LocalTime hora, LocalDate dataInicial, LocalDate dataFinal, Produto produto, Usuario usuario) {
        this.hora = hora;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.produto = produto;
        this.usuario = usuario;
    }
}
