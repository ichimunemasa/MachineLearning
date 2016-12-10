package bandit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Bandit {
	//行動の数
	private static int n_action;
	//バンディットマシンの個数
	private static int n_machine;
	//バンディットマシンと行動に対する報酬を格納する2次元配列
	private static double[][] machine;
	//Qの値
	private static double[][] q;
	//行動が選ばれた回数
	private static int[][] q_count;
	
	//コンストラクタ
	Bandit(int n_machine, int n_action){
		this.n_action = n_action;
		this.n_machine = n_machine;
		q = new double[this.n_action][this.n_machine];
		q_count = new int[this.n_action][this.n_machine];
		
		Random random = new Random();
		machine = new double[this.n_action][this.n_machine];
		//行動aの数だけマシンの報酬を決める
		for(int act_index=0; act_index< n_action; act_index++){
			double q = random.nextGaussian();
			for(int mac_index=0; mac_index<n_machine; mac_index++){
				machine[act_index][mac_index] = random.nextGaussian()+q;
			}
		}
		//System.out.println(Arrays.toString(machine[2]));
	}
	
	
	//バンディット問題開始
	public double[] play_bandit(int n_play,double epsilon){
		//n回目のプレイの平均報酬
		double[] average_reward = new double[n_play];
		//ゲームのプレイ回数だけforループ
		for(int p_index=0; p_index<n_play; p_index++){
			double total = 0.0;
			//1プレイ=全バンディットマシンで1回ずつ引くこととしている
			for(int mac_index=0; mac_index<n_machine; mac_index++){
				//mac_index個目のマシンの行動を選択
				int act_index = select_action(mac_index,epsilon);
				double reward = machine[act_index][mac_index];
				total += reward;
				update_qtable(reward,mac_index,act_index);
			}
			average_reward[p_index] = total / (double)n_machine;
		}
		return average_reward;
	}
	
	//行動を選択するメソッド
	//探索か活用か
	private int select_action(int mac_index, double epsilon){
		Random random = new Random();
		int act_index = 0;
		if(random.nextDouble() > epsilon)
			act_index = select_greedy_action(mac_index);
		else
			act_index = select_random_action();
		
		return act_index;
	}
	
	//グリーディな方の選択した時
	private int select_greedy_action(int mac_index){
		Random random = new Random();
		double max = q[0][mac_index];
		
		for(int i=1; i<q.length; i++){
			max = Math.max(max, q[i][mac_index]);
		}
		List<Integer> indexes = new ArrayList<Integer>();
		
		for(int i=0; i<q.length; i++){
			if(q[i][mac_index] == max)
				indexes.add(i);
		}
		Collections.shuffle(indexes);
		
		return indexes.get(0);
	}
	
	//探索を行うとき
	private int select_random_action(){
		Random random = new Random();
		return random.nextInt(n_action);
	}
	
	//q値の更新
	private void update_qtable(double reward, int mac_index, int act_index){
		double _q = q[act_index][mac_index];
		q_count[act_index][mac_index]++;
		q[act_index][mac_index] = _q + (reward - _q)/q_count[act_index][mac_index];
	}
	
	
	
}
