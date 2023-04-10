package com.example.learnenglish.service;
/*
 *
 *
 */

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.repository.TranslationPairRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class TranslationPairService {
      private final EntityManager entityManager;
      private final TranslationPairRepository repository;

    public TranslationPairService(TranslationPairRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    public TranslationPair findById(Integer id) {
        // ----- this code works!!!! ------- //
//        TypedQuery<TranslationPair> q = entityManager.createQuery(
//                "select tr from TranslationPair tr where tr.id = :id", TranslationPair.class);
//        q.setParameter("id", id);
//        return q.getSingleResult();
        // ----- this code works!!!! -END----//
        return repository.findById(id).get();
    }

    public void saveTranslationPair(Lesson lesson_, TranslationPair translationPair) {
        repository.save(translationPair);



//        String query = "INSERT INTO translation_pair (ukr_text, eng_text, audio_path) values (:a, :b, :c)";
//        entityManager.createNativeQuery(query)
//                        .setParameter("a", translationPair.getUkrText())
//                        .setParameter("b", translationPair.getEngText())
//                        .setParameter("c", translationPair.getAudioPath())
//                                .executeUpdate();
//        return "OK";
    }
    public int countEntities() {
        int count = (int)repository.count();
        return count;
    }
   public boolean findUserByEmail(String email){
        if(entityManager.createQuery("from User where email = email") == null) return false;
        return true;
    }
}
