#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#define file_name "data.txt"
int main(int argc, char *argv[])
{
    //先调用dup将标准输出拷贝一份，指向真正的标准输出
    int stdout_copy_fd = dup(STDOUT_FILENO);
    int file_fd = open(file_name, O_RDWR);
    //让标准输出指向文件
    dup2(file_fd, STDOUT_FILENO);
    printf("hello\n");
    //刷新缓冲区
    fflush(stdout);
    //恢复标准输出
    dup2(stdout_copy_fd, STDOUT_FILENO);
    printf("world\n");
    return 0;
}
