package com.alura.challenge_forohub.domain.topico;

import java.time.LocalDateTime;

public record DatosRespuestaTopico(
        String titulo, String mensaje, LocalDateTime fechaCreacion, boolean estado, String autor, String curso
) {
}
