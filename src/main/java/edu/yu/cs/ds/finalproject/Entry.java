package edu.yu.cs.ds.finalproject;

import java.awt.RenderingHints.Key;
import java.util.ArrayList;

public class Entry<Key extends Comparable<Key>, Value> {
	public Key key;
	public ArrayList<Row> val = new ArrayList<Row>();
	public Node child;
	
	/**
	 * constructs an entry, initializes the key, and child node, and adds the 
	 * value to the arraylist of values
	 * @param key
	 * @param val
	 * @param node
	 */
	public Entry(Key key, Row val, Node node) {
		this.key = key;
		this.val.add(val);
		this.child = node;
	}
}