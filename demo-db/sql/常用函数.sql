
-- 字符串类

-- concat拼接函数,用于拼接多个字符串
select concat(test_employees.last_name, '-', test_employees.first_name) as fullname
from test_employees;
select *
from test_employees
where first_name like concat('%', 'a', '%');

-- 字符串截取,下标从1开始
select substr('hello world', 1, 2);
-- 字符串长度
select length('hello world');
-- 大小写转换
select lower('HELLO') as a, upper('world') as b;
-- 去除首尾空格
select trim(' hello world ');
-- 替换字符串中的子字符串
select replace('hello world', 'lo', '123');


-- 数值类函数
-- 返回数值的绝对值
select abs(-1);
-- 返回大于或等于x的最小整数(向上取整), ceil和ceiling没有任何区别,只是名字不同,区别于使用者的习惯
select ceil(0.1);
select ceiling(123.123);
-- 返回小于或等于x的最大整数(向下取整)
select floor(123.6);
-- 将一个数字四舍五入到d为小数 round(x,d)
select round(1234.4535, 1);
-- 返回0-1的随机数
select rand();


-- 日期和时间函数,可以传入整数,整数代表精确到秒后面的第几位
select now();
-- 返回当前日期
select curdate();
-- 返回当前时间
select curtime();
-- 提取日期部分, expr: 这是一个日期时间表达式，可以是日期、日期时间（DATETIME）、时间戳（TIMESTAMP）等类型。返回格式为 'YYYY-MM-DD'
select date('1992-10-3 10:34');
-- 提取年份, 月份, 日期
select year('1992-10-3 10:34');
select month('1992-10-3 10:34');
select day('1992-10-3 10:34');
-- 返回两个日期之间的差值
select datediff('1992-10-3 10:34', '1992-11-3 10:34');

-- 聚合函数
-- 求和
select sum(amount)
from test_payments;
-- 求平均
select avg(amount)
from test_payments;
-- 最大最小
select max(test_payments.amount)
from test_payments;
select min(test_payments.amount)
from test_payments;

-- 条件函数的运行
select *
from test_employees;
-- 这里把账户分级officeCode进行列转行
select test_employees.employee_number,
       test_employees.last_name,
       test_employees.first_name,
       case office_code when 1 then '√' else '×' end as '青铜',
       case office_code when 2 then '√' else '×' end as '白银',
       case office_code when 3 then '√' else '×' end as '黄金',
       case office_code when 4 then '√' else '×' end as '铂金',
       case office_code when 5 then '√' else '×' end as '钻石',
       case office_code when 6 then '√' else '×' end as '王者'
from test_employees;
select test_employees.employee_number,
       test_employees.last_name,
       test_employees.first_name,
       IF(office_code = 1, '√', '×') as '青铜',
       IF(office_code = 2, '√', '×') as '白银',
       IF(office_code = 3, '√', '×') as '黄金',
       IF(office_code = 4, '√', '×') as '铂金',
       IF(office_code = 5, '√', '×') as '钻石',
       IF(office_code = 6, '√', '×') as '王者'
from test_employees;

-- 其他函数
-- 返回一个非null的表达式
select coalesce(null, null, '1223123');
select ifnull('null', '前面是null');
select cast('123' as UNSIGNED);
