/**
 * Represents a managed memory space. The memory space manages a list of allocated 
 * memory blocks, and a list free memory blocks. The methods "malloc" and "free" are 
 * used, respectively, for creating new blocks and recycling existing blocks.
 */
public class MemorySpace {
	
	// A list of the memory blocks that are presently allocated
	private LinkedList allocatedList;

	// A list of memory blocks that are presently free
	private LinkedList freeList;

	/**
	 * Constructs a new managed memory space of a given maximal size.
	 * 
	 * @param maxSize
	 *            the size of the memory space to be managed
	 */
	public MemorySpace(int maxSize) {
		// initiallizes an empty list of allocated blocks.
		allocatedList = new LinkedList();
	    // Initializes a free list containing a single block which represents
	    // the entire memory. The base address of this single initial block is
	    // zero, and its length is the given memory size.
		freeList = new LinkedList();
		freeList.addLast(new MemoryBlock(0, maxSize));
	}

	/**
	 * Allocates a memory block of a requested length (in words). Returns the
	 * base address of the allocated block, or -1 if unable to allocate.
	 * 
	 * This implementation scans the freeList, looking for the first free memory block 
	 * whose length equals at least the given length. If such a block is found, the method 
	 * performs the following operations:
	 * 
	 * (1) A new memory block is constructed. The base address of the new block is set to
	 * the base address of the found free block. The length of the new block is set to the value 
	 * of the method's length parameter.
	 * 
	 * (2) The new memory block is appended to the end of the allocatedList.
	 * 
	 * (3) The base address and the length of the found free block are updated, to reflect the allocation.
	 * For example, suppose that the requested block length is 17, and suppose that the base
	 * address and length of the the found free block are 250 and 20, respectively.
	 * In such a case, the base address and length of of the allocated block
	 * are set to 250 and 17, respectively, and the base address and length
	 * of the found free block are set to 267 and 3, respectively.
	 * 
	 * (4) The new memory block is returned.
	 * 
	 * If the length of the found block is exactly the same as the requested length, 
	 * then the found block is removed from the freeList and appended to the allocatedList.
	 * 
	 * @param length
	 *        the length (in words) of the memory block that has to be allocated
	 * @return the base address of the allocated block, or -1 if unable to allocate
	 */
	public int malloc(int length) {
		Node currentNode = freeList.getFirst(); // starting from the first node
		
		while (currentNode != null) { // searching for the right block
			MemoryBlock freeBlock = currentNode.block;  // "taking" the block from the node
			
			// if the block is big enough
			if (freeBlock.getLength() >= length) {
				
				// if the length matches exactly
				if (freeBlock.getLength() == length) {
					freeList.remove(currentNode);  // remove the block from freeList
					allocatedList.addLast(freeBlock);  // add it to allocatedList
					return freeBlock.getBaseAddress();  // returns the base address of the block
				}
	
				// if the block's length is bigger, create a new block on the same address
				MemoryBlock allocatedBlock = new MemoryBlock(freeBlock.getBaseAddress(), length);
				allocatedList.addLast(allocatedBlock);  // adding the new block to allocatedList
	
				// update the free block's space
				freeBlock.setLength(freeBlock.getLength() - length);  // update the size of the free block
				freeBlock.setBaseAddress(freeBlock.getBaseAddress() + length);  // update the address of the free block
	
				return allocatedBlock.getBaseAddress();  // returns the new address of the allocated block
			}
	
			// if the block isn't the right size, move to the next one
			currentNode = currentNode.next;
		}
		return -1;  // if no block found, return -1 to indicate allocation failed
	}
	
	
		

	/**
	 * Frees the memory block whose base address equals the given address.
	 * This implementation deletes the block whose base address equals the given 
	 * address from the allocatedList, and adds it at the end of the free list. 
	 * 
	 * @param baseAddress
	 *            the starting address of the block to freeList
	 */
	public void free(int address) {
		Node currentNode = allocatedList.getFirst();
	
		// If the list is empty, and there are no blocks to remove
		if (currentNode == null) {
			throw new IllegalArgumentException("No blocks allocated, cannot free memory");
		}
	
		while (currentNode != null) {
			MemoryBlock freeBlock = currentNode.block;
	
			// If we found the block with the requested address
			if (freeBlock.getBaseAddress() == address) {
				allocatedList.remove(currentNode);  // Remove it from the allocated list
				freeList.addLast(freeBlock);         // Add it to the free list
				return;
			}
	
			currentNode = currentNode.next;  // Continue to the next node if not found
		}
	
		// If the block was not found in the allocated list
		throw new IllegalArgumentException("Block with the given address not found in allocated memory");
	}
	
	
	/**
	 * A textual representation of the free list and the allocated list of this memory space, 
	 * for debugging purposes.
	 */
	public String toString() {
		return freeList.toString() + "\n" + allocatedList.toString();		
	}
	
	/**
	 * Performs defragmantation of this memory space.
	 * Normally, called by malloc, when it fails to find a memory block of the requested size.
	 * In this implementation Malloc does not call defrag.
	 */
	public void defrag() {
		boolean merged;  // A flag to track whether any blocks were merged
		do {
			merged = false;  // Initially, no blocks have been merged
			Node current = freeList.getFirst();  // Start from the first block in the free list
			
			while (current != null && current.next != null) {  // If there is a block after the current one
				MemoryBlock currentBlock = current.block;  // Get the current block
				MemoryBlock nextBlock = current.next.block;  // Get the next block
				
				// Check if the two blocks are adjacent (end of currentBlock + length == start of nextBlock)
				if (currentBlock.getBaseAddress() + currentBlock.getLength() == nextBlock.getBaseAddress()) {
					// If they are adjacent, merge them into one block
					MemoryBlock mergedBlock = new MemoryBlock(currentBlock.getBaseAddress(), 
															  currentBlock.getLength() + nextBlock.getLength());
					
					// Remove the next block from the freeList
					freeList.remove(current.next);
					
					// Update the current block to the new merged block
					current.block = mergedBlock;
					
					merged = true;  // Mark that a merge happened
				}
	
				// If no merge happened, move to the next block
				if (!merged) {
					current = current.next;
				}
			}
		} while (merged);  // Repeat the loop as long as there were merges in the previous iteration
	}
		
}
