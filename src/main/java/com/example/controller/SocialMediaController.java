package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // user registration
    @PostMapping("/register")
    public Account registerAccount(@RequestBody Account account) {
        return accountService.registerAccount(account);
    }

    // user login
    @PostMapping("/login")
    public Account login(@RequestBody Account loginRequest) {
        return accountService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    // creating new message
    @PostMapping("/messages")
    public Message createMessage(@RequestBody Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid message text");
        }
        try {
            return messageService.createMessage(message);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid message text");
        }
    }

    // retrieving all messages
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    // retrieving message by ID
    @GetMapping("/messages/{messageId}")
    public Message getMessageById(@PathVariable int messageId) {
        return messageService.getMessageById(messageId);
    }

    // deleting message by ID
    @DeleteMapping("/messages/{messageId}")
    public Integer deleteMessage(@PathVariable int messageId) {
        boolean deleted = messageService.deleteMessage(messageId);
        return deleted ? 1 : null;
    }

    // updating a message text by its ID
    @PatchMapping("/messages/{messageId}")
    public Integer updateMessageText(@PathVariable int messageId, @RequestBody Message newMessageText) {
        if (newMessageText.getMessageText() == null || newMessageText.getMessageText().isBlank() || newMessageText.getMessageText().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid message text");
        }
        int rowsUpdated = messageService.updateMessageText(messageId, newMessageText.getMessageText());
        if (rowsUpdated == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message not found");
        }
        return rowsUpdated;
    }

    // retrieving all messages posted by a user
    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByAccountId(@PathVariable int accountId) {
        accountService.getAccountById(accountId);
        return messageService.getMessagesByAccountId(accountId);
    }
}
