package com.oliver.fuzzy.energic.joy.validator;

import com.oliver.fuzzy.energic.joy.exception.ValidationException;

public interface Validator<T> {
    /**
     * Validates the given object by checking its fields and throws an exception if it is invalid which will be handled by a controller advice
     * @param object the object you would like to validate
     * @throws ValidationException when any parameter of the object is against the validation rules
     */
    public void validate(T object) throws ValidationException;
}
