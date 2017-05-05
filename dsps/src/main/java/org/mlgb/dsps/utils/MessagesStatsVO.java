package org.mlgb.dsps.utils;

public class MessagesStatsVO {
    private long messagesTotal;
    private long messagesConsumed;
    public long getMessagesTotal() {
        return messagesTotal;
    }
    public void setMessagesTotal(long messagesTotal) {
        this.messagesTotal = messagesTotal;
    }
    public long getMessagesConsumed() {
        return messagesConsumed;
    }
    public void setMessagesConsumed(long messagesConsumed) {
        this.messagesConsumed = messagesConsumed;
    }
    public MessagesStatsVO() {
        super();
        this.messagesTotal = 0;
        this.messagesConsumed = 0;
    }
    
}
