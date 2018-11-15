package edu.yu.cs.ds.finalproject;

public class Node {
    public int numberOfChildren;
	public Entry[] entries = new Entry[4];

    /**
     * creates a node and intializes the current number of children
     * @param k
     */
    public Node(int k) {
        numberOfChildren = k;
    }
}
