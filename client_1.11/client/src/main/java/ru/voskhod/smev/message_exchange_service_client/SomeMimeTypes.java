package ru.voskhod.smev.message_exchange_service_client;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypes;

import java.io.File;

/**
 * Класс для определения MIME типов
 */
public class SomeMimeTypes {

    /**
     * Фасад детектора MIME типов
     */
    private static Tika tika = new Tika();

    /**
     * Пытается определить MIME тип указанного файла
     *
     * @param f файл
     * @return MIME тип или пустую строку, если не удалось определить тип
     */
    public static String guessMimeType(File f) {
        try {
            String result = tika.detect(f);
            return result == null ? "" : result;
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Возращает наиболее часто используемое расширение файла для указанного MIME типа.
     *
     * @param mimeType MIME тип файла
     * @return расширение или пустую строку, если не удалось определить
     */
    public static String guessFileExtension(String mimeType) {
        try {
            return MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtension();
        } catch (Exception ex) {
            return "";
        }
    }
}
