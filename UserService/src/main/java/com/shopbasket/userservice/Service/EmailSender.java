package com.shopbasket.userservice.Service;

public interface EmailSender {
    void send(String to, String email);
}