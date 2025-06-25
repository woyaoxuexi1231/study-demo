package org.hulei.entity.jpa.utils;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.lang.Snowflake;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.jpa.pojo.Employee;
import org.hulei.util.dto.PageInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * @author hulei
 * @since 2024/11/16 0:53
 */

@Slf4j
public class MemoryDbUtil {

    /**
     * 内存数据
     */
    private static final Map<Long, Employee> data = new ConcurrentHashMap<>();
    /**
     * 雪花算法
     */
    private static final Snowflake snowflake = new Snowflake(0, 0);

    private static final int initSize = 45;

    static {
        Faker faker = new Faker(Locale.CHINA);
        for (int i = 0; i < initSize; i++) {
            // long id = snowflake.nextId();
            long id = i + 1;
            data.put(id, new Employee()
                    .setId(id)
                    .setLastName(faker.name().firstName())
                    .setFirstName(faker.name().firstName())
                    .setExtension(faker.app().name())
                    .setEmail(faker.internet().emailAddress())
                    .setOfficeCode(faker.address().countryCode())
                    .setReportsTo(0)
                    .setJobTitle(faker.job().title()));
        }
    }

    public static PageInfo<Employee> getData(int page, int size) {
        if (page < 1) {
            page = 1;
        }

        List<Employee> employeeList = data.values().stream().sorted(Comparator.comparing(Employee::getId)).collect(Collectors.toList());

        PageInfo<Employee> pageInfo = new PageInfo<>();
        List<Employee> employees = new ArrayList<>();
        for (int i = (page - 1) * size; i < employeeList.size() && employees.size() < size; i++) {
            employees.add(employeeList.get(i));
        }

        pageInfo.setPageNum(page);
        pageInfo.setPageSize(size);
        pageInfo.setPages((int) Math.ceil((double) employeeList.size() / size));

        pageInfo.setNextPage((page + 1) > ((employeeList.size() / size) + 1) ? page : (page + 1));
        pageInfo.setPrePage(Math.max((page - 1), 1));

        pageInfo.setHasPreviousPage((page - 1) > 0);
        pageInfo.setHasNextPage((page + 1) <= ((employeeList.size() / size) + 1));

        pageInfo.setTotal(employeeList.size());
        pageInfo.setList(employees);

        return pageInfo;
    }

    public static Employee getEmployeeById(long id) {
        return data.get(id);
    }

    public static void insert(Employee employee) {
        data.put(employee.getId(), employee);
    }

    public static void update(Employee employee) {
        data.put(employee.getId(), employee);
    }

    public static void delete(Long id) {
        data.remove(id);
    }

}
