package cra;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class abc {
	int count = 0;
	
	public void run() {
		// csv������ ���� ��� �Է�
		File csv = new File("C:\\Users\\LG-U\\OneDrive\\���� ȭ��\\testcsv.csv");
		BufferedWriter bw = null;
		
		try {
			//new FileWriter(csv, true)���� true�� �Է��ϸ� �̾��, false�� �Է��ϸ� ���� �������
			bw = new BufferedWriter(new FileWriter(csv, true));
			
			//i<x���� x ���� ������ �� �Է�, �� 20000��������
			for(int i=1560; i<21826; i+=7) {
			URL url = new URL("https://urlhaus.abuse.ch/browse/page/"+i+"/");
			//URLConnection con = url.openConnection();
			//con.setConnectTimeout(1000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			int check_line=0;
			
			while((line = reader.readLine()) != null) {
				if(line.contains("<a href=\"/url/") && !line.contains("Mozi") && !line.contains("mozi"))
					check_line=1;
				else
					check_line=0;
				if(check_line == 1) {
						count++;
						String temp = line.split(">")[5].split("<")[0];
						temp = temp.trim();
						bw.write(temp);
						bw.newLine();
						//System.out.println(temp + count);
						if(count == 100) {
							Thread.sleep(15000);
							count = 0;
						}
					}	
				}
			}
		} catch(Exception e) {e.printStackTrace();}
		finally {
			try {
				if(bw != null) {
					bw.flush();
					bw.close();
				} 
			} catch(Exception e) {e.printStackTrace();}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		abc aa = new abc();
		aa.run();

	}

}
