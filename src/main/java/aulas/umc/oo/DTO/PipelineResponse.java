package aulas.umc.oo.DTO;

import java.util.List; import java.util.Map;
public record PipelineResponse(String processo, List<String> tarefasExecutadas, Map<String,Object> dados) {}
