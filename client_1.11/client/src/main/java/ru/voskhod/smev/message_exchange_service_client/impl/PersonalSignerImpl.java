package ru.voskhod.smev.message_exchange_service_client.impl;

import org.w3c.dom.Element;
import ru.voskhod.smev.message_exchange_service_client.PersonalSigner;

public class PersonalSignerImpl implements PersonalSigner {

    private final Element signature;

    public PersonalSignerImpl(Element signature) {
        this.signature = signature;
    }

    public Element getSignature(Element businessContent) {
        return signature;
    }
}
