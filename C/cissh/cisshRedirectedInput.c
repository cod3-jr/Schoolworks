/* cisshRedirectedInput.c
 */
#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>

/* External functions --
 */
extern void error(char* message);

/* cisshRedirectedInput(char* command[], char* inputFile)
 * handles command lines with redirected input.
 */
void
cisshRedirectedInput(char* command[], char* inputFile)
{
  /* Ooops, we don't handle this yet.
   */
  //error("cissh: cisshRedirectedInput() is still in developmentz");
	
	pid_t		pid;
	int		status;
	int		fd;
	
	if ((pid = fork()) == 0) {
		fd = open(inputFile, O_RDONLY);
		if (fd < 0) {
			error("cissh: error opening standard input file");
			exit(1);
		}
		//redirect file to file descriptor 0
		close(0);
		if (dup(fd) < 0) {
			error("cissh: error duplicating standard input");
			perror("dup()");
			exit(1);
		}
		
		close(fd);
		execvp(command[0], command);
		
		error("cissh: failure to exectue command");
		exit(1);
	}
	else {
		if(wait(&status) < 0)
		{
	  error("cissh: error waiting for child.");
	  perror("wait");
		}
		
		if(status != 0)
			error("cissh: command exited with nonzero error status.");
	}
}
