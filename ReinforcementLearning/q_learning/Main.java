package q_learning;

public class Main {
	public static void main(String[] args){
		//迷路(環境)の設定
		int x_size = 10;
		int y_size = 9;
		Environment ev = new Environment(x_size,y_size);
		ev.init_maze();
		ev.wall_maze();
		ev.reward_maze();
		ev.print_maze();
		
		//エージェントの設定
		//エージェントの初期位置
		int x_init = 1;
		int y_init = 1;
		//状態の数
		int num_s = x_size*y_size;
		//行動の数(上下左右)
		int num_a = 4;
		//パラメータε
		int epsilon = 10;
		//パラメータα、γ,
		double alpha = 0.5;
		double gamma = 0.9;
		//試行回数
		int num_trial = 300;
		//1試行あたりのステップ数
		int num_step = 100;
		
		double[][] Qtable = new double[num_s][num_a];
		
		Agent ag = new Agent(num_s, num_a, alpha, gamma, epsilon);
		ag.init_Qtable(Qtable);
		ag.Q_learning(x_init, y_init, x_size, num_trial,  num_step, Qtable,ev);
		//ag.printQtable(Qtable);
		ag.printPolicy(x_size, y_size,Qtable, ev);
		
	}

}
