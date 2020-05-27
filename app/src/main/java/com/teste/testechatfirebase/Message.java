package com.teste.testechatfirebase;

public class Message {
    //Atributos das mensagens
    private String text; //texto da mensagem
    private long timestamp; //hora de envio da mensagem
    private String fromID; // Usuário que está enviando a mensagem e para quem está enviando

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    private String toID;






}
