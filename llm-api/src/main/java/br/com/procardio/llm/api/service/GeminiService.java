package br.com.procardio.llm.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import br.com.procardio.llm.api.dto.GeminiResponseDTO;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private RestClient restClient;

    public GeminiService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public String gerarDicaDeSaude(String cidade, String estado) {
        String prompt = String.format(
                """
                        Você é um médico cardiologista.
                        Escreva um parágrafo curto (de no máximo 3 linhas)
                        com uma dica de saúde para um paciente que mora em %s - %s,
                        levando em consideração o clima dessa região no dia de hoje. 
                        Seja cordial.
                        """, cidade, estado);

        String requestBody = String.format(
                """
                        {
                            "contents": [{
                                "parts": [{
                                    "text": "%s"
                                }]
                            }]
                        }
                        """, prompt);

        try {
            GeminiResponseDTO response = restClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(GeminiResponseDTO.class);

            return response.candidates()
                    .get(0)
                    .content()
                    .parts()
                    .get(0)
                    .text();
        } catch (Exception ex) {
            return """
                    Dica de saúde geral:
                    Mantenha-se hidratado e faça exercícios!
                    """;
        }
    }

}
