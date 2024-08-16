
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
✅ Elasticsearch security features have been automatically configured!
✅ Authentication is enabled and cluster connections are encrypted.

ℹ️  Password for the elastic user (reset with `bin/elasticsearch-reset-password -u elastic`):
LE*zCliAiSj*0Dp1Usdn

ℹ️  HTTP CA certificate SHA-256 fingerprint:
c6879864aeda3732660ea4123582c2e6f01c4eb7e8e74565da8b32a8d9f061c2

ℹ️  Configure Kibana to use this cluster:
• Run Kibana and click the configuration link in the terminal when Kibana starts.
• Copy the following enrollment token and paste it into Kibana in your browser (valid for the next 30 minutes):
eyJ2ZXIiOiI4LjE0LjAiLCJhZHIiOlsiMTkyLjE2OC44MC4xMjg6OTIwMCJdLCJmZ3IiOiJjNjg3OTg2NGFlZGEzNzMyNjYwZWE0MTIzNTgyYzJlNmYwMWM0ZWI3ZThlNzQ1NjVkYThiMzJhOGQ5ZjA2MWMyIiwia2V5IjoiZWlmMkNKRUJrcndSY2xVOWVUMUw6MmdWV0RyeWpSUkdHRXpXYy00bGo2QSJ9

ℹ️  Configure other nodes to join this cluster:
• On this node:
⁃ Create an enrollment token with `bin/elasticsearch-create-enrollment-token -s node`.
⁃ Uncomment the transport.host setting at the end of config/elasticsearch.yml.
⁃ Restart Elasticsearch.
• On other nodes:
⁃ Start Elasticsearch with `bin/elasticsearch --enrollment-token <token>`, using the enrollment token that you generated.
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━



━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
✅ Elasticsearch security features have been automatically configured!
✅ Authentication is enabled and cluster connections are encrypted.

ℹ️  Password for the elastic user (reset with `bin/elasticsearch-reset-password -u elastic`):
qNQwXe9TV1gLFRImusVi

ℹ️  HTTP CA certificate SHA-256 fingerprint:
07673538847c6a9722053de0384635981791b43de55cfb72b5e046a9914154ac

ℹ️  Configure Kibana to use this cluster:
• Run Kibana and click the configuration link in the terminal when Kibana starts.
• Copy the following enrollment token and paste it into Kibana in your browser (valid for the next 30 minutes):
`eyJ2ZXIiOiI4LjExLjQiLCJhZHIiOlsiMTkyLjE2OC44MC4xMjg6OTIwMCJdLCJmZ3IiOiIwNzY3MzUzODg0N2M2YTk3MjIwNTNkZTAzODQ2MzU5ODE3OTFiNDNkZTU1Y2ZiNzJiNWUwNDZhOTkxNDE1NGFjIiwia2V5IjoiSC1xTkRwRUJMVjczS1hzb1Uwb006QVRVd2NYdWFSVW0zckFoaEh6VmZqUSJ9`

ℹ️  Configure other nodes to join this cluster:
• On this node:
⁃ Create an enrollment token with `bin/elasticsearch-create-enrollment-token -s node`.
⁃ Uncomment the transport.host setting at the end of config/elasticsearch.yml.
⁃ Restart Elasticsearch.
• On other nodes:
⁃ Start Elasticsearch with `bin/elasticsearch --enrollment-token <token>`, using the enrollment token that you generated.
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

https://cloud.tencent.com/developer/article/2216722

1. 启动es
   nohup ./elasticsearch &
2. 启动kibana
   nohup ./kibana &


ELK架构 - Elasticsearch、Logstash和Kibana
- Elasticsearch，担任数据持久层的角色，负责储存数据
- Logstash，担任控制层的角色，负责搜集和过滤数据
- Kibana，担任视图层角色，拥有各种维度的查询和分析，并使用图形化的界面展示存放在Elasticsearch中的数据
