package ru.voskhod.smev.message_exchange_service_client;

import ru.voskhod.crypto.PipeInputStream;
import ru.voskhod.crypto.exceptions.SignatureProcessingException;

import java.io.IOException;
import java.io.InputStream;

public abstract class InAttachment {

    private final String mimeType;
    /**
     * Длина файла вложения. Может быть null, но при этом используется больше ресурсов при useFS = AUTO.
     */
    private final Long length;
    private String id = null;
    private byte[] personalSignature = null;

    /**
     * @param length размер файла вложения.
     * Если null, то используется больше ресурсов при отправке файла.
     */
    protected InAttachment(String mimeType, Long length) {
        this.mimeType = mimeType;
        this.length = length;
    }

    protected InAttachment(String mimeType) {
        this(mimeType, null);
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getLength() {
        return length;
    }

    public DigestResult getDigest() throws IOException, SignatureProcessingException {
        try (InputStream inputStream = getInputStream()) {
            PipeInputStream digestStream = SignatureOperationsClient.getDigestCollectingInputStream(inputStream);
            while (true) {
                int c = digestStream.read();
                if (c < 0)
                    break;
            }
            return new DigestResult(digestStream);
        }
    }

    public void setSignPersonal(SignatureOperationsClient signer) throws SignatureProcessingException, IOException {
        if (signer == null) {
            setPersonalSignature(null);
        } else {
            byte[] dataDigest = getDigest().getDataDigest();
            setPersonalSignature(signer.signPKCS7Detached(dataDigest));
        }
    }

    public byte[] getPersonalSignature() {
        return personalSignature;
    }

    public void setPersonalSignature(byte[] personalSignature) {
        this.personalSignature = personalSignature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Этот метод может вызываться несколько раз и каждый раз должен возвращать новый поток
     */
    public abstract InputStream getInputStream() throws IOException;
}
