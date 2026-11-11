package aulas.umc.oo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public record PipelineResponse(
        @NotBlank String processo,
        @NotNull List<String> tarefasExecutadas,
        @NotNull Map<String, Object> dados) {}
