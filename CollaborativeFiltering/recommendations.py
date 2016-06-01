#-*- coding: utf-8 -*-
import similarity
import treatMatrix
import time

#アイテムベース
def getItemRecommendation(matrix,item):
	totals = {}
	simSums = {}

	
	for other in matrix:
		if other == item : continue
		sim = similarity.cosine(matrix,item,other)
		#sim = similarity.pearson(matrix,item,other)
	
		if sim <= 0 : continue

		for user in matrix[other]:
			if user not in matrix[item] or matrix[item][user] == 0:
				totals.setdefault(user,0)
				totals[user] += matrix[other][user]*sim
				simSums.setdefault(user,0)
				simSums[user] += sim

		
		#itemEval=[(total/simSums[user],user) for user,total in totals.items()]

	itemEval={user:total/simSums[user] for user,total in totals.items()}
		

		#print str(itemEval)
	return itemEval




#ユーザベース
def getUserRecommendation(matrix,user):
	totals = {}
	simSums = {}
	
	u_average = sum(matrix[user][item] for item in matrix[user])/len(matrix[user])

	for other in matrix:
		if other == user : continue
		sim = similarity.cosine(matrix,user,other)
		#sim = similarity.pearson(matrix,user,other)
		if sim <= 0 : continue

		o_average = sum(matrix[other][item] for item in matrix[other])/len(matrix[other])

		for item in matrix[other]:
			if item not in matrix[user] or matrix[user][item] == 0:
				totals.setdefault(item,0)
				totals[item] += (matrix[other][item] - o_average)*sim
				simSums.setdefault(item,0)
				simSums[item] += sim

		#userEval=[(u_average + totals/simSums[item],item) for item,total in totals.items()]
	userEval={item:u_average+total/simSums[item] for item,total in totals.items()}
	return userEval




#協調フィルタリング
def collavorativeFiltering(prefs,calMethod):
	

	if calMethod == "User":
		print "\nUser Base \n"
		get = getUserRecommendation
		matrix = prefs
	
	elif calMethod == "Item":
		print "\nItem Base \n"
		get = getItemRecommendation
		matrix = treatMatrix.transformMatrix(prefs)
	
	else:
		print "calMethod doesn't have "
		return null

	rowNum = 0


	evaluatedMatrix = {}
	for row in matrix:
		start = time.time()
		rowEval = get(matrix,row)

		evaluatedMatrix.update({row:rowEval})
			

		elapsed_time = time.time() - start
		print str(rowNum) + " [item or user] : " + row + " [finished] : " + ("time : {0}".format(elapsed_time))+" [sec]"
		rowNum += 1

	
	return evaluatedMatrix






