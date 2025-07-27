package com.example.demo.controller;

import com.example.demo.entity.JournalEntry;
import com.example.demo.entity.User;
import com.example.demo.service.JournalEntryService;
import com.example.demo.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myJournalEntry){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveEntry(myJournalEntry,userName);
            return new ResponseEntity<>(myJournalEntry, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(myJournalEntry, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()) {
            Optional<JournalEntry> entry = journalEntryService.findById(myId);
            if(entry.isPresent()){
                return new ResponseEntity<>(entry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = journalEntryService.deleteById(myId, userName);
        if(removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId myId, @RequestBody JournalEntry newJournalEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
            if( journalEntry.isPresent()) {
                JournalEntry old = journalEntry.get();
                old.setTitle(newJournalEntry.getTitle() != null && !newJournalEntry.getTitle().equals("") ? newJournalEntry.getTitle() : old.getTitle());
                old.setContent(newJournalEntry.getContent() != null && !newJournalEntry.getContent().equals("") ? newJournalEntry.getContent() : old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
