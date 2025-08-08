package org.hulei.demo.cj.seckill;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀订单实体类
 */

@Setter
@Getter
public class SeckillOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    // Getter和Setter方法
    private Long id;                // 订单ID（主键）
    private Long userId;            // 用户ID
    private Long productId;         // 商品ID
    private String productName;     // 商品名称（冗余字段）
    private BigDecimal seckillPrice; // 秒杀价格
    private Integer quantity;       // 购买数量
    private BigDecimal totalAmount; // 订单总金额
    private Integer status;         // 订单状态（0-待支付，1-已支付，2-已取消，3-已退款）
    private Date createTime;        // 创建时间
    private Date payTime;          // 支付时间
    private String orderNo;        // 订单编号（业务主键）
    private String shippingAddress; // 收货地址
    private String contactPhone;   // 联系电话
    private String contactName;    // 联系人

    // 支付相关字段
    private String paymentType;    // 支付方式
    private String paymentNo;      // 支付流水号
    private Date paymentTime;      // 支付时间

    // 防重字段
    private String seckillToken;   // 秒杀令牌（防重复提交）

    // 构造方法
    public SeckillOrder() {
    }

    public SeckillOrder(Long userId, Long productId, Integer quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.createTime = new Date();
        this.status = 0; // 默认待支付状态
    }

    // 业务方法
    public boolean isPaid() {
        return status != null && status == 1;
    }

    public boolean canCancel() {
        return status != null && status == 0;
    }

    // 计算订单总金额
    public void calculateTotal() {
        if (seckillPrice != null && quantity != null) {
            this.totalAmount = seckillPrice.multiply(new BigDecimal(quantity));
        }
    }

    @Override
    public String toString() {
        return "SeckillOrder{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", seckillPrice=" + seckillPrice +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", createTime=" + createTime +
                ", orderNo='" + orderNo + '\'' +
                '}';
    }
}