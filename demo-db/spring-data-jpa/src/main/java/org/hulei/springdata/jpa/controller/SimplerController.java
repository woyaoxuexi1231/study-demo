package org.hulei.springdata.jpa.controller;

import org.hulei.entity.jpa.pojo.BigDataUser;
import org.hulei.entity.jpa.starter.dao.BigDataUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/10/10 22:58
 */

@RestController
public class SimplerController {

    @Autowired
    BigDataUserRepository bigDataUserRepository;

    @GetMapping(value = "/selectEmployees")
    public void selectEmployees() {
        bigDataUserRepository.fetchTop100().forEach(System.out::println);
    }

    @GetMapping("/get-users")
    public Page<BigDataUser> getUsers(@RequestParam(value = "page") int page,
                                      @RequestParam(value = "size") int size) {
        /*
        jpa 分页
        数据量大的时候这样性能会很低，可以通过把 Page 改为 Slice 这样就不会统计总数了
        Slice 是 findDataPage 方法的返回值，只需要修改掉即可
         */
        Pageable pageable = PageRequest.of(page - 1, size);
        return bigDataUserRepository.findDataPage(pageable);
    }

    @GetMapping("/get-users-by-name")
    public Page<BigDataUser> getUsersByName(@RequestParam(value = "page") int page,
                                            @RequestParam(value = "size") int size,
                                            @RequestParam(value = "name") String name) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return bigDataUserRepository.findByNameContaining(name, pageable);
    }
}
