package com.bxcodes.java.tieredindexing;

import java.io.File;
import java.util.ArrayList;

public class T4Search {

	public static void main(String[] args) {
		DataReader r = new DataReader();
		InvertedIndex t1 = new InvertedIndex();
		InvertedIndex t2 = new InvertedIndex();

		ArrayList<String> titles = new ArrayList<String>();

		while (r.next()) {
			titles.add(r.titleString);
		}
		
		File indexFile1 = new File("index_r4_t1");
		File termFile1 = new File("term_r4_t1");
		File indexFile2 = new File("index_r4_t2");
		File termFile2 = new File("term_r4_t2");
		
		t1.load(indexFile1, termFile1);
		t2.load(indexFile2, termFile2);
		
		String q = "cat, dog";
		int[] docs = new int[10];
		double[] scores = new double[10];

		int count = t1.getTopDocs(q, docs, scores);
		System.out.println(count);
		for (int i = 0; i < count; i++) {
			System.out.println(titles.get(docs[i]) + " : " + scores[i]);
		}
		
		
	}

}
