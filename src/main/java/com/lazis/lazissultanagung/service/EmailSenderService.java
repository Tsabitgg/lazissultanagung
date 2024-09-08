package com.lazis.lazissultanagung.service;

public interface EmailSenderService {
    void sendRegisterReport(String toEmail, String subject, String body);
}
