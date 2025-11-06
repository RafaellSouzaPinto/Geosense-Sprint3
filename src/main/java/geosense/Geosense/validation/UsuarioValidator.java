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

            String senha = value.getSenha() != null ? value.getSenha() : "";
            String email = value.getEmail() != null ? value.getEmail() : "";

            ValidacaoOracleService.ResultadoValidacao resultado =
                validacaoOracleService.validarSenhaELimites(
                    senha, 
                    email, 
                    tipoUsuario, 
                    operacao
                );

            if (!resultado.isValid()) {
                context.disableDefaultConstraintViolation();
                
                System.out.println("=== USUARIO VALIDATOR - ERRO DETECTADO ===");
                System.out.println("Status: " + resultado.getStatus());
                System.out.println("Mensagem: " + resultado.getMensagem());
                System.out.println("Total Erros: " + resultado.getTotalErros());
                System.out.println("Erros: " + resultado.getErros());
                
                if (resultado.getErros() != null && !resultado.getErros().isEmpty()) {
                    String[] erros = resultado.getErros().split(";");
                    System.out.println("Erros separados: " + java.util.Arrays.toString(erros));
                    
                    for (String erro : erros) {
                        if (erro.trim().isEmpty()) continue;
                        
                        String erroLower = erro.trim().toLowerCase();
                        System.out.println("Processando erro: " + erro.trim());
                        
                        if (erroLower.contains("senha") ||
                            erroLower.contains("caracteres") ||
                            erroLower.contains("número") ||
                            erroLower.contains("maiúscula") ||
                            erroLower.contains("minúscula") ||
                            erroLower.contains("espaços")) {
                            System.out.println("  -> Adicionando como erro de SENHA");
                            context.buildConstraintViolationWithTemplate(erro.trim())
                                   .addPropertyNode("senha")
                                   .addConstraintViolation();
                        } 
                        else if (erroLower.contains("email") ||
                                 erroLower.contains("formato") ||
                                 erroLower.contains("válido") ||
                                 erroLower.contains("dominio")) {
                            System.out.println("  -> Adicionando como erro de EMAIL");
                            context.buildConstraintViolationWithTemplate(erro.trim())
                                   .addPropertyNode("email")
                                   .addConstraintViolation();
                        } 
                        else {
                            System.out.println("  -> Adicionando como erro genérico");
                            context.buildConstraintViolationWithTemplate(erro.trim())
                                   .addConstraintViolation();
                        }
                    }
                } else {
                    System.out.println("  -> Usando mensagem genérica");
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

