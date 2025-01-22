import java.util.Currency;

/**
 * Represents a list of Nodes. 
 */
public class LinkedList {
	
	private Node first; // pointer to the first element of this list
	private Node last;  // pointer to the last element of this list
	private int size;   // number of elements in this list
	
	/**
	 * Constructs a new list.
	 */ 
	public LinkedList () {
		first = null;
		last = first;
		size = 0;
	}
	
	/**
	 * Gets the first node of the list
	 * @return The first node of the list.
	 */		
	public Node getFirst() {
		return this.first;
	}

	/**
	 * Gets the last node of the list
	 * @return The last node of the list.
	 */		
	public Node getLast() {
		return this.last;
	}
	
	/**
	 * Gets the current size of the list
	 * @return The size of the list.
	 */		
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Gets the node located at the given index in this list. 
	 * 
	 * @param index
	 *        the index of the node to retrieve, between 0 and size
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than the list's size
	 * @return the node at the given index
	 */		
	public Node getNode(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException(
					"index must be between 0 and size");
		}
		Node current = first; 
		for (int i = 0; i < index; i ++) {
			current = current.next;
		} 
		return current;
	}

	/**
	 * Creates a new Node object that points to the given memory block, 
	 * and inserts the node at the given index in this list.
	 * <p>
	 * If the given index is 0, the new node becomes the first node in this list.
	 * <p>
	 * If the given index equals the list's size, the new node becomes the last 
	 * node in this list.
     * <p>
	 * The method implementation is optimized, as follows: if the given 
	 * index is either 0 or the list's size, the addition time is O(1). 
	 * 
	 * @param block
	 *        the memory block to be inserted into the list
	 * @param index
	 *        the index before which the memory block should be inserted
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than the list's size
	 */
	public void add(int index, MemoryBlock block) {
		Node newNode = new Node(block);
	
		if (index < 0 || index > size) {
			throw new IllegalArgumentException("Index must be between 0 and size");
		}
	
		if (index == 0) { 
			newNode.next = first; 
			first = newNode; 
	
			if (size == 0) { // if the size is 0, also the last become the new node
				last = newNode;
			}
		} 
		
		else if (index == size) { // adding the node to be the last 
			if (last != null) {
				last.next = newNode; 
			}
			last = newNode; 
		} 
		
		else { // the index is in the mid of the list
			Node current = first;
			//finding the node before the index
			for (int i = 0; i < index - 1; i++) {
				current = current.next;
			}
			newNode.next = current.next;  // the new node pointing on thr next
			current.next = newNode;  // the one before pointing on the new node
		}
	
		size++; 
	}
	
	

	/**
	 * Creates a new node that points to the given memory block, and adds it
	 * to the end of this list (the node will become the list's last element).
	 * 
	 * @param block
	 *        the given memory block
	 */
	public void addLast(MemoryBlock block) {
		Node newNode = new Node(block);
        
		if (size == 0) {  // if the list is empty
			first = newNode;
			last = newNode;
		}
		else {
			last.next = newNode;
			last = newNode;
		}
        size++;
	}
	
	/**
	 * Creates a new node that points to the given memory block, and adds it 
	 * to the beginning of this list (the node will become the list's first element).
	 * 
	 * @param block
	 *        the given memory block
	 */
	public void addFirst(MemoryBlock block) {
		Node newNode = new Node(block);
        
		if (size == 0) {  // if the list is empty
			first = newNode;
			last = newNode;
		}
		else {
			newNode.next = first;
			first = newNode;
		}
		size++;
	}

	/**
	 * Gets the memory block located at the given index in this list.
	 * 
	 * @param index
	 *        the index of the retrieved memory block
	 * @return the memory block at the given index
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than or equal to size
	 */
	public MemoryBlock getBlock(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}
		Node node = getNode(index);
		return node.block;
	}
	

	/**
	 * Gets the index of the node pointing to the given memory block.
	 * 
	 * @param block
	 *        the given memory block
	 * @return the index of the block, or -1 if the block is not in this list
	 */
	public int indexOf(MemoryBlock block) {

		for (int i = 0; i < size; i ++) {
			if (getBlock(i) == block) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Removes the given node from this list.	
	 * 
	 * @param node
	 *        the node that will be removed from this list
	 */
	public void remove(Node node) {

		if (node == first) {
			first = first.next;
			if (first == null) {
              last = null;
			}
			size --;
		}

		Node current = first;
		while (current != null && current.next != node) { //finding the node before
			current = current.next;
		}

		if (current != null && current.next == node) { // remove the node
			current.next = node.next;  
			if (node == last) {
				last = current; 
			}
			size--;
		}
	}

	/**
	 * Removes from this list the node which is located at the given index.
	 * 
	 * @param index the location of the node that has to be removed.
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than or equal to size
	 */
/* 		public void remove(int index) {
			if (index < 0 || index >= size) {
				throw new IllegalArgumentException("Index must be between 0 and size");
			}
			remove(getNode(index));
			size --;
		}*/
		public void remove(int index) {
			if (index < 0 || index >= size) {
				throw new IllegalArgumentException("Index must be between 0 and size");
			}
		
			// במקרה של הסרה באינדקס תקני, אנחנו ניגשים לצומת המתאים
			Node current = first;
			if (index == 0) {
				first = current.next;
				if (first == null) { // אם הרשימה ריקה לאחר ההסרה, last צריך להיות null
					last = null;
				}
			} else {
				Node previous = null;
				for (int i = 0; i < index; i++) {
					previous = current;
					current = current.next;
				}
				previous.next = current.next; // מחברים את הצומת הקודם לצומת שאחריו
				if (current.next == null) { // אם מדובר בצומת אחרון
					last = previous;
				}
			}
			size--; // מעדכנים את הגודל
		}*/
		

	/**
	 * Removes from this list the node pointing to the given memory block.
	 * 
	 * @param block the memory block that should be removed from the list
	 * @throws IllegalArgumentException
	 *         if the given memory block is not in this list
	 */
	public void remove(MemoryBlock block) {
			if (first == null) {
				throw new IllegalArgumentException("The list is empty.");
			}
		
			// if the first node is pointing 
			if (first.block == block) {
				first = first.next;
				if (first == null) { 
					last = null;
				}
				size--;
				return;
			}
		
			//else, finding node 
			Node current = first;
			while (current != null && current.next != null) {
				if (current.next.block == block) {
					remove(current.next);  
					return;
				}
				current = current.next;
			}
		
			throw new IllegalArgumentException("The given memory block is not in this list");
		}	

	/**
	 * Returns an iterator over this list, starting with the first element.
	 */
	public ListIterator iterator(){
		return new ListIterator(first);
	}
	
	/**
	 * A textual representation of this list, for debugging.
	 */
	public String toString() {
		if (size == 0) {
			return "()";
		}
	    String str = "(";
		Node current = first;
		while (current != null) {
			str += current.block + " ";
			current = current.next;
		}
		return str.substring(0, str.length()-1) + ")";
	}
}