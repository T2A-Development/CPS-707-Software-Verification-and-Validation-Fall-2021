import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

/**
 * 
 */

/**
 * @author Tusaif, Avneet, Suhail
 *
 */
public class UpdateBackEnd {
	public void addCredit(String input)throws IOException {
		// this will update the user account File (uaf) file with 
		//the new amount of credit for that user
		String userName = input.substring(3, 19);
		String userRole = input.substring(19, 22);
		String userAmount = input.substring(22);
		Scanner scan = new Scanner((new File("src/user_account_file.txt")));
		while (scan.hasNextLine()) {
			String output = scan.nextLine();
			if (output.substring(0, 16).equals(userName)) {
				Float total = Float.parseFloat(output.substring(20)) + Float.parseFloat(userAmount);
				String addNewLine = String.format("%-15s", userName) + " " + userRole + " " + (String.format("%09.2f", total));
				modifyFiles("src/user_account_file.txt", output, addNewLine);
				break;
			}
		}
		scan.close();
		System.out.println("addcredit Method run complete");
	}

	public void buy(String input) throws IOException 
	{
		// still to add code here
		Scanner s = new Scanner(new File("src/available_tickets_file.txt"));
        String eTitle = input.substring(3, 23);
        String sName = input.substring(23, 37);
        String numTicketsStr = input.substring(37, 41);
        int numTickets = Integer.parseInt(numTicketsStr);
        String newLine = "";
        int atfnumTickets = 0;
        String temp = "";
        while (s.hasNextLine()) {
            temp = s.nextLine();
            String atfETitle = temp.substring(0, 19);
            String atfSName = temp.substring(20, 34);
            if (atfETitle.equals(eTitle) && atfSName.equals(sName)) {
                String atfnumTicketsStr = temp.substring(35, 38);
                atfnumTickets = Integer.parseInt(atfnumTicketsStr);
                String atfpriceTicketStr = temp.substring(39, 45);
                //int newTotal = atfnumTickets - numTickets;

                newLine = String.format("%-19s", atfETitle) + " " + String.format("%-14s", atfSName) +
                        " " + String.format("%03d", numTickets) + " " + atfpriceTicketStr;

                break;
            }
        }
        UpdateBackEnd.modifyFiles("src/available_tickets_file.txt", temp, newLine);
        s.close();

        s = new Scanner(new File("src/user_account_file.txt"));
        sName = input.substring(23, 37);
        numTicketsStr = input.substring(38, 41);
        numTickets = Integer.parseInt(numTicketsStr);
        String priceTicketStr = input.substring(42, 48);
        float priceTicket = Float.parseFloat(priceTicketStr);
        float totalCredit = (atfnumTickets - numTickets) * priceTicket;
        newLine = "";
        while (s.hasNextLine()) {
            temp = s.nextLine();
            String uafSName = temp.substring(0, 15);
            if (uafSName.equals(sName)) {
                String uafType = temp.substring(16, 18);
                String uafCreditsStr = temp.substring(20, 28);
                float uafCredits = Float.parseFloat(uafCreditsStr);
                float newTotal = uafCredits + totalCredit;

                newLine = String.format("%-15s", uafSName) + " " + uafType + " " + (String.format("%09.2f", newTotal));
                break;
            }
        }
        UpdateBackEnd.modifyFiles("src/user_account_file.txt", temp, newLine);
        s.close();
        System.out.println("Buy Method run complete");
	}

	public void sell(String input) throws IOException 
	{
		// the method will update the available ticket file (atf)  file with the 
		//new ticket available for sale
		UpdateBackEnd.modifyFiles("src/available_tickets_file.txt", "END", 
				input.substring(3) + "\nEND");
		System.out.println("Sell Method run complete");
	}

	public void create(String input) throws IOException 
	{
		// this method will update user account file (uaf) with 
		//the newly created user
		BufferedWriter buffWriter = new BufferedWriter(
				new FileWriter("src/user_account_file.txt", true)   );
		buffWriter.write("\n"+input.substring(3));
		buffWriter.close();
		System.out.println("Create Method run complete");
	}

	public void delete(String input) throws Exception {
		// delete account from user account file (uaf) and
		// any related tickets deleted from available ticket file (atf)
		String linesRead = FileUtils.fileRead("src/user_account_file.txt");
		List<String> linesList = new ArrayList<String>(Arrays.asList(linesRead.split(",")));
		List<String> updatedLines = linesList.stream().filter(s ->
		!s.contains(input.substring(3))).collect(Collectors.toList());
		String listString = String.join(", ", updatedLines);
		FileUtils.fileWrite("src/user_account_file.txt", listString);

		linesRead = FileUtils.fileRead("src/available_tickets_file.txt");
		List<String> linesList2 = new ArrayList<String>(Arrays.asList(linesRead.split(",")));
		updatedLines = linesList2.stream().filter(s -> 
		!s.contains(input.substring(3, 19))).collect(Collectors.toList());
		FileUtils.fileWrite("src/available_tickets_file.txt", updatedLines.toString());
		System.out.println("Delete Method run complete");
	}

	public void refund(String input) throws IOException 
	{
		//still to add code
		String buyer = input.substring(3, 18);
        String seller = input.substring(18, 33);
        String credit = input.substring(33);
        String oldbuy = "";
        String oldsell = "";
        String buys = "";
        String sells = "";

        Scanner s = new Scanner((new File("src/user_account_file.txt")));
        while (s.hasNextLine()) {
            String line = s.nextLine();
            String name = line.substring(0, 16);
            if (name.equalsIgnoreCase(buyer)) {
                String role = line.substring(16, 19);
                float total = Float.parseFloat(line.substring(20)) + Float.parseFloat(credit);
                oldbuy = line;
                buys = String.format("%-15s", buyer) + " " + role + " " + String.format("%09.2f", (total));
            } else if (name.equalsIgnoreCase(seller)) {
                String role = line.substring(16, 19);
                float total = Float.parseFloat(line.substring(20)) - Float.parseFloat(credit);
                oldsell = line;
                sells = String.format("%-15s", seller) + " " + role + " " + String.format("%09.2f", (total));

            }
        }
        modifyFiles("src/user_account_file.txt", oldbuy,
                buys);
        modifyFiles("src/user_account_file.txt", oldsell,
                sells);
        s.close();
        System.out.println("refund Method run complete");
	}

	static void modifyFiles(String filePath, String oldString, String newString)
			throws IOException 
	{
		// modify the file as specified as per the requirements
		File modifyFile = new File(filePath);
		String readOldContent = "";
		BufferedReader fileReader = null;
		FileWriter newFileWriter= null;
		fileReader = new BufferedReader(new FileReader(modifyFile));
		//Reading all the lines of input text file into read Old Content
		String line = fileReader.readLine();
		while (line != null) {
			readOldContent = readOldContent + line + System.lineSeparator();
			line = fileReader.readLine();
		}
		//Replacing oldString with newString in the read old Content
		String newContent = readOldContent.replaceAll(oldString, newString);
		//Rewriting the input text file with newContent
		newFileWriter = new FileWriter(modifyFile);
		newFileWriter.write(newContent);

		fileReader.close();
		newFileWriter.close();
		System.out.println("ModifyFiles Method run complete");
	}


}
