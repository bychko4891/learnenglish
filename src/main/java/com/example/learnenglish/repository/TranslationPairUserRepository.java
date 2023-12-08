package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.PhrasesAndUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranslationPairUserRepository extends CrudRepository<PhrasesAndUser, Long> {

    Optional<PhrasesAndUser> findTranslationPairUserByPhraseUser_IdAndUserId(Long translationPairId, Long userId);

}
