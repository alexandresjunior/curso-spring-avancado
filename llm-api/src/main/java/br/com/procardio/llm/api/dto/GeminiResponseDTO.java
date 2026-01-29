package br.com.procardio.llm.api.dto;

import java.util.List;

public record GeminiResponseDTO(List<CandidateDTO> candidates) {
    
}
