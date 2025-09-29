package com.example.chackchack.common.annotation.validator;

import com.example.chackchack.common.annotation.FieldMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Objects;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String first;
    private String second;

    private Object read(Object target, String prop) {
        BeanWrapper bw = new BeanWrapperImpl(target);
        return bw.getPropertyValue(prop);
    }

    public void initialize(FieldMatch a){
        first = a.first();
        second = a.second();
    }

    public boolean isValid(Object value, ConstraintValidatorContext ctx){
        Object val1 = read(value, first); Object val2 = read(value, second); // 리플렉션/레코드 접근
        return Objects.equals(val1, val2);
    }
}