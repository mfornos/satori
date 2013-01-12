package satori.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;

public final class BeanValidator {

    private BeanValidator() {
    }

    public static BeanDescriptor getConstraintsForClass(Class<?> clazz) {
        return validator().getConstraintsForClass(clazz);
    }

    public static <T> T unwrap(Class<T> clazz) {
        return validator().unwrap(clazz);
    }

    public static <T> Set<ConstraintViolation<T>> validateProperty(T object, String property, Class<?>... groups) {
        return validator().validateProperty(object, property, groups);
    }

    public static <T> Set<ConstraintViolation<T>> validateValue(Class<T> clazz, String arg1, Object arg2,
            Class<?>... groups) {
        return validator().validateValue(clazz, arg1, arg2, groups);
    }

    public static <T> Errors<T> validate(T object, Class<?>... groups) {
        return new Errors<T>(validator().validate(object, groups), object);
    }

    private static Validator validator() {
        return BeanValidatorFactory.INSTANCE.getValidator();
    }

}
