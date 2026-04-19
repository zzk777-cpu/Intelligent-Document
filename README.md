# 课程文档智能管理系统（完整前后端代码）

本仓库包含一个可直接启动的完整示例：
- 前端：Vue3 + Node.js + Vite + Element Plus
- 后端：Java 17 + Spring Boot + Maven + MyBatis-Plus
- 数据库：MySQL
- 搜索引擎：Elasticsearch（已实装）
- 智能服务：Python FastAPI + Scikit-learn + Jieba

## 目录结构

```text
Intelligent-Document
├─ backend                 # Java 后端
│  ├─ pom.xml
│  ├─ schema.sql
│  └─ src/main
├─ frontend                # Vue3 前端
│  ├─ package.json
│  └─ src
└─ nlp-service             # Python NLP 服务
   ├─ app.py
   └─ requirements.txt
```

## 1) MySQL 初始化

```sql
source backend/schema.sql;
```

默认数据库连接：
- url: `jdbc:mysql://localhost:3306/course_doc_system`
- username: `root`
- password: `root`

可在 `backend/src/main/resources/application.yml` 修改。

## 2) 启动 NLP 服务（Python）

```bash
cd nlp-service
python -m venv .venv
source .venv/bin/activate   # Windows 用 .venv\Scripts\activate
pip install -r requirements.txt
uvicorn app:app --host 0.0.0.0 --port 8000 --reload
```

## 3) 启动后端（Spring Boot）

```bash
cd backend
mvn spring-boot:run
```

默认端口：`8080`

## 4) 启动 Elasticsearch（用于全文检索）

### Docker 一键启动（推荐）
```bash
docker run -d --name course-doc-es \
  -p 9200:9200 \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  elasticsearch:8.14.3
```

后端默认连接 `http://localhost:9200`，可在 `backend/src/main/resources/application.yml` 中修改。

## 5) 启动前端（Vue3）

```bash
cd frontend
npm install
npm run dev
```

默认端口：`5173`

## 核心接口

- 创建文档（并自动触发分类+关键词）
  - `POST /api/docs`
- 文档列表/检索
  - `POST /api/docs/search`
- 文档详情
  - `GET /api/docs/{id}`
- 删除文档
  - `DELETE /api/docs/{id}`
- 手动重跑智能分析
  - `POST /api/docs/{id}/intelligence`

## 示例请求

### 创建文档

```json
{
  "title": "数据库课程教案第一章",
  "courseName": "数据库原理",
  "docType": "教案",
  "author": "张老师",
  "content": "教学目标是理解关系模型，教学过程包括讲解、讨论与练习。"
}
```

### 检索文档

```json
{
  "q": "关系模型",
  "courseName": "数据库原理",
  "pageNum": 1,
  "pageSize": 10
}
```

## 说明

1. 本示例使用了最小可运行方案，便于你在课程设计中快速演示。
2. 检索逻辑已优先走 Elasticsearch；当 ES 不可用或无命中时自动回退 SQL `LIKE` 查询。
3. 文档上传文件（PDF/Word）可在当前基础上增加解析模块（Apache Tika / POI）。
