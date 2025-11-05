package geosense.Geosense.controller;

import geosense.Geosense.dto.UsuarioDTO;
import geosense.Geosense.dto.UsuarioEditDTO;
import geosense.Geosense.dto.UsuarioComDependenciasDTO;
import geosense.Geosense.entity.TipoUsuario;
import geosense.Geosense.entity.Usuario;
import geosense.Geosense.repository.UsuarioRepository;
import geosense.Geosense.service.ValidacaoOracleService;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidacaoOracleService validacaoOracleService;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, ValidacaoOracleService validacaoOracleService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.validacaoOracleService = validacaoOracleService;
    }

    @GetMapping
    public String list(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        
        List<UsuarioComDependenciasDTO> usuariosComDependencias = usuarios.stream()
            .map(usuario -> {
                long alocacoesComoMecanico = usuarioRepository.countAlocacoesComoMecanico(usuario.getId());
                long alocacoesComoFinalizador = usuarioRepository.countAlocacoesComoFinalizador(usuario.getId());
                return new UsuarioComDependenciasDTO(usuario, alocacoesComoMecanico, alocacoesComoFinalizador);
            })
            .collect(Collectors.toList());
        
        model.addAttribute("usuarios", usuariosComDependencias);
        return "usuarios/list";
    }

    @GetMapping("/novo")
    public String createForm(Model model) {
        model.addAttribute("usuario", new UsuarioDTO("", "", ""));
        model.addAttribute("tipos", TipoUsuario.values());
        return "usuarios/form";
    }

    @PostMapping
    public String create(@Valid UsuarioDTO dto,
                         BindingResult bindingResult,
                         @RequestParam(defaultValue = "MECANICO") TipoUsuario tipo,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", dto);
            model.addAttribute("tipos", TipoUsuario.values());
            StringBuilder erros = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                if (erros.length() > 0) {
                    erros.append(" ");
                }
                erros.append(error.getDefaultMessage());
            });
            if (erros.length() > 0) {
                model.addAttribute("error", erros.toString());
            }
            return "usuarios/form";
        }
        
        try {
            ValidacaoOracleService.ResultadoValidacao resultado = 
                validacaoOracleService.validarSenhaELimites(
                    dto.getSenha(), 
                    dto.getEmail(), 
                    tipo.name(), 
                    "INSERT"
                );

            if (!resultado.isValid()) {
                model.addAttribute("usuario", dto);
                model.addAttribute("tipos", TipoUsuario.values());
                model.addAttribute("error", resultado.getErros() != null && !resultado.getErros().isEmpty() 
                    ? resultado.getErros() 
                    : resultado.getMensagem());
                return "usuarios/form";
            }
        } catch (Exception e) {
            model.addAttribute("usuario", dto);
            model.addAttribute("tipos", TipoUsuario.values());
            model.addAttribute("error", "Erro ao validar dados: " + e.getMessage());
            return "usuarios/form";
        }
        
        Usuario u = new Usuario();
        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        u.setSenha(passwordEncoder.encode(dto.getSenha()));
        u.setTipo(tipo);
        usuarioRepository.save(u);
        redirectAttributes.addFlashAttribute("success", "Usuário criado");
        return "redirect:/usuarios";
    }

    @GetMapping("/{id}/editar")
    public String editForm(@PathVariable Long id, Model model) {
        Usuario u = usuarioRepository.findById(id).orElseThrow();
        model.addAttribute("usuario", new UsuarioEditDTO(u.getNome(), u.getEmail(), ""));
        model.addAttribute("id", id);
        model.addAttribute("tipoAtual", u.getTipo());
        model.addAttribute("tipos", TipoUsuario.values());
        return "usuarios/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid UsuarioEditDTO dto,
                         BindingResult bindingResult,
                         @RequestParam(defaultValue = "MECANICO") TipoUsuario tipo,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", dto);
            model.addAttribute("id", id);
            model.addAttribute("tipoAtual", tipo);
            model.addAttribute("tipos", TipoUsuario.values());
            return "usuarios/form";
        }
        Usuario u = usuarioRepository.findById(id).orElseThrow();
        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().trim().isEmpty()) {
            u.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        u.setTipo(tipo);
        usuarioRepository.save(u);
        redirectAttributes.addFlashAttribute("success", "Usuário atualizado");
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}/excluir")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Usuário não encontrado"));
            
            long alocacoesComoMecanico = usuarioRepository.countAlocacoesComoMecanico(id);
            if (alocacoesComoMecanico > 0) {
                redirectAttributes.addFlashAttribute("error", 
                    "Não é possível excluir este usuário pois ele possui " + alocacoesComoMecanico + 
                    " alocação(ões) como mecânico responsável. Finalize ou transfira essas alocações primeiro.");
                return "redirect:/usuarios";
            }
            
            long alocacoesComoFinalizador = usuarioRepository.countAlocacoesComoFinalizador(id);
            if (alocacoesComoFinalizador > 0) {
                redirectAttributes.addFlashAttribute("error", 
                    "Não é possível excluir este usuário pois ele possui " + alocacoesComoFinalizador + 
                    " alocação(ões) como usuário de finalização. Essas alocações fazem parte do histórico do sistema.");
                return "redirect:/usuarios";
            }
            
            usuarioRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Usuário removido com sucesso");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erro ao excluir usuário: " + e.getMessage());
        }
        
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}/excluir-dependencias")
    @Transactional
    public String deleteDependencies(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Usuário não encontrado"));
            
            long alocacoesComoMecanico = usuarioRepository.countAlocacoesComoMecanico(id);
            long alocacoesComoFinalizador = usuarioRepository.countAlocacoesComoFinalizador(id);
            
            if (alocacoesComoMecanico > 0) {
                usuarioRepository.deleteAlocacoesComoMecanico(id);
            }
            
            if (alocacoesComoFinalizador > 0) {
                usuarioRepository.deleteAlocacoesComoFinalizador(id);
            }
            
            redirectAttributes.addFlashAttribute("success", 
                "Dependências removidas com sucesso! " + 
                (alocacoesComoMecanico + alocacoesComoFinalizador) + 
                " alocação(ões) foram excluídas. Agora você pode excluir o usuário.");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erro ao excluir dependências: " + e.getMessage());
        }
        
        return "redirect:/usuarios";
    }
}


