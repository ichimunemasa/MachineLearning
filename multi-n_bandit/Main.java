package bandit;

public class Main {
	public static void main(String[] args){
		int n_machine = 2000;
		int n_action = 10;
		
		int n_play = 2000;
		double[] epsilon = {0.0,0.01,0.1,1.0};
		
		Bandit bd = new Bandit(n_machine, n_action);
		
		for(int i=0; i<epsilon.length; i++){
			double[] rewards = bd.play_bandit(n_play, epsilon[i]);
			System.out.println(rewards[rewards.length-1]);
//			for(int j=0; j<rewards.length; j++){
//				//System.out.println(i + "回目 報酬 ： " + rewards[i]);
//				System.out.println(rewards[j]);
//			}
		}
		
	}
}
