alter table sn_event add title text;
alter table sn_event drop positionx;
alter table sn_event drop positiony;
alter table sn_event add longitude double precision;
alter table sn_event add latitude double precision;
alter table sn_event add label boolean;
