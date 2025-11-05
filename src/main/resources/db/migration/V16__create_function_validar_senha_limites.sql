-- Migration: Criar função FN_VALIDAR_SENHA_E_LIMITES

-- Remove a função se já existir (para permitir re-execução da migration)
BEGIN
    EXECUTE IMMEDIATE 'DROP FUNCTION FN_VALIDAR_SENHA_E_LIMITES';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -4043 THEN
            RAISE;
        END IF;
END;
/

-- Cria a função de validação
CREATE OR REPLACE FUNCTION FN_VALIDAR_SENHA_E_LIMITES(
    p_senha VARCHAR2,
    p_email VARCHAR2,
    p_tipo_usuario VARCHAR2,
    p_operacao VARCHAR2 DEFAULT 'VALIDACAO'
) RETURN VARCHAR2
IS
    v_resultado VARCHAR2(100);
    v_senha_valida BOOLEAN := TRUE;
    v_email_valido BOOLEAN := TRUE;
    v_tipo_valido BOOLEAN := TRUE;
    v_erros VARCHAR2(1000) := '';
    v_contador_erros NUMBER := 0;
    
    c_min_caracteres NUMBER := 6;
    c_max_caracteres NUMBER := 20;
    c_tipos_validos VARCHAR2(100) := 'MECANICO,ADMINISTRADOR,OPERADOR';
    c_caracteres_especiais VARCHAR2(50) := '!@#$%&*()_+-=[]{}|;:,.<>?';
    c_caracteres_proibidos VARCHAR2(50) := ' ';
    
    FUNCTION CONTEM_CARACTERE_ESPECIAL(p_texto VARCHAR2, p_caracteres VARCHAR2) RETURN BOOLEAN IS
        v_resultado BOOLEAN := FALSE;
    BEGIN
        FOR i IN 1..LENGTH(p_caracteres) LOOP
            IF INSTR(p_texto, SUBSTR(p_caracteres, i, 1)) > 0 THEN
                v_resultado := TRUE;
                EXIT;
            END IF;
        END LOOP;
        RETURN v_resultado;
    END CONTEM_CARACTERE_ESPECIAL;
    
    FUNCTION CONTEM_NUMERO(p_texto VARCHAR2) RETURN BOOLEAN IS
        v_resultado BOOLEAN := FALSE;
    BEGIN
        FOR i IN 1..LENGTH(p_texto) LOOP
            IF ASCII(SUBSTR(p_texto, i, 1)) BETWEEN 48 AND 57 THEN
                v_resultado := TRUE;
                EXIT;
            END IF;
        END LOOP;
        RETURN v_resultado;
    END CONTEM_NUMERO;
    
    FUNCTION CONTEM_MAIUSCULA(p_texto VARCHAR2) RETURN BOOLEAN IS
        v_resultado BOOLEAN := FALSE;
    BEGIN
        FOR i IN 1..LENGTH(p_texto) LOOP
            IF ASCII(SUBSTR(p_texto, i, 1)) BETWEEN 65 AND 90 THEN
                v_resultado := TRUE;
                EXIT;
            END IF;
        END LOOP;
        RETURN v_resultado;
    END CONTEM_MAIUSCULA;
    
    FUNCTION CONTEM_MINUSCULA(p_texto VARCHAR2) RETURN BOOLEAN IS
        v_resultado BOOLEAN := FALSE;
    BEGIN
        FOR i IN 1..LENGTH(p_texto) LOOP
            IF ASCII(SUBSTR(p_texto, i, 1)) BETWEEN 97 AND 122 THEN
                v_resultado := TRUE;
                EXIT;
            END IF;
        END LOOP;
        RETURN v_resultado;
    END CONTEM_MINUSCULA;
    
BEGIN
    v_resultado := '';
    v_erros := '';
    v_contador_erros := 0;
    
    IF p_senha IS NULL OR TRIM(p_senha) = '' THEN
        v_contador_erros := v_contador_erros + 1;
        v_erros := v_erros || 'Erro ' || v_contador_erros || ': Senha não pode ser nula ou vazia; ';
        v_senha_valida := FALSE;
    END IF;
    
    IF v_senha_valida AND LENGTH(p_senha) < c_min_caracteres THEN
        v_contador_erros := v_contador_erros + 1;
        v_erros := v_erros || 'Erro ' || v_contador_erros || ': Senha deve ter pelo menos ' || c_min_caracteres || ' caracteres; ';
        v_senha_valida := FALSE;
    END IF;
    
    IF v_senha_valida AND LENGTH(p_senha) > c_max_caracteres THEN
        v_contador_erros := v_contador_erros + 1;
        v_erros := v_erros || 'Erro ' || v_contador_erros || ': Senha deve ter no máximo ' || c_max_caracteres || ' caracteres; ';
        v_senha_valida := FALSE;
    END IF;
    
    IF v_senha_valida AND CONTEM_CARACTERE_ESPECIAL(p_senha, c_caracteres_proibidos) THEN
        v_contador_erros := v_contador_erros + 1;
        v_erros := v_erros || 'Erro ' || v_contador_erros || ': Senha não pode conter espaços; ';
        v_senha_valida := FALSE;
    END IF;
    
    IF v_senha_valida AND NOT CONTEM_NUMERO(p_senha) THEN
        v_contador_erros := v_contador_erros + 1;
        v_erros := v_erros || 'Erro ' || v_contador_erros || ': Senha deve conter pelo menos um número; ';
        v_senha_valida := FALSE;
    END IF;
    
    IF v_senha_valida AND NOT CONTEM_MAIUSCULA(p_senha) THEN
        v_contador_erros := v_contador_erros + 1;
        v_erros := v_erros || 'Erro ' || v_contador_erros || ': Senha deve conter pelo menos uma letra maiúscula; ';
        v_senha_valida := FALSE;
    END IF;
    
    IF v_senha_valida AND NOT CONTEM_MINUSCULA(p_senha) THEN
        v_contador_erros := v_contador_erros + 1;
        v_erros := v_erros || 'Erro ' || v_contador_erros || ': Senha deve conter pelo menos uma letra minúscula; ';
        v_senha_valida := FALSE;
    END IF;
    
    IF p_email IS NULL OR TRIM(p_email) = '' THEN
        v_contador_erros := v_contador_erros + 1;
        v_erros := v_erros || 'Erro ' || v_contador_erros || ': Email não pode ser nulo ou vazio; ';
        v_email_valido := FALSE;
    ELSIF INSTR(p_email, '@') = 0 OR INSTR(p_email, '.') = 0 THEN
        v_contador_erros := v_contador_erros + 1;
        v_erros := v_erros || 'Erro ' || v_contador_erros || ': Email deve ter formato válido (exemplo@dominio.com); ';
        v_email_valido := FALSE;
    ELSIF LENGTH(p_email) > 255 THEN
        v_contador_erros := v_contador_erros + 1;
        v_erros := v_erros || 'Erro ' || v_contador_erros || ': Email deve ter no máximo 255 caracteres; ';
        v_email_valido := FALSE;
    END IF;
    
    IF p_tipo_usuario IS NULL OR TRIM(p_tipo_usuario) = '' THEN
        v_contador_erros := v_contador_erros + 1;
        v_erros := v_erros || 'Erro ' || v_contador_erros || ': Tipo de usuário não pode ser nulo ou vazio; ';
        v_tipo_valido := FALSE;
    ELSIF INSTR(c_tipos_validos, p_tipo_usuario) = 0 THEN
        v_contador_erros := v_contador_erros + 1;
        v_erros := v_erros || 'Erro ' || v_contador_erros || ': Tipo de usuário deve ser um dos seguintes: ' || c_tipos_validos || '; ';
        v_tipo_valido := FALSE;
    END IF;
    
    IF v_email_valido AND p_operacao = 'INSERT' THEN
        DECLARE
            v_email_existe NUMBER := 0;
        BEGIN
            SELECT COUNT(*) INTO v_email_existe
            FROM USUARIO
            WHERE UPPER(EMAIL) = UPPER(p_email);
            
            IF v_email_existe > 0 THEN
                v_contador_erros := v_contador_erros + 1;
                v_erros := v_erros || 'Erro ' || v_contador_erros || ': Email já existe no sistema; ';
                v_email_valido := FALSE;
            END IF;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                v_contador_erros := v_contador_erros + 1;
                v_erros := v_erros || 'Erro ' || v_contador_erros || ': Erro ao consultar banco de dados - tabela não encontrada; ';
                v_email_valido := FALSE;
            WHEN TOO_MANY_ROWS THEN
                v_contador_erros := v_contador_erros + 1;
                v_erros := v_erros || 'Erro ' || v_contador_erros || ': Erro de integridade - múltiplos registros encontrados; ';
                v_email_valido := FALSE;
            WHEN OTHERS THEN
                v_contador_erros := v_contador_erros + 1;
                v_erros := v_erros || 'Erro ' || v_contador_erros || ': Erro ao verificar email existente; ';
                v_email_valido := FALSE;
        END;
    END IF;
    
    IF v_contador_erros = 0 THEN
        v_resultado := 'VALIDACAO_OK';
    ELSE
        v_resultado := 'VALIDACAO_ERRO';
    END IF;
    
    IF v_contador_erros = 0 THEN
        RETURN '{"status": "' || v_resultado || '", "mensagem": "Validação realizada com sucesso", "total_erros": 0, "senha_forte": true, "email_valido": true, "tipo_valido": true}';
    ELSE
        RETURN '{"status": "' || v_resultado || '", "mensagem": "Validação falhou", "total_erros": ' || v_contador_erros || ', "erros": "' || REPLACE(SUBSTR(v_erros, 1, LENGTH(v_erros)-2), '"', '\"') || '"}';
    END IF;
    
EXCEPTION
    WHEN VALUE_ERROR THEN
        RETURN '{"status": "VALIDACAO_ERRO", "mensagem": "Erro de valor nos parâmetros fornecidos", "total_erros": 1, "erros": "Parâmetros inválidos ou nulos"}';
        
    WHEN NO_DATA_FOUND THEN
        RETURN '{"status": "VALIDACAO_ERRO", "mensagem": "Erro de consulta - dados não encontrados", "total_erros": 1, "erros": "Erro ao acessar dados de referência"}';
        
    WHEN TOO_MANY_ROWS THEN
        RETURN '{"status": "VALIDACAO_ERRO", "mensagem": "Erro de integridade - múltiplos registros", "total_erros": 1, "erros": "Violação de integridade de dados"}';
        
    WHEN OTHERS THEN
        RETURN '{"status": "VALIDACAO_ERRO", "mensagem": "Erro interno na validação", "total_erros": 1, "erros": "Erro interno: ' || REPLACE(SQLERRM, '"', '\"') || '"}';
        
END FN_VALIDAR_SENHA_E_LIMITES;
/

SELECT 'Função FN_VALIDAR_SENHA_E_LIMITES criada com sucesso!' AS STATUS FROM DUAL;

