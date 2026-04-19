from typing import List

import jieba.analyse
from fastapi import FastAPI
from pydantic import BaseModel
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.naive_bayes import MultinomialNB

app = FastAPI(title="NLP Service")


class ClassifyRequest(BaseModel):
    docId: int | None = None
    title: str = ""
    content: str


class ClassifyResponse(BaseModel):
    category: str
    confidence: float


class KeywordsRequest(BaseModel):
    docId: int | None = None
    content: str
    topK: int = 10


class KeywordsResponse(BaseModel):
    keywords: List[str]


# 简单内置训练样本，实际项目建议读取标注数据集进行离线训练
train_texts = [
    "教学目标 教学过程 教学反思 课堂设计",
    "实验目的 实验步骤 实验结果 数据分析",
    "论文 摘要 参考文献 研究方法",
    "课程通知 时间安排 作业提交"
]
train_labels = ["教案", "实验报告", "课程论文", "教学通知"]
vectorizer = TfidfVectorizer()
X = vectorizer.fit_transform(train_texts)
model = MultinomialNB().fit(X, train_labels)


@app.post("/nlp/classify", response_model=ClassifyResponse)
def classify(req: ClassifyRequest):
    # 合并标题与正文做分类特征输入
    text = f"{req.title} {req.content}"
    vec = vectorizer.transform([text])
    probs = model.predict_proba(vec)[0]
    classes = model.classes_
    idx = probs.argmax()
    return ClassifyResponse(category=str(classes[idx]), confidence=float(probs[idx]))


@app.post("/nlp/keywords", response_model=KeywordsResponse)
def keywords(req: KeywordsRequest):
    # 使用 Jieba TF-IDF 提取关键词
    words = jieba.analyse.extract_tags(req.content, topK=req.topK, withWeight=False)
    return KeywordsResponse(keywords=words)


@app.get('/health')
def health():
    # 健康检查接口，便于后端探活
    return {'status': 'ok'}
