```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
✅ Elasticsearch security features have been automatically configured!
✅ Authentication is enabled and cluster connections are encrypted.

ℹ️  Password for the elastic user (reset with `bin/elasticsearch-reset-password -u elastic`):
  hX--6_2X03hpdsTqy4MB

ℹ️  HTTP CA certificate SHA-256 fingerprint:
  bfc4e2493eceafc1415e966f9e438916096590101c46ac088b54a69b39fa59de

ℹ️  Configure Kibana to use this cluster:
• Run Kibana and click the configuration link in the terminal when Kibana starts.
• Copy the following enrollment token and paste it into Kibana in your browser (valid for the next 30 minutes):
  eyJ2ZXIiOiI4LjExLjQiLCJhZHIiOlsiMTkyLjE2OC44MC4xMjg6OTIwMCJdLCJmZ3IiOiJiZmM0ZTI0OTNlY2VhZmMxNDE1ZTk2NmY5ZTQzODkxNjA5NjU5MDEwMWM0NmFjMDg4YjU0YTY5YjM5ZmE1OWRlIiwia2V5IjoiZlNWWGVaRUJGajJWVzdObS1SZmk6WWd2SV9QZzdTSFdvaGQwdzRydC1CdyJ9

ℹ️ Configure other nodes to join this cluster:
• Copy the following enrollment token and start new Elasticsearch nodes with `bin/elasticsearch --enrollment-token <token>` (valid for the next 30 minutes):
  eyJ2ZXIiOiI4LjExLjQiLCJhZHIiOlsiMTkyLjE2OC44MC4xMjg6OTIwMCJdLCJmZ3IiOiJiZmM0ZTI0OTNlY2VhZmMxNDE1ZTk2NmY5ZTQzODkxNjA5NjU5MDEwMWM0NmFjMDg4YjU0YTY5YjM5ZmE1OWRlIiwia2V5IjoiZnlWWGVaRUJGajJWVzdObS1oZE46Q1BKTWFDb2dUYktVUG8yMmc3a01yQSJ9

  If you're running in Docker, copy the enrollment token and run:
  `docker run -e "ENROLLMENT_TOKEN=<token>" docker.elastic.co/elasticsearch/elasticsearch:8.11.4`
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```


es安装
https://cloud.tencent.com/developer/article/2216722

elk安装
https://www.bilibili.com/video/BV1nF4m1c7nk/?p=9&spm_id_from=333.788.top_right_bar_window_history.content.click&vd_source=8d7ce9dd45b35258ee11a3c3ce982ea9

1. 启动es
   nohup ./elasticsearch &
2. 启动kibana
   nohup ./kibana &


ELK架构 - Elasticsearch、Logstash和Kibana
- Elasticsearch，担任数据持久层的角色，负责储存数据
- Logstash，担任控制层的角色，负责搜集和过滤数据
- Kibana，担任视图层角色，拥有各种维度的查询和分析，并使用图形化的界面展示存放在Elasticsearch中的数据


