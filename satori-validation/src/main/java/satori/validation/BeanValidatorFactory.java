package satori.validation;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.bval.jsr303.ApacheValidationProvider;

public enum BeanValidatorFactory {

    INSTANCE {

        ValidatorFactory avf = Validation.byProvider(ApacheValidationProvider.class).configure()
                .buildValidatorFactory();

        @Override
        public Validator getValidator() {
            return avf.getValidator();
        }

    };

    public abstract Validator getValidator();
}
