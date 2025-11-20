package com.espire.journalApp.scheduler;

import com.espire.journalApp.entity.JournalEntry;
import com.espire.journalApp.entity.User;
import com.espire.journalApp.enums.Sentiment;
import com.espire.journalApp.repository.UserRepositoryImpl;
import com.espire.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;


    //@Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendMail(){
        List<User> usersForSA = userRepository.getUserForSA();
        for(User user : usersForSA){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment, Integer> sentimentCount = new HashMap<>();
            for (Sentiment sentiment: sentiments){
                if(sentiment != null)
                    sentimentCount.put(sentiment,sentimentCount.getOrDefault(sentiment,0)+1);

            }
            Sentiment mostfrequentSentiment = null;
            int maxCount = 0;
            for(Map.Entry<Sentiment,Integer> entry : sentimentCount.entrySet()){
                if(entry.getValue() > maxCount){
                    mostfrequentSentiment = entry.getKey();
                    maxCount = entry.getValue();
                }
            }

            if(mostfrequentSentiment != null){
                emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days", mostfrequentSentiment.toString());
            }
        }
    }


}
