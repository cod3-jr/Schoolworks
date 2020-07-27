-- DDL to store lat & long as integers for speed. Abandonded implementation due to time constraints 
create table carintdata (
id		integer,
tstamp	integer,
lat		integer, --constraint latitude check (lat >= -9000000 and lat <= 9000000),
long	integer, --constraint longitude check (long >= -18000000 and long <= 18000000),
altitude	integer
);

copy carintdata from 'gpsintdata.csv' delimiter ',' csv header;
-----------------------------------------------

-- Part 1: Future Flying Cars
-- DDL 
create table cardata (
id		integer,
tstamp	integer,	  -- measurement of seconds since epoc
lat		numeric(8,6), --constraint latitude check (lat >= -91.0 and lat < 91.0),
long	numeric(9,6), --constraint longitude check (long > -181.0 and long < 181.0),
altitude	integer
);

copy cardata from 'gpsdata.csv' delimiter ',' csv header;

--- Query 1. Determine if user is travelling > 500mph.
--- Run time? I'm not sure. I know the initial select ∅ (n) operations since there are no joins.
--- I'm unsure of the cost of the windowing functions. Even still it's 10n so ∅ (n) ?
--- If I set the lag to 10min (600 records) the runtime would be closer to ∅ (log n)?
--- I considered the speed along 2 planes only. i.e. did not consider altitude changes
with calcCars as (
	select x.*,
		-- 3600 to convert from Miles per Second to MPH 
		-- 3958.76 = mean Radius of earth in miles 
		-- considered changing the lag offset to 60 or 600 to only test speed every minute or 10 min
		-- conversion would naturally require adjustment of 3600 value
		3600 * 3958.76 * (2 * atan2((sqrt((sin(((radians(lat) - radians((lag(lat) over (partition by id order by tstamp))))/2)) 
		* sin(((radians(lat) - radians((lag(lat) over (partition by id order by tstamp))))/2))) 
		-- sin(lat dif) * sin(lat diff) b/c power(sin(latdif)) returned inaccurate results
		+ cos(radians((lag(lat) over (partition by id order by tstamp)))) 
		* cos(radians(lat))
		* (sin(((radians(long) - radians((lag(long) over (partition by id order by tstamp))))/2)) 
		* sin(((radians(long) - radians((lag(long) over (partition by id order by tstamp))))/2))))) ,
			sqrt((1 - ((sin(((radians(lat) - radians((lag(lat) over (partition by id order by tstamp))))/2)) *
		sin(((radians(lat) - radians((lag(lat) over (partition by id order by tstamp))))/2))) 
		+ cos(radians((lag(lat) over (partition by id order by tstamp)))) 
		* cos(radians(lat))
		*  (sin(((radians(long) - radians((lag(long) over (partition by id order by tstamp))))/2)) *
		sin(((radians(long) - radians((lag(long) over (partition by id order by tstamp))))/2)))))))) as mph
	from cardata x
)
Select cc.*,  
from calcCars cc
where cc.mph > 500;

--- Query 2. Ticketing users who fly within 50ft of each other.
-- gave up on lat & long calculations, used table of x, y, z values
-- runtime... really really long. 
-- The first pass filters all records < 50 altitude...so most are excluded
-- before calculating bounding boxes and 3D 'neighbors' 

create table data_xyz (
	tstamp int,
	id int,
	x int,
	y int,
	z int
);

copy data_xyz from '/xyzdata.csv' delimiter ',' csv header;

with bbtable as (
	select a.*, 
	concat(floor(a.x/50.0),',',
		floor(a.y/50.0),',',
		floor(a.z/50.0))
	as bbox,

	concat(floor(a.x/50.0)-1,',',
		floor(a.y/50.0)+1,',',
		floor(a.z/50.0)+1)
	as bboxtop1, -- "layer above; top left"

	concat(floor(a.x/50.0),',',
		floor(a.y/50.0)+1,',',
		floor(a.z/50.0)+1)
	as bboxtop2, -- "layer above; top middle"

	concat(floor(a.x/50.0)+1,',',
		floor(a.y/50.0)+1,',',
		floor(a.z/50.0)+1)
	as bboxtop3, -- "layer above; top right"

	concat(floor(a.x/50.0)-1,',',
		floor(a.y/50.0),',',
		floor(a.z/50.0)+1)
	as bboxtop4, -- "layer above; middle left"

	concat(floor(a.x/50.0),',',
			floor(a.y/50.0),',',
			floor(a.z/50.0)+1)
	as bboxtop5, -- "layer above; middle middle"

	concat(floor(a.x/50.0)+1,',',
		floor(a.y/50.0),',',
		floor(a.z/50.0)+1)
	as bboxtop6, -- "layer above; middle right"

	concat(floor(a.x/50.0)-1,',',
		floor(a.y/50.0)-1,',',
		floor(a.z/50.0)+1)
	as bboxtop7, -- "layer above; bottom left"

	concat(floor(a.x/50.0),',',
			floor(a.y/50.0)-1,',',
			floor(a.z/50.0)+1)
	as bboxtop8, -- "layer above; bottom middle"

	concat(floor(a.x/50.0)+1,',',
		floor(a.y/50.0)-1,',',
		floor(a.z/50.0)+1)
	as bboxtop9, -- "layer above; bottom right"

		concat(floor(a.x/50.0)-1,',',
		floor(a.y/50.0)+1,',',
		floor(a.z/50.0))
	as bbox1, -- "layer same; top left"

	concat(floor(a.x/50.0),',',
		floor(a.y/50.0)+1,',',
		floor(a.z/50.0))
	as bbox2, -- "layer same; top middle"

	concat(floor(a.x/50.0)+1,',',
		floor(a.y/50.0)+1,',',
		floor(a.z/50.0))
	as bbox3, -- "layer same; top right"

	concat(floor(a.x/50.0)-1,',',
		floor(a.y/50.0),',',
		floor(a.z/50.0))
	as bbox4, -- "layer same; middle left"

	concat(floor(a.x/50.0)+1,',',
		floor(a.y/50.0),',',
		floor(a.z/50.0))
	as bbox6, -- "layer same; middle right"

	concat(floor(a.x/50.0)-1,',',
		floor(a.y/50.0)-1,',',
		floor(a.z/50.0))
	as bbox7, -- "layer same; bottom left"

	concat(floor(a.x/50.0),',',
			floor(a.y/50.0)-1,',',
			floor(a.z/50.0))
	as bbox8, -- "layer same; bottom middle"

	concat(floor(a.x/50.0)+1,',',
		floor(a.y/50.0)-1,',',
		floor(a.z/50.0))
	as bbox9, -- "layer same; bottom right"

	concat(floor(a.x/50.0)-1,',',
		floor(a.y/50.0)+1,',',
		floor(a.z/50.0)-1)
	as bboxbottom1, -- "layer below; top left"

	concat(floor(a.x/50.0),',',
		floor(a.y/50.0)+1,',',
		floor(a.z/50.0)-1)
	as bboxbottom2, -- "layer below; top middle"

	concat(floor(a.x/50.0)+1,',',
		floor(a.y/50.0)+1,',',
		floor(a.z/50.0)-1)
	as bboxbottom3, -- "layer below; top right"

	concat(floor(a.x/50.0)-1,',',
		floor(a.y/50.0),',',
		floor(a.z/50.0)-1)
	as bboxbottom4, -- "layer below; middle left"

	concat(floor(a.x/50.0),',',
			floor(a.y/50.0),',',
			floor(a.z/50.0)-1)
	as bboxbottom5, -- "layer below; middle middle"

	concat(floor(a.x/50.0)+1,',',
		floor(a.y/50.0),',',
		floor(a.z/50.0)-1)
	as bboxbottom6, -- "layer below; middle right"

	concat(floor(a.x/50.0)-1,',',
		floor(a.y/50.0)-1,',',
		floor(a.z/50.0)-1)
	as bboxbottom7, -- "layer below; bottom left"

	concat(floor(a.x/50.0),',',
			floor(a.y/50.0)-1,',',
			floor(a.z/50.0)-1)
	as bboxbottom8, -- "layer below; bottom middle"

	concat(floor(a.x/50.0)+1,',',
		floor(a.y/50.0)-1,',',
		floor(a.z/50.0)-1)
	as bboxbottom9 -- "layer below; bottom right"
from data_xyz a
where z > 50
),

distcalc as (
	select a.tstamp, a.id as car1, b.id as car2,
	round(sqrt(((b.x - a.x)^2) + ((b.y - a.y)^2) + ((b.z - a.z)^2)))
	as distance
from bbtable a
	inner join bbtable b
	on a.bbox = b.bbox or 
	a.bbox1 = b.bbox or
	a.bbox2 = b.bbox or
	a.bbox3 = b.bbox or
	a.bbox4 = b.bbox or
	a.bbox6 = b.bbox or
	a.bbox7 = b.bbox or
	a.bbox8 = b.bbox or
	a.bbox9 = b.bbox or
	a.bboxbottom1 = b.bbox or
	a.bboxbottom2 = b.bbox or
	a.bboxbottom3 = b.bbox or
	a.bboxbottom4 = b.bbox or
	a.bboxbottom5 = b.bbox or
	a.bboxbottom6 = b.bbox or
	a.bboxbottom7 = b.bbox or
	a.bboxbottom8 = b.bbox or
	a.bboxbottom9 = b.bbox or
	a.bboxtop1 = b.bbox or
	a.bboxtop2 = b.bbox or
	a.bboxtop3 = b.bbox or
	a.bboxtop4 = b.bbox or
	a.bboxtop5 = b.bbox or
	a.bboxtop6 = b.bbox or
	a.bboxtop7 = b.bbox or
	a.bboxtop8 = b.bbox or
	a.bboxtop9 = b.bbox
where a.tstamp = b.tstamp and a.id != b.id
order by a.id, a.bbox
)

select d.* from distcalc d where d.distance < 50 order by d.tstamp, d.car1, d.car2
;

-- Part 2: Cars @ stoplight camers
-- runtime: 3n (3 passes with no joins) aka O(n)

with speeds as (
	Select a.*, 
		round((sqrt((lag(a.x) over (partition by a.lplate order by a.tstamp) - a.x)^2 + 
			(lag(a.y) over (partition by a.lplate order by a.tstamp) - a.y)^2))
		/ (a.tstamp - lag(a.tstamp) over (partition by a.lplate order by a.tstamp))
		* 3600) 
		as mph
	From carxyz a
	order by a.lplate
), 
toofast as (
	select a.*
	from speeds a
	where a.mph > 50
	order by a.mph desc
)

select a.*
from toofast a
limit (select count(*)*.05 from toofast)
;

