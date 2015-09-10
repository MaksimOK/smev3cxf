package ru.voskhod.smev.message_exchange_service_client.intercept;

import java.io.ByteArrayOutputStream;

final class CustomByteArrayOutputStream extends ByteArrayOutputStream {

    private static final String UUID_START = "--uuid:";
    private static final String UUID_PATTERN = "12345678-1234-1234-1234-1234567890AB";
    private static final int MAX_LENGTH = UUID_START.length() + UUID_PATTERN.length();

    private int start1 = -1;
    private int start2 = -1;

    @Override
    public synchronized void write(byte[] b, int off, int len) {
        for (int i = off; i < len - off; i++) {
            write(b[i]);
        }
    }

    @Override
    public synchronized void write(int b) {
        if (start2 >= 0)
            return;
        super.write(b);
        if (isPartSeparator()) {
            if (start1 >= 0) {
                // это уже второй разделитель
                start2 = count - MAX_LENGTH;
            } else {
                start1 = count;
            }
        }
    }

    private static boolean isAlphaNumeric(int c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isPartSeparator() {
        if (count < MAX_LENGTH)
            return false;
        int start = count - MAX_LENGTH;
        for (int i = start, j = 0; j < UUID_START.length(); i++, j++) {
            if (buf[i] != UUID_START.charAt(j))
                return false;
        }
        for (int i = start + UUID_START.length(), j = 0; j < UUID_PATTERN.length(); i++, j++) {
            int b = buf[i];
            char c = UUID_PATTERN.charAt(j);
            if (c == '-') {
                if (b != '-')
                    return false;
            } else {
                if (!isAlphaNumeric(b))
                    return false;
            }
        }
        return true;
    }

    private int findBodyStart(int from, int to) {
        int i = from;
        int prevN = 0;
        int prevR = 0;
        while (i < to) {
            int b = buf[i++];
            if (b == '\n' || b == '\r') {
                if (b == '\n') {
                    prevN++;
                } else {
                    prevR++;
                }
                if (prevN >= 2) {
                    if (prevR >= 2 || prevR == 0)
                        return i;
                } else if (prevR >= 2) {
                    if (prevN >= 2 || prevN == 0)
                        return i;
                }
            } else {
                prevN = prevR = 0;
            }
        }
        return from;
    }

    public synchronized byte[] getParsedContent() {
        if (start1 >= 0 && start2 >= 0) {
            int from = findBodyStart(start1, start2);
            byte[] parsed = new byte[start2 - from];
            System.arraycopy(buf, from, parsed, 0, parsed.length);
            return parsed;
        } else {
            return toByteArray();
        }
    }
}
