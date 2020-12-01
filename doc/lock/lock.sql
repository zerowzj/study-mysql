#查看事务隔离级别
select @@tx_isolation;

#关闭自动提交
set autocommit=off;
#
show variables;
show variables like 'autocommit';

#查看正执行事务在
select * from information_schema.innodb_trx;
#查看正在锁的事务
select * from information_schema.innodb_locks;
#查看等待锁的事务
select * from information_schema.innodb_lock_waits;

#看哪些表锁到了
show open tables where in_use > 0;
#查看进程号
show processlist;
#删除事务
kill trx_mysql_thread_id
