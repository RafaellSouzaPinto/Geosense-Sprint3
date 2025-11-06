package geosense.Geosense.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;


@Service
public class ValidacaoOracleService {

    private final DataSource dataSource;
    private final ObjectMapper objectMapper;

    @Autowired
    public ValidacaoOracleService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.objectMapper = new ObjectMapper();
    }

    public ResultadoValidacao validarSenhaELimites(String senha, String email, String tipoUsuario, String operacao) {
        String sql = "{ ? = call FN_VALIDAR_SENHA_E_LIMITES(?, ?, ?, ?) }";
        
        try (Connection connection = dataSource.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {
            
            callableStatement.registerOutParameter(1, Types.VARCHAR);
            
            callableStatement.setString(2, senha);
            callableStatement.setString(3, email);
            callableStatement.setString(4, tipoUsuario);
            callableStatement.setString(5, operacao);
            
            callableStatement.execute();
            
            String resultadoJson = callableStatement.getString(1);
            
            // Log para debug
            System.out.println("=== DEBUG VALIDAÇÃO ORACLE ===");
            System.out.println("Senha: " + (senha != null ? senha.substring(0, Math.min(3, senha.length())) + "***" : "null"));
            System.out.println("Email: " + email);
            System.out.println("Tipo: " + tipoUsuario);
            System.out.println("Operação: " + operacao);
            System.out.println("Resultado JSON: " + resultadoJson);
            System.out.println("==============================");
            
            return parseResultado(resultadoJson);
            
        } catch (SQLException e) {
            System.err.println("ERRO SQL ao chamar função Oracle: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao chamar função de validação Oracle: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("ERRO ao processar resultado: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao processar resultado da validação: " + e.getMessage(), e);
        }
    }

    private ResultadoValidacao parseResultado(String resultadoJson) {
        try {
            System.out.println("Parseando JSON: " + resultadoJson);
            
            JsonNode jsonNode = objectMapper.readTree(resultadoJson);
            
            String status = jsonNode.get("status").asText();
            String mensagem = jsonNode.get("mensagem").asText();
            int totalErros = jsonNode.has("total_erros") ? jsonNode.get("total_erros").asInt() : 0;
            
            String erros = "";
            if (jsonNode.has("erros")) {
                erros = jsonNode.get("erros").asText();
            }
            
            boolean isValid = "VALIDACAO_OK".equals(status);
            
            System.out.println("Status: " + status);
            System.out.println("Mensagem: " + mensagem);
            System.out.println("Total Erros: " + totalErros);
            System.out.println("Erros: " + erros);
            System.out.println("IsValid: " + isValid);
            
            return new ResultadoValidacao(isValid, status, mensagem, totalErros, erros);
            
        } catch (Exception e) {
            System.err.println("ERRO ao fazer parse do JSON: " + e.getMessage());
            System.err.println("JSON recebido: " + resultadoJson);
            e.printStackTrace();
            return new ResultadoValidacao(false, "VALIDACAO_ERRO",
                "Erro ao processar resultado: " + e.getMessage(), 1, 
                "Erro ao processar resposta da validação");
        }
    }

    public static class ResultadoValidacao {
        private final boolean valid;
        private final String status;
        private final String mensagem;
        private final int totalErros;
        private final String erros;

        public ResultadoValidacao(boolean valid, String status, String mensagem, int totalErros, String erros) {
            this.valid = valid;
            this.status = status;
            this.mensagem = mensagem;
            this.totalErros = totalErros;
            this.erros = erros;
        }

        public boolean isValid() {
            return valid;
        }

        public String getStatus() {
            return status;
        }

        public String getMensagem() {
            return mensagem;
        }

        public int getTotalErros() {
            return totalErros;
        }

        public String getErros() {
            return erros;
        }
    }
}

