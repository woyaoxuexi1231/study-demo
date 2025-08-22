package org.hulei.demo.cj.ratelimit.guava;

import com.google.common.util.concurrent.RateLimiter;

import java.util.HashMap;
import java.util.Map;

public class RateLimitHelper {

    private RateLimitHelper(){}

    private static Map<String, RateLimiter> rateMap = new HashMap<>();

    public static RateLimiter getRateLimiter(String limitType, double limitCount ){
        RateLimiter rateLimiter = rateMap.get(limitType);
        if(rateLimiter == null){
            rateLimiter = RateLimiter.create(limitCount);
            rateMap.put(limitType,rateLimiter);
        }
        return rateLimiter;
    }

}
