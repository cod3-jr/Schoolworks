#!/usr/bin/perl
use strict;
use warnings;

## INSTRUCTIONS (OS X / UNIX):
## launch terminal
## command:
## crontab -e
##
## vi text editor launches.
## navigate to new line if necessary
## to enter edit mode, press: i
##
## The following crontab line will run script every 1 day:
## 
## * * */1 * * perl [filepath]/dbbackup.pl 
##
## To switch back to command mode, press: ESC
## To save file and quit vi, enter: :wq
## To confirm successful entry, enter: crontab -l


# get date for datestamp on file
my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) = localtime;
$year += 1900; $mon += 1; # convert date values to human counting
my ($tname, $dbname, $fname, $pubkey);
$dbname = "hwk8"; #Could fill these from @ARGV
$tname = "test";
$fname = $year ."-". $mon . "-" . $mday . "_" . "$tname.csv.gz";
$pubkey = "Mischa";

# in case file already exists
`/bin/rm -f e.$fname`;

# create compressed file in working directory
`/usr/local/bin/psql -d $dbname -c "COPY $tname TO stdout DELIMITER ',' CSV HEADER" \ | /usr/bin/gzip > $fname`;

# encrypt with gpg2 using pub key. Tried pipe after gzip, but decryption failed?
`/usr/local/bin/gpg2 --out e.$fname --recipient "$pubkey" --encrypt $fname`;
# remove unencrypted gzip file 
`/bin/rm $fname`;