#-*- coding: utf-8 -*-
from sklearn.ensemble import GradientBoostingClassifier

X = [[0], [1], [2], [3]]
y = [0, 0, 1, 1]

clf = GradientBoostingClassifier()
clf.fit(X, y)
print(clf.predict([[1.1]]))
print(clf.predict_proba([[0.9]]))

