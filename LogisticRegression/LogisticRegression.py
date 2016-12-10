#-*- coding: utf-8 -*-

from sklearn.linear_model import LogisticRegression
from sklearn.feature_extraction import DictVectorizer

#例) 
train=[
	{'city':'Dubai','temperature':33.},
	{'city':'London','temperature':12.},
	{'city':'San Fransisco','temperature':18.},
	{'city':'Japan','temperature':19.},
	{'city':'Japan','temperature':20.}
]

verification=[
	{'city':'Dubai','temprature':22.}
]


vec = DictVectorizer()
#独立変数
#X = [[0], [1], [2], [3]]
X = vec.fit_transform(train).toarray() 
print X
#print vec.vocabulary_
print vec.fit_transform(train)
name = vec.get_feature_names()
print name

#従属変数
#y = [0, 0, 1, 1]
y = [0,1,1,1,0]
#ロジスティック回帰
clf = LogisticRegression()
clf.fit(X, y)
#分類結果を示してくれる
#data = vec.fit_transform(verification).toarray()
#print data
print str(clf.predict([0,0,1,0.,22]))
#それぞれのクラスの確率値を示してくれる
print str(clf.predict_proba([1,0,0,0,22])[0][1])
