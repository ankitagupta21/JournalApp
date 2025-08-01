package com.example.demo.repository;

import com.example.demo.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EntryRepository extends MongoRepository<JournalEntry, ObjectId> {

}
