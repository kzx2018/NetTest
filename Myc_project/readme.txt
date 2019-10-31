

上图显示了多服务服务器的结构，这是常用的。

主服务器进程等待来自代表两个服务的两个主套接字的连接请求，

公式计算和字符串反转。一旦请求到达，主进程调用fork来创建一个从进程

处理连接。

与我们的多服务服务器示例不同，在本任务中，superd.c+sv_funcs.c作为服务器代码，在第4研讨会中给出

从进程调用execve（或任何其他exec函数）来执行两个独立的c程序，即，

用于提供这两种服务的formula_calculation.c和string_reverse.c。然而，

您可以使用现有的C代码，如Suff.C和其他亲属，作为模板。你的工作实际上是

修改此示例。关键的区别在于，必须使用exec系统调用来执行服务代码。

•当然，您需要编写TCP客户端来测试这两个服务。

这两个服务将被实现为两个独立的程序，formula_calculation.c和string_reverse.c，

而不是像研讨会的例子中那样作为两个函数，superd.c；

•两个C程序将分别编译并生成两个可执行文件。

•记住系统中可执行文件的路径，它将用作execve的输入参数

（或任何其他exec函数）系统调用。您可以从http://

linux.about.com/library/cmd/blcmdl3_execl.htm

•系统调用dup2（）或dup（）将用于复制文件描述符，该描述符可以表示套接字，标准

输入（键盘）或输出（显示屏）。您可以复习关于dup2（）或dup（）主题的研讨会2。 

gcc superd.c passiveTCP.c passiveUDP.c passivesock.c errexit.c -o superd 

gcc TCPmath.c connectTCP.c connectsock.c errexit.c -o TCPmath

