create table widgets
(
    id     bigint generated always as identity primary key,
    x      integer not null,
    y      integer not null,
    z      integer not null unique,
    width  integer not null,
    height integer not null,
    constraint positive_size check (width > 0 and height > 0)
);
