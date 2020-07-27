#!/usr/bin/perl
use strict;
use warnings;
use Getopt::Long qw(GetOptions);

#	TO DO:
#	Determine # of arguments
#		Error handling
#		Determine Join type
#	Load files
#		Open files
#		Sort files
#		Determine file sizes
#	Join Files
#		Methods for various joins
#			merge join, inner loop join, or hash join, etc.
#		Output to ouputFile
use subs qw(csv2hash field_selector help optionizer loader hash_join merge_join nested_join);
my (@file1, @file2, @options, @filenames, %option_help);
my ($is_jointype, $is_key1, $is_key2, $is_swapped); 
	$is_jointype = $is_swapped = 0;
	$is_key1 = $is_key2 = 1;
my $arg_min = 2;
my $arg_max = 8;
my $args = (scalar @ARGV);

# Hash for matching arguments to join type
my %jointypes = (
	nl => "inner loop join",
	nested => "inner loop join",
	inner => "inner loop join",
	0 => "hash join",
	m => "merge join",
	merge => "merge join",
	);
# Hash for calling appropriate join function
my %joinfuncs = (
	nl => \&nested_join,
	nested => \&nested_join,
	inner => \&nested_join,
	0 => \&hash_join,
	hash => \&hash_join,
	m => \&merge_join,
	merge => \&merge_join,
	);

print "\n\t\tWelcome to Joiner! Here goes nothing...\n";
# if arguments outside acceptable range
if (($args < $arg_min) || ($args > $arg_max)) { 
	help;
	exit;
}

# if options passed via terminal arguments process them 
if (($args > $arg_min)) { 
	optionizer; 
}

# load files
loader;

# ----------- Test printing
 
print "key1: $is_key1 \n";
print "key2: $is_key2\n\n";

# call appropriate join function
$joinfuncs{$is_jointype}();


# ------------ subroutines

sub csv2hash { ## example of csv2sql from course notes
	while(chomp($_ = <>)){
    my @r = split/,/;
    print "INSERT INTO $ARGV[0] VALUES (".
        join(",", map { "’$_’" } @r).
    ");\n";
}
}

sub hash_join {
	print "hash join\n";
}

# help partial
sub help { # print syntax help %option_help == fluff hash. complete if time allows
	print "Syntax : joiner.pl "; 
	print "[-j join_type] ";
	print "[-f1 key_field_num] [-f2 key_field_num]  file1 file2\n";
	# perhaps -v verbose??
	print "DEFAULT: hash join using field 1 for both files...\n";
	print "use 'h' or 'help' for option details...\n\n";
	
	# access option help
	if ((scalar @ARGV == 1) && ($ARGV[0] eq "h" or $ARGV[0] eq "help") ) {
		print "\t\tHELP:\n ";

		%option_help = (
		'-j' , "\tspecify join type\n\t supported types: ",
		);
	}

	
}

# loader finished
sub loader { # Unix sort csv files
	
	@file1 =  `sort -t, -k $is_key1 $ARGV[-2]` or die "Couldn't find file1: $!";
	@file2 =  `sort -t, -k $is_key2 $ARGV[-1]` or die "Couldn't find file2: $!";

	if(($#file1) > ($#file2)){ 	# if file1 larger, swap files & join keys
		($ARGV[-2], $ARGV[-1]) = ($ARGV[-1], $ARGV[-2]);
		($is_key1, $is_key2) = ($is_key2, $is_key1); 
		my @fileTemp = @file1;
		@file1 = @file2;
		@file2 = @fileTemp;
	}

	print "Joining File1:[$ARGV[-2]] with File2:[$ARGV[-1]] ".
		"using $jointypes{$is_jointype}\n"; 
}

sub merge_join {
	print "merge join \n";
}

# inner loop join finished
# technically a merge join, 
# since UNIX sorts files by key on load
sub nested_join {
	my (@r1, @r2, $r1, $r2, $temp1, $temp2);
	foreach $a (@file1){
		$r1 = $a;
		chomp $r1;
		@r1 = split /,/, $r1;
		foreach $b (@file2){
			$r2 = $b;
			chomp $r2;
			@r2 = split /,/, $r2;
			$temp1 = @r1[$is_key1-1];
			$temp2 = @r2[$is_key2-1];
			if ($temp1 eq $temp2) {
				print "$r1 $r2 \n"; 
				# $r to output rec's w/ commas
			}
		}	  
	}
}

# optionizer finished
sub optionizer { 	# process option flags 
	@options = @ARGV;
	pop @options; #removing filenames from arguments
	pop @options;
	GetOptions(
		'j=s' => \$is_jointype,
		'f1=s' => \$is_key1,
		'f2=s' => \$is_key2
		) or die "Usage: $0 -h 0=all, 1, or 2 -j JOINTYPE -1 COLUMN NUMBER -2 COLUMN NUMBER\n";
		if (scalar @options > 0) {
			print "options selected: @options\n";
		}
}