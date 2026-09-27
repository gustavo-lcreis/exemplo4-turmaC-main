package br.senac.tads.dsw.exemplo4.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.senac.tads.dsw.exemplo4.model.Avaliacao;
import br.senac.tads.dsw.exemplo4.repository.AvaliacaoRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoRepository repository;

    public AvaliacaoController(AvaliacaoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Avaliacao> listarTodas() {
        return repository.findAll(); // 200 OK por padrão
    }

    @GetMapping("/{id}")
    public Avaliacao buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Avaliacao> criar(@RequestBody @Valid Avaliacao avaliacao) {
        Avaliacao salva = repository.save(avaliacao);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salva.getId())
                .toUri();

        return ResponseEntity.created(uri).body(salva);
    }

    @PutMapping("/{id}")
    public Avaliacao atualizar(@PathVariable Long id,
                                @RequestBody @Valid Avaliacao avaliacaoAtualizada) {
        return repository.findById(id)
                .map(avaliacaoExistente -> {
                    avaliacaoExistente.setAutor(avaliacaoAtualizada.getAutor());
                    avaliacaoExistente.setComentario(avaliacaoAtualizada.getComentario());
                    avaliacaoExistente.setNota(avaliacaoAtualizada.getNota());
                    avaliacaoExistente.setProduto(avaliacaoAtualizada.getProduto());
                    return repository.save(avaliacaoExistente);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

}
