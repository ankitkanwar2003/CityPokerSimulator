package heap_package;
import java.util.ArrayList;

public class Heap{

	protected Node root;								// root of the heap
	protected Node[] nodes_array;                    // Stores the address of node corresponding to the keys
	private int max_size;                           // Maximum number of nodes heap can have 
	private static final String NullKeyException = "NullKey";      // Null key exception
	private static final String NullRootException = "NullRoot";    // Null root exception
	private static final String KeyAlreadyExistsException = "KeyAlreadyExists";   // Key already exists exception

	/* 
	   1. Can use helper methods but they have to be kept private. 
	   2. Not allowed to use any data structure. 
	*/
	private void BuildHeap_r (int[] keys, int[] values, Node x, int i, int n) throws Exception {
		if ((2*i + 1) < n) {
			if (nodes_array[keys[2*i+1]] != null) {
				throw new Exception(KeyAlreadyExistsException);
			}
			x.left = new Node(keys[2*i+1], values[2*i+1], x);
			nodes_array[x.left.key] = x.left;
			BuildHeap_r(keys, values, x.left, (2*i + 1), n);
		}
		if ((2*i+2) < n) {
			if (nodes_array[keys[2*i+2]] != null) {
				throw new Exception(KeyAlreadyExistsException);
			}
			x.right = new Node(keys[2*i+2], values[2*i+2], x);
			nodes_array[x.right.key] = x.right;
			BuildHeap_r(keys, values, x.right, (2*i + 2), n);
		}
		if ((x.left != null) && (x.right == null)) {
			x.height = 2;
			x.is_complete = false;
			return;
		} else if (x.left == null) {
			return;
		}
		x.is_complete = (x.left.is_complete && x.right.is_complete && (x.left.height == x.right.height));
		x.height = (x.left.height > x.right.height) ? x.left.height + 1 : x.right.height + 1;
	}
	public Heap(int max_size, int[] keys_array, int[] values_array) throws Exception{

		/* 
		   1. Create Max Heap for elements present in values_array.
		   2. keys_array.length == values_array.length and keys_array.length number of nodes should be created. 
		   3. Store the address of node created for keys_array[i] in nodes_array[keys_array[i]].
		   4. Heap should be stored based on the value i.e. root element of heap should 
		      have value which is maximum value in values_array.
		   5. max_size denotes maximum number of nodes that could be inserted in the heap. 
		   6. keys will be in range 0 to max_size-1.
		   7. There could be duplicate keys in keys_array and in that case throw KeyAlreadyExistsException. 
		*/
		/* 
		   For eg. keys_array = [1,5,4,50,22] and values_array = [4,10,5,23,15] : 
		   => So, here (key,value) pair is { (1,4), (5,10), (4,5), (50,23), (22,15) }.
		   => Now, when a node is created for element indexed 1 i.e. key = 5 and value = 10, 
		   	  that created node address should be saved in nodes_array[5]. 
		*/ 

		/*
		   n = keys_array.length
		   Expected Time Complexity : O(n).
		*/

		this.max_size = max_size;
		this.nodes_array = new Node[this.max_size];

		// To be filled in by the student
		int j = keys_array.length;
		if (j > 0) {
			root = new Node(keys_array[0], values_array[0], null);
			nodes_array[keys_array[0]] = root;
			BuildHeap_r(keys_array, values_array, root, 0, j);
			for (int k = j/2; k >= 0; k--) {
				percolate_down(nodes_array[keys_array[k]]);
			}
		}
	}
	private ArrayList<Integer> helper_getMax(Node t) {
		ArrayList<Integer> x = new ArrayList<>();
		x.add(t.key);
		if ((t.left != null) && (t.left.value == root.value)) {
			ArrayList<Integer> u = helper_getMax(t.left);
			for (Integer y: u) {
				x.add(y);
			}
		}
		if (t.right != null && (t.right.value == root.value)) {
			ArrayList<Integer> z = helper_getMax(t.right);
			for (Integer y: z) {
				x.add(y);
			}
		}
		return x;
	}
	public ArrayList<Integer> getMax() throws Exception{

		/* 
		   1. Returns the keys with maximum value in the heap.
		   2. There could be multiple keys having same maximum value. You have
		      to return all such keys in ArrayList (order doesn't matter).
		   3. If heap is empty, throw NullRootException.

		   Expected Time Complexity : O(1).
		*/
		if (root == null) {
			throw new Exception(NullRootException);
		}
		ArrayList<Integer> r = this.helper_getMax(root);
		return r;
	}
	private void helper_insert (Node x, Node y) {
		if (x.left == null) {//x is trivially complete when left is null so checking this first
			x.left = y;
			y.parent = x;
			x.is_complete = false;
			x.height = 2;
			percolate_up(y);
		} else if (x.is_complete) {
			helper_insert(x.left, y);
			x.is_complete = false;
			x.height = x.left.height + 1;
		} else if (x.right == null) {
			x.right = y;
			y.parent = x;
			x.is_complete = true;
			percolate_up(y);
		} else if (!x.left.is_complete) {
			helper_insert(x.left, y);
			x.is_complete = ((x.left.is_complete) && (x.left.height == x.right.height) && (x.right.is_complete));
		} else {
			helper_insert(x.right, y);
			x.is_complete = ((x.left.height == x.right.height) && (x.right.is_complete));
		}
	}
	private void percolate_up(Node x) {
		if ((x.parent == null) || ((x.parent != null) && (x.value <= x.parent.value))) {
			return;
		} else {
			int t = x.parent.value;
			int t1 = x.parent.key;
			x.parent.value = x.value;
			x.parent.key = x.key;
			x.value = t;
			x.key = t1;
			this.nodes_array[x.parent.key] = x.parent;
			this.nodes_array[x.key] = x;
			percolate_up(x.parent);
		}
	}
	public void insert(int key, int value) throws Exception{

		/* 
		   1. Insert a node whose key is "key" and value is "value" in heap 
		      and store the address of new node in nodes_array[key]. 
		   2. If key is already present in heap, throw KeyAlreadyExistsException.

		   Expected Time Complexity : O(logn).
		*/
		if (this.nodes_array[key] != null) {
			throw new Exception(KeyAlreadyExistsException);
		}
		Node x = new Node(key, value, null);
		this.nodes_array[key] = x;
		if (root == null) {
			root = x;
			return;
		}
		helper_insert(root, x);
		// To be filled in by the student
	}
	private void percolate_down(Node f) {
		int t = f.value;
		int t1 = f.key;
		if (f.left == null) {
			return;
		} else if (f.right == null) {
			if (f.value > f.left.value) {
				return;
			} else {
				f.value = f.left.value;
				f.key = f.left.key;
				f.left.value = t;
				f.left.key = t1;
				this.nodes_array[t1] = f.left;
				this.nodes_array[f.key] = f;
			}
		} else {
			if ((f.value < f.right.value) || (f.value < f.left.value)) {
				if (f.right.value > f.left.value) {
					f.value = f.right.value;
					f.key = f.right.key;
					f.right.value = t;
					f.right.key = t1;
					this.nodes_array[t1] = f.right;
					this.nodes_array[f.key] = f;
					percolate_down(f.right);
				} else {
					f.value = f.left.value;
					f.key = f.left.key;
					f.left.value = t;
					f.left.key = t1;
					this.nodes_array[t1] = f.left;
					this.nodes_array[f.key] = f;
					percolate_down(f.left);
				}
			}
		}
	}
	private void findLast(Node x) {
		if (x.left == null) {//x is trivially complete when left is null so checking this first
			root.value = x.value;
			this.nodes_array[root.key] = null;
			root.key = x.key;
			this.nodes_array[x.key] = root;
			if (x.parent != null) {
				if (x.parent.left == x) {//may not need this case anymore
					x.parent.left = null;
					x.parent.is_complete = true;
					x.parent.height -= 1;
				} else {
					x.parent.right = null;
					x.parent.is_complete = false;
				}
			} else {
				root = null;
				this.nodes_array[x.key] = null;
			}
		} else if (x.is_complete) {
			findLast(x.right);
			x.is_complete = false;
			int left = (x.left == null) ? 0 : x.left.height;
			int right = (x.right == null) ? 0 : x.right.height;
			x.height = (left > right) ? (left + 1) : (right + 1);
		} else if (x.right == null) {
			root.value = x.left.value;
			this.nodes_array[root.key] = null;
			root.key = x.left.key;
			this.nodes_array[x.left.key] = root;
			x.height -= 1;
			x.left = null;
			x.is_complete = true;
		} else  if ((!x.left.is_complete) || (x.left.height>x.right.height)){
			findLast(x.left);
			x.is_complete = ((x.left.is_complete) && (x.right.is_complete) && (x.left.height == x.right.height));
			int left = (x.left == null) ? 0 : x.left.height;
			int right = (x.right == null) ? 0 : x.right.height;
			x.height = (left > right) ? (left + 1) : (right + 1);
		} else {
			findLast(x.right);
			x.is_complete = ((x.left.is_complete) && (x.right.is_complete) && (x.left.height == x.right.height));
			int left = (x.left == null) ? 0 : x.left.height;
			int right = (x.right == null) ? 0 : x.right.height;
			x.height = (left > right) ? (left + 1) : (right + 1);
		}
	}
	public ArrayList<Integer> deleteMax() throws Exception{

		/* 
		   1. Remove nodes with the maximum value in the heap and returns their keys.
		   2. There could be multiple nodes having same maximum value. You have
		      to delete all such nodes and return all such keys in ArrayList (order doesn't matter).
		   3. If heap is empty, throw NullRootException.

		   Expected Average Time Complexity : O(logn).
		*/
		if (root == null) {
			throw new Exception(NullRootException);
		}
		//use recursion to obtain this array
		ArrayList<Integer> max_keys = this.getMax();   // Keys with maximum values in heap that will be deleted.
		// To be filled in by the student
		for (int i = 0; i < max_keys.size(); i++) {
			findLast(root);
			if (root != null) {
				percolate_down(root);
			}
			this.nodes_array[max_keys.get(i)] = null;
		}
		return max_keys;
	}

	public void update(int key, int diffvalue) throws Exception{

		/* 
		   1. Update the heap by changing the value of the node whose key is "key" to value+diffvalue.
		   2. If key doesn't exists in heap, throw NullKeyException.

		   Expected Time Complexity : O(logn).
		*/
		if (root == null) {
			throw new Exception(NullRootException);
		}
		else if (this.nodes_array[key] == null) {
			throw new Exception(NullKeyException);
		} else {
			this.nodes_array[key].value += diffvalue;
			if (diffvalue > 0) {
				percolate_up(this.nodes_array[key]);
			} else if(diffvalue < 0) {
				percolate_down(this.nodes_array[key]);
			}
		}
		
		// To be filled in by the student
	}

	public int getMaxValue() throws Exception{

		/* 
		   1. Returns maximum value in the heap.
		   2. If heap is empty, throw NullRootException.

		   Expected Time Complexity : O(1).
		*/
		if (root == null) {
			throw new Exception(NullRootException);
		}
		// To be filled in by the student

		return root.value;
	}

	private ArrayList<Integer> getKeys_r(Node k) {
		ArrayList<Integer> x = new ArrayList<Integer>();
		if (k != null) {
			x.add(k.key);
		}
		if (k.left != null) {
			ArrayList<Integer> y = getKeys_r(k.left);
			for (Integer some: y) {
				x.add(some);
			}
		}
		if (k.right != null) {
			ArrayList<Integer> z = getKeys_r(k.right);
			for (Integer some: z) {
				x.add(some);
			}
		}
		return x;
	}
	public ArrayList<Integer> getKeys() throws Exception{

		/*
		   1. Returns keys of the nodes stored in heap.
		   2. If heap is empty, throw NullRootException.
		 
		   Expected Time Complexity : O(n).
		*/
		if (root == null) {
			throw new Exception(NullRootException);
		} else {
			ArrayList<Integer> x = getKeys_r(root);
			return x;
		}
		// To be filled in by the student
	}
}