package com.plr.cvstojson;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class Main {
	public static void main(String[] args) {
		
		Main m = new Main();
	
		try {
			m.doAction();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void doAction() throws Exception {

		InputStream u = this.getClass().getResourceAsStream("charset.csv");

		if (u == null) {
			System.err.println("snif");
		}

		// fstream = new FileInputStream("charset.csv");

		DataInputStream in = new DataInputStream(u);
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

		CSVReader cvsReader = new CSVReader(br);

		List<String[]> listRows = cvsReader.readAll();

		cvsReader.close();
		
		Writer writer = new Writer();
		
		writer.write(listRows);
		
		
	}

}
