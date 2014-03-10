import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/*
 * Code adapted from http://www.mathcs.emory.edu/~cheung/Courses/323/Syllabus/Map/skip-list-impl.html#newlayer
 */

public class SkipList {

	public SkipListNode head; //first element of the top level
	public SkipListNode tail; //last element of the top level

	public int n; //number of entries in the list

	public int h; //height
	public Random r; //coin toss

	public static boolean helperPrintVal = false;

	public SkipList(){

		SkipListNode p1, p2;

		/*
		 * Create the negative and positive infinity objects
		 */

		p1 = new SkipListNode(SkipListNode.negInf);
		p2 = new SkipListNode(SkipListNode.posInf);

		/*
		 * Link the two together
		 */
		linkUpdater(p1, p2, "right", 1);
		linkUpdater(p2, p1, "left", 1);

		/*
		 * Set the head and tail
		 */

		head = p1;
		tail = p2;

		/*
		 * Other initializations
		 */

		n = 0;	//No entries in list
		h = 0;	//Height is 0


		r = new Random(); //make a new random object for the coin toss

	}


	/*
	 * Find the key corresponding to k in the SkipList on the lowest level
	 */
	public SkipListNode findEntry(String k){

		SkipListNode p;

		//Start at the head
		p = head;

		while(true){

			//Search RIGHT until you find a larger key

			while( (p.right.key) != SkipListNode.posInf && (p.right.key).compareTo(k) <= 0){

				p = p.right;	//Move to the right
			}

			//If the value is indeed larger, we'll be at p that we must go down (if it exists)

			if (p.down != null){
				p = p.down;	//Go down one level
			}
			else break; //We have reached the lowest level
		}

		return p;
	}

	/*
	 * Returns the key value
	 */

	public String getKey(String k){
		SkipListNode p;

		p = findEntry(k);

		if (k.equals(p.key)){
			return (p.key);
		}
		else 
			return null;
	}

	/*
	 * Inserts a value into the SkipList and adds layers as appropriate. Returns true if insertion was successful, false if value being inserted was already in the list.
	 */

	public boolean insert (String k){
		SkipListNode p, q;
		int i;

		p = findEntry(k);	//try and find the entry
		
	

		//Check if key is found

		if (k.equals(p.key)){	//if key is found, we're done
			if (helperPrintVal)
			System.out.println("String found at position " + k + ".");
			return false;
		}

		n = n+1;	//One mor entry in the skiplist

		//if key is not found, we will be at the lowest level and one value prior to where we want to insert

		q = new SkipListNode(k);	//Create a new Entry
		
		helperPrint(q, "insertStart", 0);

		//Insert into the lowest level after the return from find

		q.left = p;
		q.right = p.right;
		p.right.left = q;
		p.right = q;

		q.leftCount = q.rightCount = 1;

		helperPrint(q, "normal", 0);

		//Determine the height of the new node

		i = 0;		//Current level
		//int to keep track of spaces we move backwards
		int trackCount = 0;

		while (r.nextDouble() < 0.5 /*Coin toss*/){



			//Coin toss success - build one more level

			if (i >= h) //We reached the top level
			{
				addLayer();
			}

			/*
			 * Find first element with an UP-link
			 */
			while (p.up == null){
				trackCount = trackCount + p.leftCount;
				p = p.left;
			}

			//Make p point to the up element

			p = p.up;

			//Add one more entry to the column

			SkipListNode e;

			//System.out.println("Tracker count is: " + trackCount);
			e = new SkipListNode(k);

			//Initialize Links

			e.left = p;
			e.right = p.right;
			e.down = q;

			//Update node count for e and it's neighbours.
			//System.out.println("p right count is: " + p.rightCount);
			e.leftCount = p.rightCount = trackCount + 1;
			e.rightCount = p.right.leftCount = p.right.leftCount - p.rightCount + 1;



			//Change neighbours
			p.right.left = e;
			p.right = e;
			q.up = e;

			q = e;	//Sets q up for next iteration

			i = i+1;	//Current level increases by 1
			
			helperPrint(e, "normal", i);
		}

		//System.out.println("Insertion of node complete");
		/*
		 * Update the counts for all the values left in h to reflect a new insertion
		 * NOTE: P is the highest previous value to the insertion
		 */

		for (int j = 0; j < h - i; j ++){

			while (p.up == null){
				p = p.left;
				trackCount = trackCount + p.leftCount;
			}
			p = p.up;
			p.rightCount = p.rightCount + 1;
			p.right.leftCount = p.right.leftCount + 1;
		}	

		helperPrint(p, "end", 0);
		/*
		 *  make sure that the updates go up to the highest height
		 *  We travel left from p to the next up when p.up is null
		 */
		return true;

	}




	//System.out.println("Inserted key " + k + " into list with " + i  + " extra levels.");


	private void addLayer() {
		SkipListNode p1, p2;

		p1 = new SkipListNode(SkipListNode.negInf);
		p2 = new SkipListNode(SkipListNode.posInf);

		linkUpdater(p1, p2, "right", n);
		p1.down = head;

		p2.left = p1;
		p2.leftCount = n;
		p2.down = tail;

		head.up = p1;
		tail.up = p2;

		//Update head and tail

		head = p1;
		tail = p2;

		h = h + 1; //one more level
		//System.out.println("Head rank count is: " + p1.rightCount);

	}

	public void linkUpdater(SkipListNode p1, SkipListNode p2, String direction, int count){

		switch (direction){
		case ("right"):
			p1.right = p2;
		p1.rightCount = count;
		break;
		case ("left"):
			p1.left = p2;
		p1.leftCount = count;
		break;
		default: System.out.println("Unable to create node, wrong inputs");
		break;
		}

	}

	/*
	 * Deletes a string from the SkipList, returns true if string was successfully deleted, false if string was not in the list.
	 */

	public boolean delete(String k){

		SkipListNode p = findEntry(k);
		
		helperPrint(new SkipListNode(k), "deleteStart", 0);

		//previous node to deleted node
		SkipListNode q = p.left;

		//count of level
		int i = 0;

		//if p is not in the list, we're done

		if (!k.equals(p.key)){
			if (helperPrintVal)
			System.out.println("Key " + k + " not found, already removed.");
			return false;
		}

		/*
		 * if p is found and as long as p.up exists, first delete it by reestablishing links, if p was the only value in the list aside from the negs
		 * and the height isn't 0, then we delete the row.
		 */


		while(p != null){

			if (i > 0){
				//both counts merge their ranks -1 and -1 the result
				p.left.rightCount = p.right.leftCount = p.leftCount + p.rightCount - 1;
			}

			p.left.right = p.right;
			p.right.left = p.left;

			n = n - 1;

			//We then move p upwards, then if it exists we will delete the bottom pointer

			helperPrint(p, "deleteNorm", i);

			q = p.left;
			p = p.up;
			i++;

			if (p != null){
				p.down.up = null; //Remove up pointer at the bottom
				p.down = null;
			}

		}


		//Cleanup old layers with no entries anymore
		// We will also increment q as we can, and reduce the distance of the nodes by 1 node all the way to h.

		for (int height = h; height >= 0; height--){

			SkipListNode oldHead = head;
			SkipListNode oldTail = tail;

			if ((oldHead.right.key.equals(SkipListNode.posInf))){ //the list is empty, we can delete
				head = oldHead.down;
				tail = oldTail.down;
				head.up = null;
				tail.up = null;
				h = h-1;
			}

		}
		
		helperPrint(q, "delCasc", i);

		//navigate left to nearest up node and reduce all edges by 1 node

		while (h - i >= 0){

			if (q.up == null){
				q = q.left;
			}
			else {
				q = q.up;
				q.rightCount = q.right.leftCount = q.rightCount -1;
				i++;
			}

		}

		helperPrint(q, "delEnd", 0);
		return true;

		//System.out.println("Removed key " + k);

	}


	public void helperPrint(SkipListNode e, String command, int level){

		if (helperPrintVal){

			switch (command) {
			case ("insertStart"): System.out.println("Beginning insertion of key " + e.key);
				System.out.println("------------");
				break;
			case ("deleteStart"): System.out.println("Beginning deletion of key " + e.key);
				System.out.println("------------");
				break;
			case ("normal"):System.out.println("For node " + e.key + " on level " + level + " :");
				System.out.println("right pointer is " + e.rightCount + " rank(s) away from next node.");
				System.out.println("left pointer is " + e.leftCount + " rank(s) away from previous node.");
				System.out.println("------------");
				break;
			case("deleteNorm"):System.out.println("For node " + e.key + ":");
				System.out.println("Deletion at level " + level);
				System.out.println("Rank difference between nodes " + '"' + e.left.key + 
						'"' + " and " + '"' + e.right.key + '"' + " now " + e.leftCount);
				System.out.println("------------");
				break;
			case ("end"): 	System.out.println("For top level node " + e.key + " to " + e.right.key);
				System.out.println("Right pointer of " + e.key + " is " + e.rightCount + " rank(s) away from node " + e.right.key);
				System.out.println("Left pointer of " + e.key + " is " + e.leftCount + " rank(s) away from the previous node");
				System.out.println("The height of the list is currently: " + h);
				System.out.println("The number of elements in the list is: " + n);
				System.out.println("------------");
				break;
			case ("delCasc"): System.out.println("Deletion cascading up from level " + level + " to " + h);
						System.out.println("------------");
						break;
			case ("delEnd"): System.out.println("Deletion ended");
				System.out.println("The height of the list is currently: " + h);
				System.out.println("The number of elements in the list is: " + n);
				System.out.println("------------");
				break;
			}
			return;
		}
		else return;

	}

	/*
	 * Will return a reference to the ith smallest element in a skip list
	 */
	public SkipListNode select(int i){

		SkipListNode p;


		//Start at the head
		p = head;
		int horizontalCount = p.rightCount;

		while(true){

			//Search RIGHT until you find a larger count

			while( ((p.right.key) != SkipListNode.posInf) && horizontalCount <= i){

				p = p.right;	//Move to the right
				horizontalCount = p.rightCount + horizontalCount;
			}

			//If the value is indeed larger or equal, we'll be at p that we must go down (if it exists)

			if (p.down != null){
				horizontalCount = horizontalCount - p.rightCount;
				p = p.down;	//Go down one level
				horizontalCount = horizontalCount + p.rightCount;
			}
			else break; //We have reached the lowest level
		}

		return p;

	}

	/*
	 * Takes a count of randomized values to insert, a count of values to delete and does both
	 * Then takes the ranks value and returns select i for all ranks to ensure proper navigation.
	 */
	public static void randomInserter(int ins, int del, int ranks, SkipList s, boolean b){

		int[] insArray = new int[ins];
		int[] rank = new int[ranks];
		Random r = new Random();

		//populate the array with random values between 1 and 100 and inserts the value into the SkipList

		for (int i = 0; i < insArray.length; i++){
			insArray[i] = r.nextInt(100);
			String insString = Integer.toString(insArray[i]);
			while (!s.insert(insString)){
				insArray[i] = r.nextInt(100);
				insString = Integer.toString(insArray[i]);
				//System.out.println("Inserting " + insString);
			}
			if (b)
			Printout.printout(s, "Horizontal");
		}

		//Delete del random keys

		for (int j = 0; j < del ; j++){
			String deleteString = Integer.toString(insArray[r.nextInt(ins - j - 1)]);
			while(!s.delete(deleteString)){
				deleteString = Integer.toString(insArray[r.nextInt(ins - j - 1)]);
				//System.out.println("Deleting " + deleteString);
			}
			if (b)
			Printout.printout(s, "Horizontal");
		}

		for (int k = 0; k < ranks; k++){
			int l = 0;
			rank[k] = r.nextInt(ins - del) + 1;

			while(l < k){
				if (rank[k] == rank[l]){
					rank[k] = r.nextInt(ins - del) + 1;
				}else l++;
			}
		}

		Arrays.sort(rank);
		if (!b)
			Printout.printout(s, "Horizontal");
		for (int k = 0; k < ranks; k++){
			System.out.println("The node at rank " + rank[k] + " is: " + '"' + s.select(rank[k]).key + '"');
		}

	}

	public static void main(String[] args) throws IOException
	{
		
		System.out.println("Welcome to Ammar Vahanvaty's Skip List Program!");
		System.out.print("Would you like to enter debug mode? (yes/no) ----> ");
		int b = System.in.read();
		if (b == 121){
			helperPrintVal = true;
		}
		int inserts = 10;		//number of inserted values
		int deletions = 5;	//number of deletions
		int ranksDivider = 2;	// divider for ranks to ensure we don't ask for more ranks than we have nodes but still allows for variable ranks. There will always be at least 1
		int x;

		if (ranksDivider > (inserts - deletions)){
			x = (ranksDivider % (inserts - deletions)) + 1;
		}else x = ranksDivider;
		
		System.out.println("Creating SkipList with " + inserts + " insertions, " + deletions + " deletions and "
					+ x + " ranks.");

		SkipList s = new SkipList();

		randomInserter(inserts, deletions, x , s, helperPrintVal);
	}

}
