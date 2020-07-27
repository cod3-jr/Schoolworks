--First, table for populating numbers

create table numbers(number bigint);

--Use this function to fill it in:

create or replace function populate(top bigint) RETURNS void as $$
declare
i bigint:=1;
begin
while(i<=top) LOOP
insert into numbers(number) 
values(i);
i:=i+1;
END LOOP;
END; $$ LANGUAGE plpgsql;

--Function primes that returns all primes up to N

create or replace function primes(N bigint) RETURNS void AS $$

DECLARE
first bigint :=3;
last bigint :=2;

BEGIN
--create table pri and insert all odd integers from 3 to N (and 2)

create table pri(a bigint);
INSERT into pri(a)
select number
from numbers
where (number%2 != 0 or number = 2)
AND number<=N AND number!=1;

--Using 'Sieve of Erastothenes' to find primes

while (last < sqrt(n)) LOOP

first:= (select * from pri where a>last order by a limit 1);
last:= first* first;

--delete from list of primes all multiples of the primes in the range of first-last
-- (first run-through is primes in range of 3-9, second run-through would be primes in range of 11-121, etc.)

delete from pri
where a in (select n1.number * p.a
from pri as p
inner join numbers as n1
on n1.number >= p.a
and n1.number<= n/p.a
where p.a>=first
and p.a<last);

END LOOP;
END; $$ LANGUAGE plpgsql;

select populate(10000000);
select primes(10000000);
select * from pri order by 1;