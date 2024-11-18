package org.hulei.mybatis.spring;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.hulei.entity.jpa.pojo.Customer;
import org.hulei.mybatis.mapper.CustomerMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/11/17 16:24
 */

@RequiredArgsConstructor
@RestController
public class MybatisController {

    private final CustomerMapper customerMapper;

    @GetMapping(value = "/customers")
    public PageInfo<Customer> getCustomers() {
        PageHelper.startPage(1, 10);
        return new PageInfo<>(customerMapper.selectAll());
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/customers/{id}")
    public void updateCustomer(@PathVariable(value = "id") Integer id) {
        // @PathVariable 需要指定路径参数是哪个，不然就会报下面的错误
        // Name for argument of type [java.lang.String] not specified, and parameter name information not available via reflection. Ensure that the compiler uses the '-parameters' flag.

        Customer customer = new Customer();
        customer.setId(id);
        customer.setPhone("40.32.252");
        customerMapper.updateOne(customer);

        throw new RuntimeException("error");
    }
}
