package com.alura.challenge_forohub.controller;

import com.alura.challenge_forohub.domain.topico.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;


    @PostMapping
    @Transactional
    @Operation(summary = "Registra un nuevo topico en la base de datos")
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                                                UriComponentsBuilder uriComponentsBuilder) {
        Topico topico = topicoRepository.save(new Topico(datosRegistroTopico));
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico.getTitulo(), topico.getMensaje(),
                topico.getFechaCreacion(), topico.isEstado(), topico.getAutor(), topico.getCurso());

        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);

    }

    @GetMapping
    @Operation(summary = "Obtiene el listado de topicos")
    public ResponseEntity<Page<Topico>> listadoTopicos(@PageableDefault(size = 2) Pageable paginacion) {
        return ResponseEntity.ok(topicoRepository.findAll(paginacion));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene los registros del topico con ID")
    public ResponseEntity<DatosRespuestaTopico> retornaDatosTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        var datosTopico = new DatosRespuestaTopico(topico.getTitulo(), topico.getMensaje(),
                topico.getFechaCreacion(), topico.isEstado(), topico.getAutor(), topico.getCurso());
        return ResponseEntity.ok(datosTopico);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Actualiza los datos de un topico existente")
    public ResponseEntity actualizarTopico(@PathVariable Long id, @RequestBody @Valid DatosActualizarTopico datosActualizarTopico) {
        Topico topico = topicoRepository.getReferenceById(id);
        topico.actualizarDatos(datosActualizarTopico);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico.getTitulo(), topico.getMensaje(),
                topico.getFechaCreacion(), topico.isEstado(), topico.getAutor(), topico.getCurso()));

    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Elimina un topico registrado")
    public ResponseEntity eliminarTopico(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);

        if (topico.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
