package ru.voskhod.smev.message_exchange_service_client;

import ru.voskhod.crypto.PipeInputStream;

public class DigestResult {

    private final long dataSize;
    private final byte[] dataDigest;

    public DigestResult(long dataSize, byte[] dataDigest) {
        this.dataSize = dataSize;
        this.dataDigest = dataDigest;
    }

    public DigestResult(PipeInputStream digestStream) {
        this(digestStream.getSize(), digestStream.getDigest());
    }

    public long getDataSize() {
        return dataSize;
    }

    public byte[] getDataDigest() {
        return dataDigest;
    }
}
