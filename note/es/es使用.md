以下是使用 Elasticsearch 的 RESTful API 进行索引和文档的基本操作示例。假设 Elasticsearch 的服务地址是 `http://localhost:9200`，操作的索引名为 `my_index`。

------

### 1. **创建索引**

创建一个新的索引。

```bash
PUT http://localhost:9200/my_index
```

请求体（可选，用于指定设置）：

```json
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 1
  }
}
```

------

### 2. **添加文档**

向索引中添加一个文档，自动生成文档 ID。

```bash
POST http://localhost:9200/my_index/_doc
```

请求体：

```json
{
  "title": "Elasticsearch Guide",
  "author": "OpenAI",
  "published_date": "2024-01-01",
  "content": "This is a sample document for Elasticsearch."
}
```

如果希望指定文档 ID，例如 `1`：

```bash
PUT http://localhost:9200/my_index/_doc/1
```

------

### 3. **获取文档**

通过文档 ID 获取文档。

```bash
GET http://localhost:9200/my_index/_doc/1
```

------

### 4. **更新文档**

部分更新文档内容。

```bash
POST http://localhost:9200/my_index/_update/1
```

请求体：

```json
{
  "doc": {
    "author": "OpenAI GPT"
  }
}
```

------

### 5. **删除文档**

通过文档 ID 删除文档。

```bash
DELETE http://localhost:9200/my_index/_doc/1
```

------

### 6. **删除索引**

删除整个索引及其数据。

```bash
DELETE http://localhost:9200/my_index
```

------

### 7. **搜索文档**

根据查询条件搜索文档。

```bash
POST http://localhost:9200/my_index/_search
```

请求体（查询所有文档）：

```json
{
  "query": {
    "match_all": {}
  }
}
```

查询条件示例（根据 `author` 字段匹配）：

```json
{
  "query": {
    "match": {
      "author": "OpenAI"
    }
  }
}
```

------

### 8. **批量操作**

使用 `_bulk` API 进行批量操作。

```bash
POST http://localhost:9200/_bulk
```

请求体（操作用换行符分隔）：

```json
{ "index": { "_index": "my_index", "_id": "1" } }
{ "title": "First Document", "content": "Content of the first document." }
{ "delete": { "_index": "my_index", "_id": "2" } }
```

------

这些操作可以直接通过工具（如 `curl`）或 Elasticsearch 客户端（如 Kibana Dev Tools 或代码库）执行。
