-- DDL
create table car (
	carid int primary key,
	vin varchar,
	make int,
	model int,
	year int,
	color int,
	options int,
	purchprice money, 
	enddate date,  
	psalv int, 
	psold int, 
	location int -- set to 0 = reference car record
);

create table part (
	partid int primary key,
	carid int,	
	year int
	parttype int,
	location int, 	-- set to 0 = in car carid; -1 = reference part 
					-- (So system can populate data for new records)
	adddate date,	
	selldate date,
	make int,
	model int,
	condition numeric, -- range 0 - 1; 0 = scrap only, 1 = like new
	manfac int, -- if aftermarket part else null
	shareid int, -- 0 = universal e.g wheel or tire
	description text,
	color int,
	value money,
	sellprice money,
	length numeric, -- can be reused 
	width numeric,
	height numeric,
	weight numeric	
);

create table client (
	clientid int primary key,
	type int, -- e.g. 0 = scrapyard, 1 = customer etc.
	name text, -- business or client name
	contact text, -- if business
	street text,
	state varchar(2),
	zip int,
	added date,
	mphone int,
	hphone int,
	bphone int,
);

create table sale (
	saleid int primary key,
	clientid int,
	tstamp timestamp
);

create table sale_detail (
	sale_detailid int primary key,
	saleid int,
	partid int
);

create table compatible ( -- used for populating shareid in part
	parttype int,
	shareid int,
	make int,
	model int,
	beginyear int,
	endyear int
);

-- find compatible part
-- first, find exact match
select p.*
from part p
where p.parttype = [query value] and
	p.selldate = null  and
	p.make = [query value] and 
	p.model = [query value] and 
	p.year = [query value] and
	p.color = [query value]
order by p.location	 
;

-- find make & model match
select p.*
from part p
where p.parttype = [query value] and
	p.selldate = null  and
	p.make = [query value] and 
	p.model = [query value] and 
	p.year = [query value]
order by p.location	 
;

-- acceptable replacement
select r.*
from part p
	inner join part r
	on p.shareid = r.shareid and p.parttype = r.parttype
where p.parttype = [query value] and
	p.make = [query value] and
	p.model = [query value]
order by r.location
;