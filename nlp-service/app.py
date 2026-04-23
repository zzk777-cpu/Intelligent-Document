from typing import List

import jieba
import jieba.analyse
import joblib
import os

from fastapi import FastAPI
from pydantic import BaseModel

from sklearn.metrics.pairwise import cosine_similarity
from scipy.sparse import vstack

# =========================
# 初始化
# =========================
app = FastAPI(title="NLP Service")

# ⚠️ 必须和训练一致
from utils import jieba_tokenizer


# =========================
# 请求/响应模型
# =========================
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


# =========================
# 加载模型
# =========================
BASE_DIR = os.path.dirname(__file__)

model_path = os.path.join(BASE_DIR, "model.pkl")
vectorizer_path = os.path.join(BASE_DIR, "vectorizer.pkl")

model = joblib.load(model_path)
vectorizer = joblib.load(vectorizer_path)


# =========================
# 分类接口
# =========================
@app.post("/nlp/classify", response_model=ClassifyResponse)
def classify(req: ClassifyRequest):
    text = f"{req.title} {req.content}"

    vec = vectorizer.transform([text])
    probs = model.predict_proba(vec)[0]
    classes = model.classes_

    idx = probs.argmax()

    return ClassifyResponse(
        category=str(classes[idx]),
        confidence=float(probs[idx])
    )


# =========================
# 关键词提取
# =========================
@app.post("/nlp/keywords", response_model=KeywordsResponse)
def keywords(req: KeywordsRequest):
    words = jieba.analyse.extract_tags(
        req.content,
        topK=req.topK,
        withWeight=False
    )
    return KeywordsResponse(keywords=words)


# =========================
#  添加文档（用于推荐）
# =========================
@app.post("/nlp/addDoc")
def add_doc(req: ClassifyRequest):
    global documents, doc_vectors

    text = f"{req.title} {req.content}"
    vec = vectorizer.transform([text])

    documents.append({
        "title": req.title,
        "content": req.content
    })

    if doc_vectors is None:
        doc_vectors = vec
    else:
        doc_vectors = vstack([doc_vectors, vec])

    return {"msg": "added"}



# =========================
# 健康检查
# =========================
@app.get("/health")
def health():
    return {"status": "ok"}