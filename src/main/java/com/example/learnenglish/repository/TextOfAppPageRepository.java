package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.TextOfAppPage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextOfAppPageRepository extends CrudRepository<TextOfAppPage, Long> {
}
