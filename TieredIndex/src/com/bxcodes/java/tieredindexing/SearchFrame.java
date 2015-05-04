package com.bxcodes.java.tieredindexing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

public class SearchFrame extends JFrame {
	public SearchFrame(){
		setTitle("Inverted Index vs Tiered Index");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(
				Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 300,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 200);
		setResizable(true);
		setSize(500,500);
		
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints layer = new GridBagConstraints();
		layer.anchor = GridBagConstraints.CENTER;
		layer.insets = new Insets(5, 5, 5, 5);
		
		threshold.setText("0");
		
		output1.setPreferredSize(new Dimension(200,400));
		output2.setPreferredSize(new Dimension(200,400));
		
		topPanel.add(input);
		topPanel.add(threshold);
		
		add(mainPanel, topPanel, layer, 0, 1, 2, 1);
		add(mainPanel, output1, layer, 0, 2, 1, 1);
		add(mainPanel, output2, layer, 1, 2, 1, 1);
		
		this.add(mainPanel);
		
		load();
		
		input.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				searchOrig();
				searchTiered();
			}
			
		});
		
		threshold.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				searchOrig();
				searchTiered();
			}
			
		});

		setVisible(true);
		
	}
	
	private void searchTiered(){
		double t = Double.parseDouble(threshold.getText());
		
		int[] docs = new int[10];
		double scores[] = new double[10];
		
		int count = t1.getTopDocs(input.getText(), docs, scores, t);
		
		String out = "";
		double total = 0;
		out += "T1\n";
		
		for(int i = 0; i < count; i++){
			
			out += (i+1);
			out += ". ";
			out += products.get(docs[i]).title;
			out += "\n ";
			out += scores[i];
			out += "\n";
			total += scores[i];
		}
		
		int c2 = 0;
		
		if(count < 10){
			docs = new int[10 - count];
			scores = new double[10 - count];
			
			c2 = t2.getTopDocs(input.getText(), docs, scores, t);
			
			out += "T2\n";
			
			for(int i = 0; i < c2; i++){
				
				out += (i+1);
				out += ". ";
				out += products.get(docs[i]).title;
				out += "\n ";
				out += scores[i];
				out += "\n";
				total += scores[i];
			}
		}
		
		out += "average score : ";
		out += total/(count + c2);
		
		output2.setText(out);
	}
	
	private void searchOrig(){
		int[] docs = new int[10];
		double scores[] = new double[10];
		
		int count = index.getTopDocs(input.getText(), docs, scores);
		
		String out = "";
		double total = 0;
		for(int i = 0; i < count; i++){
			out += (i+1);
			out += ". ";
			out += products.get(docs[i]).title;
			out += "\n ";
			out += scores[i];
			out += "\n";
			total += scores[i];
		}
		out += "average score : ";
		out += total/count;
		
		output1.setText(out);
		
	}
	
	private void load(){
		while(r.next()){

			Product p = new Product();
			if(r.title != null){
				p.id = r.id;
				p.title = r.titleString.split("title:")[1];
				p.asin = r.asin;
				p.group = r.group;
				p.salesrank = r.salesrank;
				p.averate = r.salesrank;
			} else {
				p.id = r.id;
			}
			products.add(p);
		}
		index.load(new File("index"), new File("term"));
		t1.load(new File("index_r4.5_t1"), new File("term_r4.5_t1"));
		t2.load(new File("index_r4.5_t2"), new File("term_r4.5_t2"));
	}
	
	private void add(JPanel p, Component c, GridBagConstraints layer, int x,
			int y, int w, int h) {
		layer.gridx = x;
		layer.gridy = y;
		layer.gridwidth = w;
		layer.gridheight = h;
		p.add(c, layer);
	}
	
	
	public ArrayList<Product> products = new ArrayList<Product>();
	public DataReader r = new DataReader();
	public InvertedIndex index = new InvertedIndex();
	public InvertedIndex t1 = new InvertedIndex();
	public InvertedIndex t2 = new InvertedIndex();
	
	public JPanel mainPanel = new JPanel();
	public JPanel topPanel = new JPanel();
	public JTextField input = new JTextField(32);
	public JTextField threshold = new JTextField(5);
	public JTextArea output1 = new JTextArea();
	public JTextArea output2 = new JTextArea();
}
