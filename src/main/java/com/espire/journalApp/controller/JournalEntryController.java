package com.espire.journalApp.controller;

import com.espire.journalApp.entity.JournalEntry;
import com.espire.journalApp.entity.User;
import com.espire.journalApp.service.JournalEntryService;
import com.espire.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("{username}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String username){
        User user = userService.findByUsername(username);
        List<JournalEntry> all =  user.getJournalEntries() ;
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId myId){
         Optional<JournalEntry>  journalEntry =   journalEntryService.findById(myId);
         if(journalEntry.isPresent()){
             return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
         }
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("{username}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String username){
        try {
            journalEntryService.saveEntry(myEntry, username);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("id/{username}/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId, @PathVariable String username){
        journalEntryService.deleteById(myId, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT );
    }

    @PutMapping("id/{username}/{myId}")
    public ResponseEntity<?> updateJournalById(
            @PathVariable ObjectId myId,
            @RequestBody JournalEntry newEntry,
            @PathVariable String username)
    {
        JournalEntry old = journalEntryService.findById(myId).orElse(null);
        if(old != null){
            old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
            old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
