package ru.voskhod.smev.message_exchange_service_client;

import java.io.IOException;
import java.io.OutputStream;

public interface OutAttachment {

    interface Content {

        /**
         * Блокирующий метод записи содержимого файла в поток
         *
         * @return true, если весь файл был успешно записан; false, если был вызван метод {@link #cancel()}
         */
        boolean retrieve(OutputStream output) throws IOException, ClientSideProcessingException;

        /**
         * Может вызываться из другого потока для прерывания скачки файла
         */
        void cancel();
    }

    /**
     * @return некоторое текстовое описание файла
     */
    String getName();

    String getMimeType();

    byte[] getSignature();

    Content getContent();
}
