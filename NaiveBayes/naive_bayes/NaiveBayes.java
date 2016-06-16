package naive_bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class NaiveBayes {
	//学習用文書群のリスト
	private static List study;
	//クラス数
	private static int classNum;
	//総文書数
	private static double n=0;
	//語彙の格納
	private static Set<String> vocabulary;
	private static double[] nc;
	private static double[] Nc;
	
	//多変数ベルヌーイモデル
	//クラスCに属する訓練集合のうち単語wを含む文書数
	private static List<Map<String,Double>> nwc;
	
	//多項モデル
	//クラスCに属する文書全体でのwの出現回数
	private static List<Map<String,Double>> Nwc;
	//クラスCに属する文書数カウント

	
	//コンストラクタ
	NaiveBayes(List<List<String>> study,int classNum){
		this.study = study;
		this.classNum = classNum;
	}
	
	//getterメソッド
	//学習データを返す
	public List getStudyData(){
		return study;
	}
	//クラス数を返す
	public int getTotalClass(){
		return classNum; 
	}
	//クラスCに属する訓練集合のうち単語wを含む文書数
	public List getnwc(){
		return nwc;
	}
	//クラスCに属する訓練集合のうち単語wの出現回数
	public List getNwc(){
		return Nwc;
	}
	//すべて語彙を返す
	public Set getVocabulary(){
		return vocabulary;
	}
	//クラスCに属する文書数を格納した配列を返す
	public double[] getnc(){
		return nc;
	}
	public double[] getNc(){
		return Nc;
	}
	
	//setterメソッド
	//クラスCに属する文書数を格納した配列
	public void setnc(double[] nc){
		this.nc = nc;
	}
	public void setNc(double[] Nc){
		this.Nc = Nc;
	}
	
	//すべて語彙
	public void setVocabulary(Set vocabulary){
		this.vocabulary = vocabulary;
	}
	//多変数ベルヌーイモデル用
	//クラスCに属する訓練集合のうち単語wを含む文書数
	public void setnwc(List<Map<String,Double>> nwc){
		this.nwc = nwc;
	}
	//多項モデル用
	//クラスCに属する訓練集合のうち単語wの出現回数
	public void setNwc(List<Map<String,Double>> Nwc){
		this.Nwc = Nwc;
	}
	
	
	//多変数ベルヌーイモデル
	public void bernoulliTrain(){
		System.out.println("---多変数ベルヌーイモデル学習---");
		//多変数ベルヌーイモデルでの学習
		//学習データの取得
		List<List<String>> studyData = getStudyData();
		
		//クラスCに属する訓練集合のうち単語wを含む文書数
		List<Map<String,Double>> nwc = new ArrayList<Map<String,Double>>();
		//すべての語彙
		Set<String> vocabulary = new HashSet<String>();
		//クラスCに属する文書数を格納した配列
		double[] nc = new double[getTotalClass()];
		
		
		//クラスの数だけforループをまわす
		for(int i=0; i < studyData.size(); i++){
			//クラスCの文書データの取得
			List<String> classData = studyData.get(i);
			//それぞれのクラスにおいて単語wの出現した文書数
			Map<String,Double> classWord = new HashMap<String,Double>();
			//クラスCに属する文書数だけforループをまわす
			for(int j=0; j < classData.size(); j++){
				//クラスCに属する文書数のカウント
				nc[i] = nc[i] + 1.0;
				//単語wがいくつの文書に含まれるかをカウントするので、1つ文書に複数同じ単語があっても1とカウントする
				//重複をチェックするためのSet
				Set<String> overlap = new HashSet<String>();
				
				//文書に含まれる単語をスペースでスプリット。単語数分の配列を作る
				String[] words = classData.get(j).split(" ");
				//１つの文書に含まれる単語数分だけforループを回す
				for(int k=0; k < words.length; k++){
					//ある単語が、すでに今までにクラスCで出てきた文書に入っていて、なおかつ現在の文書でまだ一度もでてきていない場合
					if(classWord.containsKey(words[k]) && !overlap.contains(words[k])){
						classWord.put(words[k], classWord.get(words[k])+1.0);
						overlap.add(words[k]);
					}
					//ある単語が、今見ている文書に存在する場合
					else if(overlap.contains(words[k])){
						continue;
					}
					//ある単語がクラスCではじめて出てきたとき
					else{
						classWord.put(words[k], 1.0);
						overlap.add(words[k]);
						vocabulary.add(words[k]);
					}
				}
			}
			nwc.add(classWord);
		}
		setnwc(nwc);
		setVocabulary(vocabulary);
		setnc(nc);
		
//		for(int i=0; i<nwc.size(); i++){
//			for(String key: nwc.get(i).keySet()){
//				System.out.println("class" + i + " : " + key + " " + nwc.get(i).get(key));
//			}
//		}
//		
//		for(int i=0; i < nc.length; i++){
//			System.out.println(nc[i]);
//		}
		
	}
	
	//ベルヌーイモデルでの分類
	public void classifyBernoulli(String document){
		//
		Set<String> documents = new HashSet<String>();
		
		String[] words = document.split(" ");
		
		for(int i=0; i < words.length; i++){
			documents.add(words[i]);
		}
		
		//総文書数
		double n=0;
		//クラスCに属する文書数を格納した配列
		double[] nc = getnc();
		//総文書数算出
		for(int i=0; i < nc.length; i++){
			n = n + nc[i];
		}
		
		//クラスごとの値
		double[] c_value = new double[getTotalClass()];
		//全語彙
		Set<String> vocabulary = getVocabulary();
		//クラスCに属する訓練集合のうち単語wを含む文書数
		List<Map<String,Double>> nwc = getnwc();
		
		//クラスの数だけforループを回す
		System.out.print(document + ", ");
		for(int i=0; i<getTotalClass(); i++){
			c_value[i] = nc[i]/n;
			//単語が存在するか否かを見る
			for(String word : vocabulary){
				if(documents.contains(word)){
					c_value[i] = c_value[i]*nwc.get(i).get(word)/nc[i];
				}else{
					c_value[i] = c_value[i]*(1.0 - nwc.get(i).get(word)/nc[i]);
				}
			}
			System.out.print(c_value[i] + " ");
		}
		System.out.println("");

	}

	
	//多項モデル
	public void multinominalTrain(){
		System.out.println("---多項モデル学習---");
		//多項モデルでの学習
		//qw,c(多項モデル)：クラスCに属する訓練文書全体での単語wの出現回数/クラスCに属する訓練集合全体での全単語の出現回数
		//List<Map<String,Double>> qwc = new ArrayList<Map<String,Double>>();
		//学習データの取得
		List<List<String>> studyData = getStudyData();
		//クラスCに属する訓練集合のうち単語wの出現回数
		List<Map<String,Double>> Nwc = new ArrayList<Map<String,Double>>();
		//すべての語彙
		Set<String> vocabulary = new HashSet<String>();
		//クラスCに属する文書数を格納した配列
		double[] Nc = new double[getTotalClass()];
		
		
		//クラスの数だけforループをまわす
		for(int i=0; i < studyData.size(); i++){
			//クラスCの文書データの取得
			List<String> classData = studyData.get(i);
			//それぞれのクラスにおいて単語wの出現した文書数
			Map<String,Double> classWord = new HashMap<String,Double>();

			for(int j=0; j < classData.size(); j++){
				//クラスCに属する文書数のカウント
				
				//文書に含まれる単語をスペースでスプリット。単語数分の配列を作る
				String[] words = classData.get(j).split(" ");
				for(int k=0; k < words.length; k++){
					Nc[i] = Nc[i] + 1.0;
					if(classWord.containsKey(words[k])){
						classWord.put(words[k],classWord.get(words[k])+1.0);
					}else{
						classWord.put(words[k], 1.0);
						vocabulary.add(words[k]);
					}
				}
			}
			Nwc.add(classWord);
		}
		setNwc(Nwc);
		setVocabulary(vocabulary);
		setNc(Nc);
		
	}
	
	
	//多項モデルでの分類
	public void classifyMultinominal(String document){
		String[] words = document.split(" ");
		//総文書数
		double n=0;
		//クラスCに属する文書数を格納した配列
		double[] Nc = getNc();
		//クラスCに属する文書数を格納した配列
		double[] nc = getnc();
		//総文書数算出
		for(int i=0; i < nc.length; i++){
			n = n + nc[i];
		}
		
		//クラスCに属する訓練集合のうち単語wを含む文書数
		List<Map<String,Double>> Nwc = getNwc();
		
		//クラスごとの値
		double[] c_value = new double[getTotalClass()];
		
		System.out.print(document + ",");
		for(int i=0; i < getTotalClass(); i++){
			c_value[i] = nc[i]/n;
			//System.out.println(c_value[i]);
			//System.out.println(Nc[i]);
			for(int j=0; j < words.length; j++){
				//System.out.println((Nwc.get(i).get(words[j])/Nc[i]));
				c_value[i] = c_value[i]*Nwc.get(i).get(words[j])/Nc[i];
			}
			System.out.print(c_value[i] + " ");
		}
		System.out.println("");
		
	}
	
}
