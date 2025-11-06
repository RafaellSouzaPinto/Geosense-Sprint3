package geosense.Geosense.controller;

import geosense.Geosense.dto.UsuarioDTO;
import geosense.Geosense.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

	private final UsuarioService usuarioService;

	public AuthController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("usuario", new UsuarioDTO("", "", ""));
		return "register";
	}

	@PostMapping("/register")
	public String registerSubmit(@Valid UsuarioDTO usuario,
	                            BindingResult bindingResult,
	                            Model model,
	                            RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("usuario", usuario);
			
			System.out.println("=== AUTH CONTROLLER - ERROS DE VALIDAÇÃO ===");
			System.out.println("Total de erros: " + bindingResult.getErrorCount());
			
			StringBuilder erros = new StringBuilder();
			bindingResult.getAllErrors().forEach(error -> {
				System.out.println("Erro: " + error.getDefaultMessage());
				System.out.println("  Campo: " + (error instanceof org.springframework.validation.FieldError 
					? ((org.springframework.validation.FieldError) error).getField() : "global"));
				if (erros.length() > 0) {
					erros.append(" ");
				}
				String mensagem = error.getDefaultMessage();
				if (!erros.toString().contains(mensagem)) {
					erros.append(mensagem);
				}
			});
			
			System.out.println("Mensagem final de erro: " + erros.toString());
			System.out.println("=============================================");
			
			if (erros.length() > 0) {
				model.addAttribute("error", erros.toString());
			}
			return "register";
		}
		try {
			usuarioService.register(usuario);
			redirectAttributes.addFlashAttribute("success", "Cadastro realizado com sucesso. Faça login.");
			return "redirect:/login?success";
		} catch (IllegalArgumentException ex) {
			model.addAttribute("usuario", usuario);
			model.addAttribute("error", ex.getMessage());
			return "register";
		}
	}
}


