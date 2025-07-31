
# mysql 本身提供了一种递归查询使得我们可以通过这种方式来进行树形结构的查询

with recursive tree_cte as (
    -- 基础查询：选择根节点
    select e.employee_number,
           e.reports_to parent_number,
           e.last_name,
           0 as         level
    from employees e
    where reports_to is null

    union all

    -- 递归查询子节点
    select e.employee_number,
           e.reports_to parent_number,
           e.last_name,
           cte.level + 1
    from employees e
             join tree_cte cte on e.reports_to = cte.employee_number)
SELECT *
FROM tree_cte;