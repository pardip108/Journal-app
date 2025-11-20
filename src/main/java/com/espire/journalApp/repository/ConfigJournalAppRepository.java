package com.espire.journalApp.repository;

import com.espire.journalApp.entity.ConfigJournalAppEntity;
import com.espire.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntity, ObjectId> {


}
