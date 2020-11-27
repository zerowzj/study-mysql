# 当前读

```
select...lock in share mode (共享读锁)
select...for update
update
delete
insert
```

​		InnoDB 给每一个事务生成一个唯一事务 ID 的方法称为生成快照，因此这种场景称为快照读。但是对于更新数据不能使用快照读，因为更新数据时如果使用快照读会可能会覆盖其他事务的更改。另外查询时如果加锁也会采用当前读的方式。**当前读**就是读这个数据最新的提交数据。InnoDB 的多版本并发控制实现了在串行化的隔离级别下读不加锁，提高了并发性能。

​		当前读, 读取的是最新版本, 并且对读取的记录加锁, 阻塞其他事务同时改动相同记录，避免出现安全问题。例如，假设要update一条记录，但是另一个事务已经delete这条数据并且commit了，如果不加锁就会产生冲突。所以update的时候肯定要是当前读，得到最新的信息并且锁定相应的记录。

## 实现方式

**next-key锁（行记录锁+Gap间隙锁）**

间隙锁：只有在Read Repeatable、Serializable隔离级别才有，就是锁定范围空间的数据，假设id有3,4,5，锁定id>3的数据，是指的4，5及后面的数字都会被锁定，因为此时如果不锁定没有的数据，例如当加入了新的数据id=6，就会出现幻读，间隙锁避免了幻读。

1. 对主键或唯一索引，如果当前读时，where条件全部精确命中（=或者in），这种场景本身就不会出现幻读，所以只会加行记录锁。
2. 没有索引的列，当前读操作时，会加全表gap锁，生产环境要注意
3. 非唯一索引列，如果where条件部分命中（>、<、like等）或者全未命中，则会加附近Gap间隙锁。例如，某表数据如下，非唯一索引2,6,9,9,11,15。如下语句要操作非唯一索引列9的数据，gap锁将会锁定的列是(6,11]，该区间内无法插入数据。





# 快照读

​		单纯的select操作，不包括上述 select ... lock in share mode, select ... for update。

```
select
```

Read Committed隔离级别：每次select都生成一个快照读。

Read Repeatable隔离级别：开启事务后第一个select语句才是快照读的地方，而不是一开启事务就快照读。

## 实现方式

​		undolog和多版本并发控制MVCC。

​		事务会先使用“排他锁”锁定改行，将该行当前的值复制到undo log中，然后再真正地修改当前行的值，最后填写事务的**DB_TRX_ID**，使用回滚指针**DB_ROLL_PTR**指向undo log中修改前的行**DB_ROW_ID**。

​		其它：insert undo log只在事务回滚时需要, 事务提交就可以删掉了。update undo log包括update 和 delete , 回滚和快照读 都需要。

快照读是基于 MVCC 和 undo log 来实现的，适用于简单 select 语句。（读取当前版本的数据）
当前读是基于临键锁（行锁 + 间歇锁）来实现的，适用于 insert，update，delete， select … for update， select … lock in share mode 语句，以及加锁了的 select 语句。（永远读取最新的数据）

