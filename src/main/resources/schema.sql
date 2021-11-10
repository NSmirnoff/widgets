create table if not exists widgets
(
    id     bigint generated always as identity primary key,
    x      integer not null,
    y      integer not null,
    z      integer not null unique,
    width  integer not null,
    height integer not null,
    version integer not null default 1,
    constraint positive_size check (width > 0 and height > 0)
);
