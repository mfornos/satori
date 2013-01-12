package satori.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;

public class Errors<T> extends HashMap<String, ConstraintViolation<T>> {

    private static final long serialVersionUID = 2828785918927393240L;

    private final T bean;
    private final Set<ConstraintViolation<T>> violations;
    private final Map<String, String> simpleErrors;

    public Errors(Set<ConstraintViolation<T>> violations, T bean) {
        this.bean = bean;
        this.violations = violations;
        this.simpleErrors = new HashMap<String, String>();

        for (ConstraintViolation<T> violation : violations) {
            String property = violation.getPropertyPath().toString();
            put(StringUtils.isBlank(property) ? "root" : property, violation);
        }
    }

    public T croak() {
        if (hasErrors())
            throw new ValidationException(get(keySet().iterator().next()).getMessage());
        return bean;
    }

    public T getBean() {
        return bean;
    }

    // Convenience name
    public boolean getHasSimpleErrors() {
        return hasSimpleErrors();
    }

    public String getSimpleError() {
        return (hasSimpleErrors()) ? simpleErrors.entrySet().iterator().next().getValue() : null;
    }

    public Map<String, String> getSimpleErrors() {
        return simpleErrors;
    }

    public Set<ConstraintViolation<T>> getViolations() {
        return violations;
    }

    public boolean hasErrors() {
        return !isEmpty();
    }

    public boolean hasSimpleErrors() {
        return !simpleErrors.isEmpty();
    }

    public boolean isEmpty() {
        return super.isEmpty() && !hasSimpleErrors();
    }

    public Errors<T> putSimpleError(String key, String msg) {
        simpleErrors.put(key, msg);
        return this;
    }

}
