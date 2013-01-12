package satori.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import org.apache.bval.constraints.NotEmpty;
import org.junit.Test;

public class BeanValidatorTest {

    @Test
    public void test() {

        Errors<Bean> errors = BeanValidator.validate(new Bean());
        assertFalse(errors.isEmpty());
        assertEquals(2, errors.size());

        assertNotNull(errors.get("name"));
        assertNotNull(errors.get("id"));

        try {
            errors.croak();
            fail("No croak");
        } catch (ValidationException ex) {
            //
        }

    }

    static class Bean {
        @NotNull @NotEmpty String name;
        @NotNull Integer id;
    }

}
