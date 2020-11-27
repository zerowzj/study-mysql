# 一. 安装

​		My SQL安装有三种方式，包括二进制包安装（Using Generic Binaries）、RPM包安装、源码安装。一般是前两种比较多。

## 1.1 二进制安装

1. 新建用户和组

   ```
   groupadd mysql
   useradd mysql
   ```

2. 解压

   ```shell
   tar -zxvf mysql-5.7.29-linux-glibc2.12-x86_64.tar.gz -C /usr/local/mysql
   ```

3. 创建目录

   ```
   
   ```

   

4. 初始化

   ```shell
   ./mysqld --initialize \
       --user=mysql \
   	--basedir=/usr/local/mysql \
   	--datadir=/usr/local/mysql/data
   ```

5. 撒旦法

   ```
   
   ```



# 二. 配置（my.cfg）

## 2.1







1. https://www.jianshu.com/p/6430458aad78



http://www.3qphp.com/mysql/sqlquest/2859.html

