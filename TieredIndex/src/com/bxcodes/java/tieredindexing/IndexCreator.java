package com.bxcodes.java.tieredindexing;

import java.io.File;

public class IndexCreator {

	public static void main(String[] args) {
		DataReader r = new DataReader();
		InvertedIndex t1 = new InvertedIndex();
		InvertedIndex t2 = new InvertedIndex();
		
		while (r.next()) {
			if (r.title == null) {

			} else {
				if(r.salesrank <= 200000) {
					t1.feedDoc(r.id, r.title);
				} else {
					t2.feedDoc(r.id, r.title);
				}
			}
		}
		System.out.println("done.");

		File indexFile1 = new File("index_s200k_t1");
		File termFile1 = new File("term_s200k_t1");
		File indexFile2 = new File("index_s200k_t2");
		File termFile2 = new File("term_s200k_t2");
		
		t1.save(indexFile1, termFile1);
		t2.save(indexFile2, termFile2);
		
	}

}
