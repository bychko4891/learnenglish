package com.example.learnenglish.repository;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.WayForPayModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WayForPayModuleRepository extends JpaRepository<WayForPayModule, Long> {
}
