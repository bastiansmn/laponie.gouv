create table family
(
    id   bigserial
        primary key,
    name varchar(255)
);

create table family_user
(
    id    bigserial
        primary key,
    email varchar(255) unique not null,
    name  varchar(255)
);

create table link_family_user
(
    family_id bigint not null
        constraint fk8guwrboq6stcbt2cofwxlyeow
            references family,
    user_id   bigint not null
        constraint fk6gchkklqx3oprmx7futk90y6f
            references family_user
);
