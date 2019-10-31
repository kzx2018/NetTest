/* CELEBD05

   This example duplicates an open file descriptor, using dup().

 */
#define _POSIX_SOURCE
#include <errno.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#undef _POSIX_SOURCE
#include <stdio.h>

void print_inode(int fd) {
 struct stat info;
 if (fstat(fd, &info) != 0)
   fprintf(stderr,"fstat() error for fd %d: %s\n",fd,strerror(errno));
 else
   printf("The inode of fd %d is %d\n", fd, (int) info.st_ino);
}

main() {
  int fd;
  if ((fd = dup(0)) < 0)
    perror("&dupf error");
  else {
    print_inode(0);
    print_inode(fd);
    puts("The file descriptors are different but");
    puts("they point to the same file.");
    close(fd);
  }
}
