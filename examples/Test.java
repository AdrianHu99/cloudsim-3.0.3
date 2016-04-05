import org.apache.commons.math3.distribution.ExponentialDistribution;


public class Test {
	public static void main(String[] args){
		ExponentialDistribution exp = new ExponentialDistribution(50.0);
		for(int i = 0; i < 100; i++){
			System.out.println(exp.sample());
		}

	}
}

