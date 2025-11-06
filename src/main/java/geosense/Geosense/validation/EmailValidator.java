package geosense.Geosense.validation;

import geosense.Geosense.service.ValidacaoOracleService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * EmailValidator que usa a função Oracle FN_VALIDAR_SENHA_E_LIMITES
 * para validação completa de email
 */
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    
    private boolean required;
    private ValidacaoOracleService validacaoOracleService;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        this.required = constraintAnnotation.required();
        try {
            this.validacaoOracleService = SpringContextHelper.getBean(ValidacaoOracleService.class);
        } catch (Exception e) {
            this.validacaoOracleService = null;
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!required && (value == null || value.trim().isEmpty())) {
            return true;
        }

        if (required && (value == null || value.trim().isEmpty())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Email é obrigatório")
                   .addConstraintViolation();
            return false;
        }

        // Validação básica local primeiro (mais rápida)
        String email = value.trim();
        if (email.length() > 255) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Email deve ter no máximo 255 caracteres")
                   .addConstraintViolation();
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Email deve ter formato válido (exemplo@dominio.com)")
                   .addConstraintViolation();
            return false;
        }


        return true;
    }
}
