create TABLE people (
    name text not null,
    egn text not null,
    age integer,
    email text,
    PRIMARY KEY (egn)
);
create TABLE trip (
    id serial primary key not null,
    egn text References people(egn),
    arrival date not null,
    departure date not null,
    city text
);