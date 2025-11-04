package geosense.Geosense.service;

import geosense.Geosense.dto.PatioDTO;
import geosense.Geosense.entity.Patio;
import geosense.Geosense.entity.StatusVaga;
import geosense.Geosense.entity.Vaga;
import geosense.Geosense.repository.MotoRepository;
import geosense.Geosense.repository.PatioRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - PatioService")
class PatioServiceTest {

    @Mock
    private PatioRepository patioRepository;

    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private MotoRepository motoRepository;

    @InjectMocks
    private PatioService patioService;

    private PatioDTO patioDTO;
    private Patio patioEntity;
    private Vaga vaga1;
    private Vaga vaga2;

    @BeforeEach
    void setUp() {
        patioDTO = new PatioDTO(
                null,
                "São Paulo",
                "Rua das Flores, 123",
                "Unidade Centro",
                10,
                null,
                0,
                0
        );

        patioEntity = new Patio();
        patioEntity.setId(1L);
        patioEntity.setLocalizacao("São Paulo");
        patioEntity.setEnderecoDetalhado("Rua das Flores, 123");
        patioEntity.setNomeUnidade("Unidade Centro");
        patioEntity.setCapacidade(10);

        vaga1 = new Vaga();
        vaga1.setId(1L);
        vaga1.setNumero(1);
        vaga1.setStatus(StatusVaga.DISPONIVEL);

        vaga2 = new Vaga();
        vaga2.setId(2L);
        vaga2.setNumero(2);
        vaga2.setStatus(StatusVaga.OCUPADA);
    }

    @Test
    @DisplayName("Caso 1: Deve criar pátio com sucesso e gerar vagas")
    void deveCriarPatioComSucesso() {
        when(patioRepository.save(any(Patio.class))).thenReturn(patioEntity);
        when(vagaRepository.countByPatioIdAndStatus(anyLong(), any(StatusVaga.class))).thenReturn(0L);
        when(vagaRepository.countByPatioId(anyLong())).thenReturn(10L);
        when(vagaRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        PatioDTO resultado = patioService.criarPatio(patioDTO);

        assertNotNull(resultado);
        assertEquals("São Paulo", resultado.getLocalizacao());
        assertEquals("Unidade Centro", resultado.getNomeUnidade());
        assertEquals(10, resultado.getCapacidade());
        verify(patioRepository, times(1)).save(any(Patio.class));
        verify(vagaRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Caso 2: Deve buscar pátio por ID")
    void deveBuscarPatioPorId() {
        Long patioId = 1L;
        when(patioRepository.findById(patioId)).thenReturn(Optional.of(patioEntity));
        when(vagaRepository.countByPatioIdAndStatus(anyLong(), any(StatusVaga.class))).thenReturn(5L);
        when(vagaRepository.countByPatioId(anyLong())).thenReturn(10L);

        Optional<PatioDTO> resultado = patioService.buscarPorId(patioId);

        assertTrue(resultado.isPresent());
        assertEquals("Unidade Centro", resultado.get().getNomeUnidade());
        verify(patioRepository, times(1)).findById(patioId);
    }

    @Test
    @DisplayName("Caso 3: Deve retornar Optional vazio quando pátio não existe")
    void deveRetornarOptionalVazioQuandoPatioNaoExiste() {
        Long patioId = 999L;
        when(patioRepository.findById(patioId)).thenReturn(Optional.empty());

        Optional<PatioDTO> resultado = patioService.buscarPorId(patioId);

        assertFalse(resultado.isPresent());
        verify(patioRepository, times(1)).findById(patioId);
    }

    @Test
    @DisplayName("Caso 4: Deve listar todos os pátios")
    void deveListarTodosOsPatios() {
        Patio patio2 = new Patio();
        patio2.setId(2L);
        patio2.setNomeUnidade("Unidade Norte");
        patio2.setCapacidade(5);

        List<Patio> patios = Arrays.asList(patioEntity, patio2);
        when(patioRepository.findAll()).thenReturn(patios);
        when(vagaRepository.countByPatioIdAndStatus(anyLong(), any(StatusVaga.class))).thenReturn(0L);
        when(vagaRepository.countByPatioId(anyLong())).thenReturn(10L);

        List<PatioDTO> resultado = patioService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Unidade Centro", resultado.get(0).getNomeUnidade());
        assertEquals("Unidade Norte", resultado.get(1).getNomeUnidade());
        verify(patioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso 5: Deve atualizar pátio com sucesso")
    void deveAtualizarPatioComSucesso() {
        Long patioId = 1L;
        PatioDTO dtoAtualizado = new PatioDTO(
                1L,
                "Rio de Janeiro",
                "Avenida Atlântica, 456",
                "Unidade Copacabana",
                15,
                null,
                0,
                0
        );

        when(patioRepository.findById(patioId)).thenReturn(Optional.of(patioEntity));
        when(patioRepository.save(any(Patio.class))).thenReturn(patioEntity);
        when(vagaRepository.countByPatioId(anyLong())).thenReturn(10L);
        when(vagaRepository.countByPatioIdAndStatus(anyLong(), any(StatusVaga.class))).thenReturn(0L);
        when(vagaRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        PatioDTO resultado = patioService.atualizar(patioId, dtoAtualizado);

        assertNotNull(resultado);
        assertEquals("Rio de Janeiro", resultado.getLocalizacao());
        assertEquals("Unidade Copacabana", resultado.getNomeUnidade());
        verify(patioRepository, times(1)).findById(patioId);
        verify(patioRepository, times(1)).save(any(Patio.class));
    }

    @Test
    @DisplayName("Caso 6: Deve lançar exceção ao atualizar pátio inexistente")
    void deveLancarExcecaoAoAtualizarPatioInexistente() {
        Long patioId = 999L;
        when(patioRepository.findById(patioId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            patioService.atualizar(patioId, patioDTO);
        });

        assertEquals("Pátio não encontrado", exception.getMessage());
        verify(patioRepository, never()).save(any(Patio.class));
    }

    @Test
    @DisplayName("Caso 7: Deve aumentar capacidade do pátio")
    void deveAumentarCapacidadeDoPatio() {
        Long patioId = 1L;
        PatioDTO dtoAtualizado = new PatioDTO(
                1L,
                "São Paulo",
                "Rua das Flores, 123",
                "Unidade Centro",
                15,
                null,
                0,
                0
        );

        when(patioRepository.findById(patioId)).thenReturn(Optional.of(patioEntity));
        when(vagaRepository.countByPatioId(patioId)).thenReturn(10L);
        when(vagaRepository.countByPatioIdAndStatus(anyLong(), any(StatusVaga.class))).thenReturn(0L);
        when(vagaRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
        when(patioRepository.save(any(Patio.class))).thenReturn(patioEntity);

        PatioDTO resultado = patioService.atualizar(patioId, dtoAtualizado);

        assertNotNull(resultado);
        verify(vagaRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Caso 8: Deve lançar exceção ao reduzir capacidade com vagas ocupadas")
    void deveLancarExcecaoAoReduzirCapacidadeComVagasOcupadas() {
        Long patioId = 1L;
        PatioDTO dtoAtualizado = new PatioDTO(
                1L,
                "São Paulo",
                "Rua das Flores, 123",
                "Unidade Centro",
                1,
                null,
                0,
                0
        );

        patioEntity.setCapacidade(10);
        when(patioRepository.findById(patioId)).thenReturn(Optional.of(patioEntity));
        when(vagaRepository.countByPatioId(patioId)).thenReturn(10L);
        when(vagaRepository.findByPatioIdOrderByNumeroAsc(patioId)).thenReturn(Arrays.asList(vaga1, vaga2));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            patioService.atualizar(patioId, dtoAtualizado);
        });

        assertTrue(exception.getMessage().contains("Não é possível reduzir a capacidade"));
        verify(vagaRepository, never()).deleteAll(anyList());
    }

    @Test
    @DisplayName("Caso 9: Deve deletar pátio com sucesso")
    void deveDeletarPatioComSucesso() {
        Long patioId = 1L;
        List<Vaga> vagas = Arrays.asList(vaga1, vaga2);
        
        when(patioRepository.existsById(patioId)).thenReturn(true);
        when(vagaRepository.findByPatioIdOrderByNumeroAsc(patioId)).thenReturn(vagas);
        doNothing().when(patioRepository).deleteById(patioId);
        when(vagaRepository.saveAll(anyList())).thenReturn(vagas);

        patioService.deletar(patioId);

        verify(patioRepository, times(1)).existsById(patioId);
        verify(vagaRepository, times(1)).findByPatioIdOrderByNumeroAsc(patioId);
        verify(patioRepository, times(1)).deleteById(patioId);
    }

    @Test
    @DisplayName("Caso 10: Deve lançar exceção ao deletar pátio inexistente")
    void deveLancarExcecaoAoDeletarPatioInexistente() {
        Long patioId = 999L;
        when(patioRepository.existsById(patioId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            patioService.deletar(patioId);
        });

        assertEquals("Pátio não encontrado", exception.getMessage());
        verify(patioRepository, never()).deleteById(anyLong());
    }
}
