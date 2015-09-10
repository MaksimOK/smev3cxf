package ru.voskhod.smev.message_exchange_service_client.impl;

import java.io.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Хранилище сертификатов СМЭВ на базе файловой системы.
 * @author dpryakhin
 */
public class FileSystemSMEVCertificateStore extends AbstractSMEVCertificateStore {

    private final File directory;

    public FileSystemSMEVCertificateStore(File directory) {
        this.directory = directory;
    }

    @Override
    protected List<X509Certificate> getSMEVCertificates() throws IOException, CertificateException {
        List<X509Certificate> result = new ArrayList<>();
        
        File[] files = directory.listFiles();
        if (files != null) {
            for (File item : files) {
                if (item.isDirectory()) {
                    continue;
                }
                try (InputStream in = new FileInputStream(item)) {
                    BufferedInputStream b64in = new BufferedInputStream(in);
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    X509Certificate cert = (X509Certificate) cf.generateCertificate(b64in);
                    result.add(cert);
                }
            }
        }
        return result;
    }
}
