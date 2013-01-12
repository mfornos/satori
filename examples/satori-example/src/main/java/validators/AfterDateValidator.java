package validators;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.PropertyUtils;

public class AfterDateValidator implements ConstraintValidator<AfterDate, Object> {

    private String begin;
    private String end;

    @Override
    public void initialize(AfterDate afterDate) {
        begin = afterDate.start();
        end = afterDate.end();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            final Date beginDate = (Date) PropertyUtils.getProperty(value, begin);
            final Date endDate = (Date) PropertyUtils.getProperty(value, end);

            return endDate.after(beginDate);
        } catch (final Exception ignore) {
            // ignore
        }
        return true;
    }
}
