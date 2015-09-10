package ru.voskhod.smev.message_exchange_service_client.util;

import ru.voskhod.smev.message_exchange_service_client.SignatureOperationsClient;
import ru.voskhod.crypto.DigitalSignatureFactory;
import ru.voskhod.crypto.exceptions.SignatureProcessingException;
import ru.voskhod.crypto.exceptions.SignatureValidationException;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;

public class PipeOutputStream extends FilterOutputStream {

    private final MessageDigest digest;

    public PipeOutputStream(OutputStream out) throws SignatureProcessingException {
        this(out, SignatureOperationsClient.getMessageDigest());
    }

    public PipeOutputStream(OutputStream out, MessageDigest digest) {
        super(out);
        this.digest = digest;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
        digest.update((byte) b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
        digest.update(b, off, len);
    }

    public byte[] getDigest() {
        return digest.digest();
    }

    public X509Certificate validatePKCS7Signature(byte[] signature) throws SignatureProcessingException, SignatureValidationException {
        return DigitalSignatureFactory.getDigitalSignatureProcessor().validatePKCS7Signature(getDigest(), signature);
    }
}
