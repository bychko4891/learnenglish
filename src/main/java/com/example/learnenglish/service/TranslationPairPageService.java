package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.model.TranslationPairsPage;
import com.example.learnenglish.repository.TranslationPairPageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranslationPairPageService {

    private final TranslationPairPageRepository translationPairPageRepository;

    public TranslationPairPageService(TranslationPairPageRepository translationPairPageRepository) {
        this.translationPairPageRepository = translationPairPageRepository;
    }


    public Page<TranslationPairsPage> getTranslationPairsPages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return translationPairPageRepository.findAll(pageable);
    }

    public Long countTranslationPairPages() {
        return translationPairPageRepository.count();
    }


}
