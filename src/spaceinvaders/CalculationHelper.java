package spaceinvaders;

public class CalculationHelper {
	
	public CalculationHelper(){
		
	}
	
	public static int sigmoid(float x){
		
		while(x <= 1.0){
		
			float ans = (float) 0.0;
			ans = (float) (1 / (1 + Math.exp(-1 * 20 *x)));
			
			System.out.println(ans);
			x+=0.01;
			
		}
		
		
		return 0;
	}
	
	public long thirdOrder(long moveSpeed){
		
		long ans = 0;
		
		ans = (long) ((long) Math.pow(moveSpeed, 3) + Math.pow(moveSpeed, 2) + moveSpeed + 1);
		
		return ans;
	}
	
	//public static void main(String[] args){
	//	sigmoid((float)-1.0);
	//}

}
