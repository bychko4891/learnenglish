package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.TranslationPairUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranslationPairUserRepository extends CrudRepository<TranslationPairUser, Long> {

    Optional<TranslationPairUser> findTranslationPairUserByTranslationPair_IdAndUserId(Long translationPairId, Long userId);

}
