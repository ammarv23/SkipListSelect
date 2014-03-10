
public class Printout {
	
    private static boolean printWant = true;
	
	 public static void printHorizontal(SkipList skip){
	 
	 if (printWant){
	 
	     String s = "";
	     int i;
	   
	     SkipListNode p;

	     /* ----------------------------------
		Record the position of each entry
		---------------------------------- */
	     p = skip.head;

	     while ( p.down != null )
	     {
	        p = p.down;
	     }

	     i = 0;
	     while ( p != null )
	     {
	        p.pos = i++;
	        p = p.right;
	     }

	     /* -------------------
		Print...
		------------------- */
	     p = skip.head;

	     while ( p != null )
	     {
	        s = getOneRow( p );
		System.out.println(s);

	        p = p.down;
	     }
		 System.out.println("============================");
	 }
	}

	  public static String getOneRow( SkipListNode p )
	  {
	     String s;
	     int a, b, i;

	     a = 0;

	     s = "" + p.key;
	     p = p.right;


	     while ( p != null )
	     {
	        SkipListNode q;

	        q = p;
	        while (q.down != null)
		   q = q.down;
	        b = q.pos;

	        s = s + " <-";


	        for (i = a+1; i < b; i++)
	           s = s + "--------";
	 
	        s = s + "> " + p.key;

	        a = b;

	        p = p.right;
	     }

	     return(s);
	 }

	  
	  public static void printVertical(SkipList skip)
	  {
	     String s = "";

	     SkipListNode p;

	     p = skip.head;

	     while ( p.down != null )
	        p = p.down;

	     while ( p != null )
	     {
	        s = getOneColumn( p );
		System.out.println(s);

	        p = p.right;
	     }
	  }


	  public static String getOneColumn( SkipListNode p )
	  {
	     String s = "";

	     while ( p != null )
	     {
	        s = s + " " + p.key;

	        p = p.up;
	     }

	     return(s);
	  }
	  
	  public static void printout(SkipList s, String h){
		  
		  if (h.equalsIgnoreCase("horizontal")){
			 printHorizontal(s);
			 
		  }
		  else if (h.equalsIgnoreCase("vertical")){
			 printVertical(s);
		  }
		 
		  
		  
	  }

}
