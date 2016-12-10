#-*- coding: utf-8 -*-

from sklearn.feature_extraction import DictVectorizer
measurements=[
	{'city':'Dubai','temperature':33.},
	{'city':'London','temperature':12.},
	{'city':'San Fransisco','temperature':18.}
]

vec = DictVectorizer()

array = vec.fit_transform(measurements).toarray()

print array

name = vec.get_feature_names()

print name
