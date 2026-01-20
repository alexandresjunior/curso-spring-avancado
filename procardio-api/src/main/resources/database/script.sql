insert into
    tb_usuarios (nome, email, senha, cep)
values (
        'Vitor Santos',
        'vitor@treinarecife.com.br',
        '$2a$12$3LCVJ3P.vK1xlmv/gN4vCuEACg2h85szergq2YIXPJCbC8VER.Hj2',
        '50120-320'
    );

insert into
    tb_medicos (
        nome,
        email,
        crm,
        especialidade
    )
VALUES (
        'Carlos Andrade',
        'carlos@procardio.com.br',
        'carlos123',
        'CARDIOLOGIA'
    );

insert into tb_perfis (usuario_id, perfil) values (3, 'ADMIN');