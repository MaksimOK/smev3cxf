package ru.voskhod.smev.message_exchange_service_client.intercept;

import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.assembler.dev.ClientTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.ServerTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.TubeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.WebServiceException;

public final class InterceptorTubeFactory implements TubeFactory {

    private static final Logger logger = LoggerFactory.getLogger(InterceptorTubeFactory.class);

    @Override
    public Tube createTube(ClientTubelineAssemblyContext context) throws WebServiceException {
        logger.info("Creating client-side interceptor tube");
        return new InterceptorTube(context.getTubelineHead(), true);
    }

    @Override
    public Tube createTube(ServerTubelineAssemblyContext context) throws WebServiceException {
        logger.info("Creating server-side interceptor tube");
        return new InterceptorTube(context.getTubelineHead(), false);
    }
}
