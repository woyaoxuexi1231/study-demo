package org.hulei.eneity.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "payments")
public class Payments {

    @TableId(value = "customer_number", type = IdType.AUTO)
    private Integer customerNumber;

    @TableId(value = "check_number", type = IdType.AUTO)
    private String checkNumber;

    @TableField(value = "payment_date")
    private Date paymentDate;

    @TableField(value = "amount")
    private BigDecimal amount;
}