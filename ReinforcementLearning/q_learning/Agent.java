package q_learning;
import java.util.Random;

public class Agent {
	//エージェントの位置(状態)
	private static int x;
	private static int y;
	//エージェントの現在の状態
	private static int s;
	//エージェントの現在の行動
	private static int a;
	//エージェントの行動の実行によって遷移する状態
	private static int sd;
	//状態の数
	private static int num_s;
	//行動の数
	private static int num_a;
	//パラメータ
	private static double alpha;
	private static double gamma;
	private static int epsilon;
	//Qtable
	//private static double[][] Qtable;
	
	//コンストラクタ
	Agent(int num_s, int num_a,double alpha,double gamma, int epsilon){
		this.num_s = num_s;
		this.num_a = num_a;
		this.alpha = alpha;
		this.gamma = gamma;
		this.epsilon = epsilon;
		//this.Qtable = new double[num_s][num_a];
	}
	
	//Qtableの初期化
	public void init_Qtable(double[][] Qtable){
		for(int i=0; i<s; i++){
			for(int j=0; j<a; j++){
				Qtable[i][j] = 0.0;
			}
		}
	}
	
	//学習開始
	public void Q_learning(int x_init, int y_init, int x_size,int num_trial, int num_step,double[][] Qtable,Environment ev){
		//初期設定。エージェントの迷路状最初の位置とそれに対応する状態s
		x = x_init;
		y = y_init;
		int s = xy2s(x,y,x_size);
		int a;
		//報酬
		int reward = 0;
		//最大のQ値
		double Qmax = 0.0;
		//学習開始
		for(int i=0; i<num_trial; i++){
			x = x_init;
			y = y_init;
			s = xy2s(x,y,x_size);
			System.out.println((i+1) + "回目の試行:");
			for(int j=0; j<num_step; j++){
				//System.out.println((j+1) + "回目のステップ");
				//System.out.println("現在の状態s="+s+"エージェントの所在地:(" + x+ ","+ y+")");
				//行動を選択
				a = epsilon_greedy(s,num_a,Qtable);
				//System.out.println("行動"+a);
				//エージェントの行動の実行によって遷移する状態
				sd = move(a,x_size);
				//System.out.println("次の状態s="+sd+"とエージェントの所在地:(" + x+ ","+ y+")");
				reward = ev.getMaze()[x][y];
				//System.out.println("迷路上の報酬：("+ x + ","+y+") " + reward );
				Qmax=max_Qval(sd,num_a,Qtable);
				//System.out.println("最大Q値:" + Qmax);
				//最適Q値の更新
				Qtable[s][a] = (1.0-alpha)*Qtable[s][a] + alpha*((double)reward+gamma*Qmax);
				//System.out.println("状態s= "+s+ "での行動a= "+a+" を選択した時のQ値= " + Qtable[s][a]);
				
				if(reward<0){//失敗
					x = x_init;
					y = y_init;
					s = xy2s(x,y,x_size);
					System.out.println("失敗");
					break;
				}else if(reward>0){//成功
					x = x_init;
					y = y_init;
					s = xy2s(x,y,x_size);
					System.out.println("成功");
					break;
				}else{//どちらでもないので続行
					s=sd;
					//System.out.println("続行");
				}	
			}
		}
		//printQtable(Qtable);
		
		
	}
	
	//Qtableの表示
	public void printQtable(double[][] Qtable){
		for(int i=0; i<num_s; i++){
			System.out.print("状態" + i);
			for(int j=0; j<num_a;j++){
				//System.out.format("%4d",Qtable[i][j]);
				System.out.print(" " +  Qtable[i][j] + " ");
			}
			System.out.println("");
		}
	}
	
	//政策の表示
	public void printPolicy(int x_size, int y_size,double[][] Qtable,Environment ev){
		double Qmax = 0.0;
		for(int i=0; i<x_size; i++){
			for(int j=0; j<y_size; j++){
				s = xy2s(i,j,x_size);
				Qmax = max_Qval(s,num_a,Qtable);
				//a=select_action(s,num_a);
				if(Qmax==0){
				//System.out.println("状態 " + s + " : "+Qtable[s][0]+"  "+Qtable[s][1]+"  "+ Qtable[s][2]+  "  " +Qtable[s][3]);
				//if(Qtable[s][0]==0.0 && Qtable[s][1]==0.0 && Qtable[s][2]==0.0 && Qtable[s][3] == 0.0){
					System.out.format("%3d",ev.getMaze()[i][j]);
				}else{
					a=select_action(s,num_a,Qtable);
					if(a==0){
						System.out.print("  →");
					}else if(a==1){
						System.out.print("  ↓");
					}else if(a==2){
						System.out.print("  ←");
					}else{
						System.out.print("  ↑");
					}
				}
			}
			System.out.println("");
		}
	}
	
	
	
	//ロボットの移動をシュミレーションする関数
	private int move(int a, int x_size){
		if(a==0){
			y=y+1;
		}else if(a==1){
			x=x+1;
		}else if(a==2){
			y=y-1;
		}else{
			x=x-1;
		}
		int sd;
		sd=xy2s(x,y,x_size);
		//System.out.println("次の状態：" + sd);
		return sd;
	}
	
	//迷路上の座標を状態番号に変換する関数
	private int xy2s(int x, int y, int x_size){
		return x+y*x_size;
		
	}
	
	//Q値の最大値を求める関数
	private double max_Qval(int s, int num_a,double[][] Qtable){
		double max=Qtable[s][0];
		
		for(int i=1; i<num_a; i++){
			if(Qtable[s][i] > max){
				max=Qtable[s][i];
			}
		}
		//System.out.println("Qmax:" + max);
		//周辺の最大Q値
		
		return max;
	}
	
	
	//ε-greedy法
	private int epsilon_greedy(int s,int num_a,double[][] Qtable){
		int a;
		Random random = new Random();
		//1~100までの乱数生成
		int rnd = random.nextInt(100)+1;
		//System.out.println(rnd + "," + epsilon);
		//無作為に行動を選択
		if(epsilon>rnd){
			//System.out.print("無作為:");
			a=random.nextInt(num_a);
		}else{//最大のQ値をもつ行動を選択
			//System.out.print("最大:");
			a=select_action(s,num_a,Qtable);
		}
		//System.out.println("行動"+ a);
		return a;
	}
	
	//最大のQ値を持つ行動を選択するための関数
	private int select_action(int s, int num_a,double[][] Qtable){
		int[] i_max = new int[num_a];
		//System.out.println(i_max.length);
		//i_max[0] = 0;
		double max = Qtable[s][0];
		int num_i_max = 1;
		int a;
		
		for(int i=1; i<num_a; i++){
			if(Qtable[s][i] > max){
				max=Qtable[s][i];
				num_i_max=1;
				i_max[0]=i;
			}else if(Qtable[s][i] == max){//最大のQ値を持つ行動が複数個存在した時
				num_i_max++;
				//System.out.println(num_i_max);
				i_max[num_i_max-1]=i;
			}
		}
		
		Random random = new Random();
		int rnd = random.nextInt(num_i_max);
		//System.out.println(rnd);
		a=i_max[rnd];
		//System.out.println("行動                 " + a);
		return a;
	}
}
