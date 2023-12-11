package com.example.learnenglish.dto;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Word;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class WordDto {

    private Word word;

    private List<Long> phrasesApplicationId;

    public static Word convertDtoToWord(WordDto wordDto, Word word){
        word.setName(StringUtils.normalizeSpace(wordDto.getWord().getName()));
        word.getAudio().setName(StringUtils.normalizeSpace(wordDto.getWord().getName()));
        word.setTranslate(wordDto.getWord().getTranslate());
        word.setBrTranscription(wordDto.getWord().getBrTranscription());
        word.setUsaTranscription(wordDto.getWord().getUsaTranscription());
        word.setIrregularVerbPt(wordDto.getWord().getIrregularVerbPt());
        word.setIrregularVerbPp(wordDto.getWord().getIrregularVerbPp());
        word.setDescription(wordDto.getWord().getDescription());
        word.setPublished(wordDto.getWord().isPublished());
        word.setInfo(wordDto.getWord().getInfo());
        return word;

    }
}
