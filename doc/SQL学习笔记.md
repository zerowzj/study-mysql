

# 一. SQL查询执行顺序

​		SQL语言不同于其他编程语言（如C++，Java），最明显的不同体现在处理代码的顺序上。在大多数编程语言中，代码按编码顺序被处理。但在SQL语言中，第一个被处理的子句总数FROM子句，下面显示了逻辑查询处理的顺序以及步骤的序号：

```sql
[8]  SELECT [9]DISTINCT <select_list> 
[1]  FROM <left_table>
[3]  <join_type> JOIN <right_table>
[2]  ON <join_condition>
[4]  WHERE <where_condition>
[5]  GROUP BY <group_by_list>
[6]  WITH {CUBE|ROLLUP}
[7]  HAVING <having_condition>
[10] ORDER BY <order_by_list>
[11] LIMIT <limit_number>
```

​		可以看到一共有11个步骤，最先执行的是FROM操作，最后执行的是LIMIT操作。每个操作都会产生一张虚拟表。该虚拟表作为一个处理的输入。这些虚拟表对用户不是透明的，只有最后一步生成的虚拟表才会返回给用户。如果没有在查询中指定某一子句， 则将跳过相应的步骤。

1. FROM

   对FROM子句中的左表<left_table>和右表<right_table>执行笛卡尔积，产生虚拟表VT1

2. ON

   对虚拟表VT1应用ON筛选，只有那些符合<join_condition>的行才被插入虚拟表VT2中

3. JOIN

   如果指定了OUTER JOIN（如LEFT OUTER JOIN ，RIGTH OUTER JOIN），那么保留表中未匹配的行作为外部行添加到虚拟表VT2中，产生虚拟表VT3。如果FROM子句含两个以上表，则对上一个连接生成的结果表VT3和下一个表重复执行步骤1~步骤3，直到处理完所有的表为止

4. WHERE

   对虚拟表VT3应用VT3应用WEHRE过滤条件，只有符合<where_conditon>的记录才被插入虚拟表VT4中

5. GROUP BY

   根据GROUP BY 子句中的列，对VT4中的记录进行分组操作，产生VT5

6. CUBE|ROLLUP

   对表VT5进行CUBE或ROLLUP操作，产生表VT6

7. HAVING

   对虚拟表VT6应用HAVING过滤器，只有符合<having_condition>的记录才被插入虚拟表VT7中

8. SELECT

   选定指定的列，插入到虚拟表VT8中

9. DISTINCT

   去除重复数据，产生虚拟表VT9

10. ORDER BY

    将虚拟表VT9中的记录按照<order_by_list>进行排序操作，产生虚拟表VT10

11. LIMIT

    取出指定行的数据，产生虚拟表VT11，并返回给查询用户

    

# 一. 执行计划

## 1.1 语法

​		MySQL 使用 explain + sql 语句查看 执行计划，该执行计划不一定完全正确但是可以参考。

```sql
EXPLAIN SELECT * FROM user_base WHERE ub_id = 6;
```

## 1.2 分析

​		首先要查看table对应的表，在真实环境中，可能存在多个联合查询，分析时需要明确是哪张表查询出现性能瓶颈。

​		然后是查看type对应的值，第三个是possible-keys，第四个key，第五个key_len，第六个Extra

type指的是查询的类型，分为全表扫描和索引扫描。全表扫描是低效的。索引扫描又分为几个级别，包含辅助索引扫描和聚集索引扫描，各个级别不一样，性能也不一样。

1. **id**

   select查询的序列号，包含一组数字，表示查询中执行select子句或操作表的顺序。三种情况：

   - id相同：执行顺序由上至下
   - id不同：如果是子查询，id的序号会递增，id值越大优先级越高，越先被执行
   - id相同又不同（两种情况同时存在）：id如果相同，可以认为是一组，从上往下顺序执行；在所有组中，id值越大，优先级越高，越先执行

2. **select_type**

   查询的类型，主要是用于区分普通查询、联合查询、子查询等复杂的查询

   - SIMPLE

     简单的select查询，查询中不包含子查询或者union。明白

   - PRIMARY

     查询中包含任何复杂的子部分，最外层查询则被标记为primary。

   - SUBQUERY

     在select 或 where列表中包含了子查询。

   - DERIVED

     在from列表中包含的子查询被标记为derived（衍生），mysql或递归执行这些子查询，把结果放在零时表里。

   - UNION

     若第二个select出现在union之后，则被标记为union；若union包含在from子句的子查询中，外层select将被标记为derived。

   - UNION RESULT

     从union表获取结果的select。

3. table

4.  

5. **type**

   访问类型，sql查询优化中一个很重要的指标，结果值从好到坏依次是：

   ```sql
   system > const > eq_ref > ref > fulltext > ref_or_null > index_merge > unique_subquery > index_subquery > range > index > ALL
   ```

   - system

     表只有一行记录（等于系统表），这是const类型的特例，平时不会出现，可以忽略不计。

   - const

     表示通过索引一次就找到了，const用于比较primary key 或者 unique索引。因为只需匹配一行数据，所有很快。如果将主键置于where列表中，mysql就能将该查询转换为一个const。

   - eq_ref

     唯一性索引扫描，对于每个索引键，表中只有一条记录与之匹配。常见于主键 或 唯一索引扫描。

   - ref

     非唯一性索引扫描，返回匹配某个单独值的所有行。本质是也是一种索引访问，它返回所有匹配某个单独值的行，然而他可能会找到多个符合条件的行，所以它应该属于查找和扫描的混合体。

   - range

     只检索给定范围的行，使用一个索引来选择行。key列显示使用了那个索引。一般就是在where语句中出现了bettween、<、>、in等的查询。这种索引列上的范围扫描比全索引扫描要好。只需要开始于某个点，结束于另一个点，不用扫描全部索引

   - index

     遍历索引，只查询索引列，index与ALL区别为index类型只遍历索引树。这通常为ALL快，应为索引文件通常比数据文件小。（Index与ALL虽然都是读全表，但index是从索引中读取，而ALL是从硬盘读取）

   - ALL

     全表扫描，没使用到索引，遍历全表以找到匹配的行

   

6. **possible_key**

   查询涉及到的字段上存在索引，则该索引将被列出，但不一定被查询实际使用

7. **key**

   实际使用的索引，如果为NULL，则没有使用索引。查询中如果使用了覆盖索引，则该索引仅出现在key列表中。

8. **key_len**

   索引长度，

9. ref

   连接匹配条件，

10. **rows**

    返回估算的结果集数目，注意这并不是一个准确值。

11. filterd

12. Extra

    不适合在其他字段中显示，但是十分重要的额外信息。

    - Using index：使用覆盖索引
    - Using where：
    - Using filesort
    - Using temporary
    - Using join buffer (Block Nested Loop)



# 二. 索引

## 2.1 索引优化











