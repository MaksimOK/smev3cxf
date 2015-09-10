package ru.voskhod.smev.message_exchange_service_client.impl;

import ru.voskhod.smev.message_exchange_service_client.InAttachment;
import ru.voskhod.smev.message_exchange_service_client.SomeMimeTypes;

import java.io.*;

public final class FileAttachmentImpl extends InAttachment {

    private final File content;

    public FileAttachmentImpl(File content) {
        this(content, SomeMimeTypes.guessMimeType(content));
    }

    public FileAttachmentImpl(File content, String mimeType) {
        super(mimeType, content.length());
        this.content = content;
    }

    public InputStream getInputStream() throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(content));
    }
}
