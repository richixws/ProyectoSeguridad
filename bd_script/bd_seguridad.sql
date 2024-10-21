-- Secuencias para los IDs
CREATE SEQUENCE seq_sw_persona START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sw_sistema START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sw_usuario START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sw_rol START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sw_perfil START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sw_perfil_acceso START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sw_perfil_usuario START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sw_hist_password START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sw_opcion START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sw_entidad START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sw_modulo START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sw_parametros START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sw_auditoria START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sw_files START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE seq_sw_documento_identidad START WITH 1 INCREMENT BY 1;

-- Creación de tablas
CREATE TABLE SW_PERSONA (
    id_persona NUMBER(10) DEFAULT seq_sw_persona.NEXTVAL NOT NULL,
    tipo_documento NUMBER(5) NOT NULL,
    documento_identidad VARCHAR2(50) NOT NULL,
    ap_pat VARCHAR2(50) NOT NULL,
    ap_mat VARCHAR2(50) NOT NULL,
    nombres VARCHAR2(50) NOT NULL,
     is_deleted NUMBER(1) DEFAULT 0 NOT NULL
    CONSTRAINT PK_SW_PERSONA PRIMARY KEY (id_persona)
);

CREATE TABLE SW_SISTEMA (
    id_sistema NUMBER(10) DEFAULT seq_sw_sistema.NEXTVAL NOT NULL,
    codigo VARCHAR2(50) NOT NULL,
    nombre VARCHAR2(100) NOT NULL,
    version VARCHAR2(50) NOT NULL,
    logo_main VARCHAR2(50) NOT NULL,
    logo_head VARCHAR2(50) NOT NULL,
    url VARCHAR2(500) NOT NULL,
    is_deleted NUMBER(1) DEFAULT 0 NOT NULL,--DEFAULT 0 establece el valor predeterminado como false (no eliminado).
    
    hora_creacion TIMESTAMP NULL, -- Mapea LocalDateTime a TIMESTAMP en Oracle
    hora_eliminacion TIMESTAMP NULL, -- Permitir nulos para la eliminación
    hora_actualizacion TIMESTAMP NULL, -- Permitir nulos para la actualización
    usuario_creacion VARCHAR2(50) NULL, -- Limitar la longitud a 50 caracteres
    usuario_eliminacion VARCHAR2(50) NULL, -- Permitir nulos para la eliminación
    usuario_actualizacion VARCHAR2(50) NULL, -- Permitir nulos para la actualización
    CONSTRAINT PK_SW_SISTEMA PRIMARY KEY (id_sistema)
);

CREATE TABLE SW_OPCION (
    id_opcion NUMBER(10) DEFAULT seq_sw_opcion.NEXTVAL NOT NULL,
    id_modulo NUMBER(10) NOT NULL,
    nombre VARCHAR2(100) NOT NULL,
    url VARCHAR2(300) NOT NULL,
    CONSTRAINT PK_SW_OPCION PRIMARY KEY (id_opcion)
);



CREATE TABLE SW_ENTIDAD (
    id_entidad NUMBER(10) DEFAULT seq_sw_entidad.NEXTVAL NOT NULL,
    id_documento NUMBER(10) NOT NULL,
    numero_documento VARCHAR2(25) NOT NULL,
    nombre VARCHAR2(100) NOT NULL,
    sigla VARCHAR2(50),
    cod_externo VARCHAR2(50),
    is_deleted NUMBER(1) DEFAULT 0 NOT NULL,
    
    hora_creacion TIMESTAMP NULL, -- Mapea LocalDateTime a TIMESTAMP en Oracle
    hora_eliminacion TIMESTAMP NULL, -- Permitir nulos para la eliminación
    hora_actualizacion TIMESTAMP NULL, -- Permitir nulos para la actualización
    usuario_creacion VARCHAR2(50) NULL, -- Limitar la longitud a 50 caracteres
    usuario_eliminacion VARCHAR2(50) NULL , -- Permitir nulos para la eliminación
    usuario_actualizacion VARCHAR2(50) NULL, -- Permitir nulos para la actualización
    
    
    CONSTRAINT PK_SW_ENTIDAD PRIMARY KEY (id_entidad),
    CONSTRAINT FK_SW_ENTIDAD_DOCUMENTO FOREIGN KEY (id_documento) REFERENCES SW_DOCUMENTO_IDENTIDAD (id_documento)
);

CREATE TABLE SW_MODULO (
    id_modulo NUMBER(10) DEFAULT seq_sw_modulo.NEXTVAL NOT NULL,
    id_sistema NUMBER(10) NOT NULL,
    order_date DATE NOT NULL,
    CONSTRAINT PK_SW_MODULO PRIMARY KEY (id_modulo),
    CONSTRAINT FK_SW_MODULO_SISTEMA FOREIGN KEY (id_sistema) REFERENCES SW_SISTEMA(id_sistema)
);

CREATE TABLE SW_USUARIO (
    id_usuario NUMBER(10) DEFAULT seq_sw_usuario.NEXTVAL NOT NULL,
    id_persona NUMBER(10) NOT NULL,
    correo_institucional VARCHAR2(50),
    fecha_creacion DATE DEFAULT SYSDATE NOT NULL,
    doc_sustento VARCHAR2(500) NOT NULL,
    ambito VARCHAR2(20) NOT NULL,
    usuario VARCHAR2(100) NOT NULL,
    password VARCHAR2(100) NOT NULL,
    CONSTRAINT PK_SW_USUARIO PRIMARY KEY (id_usuario),
    CONSTRAINT FK_SW_USUARIO_PERSONA FOREIGN KEY (id_persona) REFERENCES SW_PERSONA(id_persona)
);

CREATE TABLE SW_HIST_PASSWORD (
    id_hist_password NUMBER(10) DEFAULT seq_sw_hist_password.NEXTVAL NOT NULL,
    id_usuario NUMBER(10) NOT NULL,
    password VARCHAR2(100) NOT NULL,
    fecha_hora TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT PK_SW_HIST_PASSWORD PRIMARY KEY (id_hist_password),
    CONSTRAINT FK_SW_HIST_PASSWORD_USUARIO FOREIGN KEY (id_usuario) REFERENCES SW_USUARIO(id_usuario)
);

CREATE TABLE SW_ROL (
    id_rol NUMBER(10) DEFAULT seq_sw_rol.NEXTVAL NOT NULL,
    id_sistema NUMBER(10) NOT NULL,
    nombre VARCHAR2(100) NOT NULL,
    estado NUMBER(1) NOT NULL,
    ultlin NUMBER(10) NOT NULL,
    CONSTRAINT PK_SW_ROL PRIMARY KEY (id_rol),
    CONSTRAINT FK_SW_ROL_SISTEMA FOREIGN KEY (id_sistema) REFERENCES SW_SISTEMA(id_sistema)
);


CREATE TABLE SW_PERFIL (
    id_perfil NUMBER(10) DEFAULT seq_sw_perfil.NEXTVAL NOT NULL,
    id_entidad NUMBER(10) NOT NULL,
    id_rol NUMBER(10) NOT NULL,
    nombre VARCHAR2(500) NOT NULL,
    CONSTRAINT PK_SW_PERFIL PRIMARY KEY (id_perfil),
    CONSTRAINT FK_SW_PERFIL_ROL FOREIGN KEY (id_rol) REFERENCES SW_ROL(id_rol)
);


CREATE TABLE SW_PERFIL_USUARIO (
    id_perfil_usuario NUMBER(10) DEFAULT seq_sw_perfil_usuario.NEXTVAL NOT NULL,
    id_perfil NUMBER(10) NOT NULL,
    id_usuario NUMBER(10) NOT NULL,
    CONSTRAINT PK_SW_PERFIL_USUARIO PRIMARY KEY (id_perfil_usuario),
    CONSTRAINT FK_SW_PERFIL_USUARIO_PERFIL FOREIGN KEY (id_perfil) REFERENCES SW_PERFIL(id_perfil),
    CONSTRAINT FK_SW_PERFIL_USUARIO_USUARIO FOREIGN KEY (id_usuario) REFERENCES SW_USUARIO(id_usuario)
);


CREATE TABLE SW_PERFIL_ACCESO (
    id_perfil_acceso NUMBER(10) DEFAULT seq_sw_perfil_acceso.NEXTVAL NOT NULL,
    shipment_date DATE NOT NULL,
    CONSTRAINT PK_SW_PERFIL_ACCESO PRIMARY KEY (id_perfil_acceso)
);

CREATE TABLE SW_PARAMETROS (
    id_parametros NUMBER(10) DEFAULT seq_sw_parametros.NEXTVAL NOT NULL,
    descripcion VARCHAR2(100) NOT NULL,
    valor VARCHAR2(100) NOT NULL,
    fecha_registro DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT PK_SW_PARAMETROS PRIMARY KEY (id_parametros)
);

CREATE TABLE SW_AUDITORIA (
    id_auditoria NUMBER(10) DEFAULT seq_sw_auditoria.NEXTVAL NOT NULL,
    id_sistema NUMBER(10),
    id_usuario NUMBER(10),
    id_perfil NUMBER(10),
    id_rol NUMBER(10),
    id_opcion NUMBER(10),
    cod_evento VARCHAR2(5),
    cod_descripcion VARCHAR2(5),
    descripcion VARCHAR2(300),
    resultado VARCHAR2(5),
    ip_cliente VARCHAR2(15),
    host_cliente VARCHAR2(50),
    fecha_hora TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT PK_SW_AUDITORIA PRIMARY KEY (id_auditoria)
);


CREATE TABLE SW_FILES (
    id_files NUMBER(10) DEFAULT seq_sw_files.NEXTVAL NOT NULL,
    id_identity number(10) NOT NULL,
    filename VARCHAR2(255) NOT NULL,
    path VARCHAR2(1000) NOT NULL,
    extension VARCHAR2(50) NOT NULL,
    mime VARCHAR2(50) NOT NULL,
    sizes NUMBER(25) NOT NULL,
    id_modulo varchar2(50) NOT NULL,
    id_usuario varchar(50) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL,

    CONSTRAINT PK_SW_FILES PRIMARY KEY (id_files)
);


CREATE TABLE SW_DOCUMENTO_IDENTIDAD (
    id_documento NUMBER(10) DEFAULT seq_sw_documento_identidad.NEXTVAL NOT NULL,
    tipo_documento VARCHAR2(25) NOT NULL,
    CONSTRAINT UC_TIPO_NUMERO UNIQUE (tipo_documento),
    CONSTRAINT PK_SW_DOCUMENTO_IDENTIDAD PRIMARY KEY (id_documento)
);


