package ru.voskhod.crypto.impl;

import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

class SignatureNamespaceContext implements NamespaceContext {

    static final String XMLDSIG_NS = "http://www.w3.org/2000/09/xmldsig#";

    @Override
    public String getNamespaceURI(String prefix) {
        if ("ds".equals(prefix)) {
            return XMLDSIG_NS;
        } else {
            return null;
        }
    }

    @Override
    public String getPrefix(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<String> getPrefixes(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
