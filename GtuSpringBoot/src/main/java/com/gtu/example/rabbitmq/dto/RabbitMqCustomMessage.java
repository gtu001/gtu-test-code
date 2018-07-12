package com.gtu.example.rabbitmq.dto;

import java.io.Serializable;

public final class RabbitMqCustomMessage implements Serializable {

    private static final long serialVersionUID = 8341756211921335595L;
    private String text;
    private int priority;
    private boolean secret;

    // Default constructor is needed to deserialize JSON
    public RabbitMqCustomMessage() {
    }

    public RabbitMqCustomMessage(String text, int priority, boolean secret) {
        this.text = text;
        this.priority = priority;
        this.secret = secret;
    }

    public String getText() {
        return text;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isSecret() {
        return secret;
    }

    @Override
    public String toString() {
        return "CustomMessage{" + "text='" + text + '\'' + ", priority=" + priority + ", secret=" + secret + '}';
    }
}