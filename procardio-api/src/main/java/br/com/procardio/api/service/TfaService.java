package br.com.procardio.api.service;

import org.springframework.stereotype.Service;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;

@Service
public class TfaService {

    private final DefaultSecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final QrGenerator qrGenerator = new ZxingPngQrGenerator();
    private final CodeVerifier codeVerifier;

    public TfaService(CodeVerifier codeVerifier) {
        this.codeVerifier = codeVerifier;
    }

    public String gerarNovoSegredo() {
        return secretGenerator.generate();
    }

    public byte[] gerarQrCodeImagem(String secret, String email) throws QrGenerationException {
        QrData data = new QrData.Builder()
                .label(email)
                .secret(secret)
                .issuer("ProCardio API")
                .build();

        return qrGenerator.generate(data);
    }

    public boolean codigoValido(String secret, String code) {
        return codeVerifier.isValidCode(secret, code);
    }

}