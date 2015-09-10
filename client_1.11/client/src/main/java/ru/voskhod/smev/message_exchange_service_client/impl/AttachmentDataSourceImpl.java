package ru.voskhod.smev.message_exchange_service_client.impl;

import ru.voskhod.smev.message_exchange_service_client.InAttachment;

import javax.activation.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class AttachmentDataSourceImpl implements DataSource {

    private final InAttachment attachment;

    public AttachmentDataSourceImpl(InAttachment attachment) {
        this.attachment = attachment;
    }

    public String getContentType() {
        return attachment.getMimeType();
    }

    public InputStream getInputStream() throws IOException {
        return attachment.getInputStream();
    }

    public String getName() {
        return null;
    }

    public OutputStream getOutputStream() {
        return null;
    }
}
