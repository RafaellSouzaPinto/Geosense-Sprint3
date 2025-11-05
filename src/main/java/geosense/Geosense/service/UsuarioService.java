package geosense.Geosense.service;

import geosense.Geosense.dto.CredentialsDTO;
import geosense.Geosense.dto.UsuarioDTO;
import geosense.Geosense.entity.TipoUsuario;
import geosense.Geosense.entity.Usuario;
import geosense.Geosense.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private static final String ADMIN_EMAIL = "mottu@gmail.com";
    private static final String ADMIN_PASSWORD = "Geosense@2025";

    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final ValidacaoOracleService validacaoOracleService;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder passwordEncoder, ValidacaoOracleService validacaoOracleService) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.validacaoOracleService = validacaoOracleService;
    }

    public ResponseEntity<String> login(CredentialsDTO cred) {
        if (ADMIN_EMAIL.equals(cred.getEmail()) && ADMIN_PASSWORD.equals(cred.getSenha())) {
            Optional<Usuario> adminOpt = repo.findFirstByTipo(TipoUsuario.ADMINISTRADOR);
            if (adminOpt.isPresent()) {
                return ResponseEntity.ok("Bem vindo ao modo administrador");
            } else {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail(ADMIN_EMAIL);
                admin.setSenha(passwordEncoder.encode(ADMIN_PASSWORD));
                admin.setTipo(TipoUsuario.ADMINISTRADOR);
                repo.save(admin);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("Administrador vinculado");
            }
        }

        Optional<Usuario> mecanicoOpt = repo
                .findByEmailAndSenhaAndTipo(cred.getEmail(), cred.getSenha(), TipoUsuario.MECANICO);

        return mecanicoOpt
                .map(u -> ResponseEntity.ok("Logado como mecânico: " + u.getNome()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Credenciais inválidas"));
    }

    public Usuario register(UsuarioDTO dto) {
        if (ADMIN_EMAIL.equals(dto.getEmail()) && ADMIN_PASSWORD.equals(dto.getSenha())) {
            Optional<Usuario> adminExistente = repo.findFirstByTipo(TipoUsuario.ADMINISTRADOR);
            return adminExistente.orElseThrow(() ->
                    new IllegalArgumentException("Administrador já existe. Use o login."));
        }

        String tipoUsuario = TipoUsuario.MECANICO.name();
        ValidacaoOracleService.ResultadoValidacao resultado = 
            validacaoOracleService.validarSenhaELimites(
                dto.getSenha(), 
                dto.getEmail(), 
                tipoUsuario, 
                "INSERT"
            );

        if (!resultado.isValid()) {
            throw new IllegalArgumentException(resultado.getErros() != null && !resultado.getErros().isEmpty() 
                ? resultado.getErros() 
                : resultado.getMensagem());
        }

        Usuario u = new Usuario();
        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        u.setSenha(passwordEncoder.encode(dto.getSenha()));
        u.setTipo(TipoUsuario.MECANICO);
        return repo.save(u);
    }

    public Optional<Usuario> findById(Long id) {
        return repo.findById(id);
    }

    public ResponseEntity<?> update(Long id, UsuarioDTO dto) {
        return repo.findById(id)
                .map(u -> {
                    String tipoUsuario = u.getTipo().name();
                    ValidacaoOracleService.ResultadoValidacao resultado = 
                        validacaoOracleService.validarSenhaELimites(
                            dto.getSenha(), 
                            dto.getEmail(), 
                            tipoUsuario, 
                            "VALIDACAO"
                        );

                    if (!resultado.isValid()) {
                        return ResponseEntity.badRequest()
                            .body(resultado.getErros() != null && !resultado.getErros().isEmpty() 
                                ? resultado.getErros() 
                                : resultado.getMensagem());
                    }
                    
                    u.setNome(dto.getNome());
                    u.setEmail(dto.getEmail());
                    u.setSenha(passwordEncoder.encode(dto.getSenha()));
                    repo.save(u);
                    return ResponseEntity.ok(u);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<Usuario> listAll() {
        return repo.findAll();
    }
}