package geosense.Geosense.validation;

import geosense.Geosense.dto.UsuarioDTO;
import geosense.Geosense.service.ValidacaoOracleService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsuarioValidator implements ConstraintValidator<ValidUsuario, UsuarioDTO> {

    private ValidacaoOracleService validacaoOracleService;

    @Override
    public void initialize(ValidUsuario constraintAnnotation) {
        try {
            this.validacaoOracleService = SpringContextHelper.getBean(ValidacaoOracleService.class);
        } catch (Exception e) {
            // Se não conseguir obter o serviço, validação falhará
            // Isso pode acontecer em testes ou inicialização
        }
    }

    @Override
    public boolean isValid(UsuarioDTO value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (validacaoOracleService == null) {
            return true;
        }

        try {
            String tipoUsuario = "MECANICO";

            String operacao = "VALIDACAO";

            ValidacaoOracleService.ResultadoValidacao resultado =
                validacaoOracleService.validarSenhaELimites(
                    value.getSenha() != null ? value.getSenha() : "", 
                    value.getEmail() != null ? value.getEmail() : "", 
                    tipoUsuario, 
                    operacao
                );

            if (!resultado.isValid()) {
                context.disableDefaultConstraintViolation();
                
                if (resultado.getErros() != null && !resultado.getErros().isEmpty()) {
                    String[] erros = resultado.getErros().split(";");
                    for (String erro : erros) {
                        if (erro.trim().isEmpty()) continue;
                        
                        String erroLower = erro.trim().toLowerCase();
                        
                        if (erroLower.contains("senha") ||
                            erroLower.contains("caracteres") ||
                            erroLower.contains("número") ||
                            erroLower.contains("maiúscula") ||
                            erroLower.contains("minúscula") ||
                            erroLower.contains("espaços")) {
                            context.buildConstraintViolationWithTemplate(erro.trim())
                                   .addPropertyNode("senha")
                                   .addConstraintViolation();
                        } 
                        else if (erroLower.contains("email") ||
                                 erroLower.contains("formato") ||
                                 erroLower.contains("válido") ||
                                 erroLower.contains("dominio")) {
                            context.buildConstraintViolationWithTemplate(erro.trim())
                                   .addPropertyNode("email")
                                   .addConstraintViolation();
                        } 
                        else {
                            context.buildConstraintViolationWithTemplate(erro.trim())
                                   .addConstraintViolation();
                        }
                    }
                } else {
                    context.buildConstraintViolationWithTemplate(resultado.getMensagem())
                           .addConstraintViolation();
                }
                
                return false;
            }

            return true;
        } catch (Exception e) {
            System.err.println("Erro ao validar com Oracle: " + e.getMessage());
            return true;
        }
    }
}

