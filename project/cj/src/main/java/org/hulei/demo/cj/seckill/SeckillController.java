package org.hulei.demo.cj.seckill;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hulei.common.security.pojo.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2025/8/8 18:38
 */

@RequiredArgsConstructor
@RequestMapping("/seckill")
@RestController
public class SeckillController {

    /**
     * 库存预加载服务
     */
    final SeckillStockService seckillStockService;
    /**
     * 秒杀核心服务
     */
    final SeckillService seckillService;

    @PostMapping("/init-stock/{productId}/{stock}")
    public String initStock(@PathVariable Long productId,
                            @PathVariable Integer stock) {
        return seckillStockService.initStock(productId, stock);
    }

    @GetMapping("/remain-stock/{productId}")
    public int getRemainStock(@PathVariable Long productId) {
        return seckillStockService.getRemainStock(productId);
    }

    @PostMapping("/try-seckill/{productId}/{quantity}/{userId}")
    public SeckillService.SeckillResult trySeckill(@PathVariable long productId,
                                                   @PathVariable int quantity,
                                                   @PathVariable Long userId
    ) {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // User principal = (User) authentication.getPrincipal();
        return seckillService.trySeckill(productId, userId, quantity);
        // return null;
    }
}
