package com.bxcodes.java.tieredindexing;

import java.io.*;

public class DataReader {

	public DataReader() {
		file = new File("amazon-meta.txt");
		try {
			reader = new BufferedReader(new FileReader(file));
			end = false;
			reader.readLine();
			reader.readLine();
			reader.readLine();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printsp() {

		try {
			reader.readLine();
			reader.readLine();
			String l = reader.readLine();
			if (!l.trim().split(" ")[0].equals("title:")) {
				System.out.println(l);
			}
			while (!reader.readLine().equals("")) {

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printnext() {
		try {
			System.out.println(reader.readLine().trim().split("Id:\\s+")[1]);
			System.out
					.println(reader.readLine().trim().split("(?=[,.])|\\s+")[1]);
			System.out
					.println(reader.readLine().trim().split("(?=[,.])|\\s+")[1]);
			while (!reader.readLine().equals("")) {

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean next() {
		if (end) {
			return false;
		}
		try {
			String l = reader.readLine();
			if (l == null) {
				end = true;
				return false;
			} else {
				id = Integer.parseInt(l.split("\\s+")[1]);
				asin = reader.readLine().split("\\s+")[1];
				titleString = reader.readLine();
				title = titleString.trim().toLowerCase()
						.replaceAll("[,.!?():;/\\-\\[\\]\"]", " ")
						.split("\\s+");
				if (!title[0].equals("title")) {
					title = null;
					while (!reader.readLine().equals("")) {

					}
				} else {
					title[0] = "";
					group = reader.readLine().split("group:\\s+")[1];
					salesrank = Integer.parseInt(reader.readLine().split(
							"salesrank:\\s+")[1]);
					reader.readLine();
					int cate = Integer.parseInt(reader.readLine().split(
							"categories:\\s+")[1]);
					for (int i = 0; i < cate; i++) {
						reader.readLine();
					}
					averate = Double.parseDouble(reader.readLine().split(
							"avg rating:\\s+")[1]);
					while (!reader.readLine().equals("")) {

					}
				}
				return true;

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public int id;
	public String asin;
	public String[] title;
	public String titleString;
	public String group;
	public int salesrank;
	public double averate;

	private File file;
	private BufferedReader reader;
	private boolean end;
}
