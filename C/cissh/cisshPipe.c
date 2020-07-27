/* cisshPipe.c
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

/* cisshPipe(char* command1[], char* command2[])
 * handles command lines with pipes.
 */
void
cisshPipe(char* command1[], char* command2[])
{
  /* Ooops, we don't handle this yet.
   */
  //error("cissh: cisshPipe() is not yet implemented");
	
	pid_t   pid;
	int     fd[2];
	int		status;
	
	if ((pid = fork()) == 0) {
		pipe(fd);
		if (!fork()) {
			/* close normal stdout */
			close(1);
			/* make stdout same as fd[1] */
			dup(fd[1]);
			/* don't need stdinput */
			close(fd[0]);
			execvp(command1[0], command1);
		} else {
			//close normal stdin
			close(0);
			//make stdin same as fd[0]
			dup(fd[0]);
			close(fd[1]);
			execvp(command2[0], command2);
		}
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
