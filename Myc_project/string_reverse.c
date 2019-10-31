#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>
#define	LINELEN		30

/*------------------------------------------------------------------------
 * main - Concurrent TCP server for ECHO service
 *------------------------------------------------------------------------
 */
int
main(int argc, char *argv[])
{
	char buf[LINELEN];
	char str[LINELEN];
 	int   cc,fd;
	fd = atoi(argv[1]);
	while (cc = read(fd, buf, sizeof buf)) {
	if (cc < 0)
		exit(0);

	if(cc==2){
    		dup2(fd, STDOUT_FILENO);
    		printf("%c\n",buf[0]);
    		fflush(stdout);	
	}
	else if(cc==3){
		dup2(fd, STDOUT_FILENO);
    		printf("%c%c\n",buf[1],buf[0]);
    		fflush(stdout);	
	}
	else{
    		dup2(fd, STDOUT_FILENO);
		for(int j=cc-2;j>=0;j--){
			printf("%c",buf[j]);
		}
		printf("\n");
		fflush(stdout);
    	}

	}
	return 0;
}

/*
	cc = read(fd, buf, sizeof buf);

	if(cc==2){
    		dup2(fd, STDOUT_FILENO);
    		printf("%s\n",buf);
    		fflush(stdout);	
	}
	else if(cc==3){
		dup2(fd, STDOUT_FILENO);
    		printf("%c%c\n",buf[1],buf[0]);
    		fflush(stdout);	
	}
	else{
    		int i,j;
		char k;
		i=0;
		j=strlen(buf)-1;
		
   		for(i,j;j>=0;j--)
		{
			if((buf[j]<='z'&&buf[j]>='a')||(buf[j]<='Z'&&buf[j]>='A')||buf[j]<='9'&&buf[j]>='0'||buf[j]=='\n'||buf[j]=='\r'||buf[j]=='!'){
			str[i++]=buf[j];
			}
		}
		str[i] = '\0';
		write(fd, str, strlen(str));
    	}
*/
