package com.example.learnenglish.repository;

import com.example.learnenglish.model.TranslationPair;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationPairRepository extends CrudRepository<TranslationPair, Integer> {

}
