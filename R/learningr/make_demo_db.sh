rm -f example.sqlite

sqlite3 example.sqlite << EOF
create table mytable (
  label char[5],
  value real
);

insert into mytable (label, value) values ("me", 10.0);
insert into mytable (label, value) values ("you", 20.0);
EOF
