package com.example.zumbasquad.controller;

import com.example.zumbasquad.exceptions.BadRequestException;
import com.example.zumbasquad.exceptions.ResourceNotFoundException;
import com.example.zumbasquad.model.*;
import com.example.zumbasquad.service.CidadeService;
import com.example.zumbasquad.service.ProdutoService;
import com.fasterxml.jackson.annotation.JsonMerge;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {
    private final ProdutoService service;

    @Autowired
    private AtomicLong idGenerator;
    @Autowired
    private CidadeService cidadeService;

    final static Logger log = LoggerFactory.getLogger(ProdutoController.class);

    public ProdutoController(ProdutoService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public Produto cadastrarProduto(@RequestBody @JsonMerge Produto produto) throws BadRequestException {
        try {
            if (produto.getCategoria().getQualificacao().equalsIgnoreCase("Hotéis")){
                produto.getCategoria().setId(1L);
            } else if (produto.getCategoria().getQualificacao().equalsIgnoreCase("Hostels")){
                produto.getCategoria().setId(2L);
            } else if (produto.getCategoria().getQualificacao().equalsIgnoreCase("Apartamentos")){
                produto.getCategoria().setId(3L);
            } else if (produto.getCategoria().getQualificacao().equalsIgnoreCase("Pousadas")){
                produto.getCategoria().setId(4L);
            } else {
                throw new BadRequestException("Categoria não existe!");
            }

            if (produto.getDescricao() != null && produto.getDescricao().getId() == null){
                Descricao descricao = new Descricao(idGenerator.incrementAndGet(), produto.getDescricao().getTitulo(), produto.getDescricao().getTexto());
                produto.setDescricao(descricao);
            }

            if (produto.getLocalizacao() != null && produto.getLocalizacao().getId() == null){
                Localizacao localizacao = new Localizacao(idGenerator.incrementAndGet(), produto.getLocalizacao().getEndereco(), produto.getLocalizacao().getDistancia(), produto.getLocalizacao().getCoordenadas(), produto.getLocalizacao().getCentro());
                produto.setLocalizacao(localizacao);
            }

            if (produto.getDetalhes() != null && produto.getDetalhes().getId() == null){
                Detalhe detalhe = new Detalhe(idGenerator.incrementAndGet(), produto.getDetalhes().getRegrasDaCasa(), produto.getDetalhes().getPoliticaDeCancelamento(), produto.getDetalhes().getSaudeESeguranca());
                produto.setDetalhes(detalhe);
            }

            List<Imagem> imagens = new ArrayList<>();
            if (produto.getImagens() != null){
                for (Imagem imagem : produto.getImagens()){
                    if (imagem.getId() == null){
                        Imagem novaImagem = new Imagem(idGenerator.incrementAndGet(), imagem.getUrl(), produto);
                        imagens.add(novaImagem);
                    }
                }
            }
            produto.setImagens(imagens);

            for (Caracteristica caracteristica : produto.getCaracteristicas()){
                if (caracteristica.getNome().equalsIgnoreCase("Wi-fi")){
                    caracteristica.setId(1L);
                } else if (caracteristica.getNome().equalsIgnoreCase("Piscina")){
                    caracteristica.setId(2L);
                } else if (caracteristica.getNome().equalsIgnoreCase("Pets")){
                    caracteristica.setId(3L);
                } else if (caracteristica.getNome().equalsIgnoreCase("TV")){
                    caracteristica.setId(4L);
                } else if (caracteristica.getNome().equalsIgnoreCase("Cozinha")){
                    caracteristica.setId(5L);
                } else if (caracteristica.getNome().equalsIgnoreCase("Estacionamento")){
                    caracteristica.setId(6L);
                } else if (caracteristica.getNome().equalsIgnoreCase("Jacuzzi")){
                    caracteristica.setId(7L);
                } else if (caracteristica.getNome().equalsIgnoreCase("Ar-condicionado")){
                    caracteristica.setId(8L);
                } else {
                    throw new BadRequestException("Caracteristica não existe");
                }
            }

            List<Cidade> cidadesBanco = cidadeService.getAll();
            for (Cidade c: cidadesBanco){
                if (produto.getCidade().getNome().equals(c.getNome())){
                    produto.getCidade().setId(c.getId());
                }
            }

//            if (produto.getCidade().getNome().equalsIgnoreCase("Rio de Janeiro")){
//                produto.getCidade().setId(1L);
//            } else if (produto.getCidade().getNome().equalsIgnoreCase("Florianópolis")){
//                produto.getCidade().setId(2L);
//            } else if (produto.getCidade().getNome().equalsIgnoreCase("Salvador")){
//                produto.getCidade().setId(3L);
//            } else if (produto.getCidade().getNome().equalsIgnoreCase("Fortaleza")){
//                produto.getCidade().setId(4L);
//            } else if (produto.getCidade().getNome().equalsIgnoreCase("Natal")){
//                produto.getCidade().setId(5L);
//            } else if (produto.getCidade().getNome().equalsIgnoreCase("Porto de Galinhas")){
//                produto.getCidade().setId(6L);
//            } else if (produto.getCidade().getNome().equalsIgnoreCase("João Pessoa")){
//                produto.getCidade().setId(7L);
//            } else if (produto.getCidade().getNome().equalsIgnoreCase("Maceió")){
//                produto.getCidade().setId(8L);
//            } else if (produto.getCidade().getNome().equalsIgnoreCase("Jericoacoara")){
//                produto.getCidade().setId(9L);
//            } else if (produto.getCidade().getNome().equalsIgnoreCase("Maragogi")){
//                produto.getCidade().setId(10L);
//            } else if (produto.getCidade().getNome().equalsIgnoreCase("Morro de São Paulo")){
//                produto.getCidade().setId(11L);
//            } else if (produto.getCidade().getNome().equalsIgnoreCase("Fernando de Noronha")){
//                produto.getCidade().setId(12L);
//            } else if (produto.getCidade().getNome().equalsIgnoreCase("Ubatuba")){
//                produto.getCidade().setId(13L);
//            } else {
//                throw new BadRequestException("Cidade não existe!");
//            }

            log.info("Cadastrado novo produto com sucesso.");
            return service.add(produto);
        } catch (Exception e) {
            log.error("Não foi possível cadastrar o produto com base nas informações recebidas.");
            throw new BadRequestException("Não foi possível cadastrar o produto com base nas informações recebidas.");
        }
    }

    @GetMapping
    public List<Produto> buscarTodos() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoId(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            service.getById(id);
            log.info("Encontrado o produto solicitado de id: " + id);
            return ResponseEntity.ok(service.getById(id));
        } catch (Exception e) {
            log.error("Não foi encontrado o produto solicitado de id: " + id);
            throw new ResourceNotFoundException("Não foi encontrado o produto solicitado de id: " + id);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Produto> atualizarProdutoPorId(@RequestBody Produto produto, @PathVariable Long id) throws ResourceNotFoundException {
        try{
            Produto produtoAtualizado = service.getById(id);
            produtoAtualizado.setNome(produto.getNome());
            produtoAtualizado.setAvaliacao(produto.getAvaliacao());
            produtoAtualizado.setEstrelas(produto.getEstrelas());
            produtoAtualizado.setFavorito(produto.isFavorito());
            log.info("Atualizado o produto de id: " + id);
            return ResponseEntity.ok(service.update(produtoAtualizado));
        } catch (Exception e){
            log.error("Não foi possível atualizar o produto de id: "+ id);
            throw new ResourceNotFoundException("Não foi possivel atualizar o produto de id: " + id);
        }
    }

    @GetMapping("/por_cidade/{id}")
    public List<Produto> buscarProdutosPorCidadeId(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            log.info("Encontrado a lista de produtos solicitado");
            return service.getAllProductsByCityId(id);
        } catch (Exception e) {
            log.error("Não foi encontrado a lista de produtos solicitado");
            throw new ResourceNotFoundException("Não foi encontrado a lista de produtos solicitado.");
        }
    }

    @GetMapping("/cidade")
    public List<Produto> buscarProdutosPorCidade(@RequestParam String nomeCidade) throws ResourceNotFoundException {
        try {
            log.info("Encontrado a lista de produtos solicitado");
            return service.getAllProductsByCityName(nomeCidade);
        } catch (Exception e) {
            log.error("Não foi encontrado a lista de produtos solicitado");
            throw new ResourceNotFoundException("Não foi encontrado a lista de produtos solicitado.");
        }
    }

    @GetMapping("/por_categoria/{id}")
    public List<Produto> buscarProdutosPorCategoriaId(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            log.info("Encontrado a lista de produtos solicitado");
            return service.getAllProductsByCategoryId(id);
        } catch (Exception e) {
            log.error("Não foi encontrado a lista de produtos solicitado");
            throw new ResourceNotFoundException("Não foi encontrado a lista de produtos solicitado.");
        }
    }

    @GetMapping("/categoria")
    public List<Produto> buscarProdutosPorCategoria(@RequestParam String categoria) throws ResourceNotFoundException {
        try {
            log.info("Encontrado a lista de produtos solicitado");
            return service.getAllProductsByCategoryQualification(categoria);
        } catch (Exception e) {
            log.error("Não foi encontrado a lista de produtos solicitado");
            throw new ResourceNotFoundException("Não foi encontrado a lista de produtos solicitado.");
        }
    }

    @GetMapping("/datas")
    public List<Produto> buscarProdutosPorDatas(@RequestParam LocalDate dataInicial, @RequestParam LocalDate dataFinal) throws ResourceNotFoundException{
        try {
            log.info("Encontrado a lista de produtos solicitado");
            return service.getAllProductsByDates(dataInicial, dataFinal);
        } catch (Exception e) {
            log.error("Não foi encontrado a lista de produtos solicitado");
            throw new ResourceNotFoundException("Não foi encontrado a lista de produtos solicitado.");
        }
    }

    @GetMapping("/datasDisponiveis")
    public List<Produto> getDifferenceBetweenProdutos(@RequestParam LocalDate dataInicial, @RequestParam LocalDate dataFinal) throws ResourceNotFoundException {
        List<Produto> allProdutos = buscarTodos();
        List<Produto> dateProdutos = buscarProdutosPorDatas(dataInicial, dataFinal);

        Set<Long> allProdutoIds = allProdutos.stream().map(Produto::getId).collect(Collectors.toSet());
        Set<Long> dateProdutoIds = dateProdutos.stream().map(Produto::getId).collect(Collectors.toSet());

        Set<Long> diffIds = new HashSet<>(allProdutoIds);
        diffIds.removeAll(dateProdutoIds);

        return allProdutos.stream().filter(produto -> diffIds.contains(produto.getId())).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<Produto> buscarProdutosPorDatasOuCidade(@RequestParam(required = false) LocalDate dataInicial,
                                                        @RequestParam(required = false) LocalDate dataFinal,
                                                        @RequestParam(required = false) String cidade) throws ResourceNotFoundException{
        try {
            log.info("Encontrado a lista de produtos solicitado");
            return service.getAllProductsByDatesOrCity(dataInicial, dataFinal, cidade);
        } catch (Exception e) {
            log.error("Não foi encontrado a lista de produtos solicitado");
            throw new ResourceNotFoundException("Não foi encontrado a lista de produtos solicitado.");
        }
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<String> processErrorBadRequest(BadRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
