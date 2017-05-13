package org.mlgb.dsps.util;

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
    @Override
    public String toString() {
        return "Messages stats:\n" + "#messages: " + this.messagesTotal + "\n"
                + "#consumed messages: " + this.messagesConsumed;
    }
    
}
