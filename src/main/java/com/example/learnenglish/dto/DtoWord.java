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
public class DtoWord {
    private Word word;
//    private Category mainCategorySelect;
//    private Category subcategorySelect;
//    private Category subSubcategorySelect;
    private List<Long> phrasesApplicationId;
    public DtoWord() {
    }

    public static Word convertDtoToWord(DtoWord dtoWord, Word word){
        word.setName(StringUtils.normalizeSpace(dtoWord.getWord().getName()));
        word.getAudio().setName(StringUtils.normalizeSpace(dtoWord.getWord().getName()));
        word.setTranslate(dtoWord.getWord().getTranslate());
        word.setBrTranscription(dtoWord.getWord().getBrTranscription());
        word.setUsaTranscription(dtoWord.getWord().getUsaTranscription());
        word.setIrregularVerbPt(dtoWord.getWord().getIrregularVerbPt());
        word.setIrregularVerbPp(dtoWord.getWord().getIrregularVerbPp());
        word.setDescription(dtoWord.getWord().getDescription());
        word.setPublished(dtoWord.getWord().isPublished());
        word.setInfo(dtoWord.getWord().getInfo());
        return word;

    }
}
