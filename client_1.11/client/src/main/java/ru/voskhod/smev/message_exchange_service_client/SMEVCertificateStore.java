package ru.voskhod.smev.message_exchange_service_client;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Абстрактный класс - хранилище, в котором лежат сертификаты СМЭВ.
 * Эти сертификаты используются, чтобы убеждаться, что ЭП-СМЭВ проставлена действительно СМЭВ.
 * Наследники реализуют выборку сертификатов, например, из файловой системы, БД, или KeyStore.
 * @author dpryakhin
 */
public abstract class SMEVCertificateStore {

    private static SMEVCertificateStore instance;

    public static SMEVCertificateStore getInstance() {
        return instance;
    }

    public static void setInstance(SMEVCertificateStore instance) {
        SMEVCertificateStore.instance = instance;
    }

    /**
     * Известен ли такой сертификат СМЭВ.
     * @param certificate сертификат, предположительно принадлежащий СМЭВ.
     * @return true, если такой сертификат известен как принадлежащий СМЭВ.
     * @throws IOException невозможно прочитать сертификаты из долговременной памяти.
     * @throws CertificateException невозможно преобразовать сертификат из бинарного формата в Java-объект.
     */
    public abstract boolean isKnown(X509Certificate certificate) throws IOException, CertificateException;
}
