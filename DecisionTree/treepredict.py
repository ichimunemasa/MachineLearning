#-*- coding: utf-8 -*-

my_data=[['slashdot','USA','yes',18,'None'],
         ['google','France','yes',23,'Premium'],
         ['digg','USA','yes',24,'Basic'],
         ['kiwtobes','France','yes',23,'Basic'],
         ['google','UK','no',21,'Premium'],
         ['(direct)','New Zealand','no',12,'None'],
         ['(direct)','UK','no',21,'Basic'],
         ['google','USA','no',24,'Premium'],
         ['slashdot','France','yes','19','None'],
         ['digg','USA','no',18,'None'],
         ['google','UK','no',18,'None'],
         ['kiwitobes','UK','no',19,'None'],
         ['digg','New Zealand','yes',12,'Basic'],
         ['slashdot','UK','no',21,'None'],
         ['google','UK','yes',18,'Basic'],
         ['kiwitobes','France','yes',19,'Basic']]

my_data2=[['sunny','strong','high','no'],
         ['cloudy','weak','high','yes'],
         ['cloudy','strong','low','no'],
         ['sunny','weak','high','yes'],
         ['rainy','weak','high','no']]


class decisionnode:
    def __init__(self,col=-1,value=None,results=None,tb=None,fb=None):
        #テストされる基準のインデックス値
        self.col=col
        #結果が真となるのに必要な値
        self.value=value
        #この枝が持つ帰結のディクショナリで、終点(endpoint)以外ではNoneとなる
        self.results=results
        #このノードの結果が真のときtb,偽のときfbにたどる次のdicisionnode
        self.tb=tb
        self.fb=fb

#特定の項目に基づき集合を分割する
#項目の値が数値でも名前でも処理可能
def divideset(rows,column,value):
    #行が最初のグループ(true)に入るか第二のグループ(false)に入るか教えてくれる関数
    split_function=None
    if isinstance(value,int) or isinstance(value,float):
        split_function=lambda row:row[column]>=value
    else:
        split_function=lambda row:row[column]==value
    #行を２つの集合に分けて返す
    set1=[row for row in rows if split_function(row)]
    set2=[row for row in rows if not split_function(row)]

    return (set1,set2)

#可能な帰結(各業の最終項目)を集計
def uniquecounts(rows):
    results={}
    for row in rows:
        #帰結は最後の項目
        r=row[len(row)-1]
        if r not in results: results[r]=0
        results[r]+=1
    return results

#無作為においた要素が間違ったカテゴリーに入る確率
def giniimpurity(rows):
    total=len(rows)
    counts=uniquecounts(rows)
    imp=0
    for k1 in counts:
        p1=float(counts[k1])/total
        for k2 in counts:
            if k1==k2: continue
            p2=float(counts[k2])/total
            imp+=p1*p2
    return imp

#エントロピーは可能な帰結それぞれp(x)log(p(x))を合計したもの
def entropy(rows):
    from math import log
    log2=lambda x: log(x)/log(2)
    results=uniquecounts(rows)
    #エントロピーの計算
    ent=0.0
    for r in results.keys():
        p=float(results[r])/len(rows)
        ent=ent-p*log2(p)
    return ent

def buildtree(rows,scoref=entropy):
    if len(rows)==0:return decisionnode()
    current_score=scoref(rows)

    #最良分割基準の追跡に使う変数のセットアップ
    best_gain=0.0
    best_criteria=None
    best_sets=None

    column_count=len(rows[0])-1
    for col in range(0,column_count):
        #まずこの項目が取りうる値のリストを生成
        column_values={}
        for row in rows:
            column_values[row[col]]=1

        #項目が取るそれぞれの値により行を振り分けてみる
        for value in column_values.keys():
            #print value
            (set1,set2)=divideset(rows,col,value)

            #情報ゲイン
            p=float(len(set1))/len(rows)
            gain=current_score-p*scoref(set1)-(1-p)*scoref(set2)
            if gain>best_gain and len(set1)>0 and len(set2)>0:
                best_gain=gain
                best_criteria=(col,value)
                best_sets=(set1,set2)

    print best_gain
    print best_criteria
    #次の段階の枝の生成
    if best_gain>0:
        print best_sets[0]
        print best_sets[1]
        trueBranch=buildtree(best_sets[0])
        falseBranch=buildtree(best_sets[1])
        return decisionnode(col=best_criteria[0],value=best_criteria[1],tb=trueBranch,fb=falseBranch)
    else:
        return decisionnode(results=uniquecounts(rows))

def classify(observation,tree):
    if tree.results != None:
        return tree.results
    else:
        v=observation[tree.col]
        branch=None
        if isinstance(v,int) or isinstance(v,float):
            if v>=tree.value: branch=tree.tb
            else: branch=tree.fb
        else:
            if v==tree.value: branch=tree.tb
            else: branch=tree.fb
        return classify(observation,branch)


def printtree(tree,indent=''):
    #このノードはリーフ(葉=末端)か?
    if tree.results != None:
        print str(tree.results)
    else:
        #基準の出力
        print str(tree.col)+':'+str(tree.value)+'? '
        #枝の出力
        print indent+'T->',
        printtree(tree.tb,indent+' ')
        print indent+'F->',
        printtree(tree.fb,indent+' ')

def prune(tree,mingain):
    #葉でない枝にはさらに刈り込みをかけていく
    if tree.tb.results==None:
        prune(tree.tb,mingain)
    if tree.fb.results==None:
        prune(tree.fb,mingain)
    #両方の枝が葉になったら、両者を統合スべきか調べる
    if tree.tb.results!=None and tree.fb.results!=None:
        #両者を合わせたデータセットを構築
        tb,fb=[],[]
        for v,c in tree.tb.results.items():
            tb+=[[v]]*c
        for v,c in tree.fb.results.items():
            fb+=[[v]]*c
        #エントロピーの減少度を調べる
        delta=entropy(tb+fb)-(entropy(tb)+entropy(fb))/2
        print tree.value
        print "entropy(tb,fb):" + str(entropy(tb+fb))
        print "entropy(tb)" + str(entropy(tb))
        print "entropy(fb)" + str(entropy(fb))
        print "delta:" + str(delta)


        if delta<mingain:
            #枝の統合
            print "eda kiri"
            tree.tb,tree.fb=None,None
            tree.results=uniquecounts(tb+fb)

def mdclassify(observation,tree):
    if tree.results!=None:
        return tree.results
    else:
        v=observation[tree.col]
        if v==None:
            tr,fr=mdclassify(observation,tree.tb),mdclassify(observation,tree.fb)
            tcount=sum(tr.values())
            fcount=sum(fr.values())
            tw=float(tcount)/(tcount+fcount)
            fw=float(fcount)/(tcount+fcount)
            result={}
            for k,v in tr.items(): result[k]=v*tw
            for k,v in fr.items():
                if k not in result: result[k]=0
                result[k]+=v*fw
            return result
        else:
            if isinstance(v,int) or isinstance(v,float):
                if v>=tree.value: branch=tree.tb
                else: branch=tree.fb
            else:
                if v==tree.value: branch=tree.tb
                else: branch=tree.fb
            return mdclassify(observation,branch)
