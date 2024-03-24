-- 如果存在这个存储过程那么删除它
DROP PROCEDURE IF EXISTS sp_clean_table;
-- 改变语句的结束符为 //
DELIMITER //
-- 创建sp_clean_table()这个存储过程
CREATE PROCEDURE sp_clean_table()
-- 开始
BEGIN
    -- 定义一个变量 done,这个用于标记是否完成
    DECLARE done INT DEFAULT FALSE;
-- 定义一个变量 v_table_name,用于拼接表名
    DECLARE v_table_name VARCHAR(255);
-- 定义游标  查询一个表的  未使用空间大小/(数据长度+索引长度) > 0.1
    DECLARE cur CURSOR FOR select concat(table_schema, '.', table_name)
                           from information_schema.tables
                           where table_schema in ('ast_db')
                             and round(data_free / (data_length + index_length), 2) >= 0.1;
    DECLARE cur2 CURSOR FOR select concat(table_schema, '.', table_name) from information_schema.tables where table_schema in ('ast_db');
-- 当游标无法继续读取数据时，设置 done 变量为 TRUE，表示结束遍历。
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
-- 打开游标
    OPEN cur;

    read_loop:
    LOOP
        FETCH cur INTO v_table_name;
        IF done THEN
            LEAVE read_loop;
        END IF;

--
        select v_table_name from dual;
-- 强制优化表和分析表 它重建表以更新索引统计信息并释放聚簇索引中未使用的空间
        set @v_hs_sql = concat('alter table ', v_table_name, ' force;');
        prepare hs_stmt from @v_hs_sql;
        execute hs_stmt;
        deallocate prepare hs_stmt;
    END LOOP;

    CLOSE cur;

    SET done = FALSE;
    OPEN cur2;

    read_loop:
    LOOP
        FETCH cur2 INTO v_table_name;
        IF done THEN
            LEAVE read_loop;
        END IF;

        set @v_hs_sql = concat('analyze table ', v_table_name, ';');
        prepare hs_stmt from @v_hs_sql;
        execute hs_stmt;
        deallocate prepare hs_stmt;
    END LOOP;

    CLOSE cur2;

END //
DELIMITER ;

