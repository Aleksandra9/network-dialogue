create table if not exists dialogue
(
    id varchar not null default gen_random_uuid(),
    dialogue_id varchar,
    from_user_id varchar,
    to_user_id varchar,
    text varchar,
    status varchar,
    create_datetime timestamp with time zone default now()
);