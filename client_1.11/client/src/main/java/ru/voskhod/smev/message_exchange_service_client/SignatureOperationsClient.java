package ru.voskhod.smev.message_exchange_service_client;

import org.w3c.dom.Element;
import ru.voskhod.crypto.DigitalSignatureFactory;
import ru.voskhod.crypto.PipeInputStream;
import ru.voskhod.crypto.exceptions.SignatureProcessingException;
import ru.voskhod.crypto.exceptions.SignatureValidationException;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public final class SignatureOperationsClient {

    private final PrivateKey privateKey;
    private final X509Certificate certificate;

    public SignatureOperationsClient(PrivateKey privateKey, X509Certificate certificate) {
        if (privateKey == null)
            throw new IllegalArgumentException("Private key cannot be null");
        if (certificate == null)
            throw new IllegalArgumentException("Certificate cannot be null");
        this.privateKey = privateKey;
        this.certificate = certificate;
    }

    public Element signXMLDSigDetached(Element document2Sign, String signatureId) throws SignatureProcessingException {
        return DigitalSignatureFactory.getDigitalSignatureProcessor().signXMLDSigDetached(document2Sign, signatureId, privateKey, certificate);
    }

    public X509Certificate validateXMLDSigSignature(Element signedContent, Element detachedSignature)
            throws SignatureProcessingException, SignatureValidationException {
        return DigitalSignatureFactory.getDigitalSignatureProcessor().validateXMLDSigDetachedSignature(signedContent, detachedSignature);
    }

    public static MessageDigest getMessageDigest() throws SignatureProcessingException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("GOST3411");
        } catch (NoSuchAlgorithmException e) {
            throw new SignatureProcessingException("Криптопровайдер не поддерживает алгоритм ГОСТ Р 34.11-94", e);
        }
        return digest;
    }

    public static PipeInputStream getDigestCollectingInputStream(InputStream signedContent) throws SignatureProcessingException {
        MessageDigest digest = getMessageDigest();
        return new PipeInputStream(signedContent, digest);
    }

    public byte[] signPKCS7Detached(byte[] digest) throws SignatureProcessingException {
        return DigitalSignatureFactory.getDigitalSignatureProcessor().signPKCS7Detached(digest, privateKey, certificate);
    }

    public X509Certificate validatePKCS7Signature(byte[] digest, byte[] signature)
            throws SignatureProcessingException, SignatureValidationException {
        return DigitalSignatureFactory.getDigitalSignatureProcessor().validatePKCS7Signature(digest, signature);
    }

    public X509Certificate getCertificate() {
        return certificate;
    }
}
