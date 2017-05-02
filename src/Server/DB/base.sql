create database Data;

use Data;

create table accounts
(
	user_id int(12) primary key not null,
	email varchar(40) unique key not null,
	password varchar(40) unique key not null,
	nickname varchar(30) unique key not null,
	backup_code varchar(4) 
);

create table game_data
(
	user_id int(12) not null,
	games_joined int(10),
	games_won int(10),
	mode_escape int(10),
	mode_pursuit int(10),
	foreign key fk_gamedata(user_id) references accounts(user_id) on update cascade
);

create or replace trigger game_data_ins
after insert on accounts
begin
insert into game_data (user_id, games_joined, games_won, mode_escape, mode_pursuit)
values (new.user_id, 0, 0, 0, 0);
end;
/
