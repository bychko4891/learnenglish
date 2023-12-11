package com.example.learnenglish.mapper;

import com.example.learnenglish.dto.WordDto;
import com.example.learnenglish.model.Word;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface WordMapper extends Mappable<Word, WordDto> {

    @Override
    Word toModel(WordDto dto);
}
