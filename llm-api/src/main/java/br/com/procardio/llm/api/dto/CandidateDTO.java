package br.com.procardio.llm.api.dto;

public record CandidateDTO(
    ContentDTO content,
    String finishReason,
    Integer index
) {
    
}
