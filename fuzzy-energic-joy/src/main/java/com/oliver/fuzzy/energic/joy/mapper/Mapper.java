package com.oliver.fuzzy.energic.joy.mapper;

/**
 * A generic mapper interface which transforms from a type to a target type
 * @param <F> Type you would like to transform from
 * @param <T> Type you would like to transform to
 */
public interface Mapper<F, T>{
    T transform(F fromObject);
}
