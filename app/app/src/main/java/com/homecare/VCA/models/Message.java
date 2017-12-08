package com.homecare.VCA.models;


import java.util.Date;

public class Message {

    private String sentFrom;
    private Date sentOnDate;
    private String messageText;

    public Message(String sentFrom, Date sentOnDate, String messageText) {
        this.sentFrom = sentFrom;
        this.sentOnDate = sentOnDate;
        this.messageText = messageText;
    }

    public String getSentFrom() {
        return sentFrom;
    }

    public void setSentFrom(String sentFrom) {
        this.sentFrom = sentFrom;
    }

    public Date getSentOnDate() {
        return sentOnDate;
    }

    public void setSentOnDate(Date sentOnDate) {
        this.sentOnDate = sentOnDate;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
