zuul.routes.hiapi.path=/hiapi/**
zuul.routes.hiapi.service-id=eureka-client
zuul.routes.ribbon.path=/ribbonapi/**
zuul.routes.ribbon.service-id=eureka-ribbon-client
zuul.routes.feign.path=/feignapi/**
zuul.routes.feign.service-id=eureka-feign-client
zuul.prefix=/v1

