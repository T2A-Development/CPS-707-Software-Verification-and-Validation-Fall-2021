import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class BackEnd{
	public static void main(String[] args) throws Exception {
		//This is where back end reads through daily transaction file to apply changes to User Account File (uaf) and Available Ticket File(atf)
		mergeFiles();
		Scanner scanner = new Scanner(new File("src/daily_transaction_file.txt"));
		while (scanner.hasNextLine()) 
		{
			String action = scanner.nextLine();
			UpdateBackEnd update = new UpdateBackEnd();
			if (action.length() > 0) {
				if (action.substring(0, 2).equals("01")) {
					update.create(action);
				} else if (action.substring(0, 2).equals("02")) {
					update.delete(action);
				} else if (action.substring(0, 2).equals("03")) {
					update.sell(action);
				} else if (action.substring(0, 2).equals("04")) {
					update.buy(action);
				} else if (action.substring(0, 2).equals("05")) {
					update.refund(action);
				} else if (action.substring(0, 2).equals("06")) {
					update.addCredit(action);
				} else if (action.substring(0, 2).equals("00")) {

				} else {
					//do nothing
               		}
             	}
           	}
		scanner.close();
		System.out.println("Main Method run complete");
	}

	public static void mergeFiles() throws IOException 
	{
		// This method merges multiple daily transaction files into one and stores them in one directory.
		File createDir = new File("src/output");
		PrintWriter prWriter = new PrintWriter("src/daily_transaction_file.txt");
		String[] fileNames = createDir.list();
		for (String fileName : fileNames) {
			if (fileName.contains("daily_transaction_file")) {
				File createFile = new File(createDir, fileName);
               	// create object of BufferedReader
               	BufferedReader buffReader = new BufferedReader(new FileReader(createFile));
               	// Read from current file
               	String line = buffReader.readLine();
               	while (line != null) {
                   	// write to the output file
                  	prWriter.println(line);
                  	line = buffReader.readLine();
               	}
               	prWriter.flush();
               	buffReader.close();
       		}
            prWriter.close();
            System.out.println("mergeFiles Method run complete");
		}
	}
}
