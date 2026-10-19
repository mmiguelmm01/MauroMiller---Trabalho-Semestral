-- =====================================================
-- SGAHCP - Schema
-- ATENCAO: apaga as tabelas existentes e recria tudo (so para desenvolvimento)
-- Correr:  mariadb -h 127.0.0.1 -P 3307 -u clinica_user -p SGAHCP < schema.sql
-- =====================================================
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS obito, prescricao, consulta, triagem, paciente, trabalhador;
SET FOREIGN_KEY_CHECKS = 1;

-- Uma unica tabela para toda a hierarquia Trabalhador (Administrador, Medico,
-- Enfermeiro, Recepcionista). A coluna "nivel" diz qual a subclasse Java a criar.
-- Colunas especificas de uma subclasse ficam NULL nas outras.
CREATE TABLE trabalhador (
    id                          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    -- dados herdado de Pessoa
    nome_completo               VARCHAR(60)  NOT NULL,
    data_nascimento             DATE         NOT NULL,
    numero_bi                   VARCHAR(13)  NULL UNIQUE,
    endereco_residencial        VARCHAR(120) NOT NULL,
    telefone                    CHAR(9)      NOT NULL,
    -- dados de Trabalhador
    email                       VARCHAR(80)  NULL UNIQUE,
    username                    VARCHAR(40)  NOT NULL UNIQUE,
    senha_hash                  VARCHAR(120) NOT NULL,
    naturalidade                VARCHAR(60)  NULL,
    salario                     DECIMAL(10,2) NOT NULL DEFAULT 0,
    carga_horaria               DECIMAL(4,1)  NOT NULL DEFAULT 0,
    nivel                       ENUM('ADMINISTRADOR','MEDICO','ENFERMEIRO','RECEPCIONISTA') NOT NULL,
    activo                      BOOLEAN      NOT NULL DEFAULT TRUE,
    -- somente para Medico
    especialidade               VARCHAR(80)  NULL,
    numero_ordem                VARCHAR(30)  NULL,
    -- Medico e Enfermeiro
    numero_registo_profissional VARCHAR(30)  NULL,
    -- somente para Enfermeiro
    sector                      VARCHAR(60)  NULL,
    categoria                   VARCHAR(40)  NULL,
    -- somente pars Recepcionista
    balcao                      VARCHAR(30)  NULL,
    data_criacao                DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_trab_telefone CHECK (telefone REGEXP '^[0-9]{9}$'),
    CONSTRAINT ck_trab_medico CHECK (nivel = 'MEDICO' OR (especialidade IS NULL AND numero_ordem IS NULL)),
    CONSTRAINT ck_trab_enfermeiro CHECK (nivel = 'ENFERMEIRO' OR (sector IS NULL AND categoria IS NULL)),
    CONSTRAINT ck_trab_registo CHECK (nivel IN ('MEDICO','ENFERMEIRO') OR numero_registo_profissional IS NULL),
    CONSTRAINT ck_trab_balcao CHECK (nivel = 'RECEPCIONISTA' OR balcao IS NULL)
) ENGINE=InnoDB;

-- RN12: em emergencia os dados administrativos podem ser completados depois,
-- por isso numero_bi (RN05) e endereco_residencial aceitam NULL no paciente.
CREATE TABLE paciente (
    id                   INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nome_completo        VARCHAR(60)  NOT NULL,
    data_nascimento      DATE         NOT NULL,
    numero_bi            VARCHAR(13)  NULL UNIQUE,
    endereco_residencial VARCHAR(120) NULL,
    telefone             CHAR(9)      NOT NULL,
    sexo                 ENUM('M','F') NOT NULL,
    grupo_sanguineo      ENUM('A+','A-','B+','B-','AB+','AB-','O+','O-') NULL,
    nome_acompanhante    VARCHAR(60)  NULL,
    papel_acompanhante   VARCHAR(30)  NULL,
    estado               ENUM('VIVO','FALECIDO') NOT NULL DEFAULT 'VIVO',
    registado_por        INT UNSIGNED NOT NULL,
    data_criacao         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_pac_telefone CHECK (telefone REGEXP '^[0-9]{9}$'),
    CONSTRAINT fk_pac_registado_por FOREIGN KEY (registado_por) REFERENCES trabalhador(id)
) ENGINE=InnoDB;

CREATE TABLE triagem (
    id                INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    paciente_id       INT UNSIGNED NOT NULL,
    enfermeiro_id     INT UNSIGNED NOT NULL,
    nivel_risco       ENUM('A','B','C','D','E') NOT NULL,
    queixa_principal  VARCHAR(255) NOT NULL,
    temperatura       DECIMAL(4,1) NULL,
    pressao_arterial  VARCHAR(10)  NULL,
    freq_cardiaca     SMALLINT UNSIGNED NULL,
    data_criacao      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tri_paciente   FOREIGN KEY (paciente_id)   REFERENCES paciente(id),
    CONSTRAINT fk_tri_enfermeiro FOREIGN KEY (enfermeiro_id) REFERENCES trabalhador(id)
) ENGINE=InnoDB;

CREATE TABLE consulta (
    id                INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    paciente_id       INT UNSIGNED NOT NULL,
    medico_id         INT UNSIGNED NOT NULL,
    triagem_id        INT UNSIGNED NULL,
    urgencia          BOOLEAN      NOT NULL DEFAULT FALSE,
    estado            ENUM('AGENDADA','EM_CURSO','CONCLUIDA','CANCELADA') NOT NULL DEFAULT 'AGENDADA',
    diagnostico       TEXT NULL,
    tratamento        TEXT NULL,
    data_hora_inicio  DATETIME     NULL,
    data_hora_fim     DATETIME     NULL,
    data_criacao      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_con_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id),
    CONSTRAINT fk_con_medico   FOREIGN KEY (medico_id)   REFERENCES trabalhador(id),
    CONSTRAINT fk_con_triagem  FOREIGN KEY (triagem_id)  REFERENCES triagem(id)
) ENGINE=InnoDB;

CREATE TABLE prescricao (
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    consulta_id   INT UNSIGNED NOT NULL,
    medicamento   VARCHAR(120) NOT NULL,
    dosagem       VARCHAR(60)  NOT NULL,
    frequencia    VARCHAR(60)  NOT NULL,
    duracao_dias  SMALLINT UNSIGNED NOT NULL,
    instrucoes    VARCHAR(255) NULL,
    data_criacao  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pre_consulta FOREIGN KEY (consulta_id) REFERENCES consulta(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE obito (
    id           INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    paciente_id  INT UNSIGNED NOT NULL UNIQUE,
    consulta_id  INT UNSIGNED NULL,
    medico_id    INT UNSIGNED NOT NULL,
    data_obito   DATETIME     NOT NULL,
    causa        VARCHAR(255) NOT NULL,
    observacoes  TEXT NULL,
    data_criacao DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_obi_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id),
    CONSTRAINT fk_obi_consulta FOREIGN KEY (consulta_id) REFERENCES consulta(id),
    CONSTRAINT fk_obi_medico   FOREIGN KEY (medico_id)   REFERENCES trabalhador(id)
) ENGINE=InnoDB;
