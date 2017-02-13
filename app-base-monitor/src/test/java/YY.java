import java.util.ArrayList;
import java.util.List;

public class YY {
	
	public static void main(String[] args) {
		
		for(int i=0;i<100;i++){
			invoked();
		}
		
	}
	
	
	public static void invoked(){
		long start = System.currentTimeMillis();
		List<String> list  = new ArrayList<String>();
		for(int i=0;i<1000;i++){
			list.add(i+"");
		}
		
		for(int j=1000;j<100000;j++){
			list.remove(0);
			list.add(j+"");
		}
		System.out.println(list.get(999));
		
		System.out.println("cost:"+(System.currentTimeMillis()-start));
	}
}
