
public class SkipListNode {
	
	public String key;
	
	public SkipListNode down, left, right, up;
	public int leftCount, rightCount;
	
	public int pos;

	
	//To define the infinite entries we use specialized values as elements
	
	public static String negInf = "-oo";
	public static String posInf = "+oo";
	
	
	public SkipListNode(String valueStart){
		key = valueStart;
		
		up = down = right = left = null;
		leftCount = rightCount = 0;
	}
	

}
