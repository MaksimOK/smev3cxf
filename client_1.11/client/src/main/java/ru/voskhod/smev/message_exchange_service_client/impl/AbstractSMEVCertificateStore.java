package ru.voskhod.smev.message_exchange_service_client.impl;

import ru.voskhod.smev.message_exchange_service_client.SMEVCertificateStore;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Абстрактный класс - хранилище, в котором лежат сертификаты СМЭВ.
 * Эти сертификаты используются, чтобы убеждаться, что ЭП-СМЭВ проставлена действительно СМЭВ.
 * Наследники реализуют выборку сертификатов, например, из файловой системы, БД, или KeyStore.
 * @author dpryakhin
 */
public abstract class AbstractSMEVCertificateStore extends SMEVCertificateStore {

    private static final long CACHE_UPDATE_INTERVAL_MILLIS = 1000;

    private final List<X509Certificate> certificateCache = new ArrayList<>();
    private final List<X509Certificate> negativeCertificateCache = new ArrayList<>();
    private long lastCacheUpdate = 0;

    /**
     * Известен ли такой сертификат СМЭВ.
     * @param certificate сертификат, предположительно принадлежащий СМЭВ.
     * @return true, если такой сертификат известен как принадлежащий СМЭВ.
     * @throws java.io.IOException невозможно прочитать сертификаты из долговременной памяти.
     * @throws java.security.cert.CertificateException невозможно преобразовать сертификат из бинарного формата в Java-объект.
     */
    public boolean isKnown(X509Certificate certificate) throws IOException, CertificateException {
        // Может случиться так, что по ходу работы приложения в хранилище добавили сертификат.
        // Поэтому при отсутствии его в кэше пытаемся обновить кэш.
        synchronized (this) {
            if (certificateCache.contains(certificate))
                return true;
            long now = System.currentTimeMillis();
            if (negativeCertificateCache.contains(certificate) && now - lastCacheUpdate <= CACHE_UPDATE_INTERVAL_MILLIS)
                return false;

            certificateCache.clear();
            negativeCertificateCache.clear();
            certificateCache.addAll(getSMEVCertificates());
            lastCacheUpdate = System.currentTimeMillis();

            boolean found = certificateCache.contains(certificate);
            if (!found) {
                negativeCertificateCache.add(certificate);
            }
            return found;
        }
    }

    /**
     * Прочитать сертификаты СМЭВ из долговременной памяти.
     * @return список сертификатов.
     * @throws java.io.IOException невозможно прочитать сертификаты из долговременной памяти.
     * @throws java.security.cert.CertificateException невозможно преобразовать сертификат из бинарного формата в Java-объект.
     */
    protected abstract List<X509Certificate> getSMEVCertificates() throws IOException, CertificateException;
}
