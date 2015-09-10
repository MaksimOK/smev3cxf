package ru.voskhod.smev.message_exchange_service_client.intercept;

import java.io.UnsupportedEncodingException;

/**
 * @author bshalin@it.ru
 */
public final class InterceptorStorage {

    private static final InterceptorStorage request = new InterceptorStorage();
    private static final InterceptorStorage response = new InterceptorStorage();

    private final ThreadLocal<byte[]> data = new ThreadLocal<byte[]>();
    private final ThreadLocal<Boolean> intercept = new ThreadLocal<Boolean>();

    public static InterceptorStorage getRequest() {
        return request;
    }

    public static InterceptorStorage getResponse() {
        return response;
    }

    public void set(byte[] data) {
        this.data.set(data);
    }

    public byte[] get() {
        return data.get();
    }

    public String getString() {
        byte[] bytes = get();
        if (bytes == null)
            return null;
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return new String(bytes);
        }
    }

    public void setIntercept(boolean on) {
        intercept.set(on);
    }

    public boolean isIntercept() {
        Boolean value = intercept.get();
        return value != null && value.booleanValue();
    }
}
