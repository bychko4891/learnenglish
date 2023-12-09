package com.example.learnenglish.mapper;

public interface Mappable<E, D> {

    E toModel(D dto);

//    List<E> toEntity(List<D> d);

    D toDto(E entity);

//    List<D> toDto(List<E> e);

}
