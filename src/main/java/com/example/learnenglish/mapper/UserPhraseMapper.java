package com.example.learnenglish.mapper;

import com.example.learnenglish.dto.PhraseUserDto;
import com.example.learnenglish.model.PhraseUser;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserPhraseMapper extends Mappable<PhraseUser, PhraseUserDto>{

//    PhraseUser toModel();

}
