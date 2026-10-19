-- =====================================================
-- SGAHCP - Dados de teste
-- Correr depois do schema.sql:
--   mariadb -h 127.0.0.1 -P 3307 -u clinica_user -p SGAHCP < data.sql
-- =====================================================

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE obito;
TRUNCATE TABLE prescricao;
TRUNCATE TABLE consulta;
TRUNCATE TABLE triagem;
TRUNCATE TABLE paciente;
TRUNCATE TABLE trabalhador;
SET FOREIGN_KEY_CHECKS = 1;

-- ---------- TRABALHADOR ----------
-- As senhas sao placeholders; no Java vais guardar hashes reais (ex.: BCrypt)
INSERT INTO trabalhador
 (nome_completo, data_nascimento, numero_bi, endereco_residencial, telefone, email, username, senha_hash,
  naturalidade, salario, carga_horaria, nivel, especialidade, numero_ordem, numero_registo_profissional,
  sector, categoria, balcao) VALUES
('Admin Sistema', '1985-01-10', '110100000001A', 'Maputo, Polana',   '840000001', 'admin@sgahcp.local',  'admin',     'HASH_TEMPORARIO',
  'Maputo', 45000.00, 40.0, 'ADMINISTRADOR', NULL, NULL, NULL, NULL, NULL, NULL),
('Ana Macuacua',  '1988-06-21', '110100000002B', 'Maputo, Sommerschield', '840000002', 'ana@sgahcp.local',    'amacuacua', 'HASH_TEMPORARIO',
  'Gaza', 80000.00, 40.0, 'MEDICO', 'Clinica Geral', 'OM-1234', 'RP-0001', NULL, NULL, NULL),
('Carlos Sitoe',  '1992-09-03', '110100000003C', 'Matola, Machava', '840000003', 'carlos@sgahcp.local', 'csitoe',    'HASH_TEMPORARIO',
  'Inhambane', 30000.00, 44.0, 'ENFERMEIRO', NULL, NULL, 'RP-0002', 'Urgencias', 'Enfermeiro Generalista', NULL),
('Marta Chongo',  '1995-12-15', '110100000004D', 'Maputo, Maxaquene', '840000004', 'marta@sgahcp.local',  'mchongo',   'HASH_TEMPORARIO',
  'Maputo', 22000.00, 40.0, 'RECEPCIONISTA', NULL, NULL, NULL, NULL, NULL, 'Balcao 1');

-- ---------- PACIENTE ----------
-- registado_por = 4 (Marta Chongo, recepcionista)
-- O terceiro paciente nao tem BI nem morada (RN05 e RN12) e vem com acompanhante.
INSERT INTO paciente
 (nome_completo, data_nascimento, numero_bi, endereco_residencial, telefone, sexo, grupo_sanguineo,
  nome_acompanhante, papel_acompanhante, registado_por) VALUES
('Joao Mabunda',  '1990-05-14', '110200000001E', 'Maputo, Alto-Mae', '820000001', 'M', 'O+', NULL, NULL, 4),
('Isabel Tembe',  '1985-11-02', '110200000002F', 'Matola, Fomento',  '820000002', 'F', 'A+', NULL, NULL, 4),
('Rui Nhantumbo', '1948-03-20', NULL,            NULL,               '820000003', 'M', 'B-', 'Paula Nhantumbo', 'Filha', 4);

-- ---------- TRIAGEM e CONSULTA (para testar os repositories) ----------
INSERT INTO triagem (paciente_id, enfermeiro_id, nivel_risco, queixa_principal, temperatura, pressao_arterial, freq_cardiaca) VALUES
(3, 3, 'D', 'Dor no peito e falta de ar', 37.8, '150/95', 102);

INSERT INTO consulta (paciente_id, medico_id, triagem_id, urgencia, estado, data_hora_inicio) VALUES
(3, 2, 1, TRUE, 'EM_CURSO', NOW());
