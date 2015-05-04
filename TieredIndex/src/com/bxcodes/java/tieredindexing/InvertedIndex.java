package com.bxcodes.java.tieredindexing;

import java.awt.Point;
import java.io.*;
import java.util.*;

public class InvertedIndex {

	public InvertedIndex() {
		lists = new ArrayList<InvertedList>();
		termList = new HashMap<String, Integer>();
		titleLength = new HashMap<Integer, Integer>();
	}
	
	public int getTopDocs(String query, int[] docs, double[] scores, double threshold){
		String[] t = query.trim().toLowerCase()
				.replaceAll("[,.!?():;/\\-\\[\\]\"]", " ").split("\\s+");
		return getTopDocs(t, docs, scores, threshold);
	}

	public int getTopDocs(String query, int[] docs, double[] scores) {
		String[] t = query.trim().toLowerCase()
				.replaceAll("[,.!?():;/\\-\\[\\]\"]", " ").split("\\s+");
		return getTopDocs(t, docs, scores);
	}
	
	public int getTopDocs(String[] t, int[] docs, double[] scores, double threshold) {

		
		int size = docs.length;

		double k1 = 1.2;
		double b = 0.75;
		double avg = 1.0 * totalLength / viableDocs;

		int count = 0;

		ArrayList<Integer> d = new ArrayList<Integer>();
		ArrayList<InvertedList> l = new ArrayList<InvertedList>();

		for (int i = 0; i < t.length; i++) {
			if (termList.containsKey(t[i])) {
				int id = termList.get(t[i]);
				InvertedList list = lists.get(id);
				if (!l.contains(list)) {
					l.add(list);
				}

				for (Point p : list.postings) {
					if (!d.contains(p.x)) {
						d.add(p.x);
					}
				}
			}
		}

		for (Integer i : d) {
			double k = k1 * ((1 - b) + b * titleLength.get(i) / avg);
			double score = 0;
			for (InvertedList list : l) {
				double p = (k1 + 1) * list.freq(i) / (k + list.freq(i));
				score += Math.log10((0.5 + viableDocs - l.size())
						/ (0.5 + l.size()))
						* p;
			}
			

			for (int j = size - 1; j >= 0; j--) {
				if (score > scores[j]) {
					if (j != size - 1) {
						scores[j + 1] = scores[j];
						docs[j + 1] = docs[j];
					}
					scores[j] = score;
					docs[j] = i;
				} else {
					if (j != size - 1) {
						count++;
					}
					break;
				}
			}

		}
		count = 0;
		
		for(int j = 0; j < size; j++){
			if(scores[j] >= threshold){
				count++;
			}
		}

		return count > size ? size : count;
	}

	public int getTopDocs(String[] t, int[] docs, double[] scores) {

		int size = docs.length;

		double k1 = 1.2;
		double b = 0.75;
		double avg = 1.0 * totalLength / viableDocs;

		int count = 0;

		ArrayList<Integer> d = new ArrayList<Integer>();
		ArrayList<InvertedList> l = new ArrayList<InvertedList>();

		for (int i = 0; i < t.length; i++) {
			if (termList.containsKey(t[i])) {
				int id = termList.get(t[i]);
				InvertedList list = lists.get(id);
				if (!l.contains(list)) {
					l.add(list);
				}

				for (Point p : list.postings) {
					if (!d.contains(p.x)) {
						d.add(p.x);
					}
				}
			}
		}

		for (Integer i : d) {
			double k = k1 * ((1 - b) + b * titleLength.get(i) / avg);
			double score = 0;
			for (InvertedList list : l) {
				double p = (k1 + 1) * list.freq(i) / (k + list.freq(i));
				score += Math.log10((0.5 + viableDocs - l.size())
						/ (0.5 + l.size()))
						* p;
			}

			for (int j = size - 1; j >= 0; j--) {
				if (score > scores[j]) {
					if (j != size - 1) {
						scores[j + 1] = scores[j];
						docs[j + 1] = docs[j];
					}
					scores[j] = score;
					docs[j] = i;
				} else {
					if (j != size - 1) {
						count++;
					}
					break;
				}
			}

		}

		return count > size ? size : count;
	}

	public void feedDoc(int docId, String[] terms) {
		for (int i = 0; i < terms.length; i++) {
			String term = terms[i];
			if (term.equals("")) {
				continue;
			}
			int freq = 1;
			for (int j = i + 1; j < terms.length; j++) {
				if (terms[j].equals(term)) {
					freq++;
					terms[j] = "";
				}
			}
			addPosting(docId, term, freq);
		}
	}

	public void load(File index, File term) {
		try {

			BufferedReader reader = new BufferedReader(new FileReader(term));
			DataInputStream in = new DataInputStream(new FileInputStream(index));

			int size = Integer.parseInt(reader.readLine());

			for (int i = 0; i < size; i++) {
				String data[] = reader.readLine().split(" ");
				termList.put(data[0], i);
				int count = Integer.parseInt(data[1]);
				InvertedList l = new InvertedList(data[0]);
				for (int j = 0; j < count; j++) {
					l.addPosting(in.readInt(), in.readInt());
				}
				lists.add(l);
			}

			reader.close();
			in.close();

			DataInputStream info = new DataInputStream(new FileInputStream(
					"info"));

			totalDocs = info.readInt();
			viableDocs = info.readInt();
			totalLength = info.readInt();

			for (int i = 0; i < totalDocs; i++) {
				titleLength.put(i, info.readInt());
			}

			info.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void save(File index, File term) {
		try {
			DataOutputStream o = new DataOutputStream(new FileOutputStream(
					index));
			BufferedWriter w = new BufferedWriter(new FileWriter(term));

			w.write(lists.size() + "\n");

			for (InvertedList l : lists) {
				w.write(l.term + " " + l.size() + "\n");
				l.writeData(o);
				o.flush();
			}

			w.close();
			o.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addPosting(int docId, String term, int freq) {
		int termId;
		InvertedList list;
		if (termList.containsKey(term)) {
			termId = termList.get(term);
			list = lists.get(termId);
		} else {
			termId = lists.size();
			termList.put(term, termId);
			list = new InvertedList(term);
			lists.add(list);
		}
		list.addPosting(docId, freq);
	}

	public int totalDocs;
	public int viableDocs;
	public int totalLength;

	private HashMap<Integer, Integer> titleLength;
	private HashMap<String, Integer> termList;
	private ArrayList<InvertedList> lists;
}
