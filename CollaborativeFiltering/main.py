#-*- coding: utf-8 -*-
import recommendations as rec


if __name__ == "__main__":
	#評価値を予測する行列(辞書形式)
	dictionary = {
	"Momo":{"Star Wars":3.0,"Magical Banana":2.0},
	"Lisa Rose":{"Snakes on a Plane":3.5,"Superman Returns":3.5,"The Night Listener":3.0},
	"Gene Seymour":{"Lady in the Water":3.0,"Snakes on a Plane":3.5,"You Me and Dupree":3.5,"The Night Listener":3.0},
	"Michael Phillips":{"Lady in the Water":2.5,"Snakes on a Plane":3.0,"Superman Returns":3.5,"The Night Listener":4.0},
	"Claudia Puig":{"Just My Luck":3.0,"Superman Returns":4.0,"You Me and Dupree":2.5,"The Night Listener":4.5},
	"Mick LaSalle":{"Snakes on a Plane":4.0,"Just My Luck":2.0,"Superman Returns":3.0,"You Me and Dupree":2.0},
	"Jack Matthews":{"Lady in the Water":3.0,"Snakes on a Plane":4.0,"Superman Returns":5.0,"You Me and Dupree":3.5},
	"Toby":{"Snakes on a Plane":4.5,"Superman Returns":4.0,"You Me and Dupree":1.0}
	}

	#ユーザベース ("User")or アイテムベース("Item")
	calMethod = "User"

	#協調フィルタリング後の行列
	evalMatrix = rec.collavorativeFiltering(dictionary,calMethod)

	#出力
	print  "\n\n"+str(evalMatrix)

	
