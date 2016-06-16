package naive_bayes;

import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args){
		//学習用データのデータ構造
		List<List<String>> study = new ArrayList<List<String>>();
		
		List<String> classDocuments_P = new ArrayList<String>();
		String positive01 = "good bad good good"; classDocuments_P.add(positive01);
		String positive02 = "exciting exciting";classDocuments_P.add(positive02);
		String positive03 = "good good exciting boring";classDocuments_P.add(positive03);
		study.add(classDocuments_P);
		
		List<String> classDocuments_N = new ArrayList<String>();
		String negative01 = "bad boring boring boring"; classDocuments_N.add(negative01);
		String negative02 = "bad good bad"; classDocuments_N.add(negative02);
		String negative03 = "bad bad boring exciting"; classDocuments_N.add(negative03);
		study.add(classDocuments_N);
				
		
		//クラス数
		int classNum = 2;
		
		NaiveBayes nb = new NaiveBayes(study,classNum);
		nb.bernoulliTrain();
		nb.classifyBernoulli("good good bad boring");
		nb.multinominalTrain();
		nb.classifyMultinominal("good good bad boring");
		
	}
}
