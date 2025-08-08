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
    public void initStock(@PathVariable Long productId,
                          @PathVariable Integer stock) {
        seckillStockService.initStock(productId, stock);
    }

    @GetMapping("/init-stock/{productId}")
    public int getRemainStock(@PathVariable Long productId) {
        return seckillStockService.getRemainStock(productId);
    }

    @GetMapping("/try-seckill/{productId}/{quantity}")
    public SeckillService.SeckillResult trySeckill(@PathVariable long productId,
                                                   @PathVariable int quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        return seckillService.trySeckill(productId, principal.getId(), quantity);
    }
}
