package geosense.Geosense.validation;

import geosense.Geosense.service.ValidacaoOracleService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SenhaValidator implements ConstraintValidator<ValidSenha, String> {
    
    private int minLength;
    private int maxLength;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigit;
    private boolean requireSpecialChar;
    private boolean required;
    private ValidacaoOracleService validacaoOracleService;
    
    @Override
    public void initialize(ValidSenha constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.requireUppercase = constraintAnnotation.requireUppercase();
        this.requireLowercase = constraintAnnotation.requireLowercase();
        this.requireDigit = constraintAnnotation.requireDigit();
        this.requireSpecialChar = constraintAnnotation.requireSpecialChar();
        this.required = constraintAnnotation.required();
        
        try {
            this.validacaoOracleService = SpringContextHelper.getBean(ValidacaoOracleService.class);
        } catch (Exception e) {
            this.validacaoOracleService = null;
        }
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!required && (value == null || value.isEmpty())) {
            return true;
        }
        
        if (required && (value == null || value.isEmpty())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Senha é obrigatória")
                   .addConstraintViolation();
            return false;
        }


        int minOracle = 6;
        int maxOracle = 20;
        
        if (value.length() < minOracle) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Senha deve ter pelo menos " + minOracle + " caracteres")
                   .addConstraintViolation();
            return false;
        }
        
        if (value.length() > maxOracle) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Senha deve ter no máximo " + maxOracle + " caracteres")
                   .addConstraintViolation();
            return false;
        }

        if (value.contains(" ")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Senha não pode conter espaços")
                   .addConstraintViolation();
            return false;
        }

        if (!value.matches(".*[0-9].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Senha deve conter pelo menos um número")
                   .addConstraintViolation();
            return false;
        }

        if (!value.matches(".*[A-Z].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Senha deve conter pelo menos uma letra maiúscula")
                   .addConstraintViolation();
            return false;
        }

        if (!value.matches(".*[a-z].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Senha deve conter pelo menos uma letra minúscula")
                   .addConstraintViolation();
            return false;
        }
        
        return true;
    }
}
