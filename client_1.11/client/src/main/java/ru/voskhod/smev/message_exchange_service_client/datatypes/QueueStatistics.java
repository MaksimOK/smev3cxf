package ru.voskhod.smev.message_exchange_service_client.datatypes;

public final class QueueStatistics {

    private final String queueName;
    private final long pendingMessagesNumber;

    public QueueStatistics(String queueName, long pendingMessagesNumber) {
        this.queueName = queueName;
        this.pendingMessagesNumber = pendingMessagesNumber;
    }

    public String getQueueName() {
        return queueName;
    }

    public long getPendingMessagesNumber() {
        return pendingMessagesNumber;
    }
}
