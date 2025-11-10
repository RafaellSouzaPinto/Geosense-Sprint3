package geosense.Geosense.service;

import geosense.Geosense.dto.MotoDTO;
import geosense.Geosense.entity.Moto;
import geosense.Geosense.entity.StatusVaga;
import geosense.Geosense.entity.Vaga;
import geosense.Geosense.repository.MotoRepository;
import geosense.Geosense.repository.VagaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - MotoService")
class MotoServiceTest {

    @Mock
    private MotoRepository motoRepository;

    @Mock
    private VagaRepository vagaRepository;

    @InjectMocks
    private MotoService motoService;

    private MotoDTO motoDTOValida;
    private Moto motoEntity;
    private Vaga vagaDisponivel;

    @BeforeEach
    void setUp() {
        motoDTOValida = new MotoDTO(
                null,
                "Mottu E",
                "ABC1234",
                "CHASSI123456789",
                "reparos simples",
                null
        );

        motoEntity = new Moto();
        motoEntity.setId(1L);
        motoEntity.setModelo("Mottu E");
        motoEntity.setPlaca("ABC1234");
        motoEntity.setChassi("CHASSI123456789");
        motoEntity.setProblemaIdentificado("reparos simples");

        vagaDisponivel = new Vaga();
        vagaDisponivel.setId(1L);
        vagaDisponivel.setNumero(1);
        vagaDisponivel.setStatus(StatusVaga.DISPONIVEL);
    }

    @Test
    @DisplayName("Caso 1: Deve registrar moto com sucesso quando dados são válidos")
    void deveRegistrarMotoComSucesso() {
        when(motoRepository.save(any(Moto.class))).thenReturn(motoEntity);

        MotoDTO resultado = motoService.registrar(motoDTOValida);

        assertNotNull(resultado);
        assertEquals("Mottu E", resultado.getModelo());
        assertEquals("ABC1234", resultado.getPlaca());
        assertEquals("CHASSI123456789", resultado.getChassi());
        assertEquals("reparos simples", resultado.getProblemaIdentificado());
        verify(motoRepository, times(1)).save(any(Moto.class));
    }

    @Test
    @DisplayName("Caso 2: Deve lançar exceção quando placa e chassi estão vazios")
    void deveLancarExcecaoQuandoPlacaEChassiVazios() {
        MotoDTO dtoInvalido = new MotoDTO(
                null,
                "Mottu E",
                "",
                "",
                "reparos simples",
                null
        );

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            motoService.registrar(dtoInvalido);
        });

        assertEquals("Informe a placa ou o chassi obrigatoriamente.", exception.getMessage());
        verify(motoRepository, never()).save(any(Moto.class));
    }

    @Test
    @DisplayName("Caso 3: Deve registrar moto apenas com placa (sem chassi)")
    void deveRegistrarMotoApenasComPlaca() {
        MotoDTO dtoComPlaca = new MotoDTO(
                null,
                "Mottu Pop",
                "XYZ9876",
                null,
                null,
                null
        );

        Moto motoSalva = new Moto();
        motoSalva.setId(2L);
        motoSalva.setModelo("Mottu Pop");
        motoSalva.setPlaca("XYZ9876");
        motoSalva.setChassi(null);

        when(motoRepository.save(any(Moto.class))).thenReturn(motoSalva);

        MotoDTO resultado = motoService.registrar(dtoComPlaca);

        assertNotNull(resultado);
        assertEquals("Mottu Pop", resultado.getModelo());
        assertEquals("XYZ9876", resultado.getPlaca());
        assertNull(resultado.getChassi());
        verify(motoRepository, times(1)).save(any(Moto.class));
    }

    @Test
    @DisplayName("Caso 4: Deve registrar moto apenas com chassi (sem placa)")
    void deveRegistrarMotoApenasComChassi() {
        MotoDTO dtoComChassi = new MotoDTO(
                null,
                "Mottu Sport",
                null,
                "CHASSI987654321",
                null,
                null
        );

        Moto motoSalva = new Moto();
        motoSalva.setId(3L);
        motoSalva.setModelo("Mottu Sport");
        motoSalva.setPlaca(null);
        motoSalva.setChassi("CHASSI987654321");

        when(motoRepository.save(any(Moto.class))).thenReturn(motoSalva);

        MotoDTO resultado = motoService.registrar(dtoComChassi);

        assertNotNull(resultado);
        assertEquals("Mottu Sport", resultado.getModelo());
        assertNull(resultado.getPlaca());
        assertEquals("CHASSI987654321", resultado.getChassi());
        verify(motoRepository, times(1)).save(any(Moto.class));
    }

    @Test
    @DisplayName("Caso 5: Deve lançar exceção quando problema identificado é inválido")
    void deveLancarExcecaoQuandoProblemaInvalido() {
        MotoDTO dtoComProblemaInvalido = new MotoDTO(
                null,
                "Mottu E",
                "ABC1234",
                null,
                "problema inexistente",
                null
        );

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            motoService.registrar(dtoComProblemaInvalido);
        });

        assertTrue(exception.getMessage().contains("Problema identificado inválido"));
        verify(motoRepository, never()).save(any(Moto.class));
    }

    @Test
    @DisplayName("Caso 6: Deve registrar moto com vaga associada")
    void deveRegistrarMotoComVaga() {
        MotoDTO dtoComVaga = new MotoDTO(
                null,
                "Mottu E",
                "ABC1234",
                null,
                "reparos simples",
                1L
        );

        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vagaDisponivel));

        Moto motoComVaga = new Moto();
        motoComVaga.setId(1L);
        motoComVaga.setModelo("Mottu E");
        motoComVaga.setPlaca("ABC1234");
        motoComVaga.setVaga(vagaDisponivel);

        when(motoRepository.save(any(Moto.class))).thenReturn(motoComVaga);

        MotoDTO resultado = motoService.registrar(dtoComVaga);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getVagaId());
        verify(vagaRepository, times(1)).findById(1L);
        verify(motoRepository, times(1)).save(any(Moto.class));
    }

    @Test
    @DisplayName("Caso 7: Deve listar todas as motos")
    void deveListarTodasAsMotos() {
        Moto moto1 = new Moto();
        moto1.setId(1L);
        moto1.setModelo("Mottu E");
        moto1.setPlaca("ABC1234");

        Moto moto2 = new Moto();
        moto2.setId(2L);
        moto2.setModelo("Mottu Pop");
        moto2.setPlaca("XYZ9876");

        when(motoRepository.findAll()).thenReturn(Arrays.asList(moto1, moto2));

        List<MotoDTO> resultado = motoService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Mottu E", resultado.get(0).getModelo());
        assertEquals("Mottu Pop", resultado.get(1).getModelo());
        verify(motoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso 8: Deve remover moto por ID")
    void deveRemoverMotoPorId() {
        Long motoId = 1L;
        doNothing().when(motoRepository).deleteById(motoId);

        motoService.remover(motoId);

        verify(motoRepository, times(1)).deleteById(motoId);
    }

    @Test
    @DisplayName("Caso 9: Deve aceitar problema identificado como null")
    void deveAceitarProblemaIdentificadoComoNull() {
        MotoDTO dtoSemProblema = new MotoDTO(
                null,
                "Mottu Sport",
                "ABC1234",
                null,
                null,
                null
        );

        Moto motoSalva = new Moto();
        motoSalva.setId(1L);
        motoSalva.setModelo("Mottu Sport");
        motoSalva.setPlaca("ABC1234");
        motoSalva.setProblemaIdentificado(null);

        when(motoRepository.save(any(Moto.class))).thenReturn(motoSalva);

        MotoDTO resultado = motoService.registrar(dtoSemProblema);

        assertNotNull(resultado);
        assertNull(resultado.getProblemaIdentificado());
        verify(motoRepository, times(1)).save(any(Moto.class));
    }

    @Test
    @DisplayName("Caso 10: Deve lançar exceção quando vaga não existe")
    void deveLancarExcecaoQuandoVagaNaoExiste() {
        MotoDTO dtoComVagaInexistente = new MotoDTO(
                null,
                "Mottu E",
                "ABC1234",
                null,
                "reparos simples",
                999L
        );

        when(vagaRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            motoService.registrar(dtoComVagaInexistente);
        });

        assertEquals("Vaga não encontrada", exception.getMessage());
        verify(vagaRepository, times(1)).findById(999L);
        verify(motoRepository, never()).save(any(Moto.class));
    }
}
