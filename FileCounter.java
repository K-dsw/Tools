import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FileCounter {
	public Map<String, Integer> map = new HashMap<String, Integer>();	
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入文件夹路径:");
		String arg= scanner.next();
		if (arg != null && arg.length() > 0) {
			File file = new File(arg);
			FileCounter fileCounter = new FileCounter();
			fileCounter.countFiles(file);
			if (fileCounter.map.size() > 0) {
				List<Map.Entry<String, Integer>> filesinfo =
				    new ArrayList<Map.Entry<String, Integer>>(fileCounter.map.entrySet());

				Collections.sort(filesinfo, new Comparator<Map.Entry<String, Integer>>() {   
				    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
				        //return (o1.getValue() - o2.getValue()); 
				        return (o1.getKey()).toString().compareTo(o2.getKey());
				    }
				});
				FileWriter fileWriter = new FileWriter("result.txt");
				System.out.println("已生成result.txt文件...");
				fileWriter.write("*************CPP文件统计如下***********\r\n");
				for (int i = 0; i < filesinfo.size(); i++) {
					fileWriter.write(filesinfo.get(i).getKey() + "\r\n");
				}
				fileWriter.write("*************文件对应的LOC如下***********\r\n");				
				for (int i = 0; i < filesinfo.size(); i++) {
					fileWriter.write(filesinfo.get(i).getValue() + "\r\n");
				}
				fileWriter.close();
			}
		}
		scanner.close();
	}
	
	public void countFiles(File file) throws IOException{
		if (!file.exists()) {
			throw new IllegalArgumentException("目录" + file + " 不存在！");
		}
		if (!file.isDirectory()) {
			throw new IllegalArgumentException(file + "不是目录！");
		}
		File[] files = file.listFiles();
		for (File file2 : files) {
			if (file2.isDirectory()) {
				countFiles(file2);
			}else {
				String[] token = file2.getName().split("\\.");
				if (token.length > 1 && token[1].equals("cpp")) {
					map.put(file2.getName(), getFileLines(file2));
				}
			}
		}
	}
	
	public int getFileLines(File file) throws IOException {
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		int filelines = 0;
		while (bufferedReader.readLine() != null) {
			filelines++;
		}
		bufferedReader.close();
		fileReader.close();
		return filelines;
	}

}
