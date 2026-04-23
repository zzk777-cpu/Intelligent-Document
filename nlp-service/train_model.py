import jieba
import joblib
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.naive_bayes import MultinomialNB

texts = []
labels = []

# 读取数据
with open("train_data.txt", "r", encoding="utf-8") as f:
    for line in f:
        if not line.strip():
            continue
        label, text = line.strip().split("\t")
        texts.append(text)
        labels.append(label)

print("数据量:", len(texts))

# 分词
from utils import jieba_tokenizer

# 向量化
vectorizer = TfidfVectorizer(tokenizer=jieba_tokenizer)
X = vectorizer.fit_transform(texts)

# 训练
model = MultinomialNB()
model.fit(X, labels)

# 保存
joblib.dump(model, "model.pkl")
joblib.dump(vectorizer, "vectorizer.pkl")

print("✅ 训练完成")