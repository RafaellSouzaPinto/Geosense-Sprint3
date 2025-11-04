package geosense.Geosense.controller;

import geosense.Geosense.repository.AlocacaoMotoRepository;
import geosense.Geosense.repository.MotoRepository;
import geosense.Geosense.repository.PatioRepository;
import geosense.Geosense.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - HomeController")
class HomeControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PatioRepository patioRepository;

    @Mock
    private MotoRepository motoRepository;

    @Mock
    private AlocacaoMotoRepository alocacaoMotoRepository;

    @Mock
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        lenient().when(usuarioRepository.count()).thenReturn(5L);
        lenient().when(patioRepository.count()).thenReturn(3L);
        lenient().when(motoRepository.count()).thenReturn(10L);
        lenient().when(alocacaoMotoRepository.count()).thenReturn(8L);
    }

    @Test
    @DisplayName("Caso 1: Deve retornar view index na rota raiz")
    void deveRetornarViewIndex() {
        String view = homeController.index();

        assertEquals("index", view);
    }

    @Test
    @DisplayName("Caso 2: Deve retornar view login na rota /login")
    void deveRetornarViewLogin() {
        String view = homeController.login();

        assertEquals("login", view);
    }

    @Test
    @DisplayName("Caso 3: Deve retornar view admin com contadores corretos")
    void deveRetornarViewAdminComContadores() {
        String view = homeController.admin(model);

        assertEquals("admin", view);
        verify(usuarioRepository, times(1)).count();
        verify(patioRepository, times(1)).count();
        verify(motoRepository, times(1)).count();
        verify(alocacaoMotoRepository, times(1)).count();
        verify(model, times(1)).addAttribute("userCount", 5L);
        verify(model, times(1)).addAttribute("patioCount", 3L);
        verify(model, times(1)).addAttribute("motoCount", 10L);
        verify(model, times(1)).addAttribute("alocacaoCount", 8L);
    }

    @Test
    @DisplayName("Caso 4: Deve retornar contadores zerados quando não há dados")
    void deveRetornarContadoresZerados() {
        when(usuarioRepository.count()).thenReturn(0L);
        when(patioRepository.count()).thenReturn(0L);
        when(motoRepository.count()).thenReturn(0L);
        when(alocacaoMotoRepository.count()).thenReturn(0L);

        String view = homeController.admin(model);

        assertEquals("admin", view);
        verify(model, times(1)).addAttribute("userCount", 0L);
        verify(model, times(1)).addAttribute("patioCount", 0L);
        verify(model, times(1)).addAttribute("motoCount", 0L);
        verify(model, times(1)).addAttribute("alocacaoCount", 0L);
    }

    @Test
    @DisplayName("Caso 5: Deve retornar contadores com valores grandes")
    void deveRetornarContadoresComValoresGrandes() {
        when(usuarioRepository.count()).thenReturn(1000L);
        when(patioRepository.count()).thenReturn(500L);
        when(motoRepository.count()).thenReturn(5000L);
        when(alocacaoMotoRepository.count()).thenReturn(4500L);

        String view = homeController.admin(model);

        assertEquals("admin", view);
        verify(model, times(1)).addAttribute("userCount", 1000L);
        verify(model, times(1)).addAttribute("patioCount", 500L);
        verify(model, times(1)).addAttribute("motoCount", 5000L);
        verify(model, times(1)).addAttribute("alocacaoCount", 4500L);
    }
}
