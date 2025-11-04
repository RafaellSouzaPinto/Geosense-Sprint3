package geosense.Geosense.controller;

import geosense.Geosense.dto.MotoDTO;
import geosense.Geosense.dto.PatioDTO;
import geosense.Geosense.entity.Moto;
import geosense.Geosense.entity.StatusVaga;
import geosense.Geosense.entity.Vaga;
import geosense.Geosense.repository.AlocacaoMotoRepository;
import geosense.Geosense.repository.MotoRepository;
import geosense.Geosense.repository.VagaRepository;
import geosense.Geosense.service.PatioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - MotoController")
class MotoControllerTest {

    @Mock
    private MotoRepository motoRepository;

    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private PatioService patioService;

    @Mock
    private AlocacaoMotoRepository alocacaoRepository;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private MotoController motoController;

    private MotoDTO motoDTOValida;
    private Moto motoEntity;
    private Vaga vagaDisponivel;

    @BeforeEach
    void setUp() {
        motoDTOValida = new MotoDTO(
                null,
                "Honda CB 600F",
                "ABC1234",
                "CHASSI123456789",
                "reparos simples",
                1L
        );

        motoEntity = new Moto();
        motoEntity.setId(1L);
        motoEntity.setModelo("Honda CB 600F");
        motoEntity.setPlaca("ABC1234");
        motoEntity.setChassi("CHASSI123456789");
        motoEntity.setProblemaIdentificado("reparos simples");

        vagaDisponivel = new Vaga();
        vagaDisponivel.setId(1L);
        vagaDisponivel.setNumero(1);
        vagaDisponivel.setStatus(StatusVaga.DISPONIVEL);
        vagaDisponivel.setMoto(null);
    }

    @Test
    @DisplayName("Caso 1: Deve retornar view de listagem de motos")
    void deveRetornarViewDeListagem() {
        List<Moto> motos = Arrays.asList(motoEntity);
        when(motoRepository.findAll()).thenReturn(motos);

        String view = motoController.list(model);

        assertEquals("motos/list", view);
        verify(motoRepository, times(1)).findAll();
        verify(model, times(1)).addAttribute("motos", motos);
    }

    @Test
    @DisplayName("Caso 2: Deve retornar formulário de criação de moto")
    void deveRetornarFormularioCriacao() {
        List<PatioDTO> patios = Arrays.asList(new PatioDTO(1L, "SP", "Rua Teste", "Unidade Teste", 10, null, 5, 5));
        List<Vaga> vagas = Arrays.asList(vagaDisponivel);

        when(patioService.listarTodos()).thenReturn(patios);
        when(vagaRepository.findAll()).thenReturn(vagas);

        String view = motoController.createForm(model);

        assertEquals("motos/form", view);
        verify(patioService, times(1)).listarTodos();
        verify(vagaRepository, times(1)).findAll();
        verify(model, times(1)).addAttribute(eq("moto"), any(MotoDTO.class));
        verify(model, times(1)).addAttribute("patios", patios);
        verify(model, times(1)).addAttribute("vagas", vagas);
    }

    @Test
    @DisplayName("Caso 3: Deve criar moto com sucesso quando dados são válidos")
    void deveCriarMotoComSucesso() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vagaDisponivel));
        when(motoRepository.existsByVagaId(1L)).thenReturn(false);
        when(motoRepository.save(any(Moto.class))).thenReturn(motoEntity);

        String view = motoController.create(motoDTOValida, bindingResult, redirectAttributes, model);

        assertEquals("redirect:/motos", view);
        verify(motoRepository, atLeastOnce()).save(any(Moto.class));
        verify(vagaRepository, atLeastOnce()).save(any(Vaga.class));
        verify(redirectAttributes, times(1)).addFlashAttribute("success", "Moto criada com sucesso");
    }

    @Test
    @DisplayName("Caso 4: Deve retornar formulário quando há erros de validação")
    void deveRetornarFormularioComErrosValidacao() {
        when(bindingResult.hasErrors()).thenReturn(true);
        List<PatioDTO> patios = Arrays.asList(new PatioDTO(1L, "SP", "Rua Teste", "Unidade Teste", 10, null, 5, 5));
        List<Vaga> vagas = Arrays.asList(vagaDisponivel);

        when(patioService.listarTodos()).thenReturn(patios);
        when(vagaRepository.findAll()).thenReturn(vagas);

        String view = motoController.create(motoDTOValida, bindingResult, redirectAttributes, model);

        assertEquals("motos/form", view);
        verify(motoRepository, never()).save(any(Moto.class));
        verify(model, times(1)).addAttribute("moto", motoDTOValida);
        verify(model, times(1)).addAttribute("patios", patios);
        verify(model, times(1)).addAttribute("vagas", vagas);
    }

    @Test
    @DisplayName("Caso 5: Deve lançar exceção quando vaga não está disponível")
    void deveLancarExcecaoQuandoVagaNaoDisponivel() {
        Vaga vagaOcupada = new Vaga();
        vagaOcupada.setId(1L);
        vagaOcupada.setNumero(1);
        vagaOcupada.setStatus(StatusVaga.OCUPADA);
        vagaOcupada.setMoto(motoEntity);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vagaOcupada));

        String view = motoController.create(motoDTOValida, bindingResult, redirectAttributes, model);

        assertEquals("redirect:/motos", view);
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("error"), anyString());
        verify(motoRepository, never()).save(any(Moto.class));
    }

    @Test
    @DisplayName("Caso 6: Deve retornar formulário de edição")
    void deveRetornarFormularioEdicao() {
        Long motoId = 1L;
        List<PatioDTO> patios = Arrays.asList(new PatioDTO(1L, "SP", "Rua Teste", "Unidade Teste", 10, null, 5, 5));
        List<Vaga> vagas = Arrays.asList(vagaDisponivel);

        when(motoRepository.findById(motoId)).thenReturn(Optional.of(motoEntity));
        when(patioService.listarTodos()).thenReturn(patios);
        when(vagaRepository.findAll()).thenReturn(vagas);

        String view = motoController.editForm(motoId, model);

        assertEquals("motos/form", view);
        verify(motoRepository, times(1)).findById(motoId);
        verify(model, times(1)).addAttribute(eq("moto"), any(MotoDTO.class));
        verify(model, times(1)).addAttribute("id", motoId);
        verify(model, times(1)).addAttribute("patios", patios);
        verify(model, times(1)).addAttribute("vagas", vagas);
    }

    @Test
    @DisplayName("Caso 7: Deve atualizar moto com sucesso")
    void deveAtualizarMotoComSucesso() {
        Long motoId = 1L;
        MotoDTO dtoAtualizado = new MotoDTO(
                1L,
                "Honda CB 600F Atualizado",
                "ABC1234",
                "CHASSI123456789",
                "motor defeituoso",
                1L
        );

        when(bindingResult.hasErrors()).thenReturn(false);
        when(motoRepository.findById(motoId)).thenReturn(Optional.of(motoEntity));
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vagaDisponivel));
        when(motoRepository.save(any(Moto.class))).thenReturn(motoEntity);
        when(vagaRepository.save(any(Vaga.class))).thenReturn(vagaDisponivel);

        String view = motoController.update(motoId, dtoAtualizado, bindingResult, redirectAttributes, model);

        assertEquals("redirect:/motos", view);
        verify(motoRepository, times(1)).findById(motoId);
        verify(motoRepository, atLeastOnce()).save(any(Moto.class));
        verify(redirectAttributes, times(1)).addFlashAttribute("success", "Moto atualizada com sucesso");
    }

    @Test
    @DisplayName("Caso 8: Deve excluir moto com sucesso")
    void deveExcluirMotoComSucesso() {
        Long motoId = 1L;
        motoEntity.setVaga(null);

        when(motoRepository.findById(motoId)).thenReturn(Optional.of(motoEntity));
        when(alocacaoRepository.findHistoricoByMoto(motoEntity)).thenReturn(Arrays.asList());
        doNothing().when(motoRepository).delete(motoEntity);

        String view = motoController.delete(motoId, redirectAttributes);

        assertEquals("redirect:/motos", view);
        verify(motoRepository, times(1)).findById(motoId);
        verify(motoRepository, times(1)).delete(motoEntity);
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("success"), anyString());
    }

    @Test
    @DisplayName("Caso 9: Deve rejeitar criação quando placa e chassi estão vazios")
    void deveRejeitarCriacaoQuandoPlacaEChassiVazios() {
        MotoDTO dtoInvalido = new MotoDTO(
                null,
                "Honda CB 600F",
                "",
                "",
                "reparos simples",
                null
        );

        List<PatioDTO> patios = Arrays.asList(new PatioDTO(1L, "SP", "Rua Teste", "Unidade Teste", 10, null, 5, 5));
        List<Vaga> vagas = Arrays.asList(vagaDisponivel);

        when(bindingResult.hasErrors()).thenReturn(true);
        doNothing().when(bindingResult).rejectValue(anyString(), anyString(), anyString());
        when(patioService.listarTodos()).thenReturn(patios);
        when(vagaRepository.findAll()).thenReturn(vagas);

        String view = motoController.create(dtoInvalido, bindingResult, redirectAttributes, model);

        assertEquals("motos/form", view);
        verify(motoRepository, never()).save(any(Moto.class));
        verify(bindingResult, times(1)).rejectValue(eq("placa"), eq("required"), anyString());
        verify(model, times(1)).addAttribute("moto", dtoInvalido);
        verify(model, times(1)).addAttribute("patios", patios);
        verify(model, times(1)).addAttribute("vagas", vagas);
    }

    @Test
    @DisplayName("Caso 10: Deve criar moto sem vaga quando vagaId é null")
    void deveCriarMotoSemVaga() {
        MotoDTO dtoSemVaga = new MotoDTO(
                null,
                "Honda CB 600F",
                "ABC1234",
                null,
                "reparos simples",
                null
        );

        when(bindingResult.hasErrors()).thenReturn(false);
        when(motoRepository.save(any(Moto.class))).thenReturn(motoEntity);

        String view = motoController.create(dtoSemVaga, bindingResult, redirectAttributes, model);

        assertEquals("redirect:/motos", view);
        verify(motoRepository, times(1)).save(any(Moto.class));
        verify(vagaRepository, never()).findById(anyLong());
        verify(redirectAttributes, times(1)).addFlashAttribute("success", "Moto criada com sucesso");
    }
}
