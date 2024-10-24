参考文章: [Admin API | Apache APISIX® -- Cloud-Native API Gateway](https://apisix.apache.org/zh/docs/apisix/admin-api/)

## apisix amdin api的配置

```yaml
deployment:
    admin:
        admin_key:
        - name: admin
        	# 使用默认的 Admin API Key 存在安全风险，部署到生产环境时请及时更新
            key: edd1c9f034335f136f87ad84b625c8f1  
            role: admin
        # http://nginx.org/en/docs/http/ngx_http_access_module.html#allow
        allow_admin:                    
            - 127.0.0.0/24
        admin_listen:
        	# Admin API 监听的 IP，如果不设置，默认为“0.0.0.0”。
            ip: 0.0.0.0
            # Admin API 监听的 端口，必须使用与 node_listen 不同的端口。
            port: 9180                  
```

