package ru.voskhod.smev.message_exchange_service_client.intercept;

import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

final class InterceptorTube extends AbstractFilterTubeImpl {

    private static final Logger logger = LoggerFactory.getLogger(InterceptorTube.class);

    private final boolean client;

    InterceptorTube(Tube tube, boolean client) {
        super(tube);
        this.client = client;
    }

    private InterceptorTube(InterceptorTube original, TubeCloner cloner) {
        super(original, cloner);
        this.client = original.client;
    }

    @Override
    public InterceptorTube copy(TubeCloner cloner) {
        return new InterceptorTube(this, cloner);
    }

    private void intercept(String what, InterceptorStorage storage, Packet packet) {
        if (storage.isIntercept()) {
            logger.debug(String.format("Message %s intercepted on %s side", what, client ? "client" : "server"));
            try {
                CustomByteArrayOutputStream baos = new CustomByteArrayOutputStream();
                Packet requestCopy = packet.copy(true);
                requestCopy.writeTo(baos);
                storage.set(baos.getParsedContent());
            } catch (IOException ex) {
                logger.error(null, ex);
            }
        }
    }

    @Override
    public NextAction processRequest(Packet request) {
        intercept("request", InterceptorStorage.getRequest(), request);
        return super.processRequest(request);
    }

    @Override
    public NextAction processResponse(Packet response) {
        intercept("response", InterceptorStorage.getResponse(), response);
        return super.processResponse(response);
    }

    @Override
    public NextAction processException(Throwable throwable) {
        logger.warn(String.format("Message processing exception intercepted on %s side", client ? "client" : "server"));
        return super.processException(throwable);
    }
}
