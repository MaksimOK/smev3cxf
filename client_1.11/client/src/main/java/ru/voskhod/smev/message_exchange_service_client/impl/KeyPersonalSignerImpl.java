package ru.voskhod.smev.message_exchange_service_client.impl;

import org.w3c.dom.Element;
import ru.voskhod.crypto.exceptions.SignatureProcessingException;
import ru.voskhod.smev.message_exchange_service_client.PersonalSigner;
import ru.voskhod.smev.message_exchange_service_client.SignatureOperationsClient;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class KeyPersonalSignerImpl implements PersonalSigner {

    private final SignatureOperationsClient signer;

    public KeyPersonalSignerImpl(SignatureOperationsClient signer) {
        this.signer = signer;
    }

    public KeyPersonalSignerImpl(PrivateKey privateKey, X509Certificate certificate) {
        this(new SignatureOperationsClient(privateKey, certificate));
    }

    public static KeyPersonalSignerImpl create(SignatureOperationsClient signer) {
        return signer == null ? null : new KeyPersonalSignerImpl(signer);
    }

    public Element getSignature(Element businessContent) throws SignatureProcessingException {
        businessContent.setAttribute("Id", "PERSONAL_SIGNATURE");
        businessContent.setIdAttribute("Id", true);
        return signer.signXMLDSigDetached(businessContent, null);
    }
}
