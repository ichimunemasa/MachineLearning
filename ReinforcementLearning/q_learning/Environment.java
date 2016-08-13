package q_learning;

public class Environment {
	//盤面の大きさxとy
	private static int x_size;
	private static int y_size;
	//迷路の盤面
	private static int[][] maze;
	
	Environment(int x_size, int y_size){
		this.x_size = x_size;
		this.y_size = y_size;
		this.maze = new int[x_size][y_size];
	}
	
	//迷路のgetterメソッド
	public int[][] getMaze(){
		return maze;
	}
	
	//初期化
	public void init_maze(){
		for(int i=0; i<x_size; i++){
			for(int j=0; j<y_size; j++){
				if(i==0 || j==0 ||i==(x_size-1) ||j==(y_size-1) )
					maze[i][j] = -1;
				else
					maze[i][j] = 0;
			}
		}
	}
	
	//壁の設定
	public void wall_maze(){
		//後で変更
		maze[2][2] = -1;
		maze[3][2] = -1;
		maze[6][3] = -1;
		maze[7][3] = -1;
		maze[8][3] = -1;
		maze[2][6] = -1;
		maze[2][7] = -1;
		maze[3][6] = -1;
		maze[6][7] = -1;
				
	}
	
	//報酬の設定
	public void reward_maze(){
		//後で変更
		maze[8][6] = 10;
	}
	
	//迷路の出力
	public void print_maze(){
		for(int i=0; i<x_size; i++){
			for(int j=0; j<y_size; j++){
				System.out.format("%3d",maze[i][j]);
			}
			System.out.println("");
		}
	}
	

}
