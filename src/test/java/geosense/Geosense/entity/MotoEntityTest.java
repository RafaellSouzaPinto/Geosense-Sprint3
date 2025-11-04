package geosense.Geosense.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários - Moto Entity")
class MotoEntityTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Caso 1: Deve criar moto válida")
    void deveCriarMotoValida() {
        Moto moto = new Moto();
        moto.setModelo("Honda CB 600F");
        moto.setPlaca("ABC1234");
        moto.setChassi("CHASSI123456789");

        assertNotNull(moto);
        assertEquals("Honda CB 600F", moto.getModelo());
        assertEquals("ABC1234", moto.getPlaca());
        assertEquals("CHASSI123456789", moto.getChassi());

        Set<ConstraintViolation<Moto>> violations = validator.validate(moto);
        assertTrue(violations.isEmpty(), "Moto deve ser válida");
    }

    @Test
    @DisplayName("Caso 2: Deve falhar validação quando modelo é null")
    void deveFalharValidacaoQuandoModeloNull() {
        Moto moto = new Moto();
        moto.setModelo(null);
        moto.setPlaca("ABC1234");

        Set<ConstraintViolation<Moto>> violations = validator.validate(moto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("modelo")));
    }

    @Test
    @DisplayName("Caso 3: Deve falhar validação quando modelo está vazio")
    void deveFalharValidacaoQuandoModeloVazio() {
        Moto moto = new Moto();
        moto.setModelo("");
        moto.setPlaca("ABC1234");

        Set<ConstraintViolation<Moto>> violations = validator.validate(moto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("modelo")));
    }

    @Test
    @DisplayName("Caso 4: Deve falhar validação quando modelo excede tamanho máximo")
    void deveFalharValidacaoQuandoModeloExcedeTamanho() {
        Moto moto = new Moto();
        moto.setModelo("A".repeat(51)); // Excede 50 caracteres
        moto.setPlaca("ABC1234");

        Set<ConstraintViolation<Moto>> violations = validator.validate(moto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("modelo")));
    }

    @Test
    @DisplayName("Caso 5: Deve aceitar placa nula (opcional)")
    void deveAceitarPlacaNula() {
        Moto moto = new Moto();
        moto.setModelo("Honda CB 600F");
        moto.setPlaca(null);
        moto.setChassi("CHASSI123456789");

        Set<ConstraintViolation<Moto>> violations = validator.validate(moto);

        assertTrue(violations.stream()
                .noneMatch(v -> v.getPropertyPath().toString().equals("placa") 
                        && v.getMessage().contains("null")));
    }

    @Test
    @DisplayName("Caso 6: Deve aceitar chassi nulo (opcional)")
    void deveAceitarChassiNulo() {
        Moto moto = new Moto();
        moto.setModelo("Honda CB 600F");
        moto.setPlaca("ABC1234");
        moto.setChassi(null);

        Set<ConstraintViolation<Moto>> violations = validator.validate(moto);

        assertTrue(violations.stream()
                .noneMatch(v -> v.getPropertyPath().toString().equals("chassi") 
                        && v.getMessage().contains("null")));
    }

    @Test
    @DisplayName("Caso 7: Deve configurar e recuperar vaga corretamente")
    void deveConfigurarERecuperarVaga() {
        Moto moto = new Moto();
        moto.setModelo("Honda CB 600F");
        
        Vaga vaga = new Vaga();
        vaga.setId(1L);
        vaga.setNumero(1);
        vaga.setStatus(StatusVaga.DISPONIVEL);

        moto.setVaga(vaga);

        assertNotNull(moto.getVaga());
        assertEquals(1L, moto.getVaga().getId());
        assertEquals(1, moto.getVaga().getNumero());
    }

    @Test
    @DisplayName("Caso 8: Deve aceitar problema identificado como null")
    void deveAceitarProblemaIdentificadoNull() {
        Moto moto = new Moto();
        moto.setModelo("Honda CB 600F");
        moto.setPlaca("ABC1234");
        moto.setProblemaIdentificado(null);

        assertNull(moto.getProblemaIdentificado());
        Set<ConstraintViolation<Moto>> violations = validator.validate(moto);
        assertTrue(violations.isEmpty(), "Moto deve ser válida mesmo com problema null");
    }

    @Test
    @DisplayName("Caso 9: Deve configurar e recuperar defeitos corretamente")
    void deveConfigurarERecuperarDefeitos() {
        Moto moto = new Moto();
        moto.setModelo("Honda CB 600F");

        Defeito defeito1 = new Defeito();
        defeito1.setId(1L);

        Defeito defeito2 = new Defeito();
        defeito2.setId(2L);

        moto.setDefeitos(java.util.Arrays.asList(defeito1, defeito2));

        assertNotNull(moto.getDefeitos());
        assertEquals(2, moto.getDefeitos().size());
    }

    @Test
    @DisplayName("Caso 10: Deve configurar e recuperar histórico de alocações")
    void deveConfigurarERecuperarHistoricoAlocacoes() {
        Moto moto = new Moto();
        moto.setModelo("Honda CB 600F");

        AlocacaoMoto alocacao1 = new AlocacaoMoto();
        AlocacaoMoto alocacao2 = new AlocacaoMoto();

        moto.setHistoricoAlocacoes(java.util.Arrays.asList(alocacao1, alocacao2));

        assertNotNull(moto.getHistoricoAlocacoes());
        assertEquals(2, moto.getHistoricoAlocacoes().size());
    }

    @Test
    @DisplayName("Caso 11: Deve usar construtor completo corretamente")
    void deveUsarConstrutorCompleto() {
        Vaga vaga = new Vaga();
        vaga.setId(1L);

        Defeito defeito = new Defeito();
        defeito.setId(1L);

        AlocacaoMoto alocacao = new AlocacaoMoto();

        Moto moto = new Moto(
                1L,
                "Honda CB 600F",
                "ABC1234",
                "CHASSI123456789",
                "reparos simples",
                vaga,
                java.util.Arrays.asList(defeito),
                java.util.Arrays.asList(alocacao)
        );

        assertEquals(1L, moto.getId());
        assertEquals("Honda CB 600F", moto.getModelo());
        assertEquals("ABC1234", moto.getPlaca());
        assertEquals("CHASSI123456789", moto.getChassi());
        assertEquals("reparos simples", moto.getProblemaIdentificado());
        assertNotNull(moto.getVaga());
        assertNotNull(moto.getDefeitos());
        assertNotNull(moto.getHistoricoAlocacoes());
    }
}
