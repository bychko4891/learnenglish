package com.example.learnenglish.mapper;

import com.example.learnenglish.dto.PhraseUserDto;
import com.example.learnenglish.model.users.PhraseUser;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PhraseUserMapper extends Mappable<PhraseUser, PhraseUserDto>{

//    PhraseUser toModel();

}
