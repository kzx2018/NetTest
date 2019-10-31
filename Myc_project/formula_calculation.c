#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <math.h>

#define	LINELEN		72


/*------------------------------------------------------------------------
 * main - Concurrent TCP server for MATH service
 *------------------------------------------------------------------------
 */
int
main(int argc, char *argv[])
{
	char buf[LINELEN],str1[LINELEN],str2[LINELEN],str[LINELEN];
	int  fd,cc;
	int x,n;
	fd = atoi(argv[1]);	
	cc = read(fd, buf, sizeof buf);

	int i = 0,j = 0;
	while(buf[i]>='0'&&buf[i]<='9'){
		str1[j++] = buf[i];
		i++;
	}
	str1[j]='\0';
	j = 0;
	i++;
	while(buf[i]>='0'&&buf[i]<='9'&&i<=cc-2){
		str2[j++] = buf[i];
		i++;
	}
	str2[j]='\0';
	x= atoi(str1);
	n= atoi(str2);
	double answer = 0;
	double e=2.71828;
	for(int k=0;k<n;k++){
	  double temp = pow(e+x,n);
	  answer += temp;
	}
	sprintf(str,"%.6lf",answer);
	dup2(fd, STDOUT_FILENO);
	
	for(int k = 0;k<strlen(str);k++){
		printf("%c",str[k]);
	}
	printf("\n");
	fflush(stdout);
	

	return 0;
}


/*
	cc = read(fd, str, sizeof str);
	for(int i=0;i<cc-1;i++){
		buf[i]=str[i];
	}
	
	int i = 0,j = 0;
	while(buf[i]>='0'&&buf[i]<='9'){
		str1[j++] = buf[i];
		i++;
	}
	str1[j]='\0';
	j = 0;
	i++;
	while(buf[i]>='0'&&buf[i]<='9'){
		str2[j++] = buf[i];
		i++;
	}
	str2[j]='\0';
	
	x= atoi(str1);
	n= atoi(str2);
	double answer = 0;
	double e=2.71828;
	for(int k=0;k<n;k++){
	  double temp = pow(e+x,n);
	  answer += temp;
	}
	sprintf(str, "%.6lf",answer);
	dup2(fd, STDOUT_FILENO);
    	for(int k = 0;k<cc;k++){
		printf("%c",str[k]);
	}
	printf("\n");
*/
	/*while (cc = read(fd, buf, sizeof buf)) {

	int i = 0,j = 0;
	while(buf[i]>='0'&&buf[i]<='9'){
		str1[j++] = buf[i];
		i++;
	}
	str1[j]='\0';
	j = 0;
	i++;
	while(buf[i]>='0'&&buf[i]<='9'){
		str2[j++] = buf[i];
		i++;
	}
	str2[j]='\0';
	
    	dup2(fd, STDOUT_FILENO);
	printf("%s",buf);
	/*x= atoi(str1);
	n= atoi(str2);
	float answer = 0;
	float e=2.71828;
	for(int k=0;k<n;k++){
	  float temp = pow(e+x,n);
	  answer += temp;
	}
	sprintf(str, "%.6lf",answer);
	dup2(fd, STDOUT_FILENO);
    	
	printf("%c%c%c%c",str[0],str[1],str[2],str[3]);
	printf("%c%c%c",str[4],str[5],str[6]);
	
	printf("\n");
	fflush(stdout);
	
	

	}*/
