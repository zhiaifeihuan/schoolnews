create table sn_user(
	username varchar(128) primary key unique not null,
	nickname varchar(128) unique not null,
	password varchar(128) not null,
        gender varchar(4),
	introduce varchar(256),
	identity varchar(128),
	major varchar(128),
	phone varchar(128),
	registertime timestamp default now()
);
create table sn_event(
	eventid SERIAL primary key,
	username varchar(128)  not null,
	happentime timestamp default now(),
	event text not null,
	longitude double precision not null,
	latitude double precision not null,
        title text,
        label boolean
        
	
);
