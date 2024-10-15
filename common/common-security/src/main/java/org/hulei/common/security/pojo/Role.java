package org.hulei.common.security.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.security.pojo
 * @className: Role
 * @description:
 * @author: h1123
 * @createDate: 2023/5/9 20:48
 */

@TableName(value = "role")
@Data
public class Role implements GrantedAuthority {

    @TableId
    private Long id;

    @TableField(value = "name")
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
