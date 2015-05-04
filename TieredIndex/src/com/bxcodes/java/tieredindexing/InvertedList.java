package com.bxcodes.java.tieredindexing;

import java.awt.Point;
import java.io.*;
import java.util.LinkedList;

public class InvertedList {

	public InvertedList(String term) {
		this.term = term;
		postings = new LinkedList<Point>();
	}

	public void addPosting(int docId, int freq) {
		postings.add(new Point(docId, freq));
	}

	public int freq(int id) {
		for (Point p : postings) {
			if (p.x == id) {
				return p.y;
			}
		}
		return 0;
	}

	public int size() {
		return postings.size();
	}

	public void writeData(DataOutputStream o) throws IOException {
		for (Point p : postings) {
			o.writeInt(p.x);
			o.writeInt(p.y);
		}
	}

	public final String term;

	public LinkedList<Point> postings;
}
