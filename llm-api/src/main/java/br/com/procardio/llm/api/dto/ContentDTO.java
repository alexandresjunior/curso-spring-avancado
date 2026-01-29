package br.com.procardio.llm.api.dto;

import java.util.List;

public record ContentDTO(
    List<PartDTO> parts,
    String role
) {
    
}
