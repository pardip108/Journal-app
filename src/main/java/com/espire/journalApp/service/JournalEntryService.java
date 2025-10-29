package com.espire.journalApp.service;

import com.espire.journalApp.entity.JournalEntry;
import com.espire.journalApp.entity.User;
import com.espire.journalApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;


    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username){
        try{
            User user = userService.findByUsername(username);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
//        user.setUsername(null); // deberately null set kar diye ab user me entry save nahi ho payega
            //because user entity me hum username ko not null rakhe huwe h so
            userService.saveUser(user);
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("an error occured while saving the entry.", e);
        }
    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id, String username){
        try{
            User user = userService.findByUsername(username);
            boolean removed = user.getJournalEntries().removeIf(x ->  x.getId().equals(id));
            if(removed){
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        }catch (Exception e){
            log.info("exception", e);
            throw new RuntimeException("an error occured while saving the entry.", e);
        }
    }


}

//controller-> service -> repository