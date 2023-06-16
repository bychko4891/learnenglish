package com.example.learnenglish.repository;

import com.example.learnenglish.model.PageApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageApplicationRepository extends CrudRepository<PageApplication, Long> {
}
