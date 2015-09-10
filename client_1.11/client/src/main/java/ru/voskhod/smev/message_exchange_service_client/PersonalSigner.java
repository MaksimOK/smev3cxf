package ru.voskhod.smev.message_exchange_service_client;

import org.w3c.dom.Element;
import ru.voskhod.crypto.exceptions.SignatureProcessingException;

public interface PersonalSigner {

    Element getSignature(Element businessContent) throws SignatureProcessingException;
}
