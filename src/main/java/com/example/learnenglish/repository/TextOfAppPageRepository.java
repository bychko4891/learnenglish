package com.example.learnenglish.repository;

import com.example.learnenglish.model.TextOfAppPage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextOfAppPageRepository extends CrudRepository<TextOfAppPage, Long> {
}
