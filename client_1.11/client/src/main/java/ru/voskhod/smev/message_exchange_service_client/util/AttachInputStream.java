package ru.voskhod.smev.message_exchange_service_client.util;

import java.io.Closeable;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AttachInputStream extends FilterInputStream {

    private final Closeable obj;

    public AttachInputStream(Closeable obj, InputStream in) {
        super(in);
        this.obj = obj;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } catch (IOException ex) {
            try {
                obj.close();
            } catch (IOException ex2) {
                ex.addSuppressed(ex2);
            }
            throw ex;
        }
        obj.close();
    }
}
