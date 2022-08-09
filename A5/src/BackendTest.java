import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import java.io.*;


public class BackendTest {
    public static void main(String[] args) throws IOException {
        undo();
        testAdd();
        undo();
        testBuy();
        undo();
        testSell();
        undo();
        testCreate();
        undo();
        testDelete();
        undo();
        testRefund();
        undo();
        testModify();
        undo();
        testMerger();
        undo();
        testBackEnd();
    }

    public static void testAdd() throws IOException {
        UpdateBackEnd update = new UpdateBackEnd();
        update.addCredit("06 username2       BS 001000.00");
        //statement coverage
        try {
			Assert.assertEquals(FileUtils.fileRead("src/output/add01_uaf_output.txt"),
			        FileUtils.fileRead("src/user_account_file.txt"));
        //loop coverage
        FileWriter fw = new FileWriter("src/user_account_file.txt");
        fw.close();
        update.addCredit("06 username2       BS 001000.00");
        Assert.assertEquals(FileUtils.fileRead("src/output/loop01_add_uaf_output.txt"),
                FileUtils.fileRead("src/user_account_file.txt"));


        fw = new FileWriter("src/user_account_file.txt");
        fw.write("username1       FS 999000.00\n");
        fw.close();
        update.addCredit("06 username1       FS 000500.00");
        Assert.assertEquals(FileUtils.fileRead("src/output/loop02_add_uaf_output.txt"),
                FileUtils.fileRead("src/user_account_file.txt"));


        fw = new FileWriter("src/user_account_file.txt");
        fw.write("username1       FS 999000.00\n");
        fw.write("username2       BS 001000.00\n");
        fw.close();
        update.addCredit("06 username2       BS 000500.00");
        Assert.assertEquals(FileUtils.fileRead("src/output/loop03_add_uaf_output.txt"),
                FileUtils.fileRead("src/user_account_file.txt"));


        fw = new FileWriter("src/user_account_file.txt");
        fw.write("username1       FS 999000.00\n");
        fw.write("username2       BS 001000.00\n");
        fw.write("username3       SS 000500.99\n");
        fw.write("username4       AA 500000.00\n");
        fw.write("admin1          AA 001500.00\n");
        fw.write("nomoney         FS 000000.00\n");
        fw.close();
        update.addCredit("06 nomoney         FS 000500.00");
        Assert.assertEquals(FileUtils.fileRead("src/output/loop04_add_uaf_output.txt"),
                FileUtils.fileRead("src/user_account_file.txt"));

        //Decision coverage

        fw = new FileWriter("src/user_account_file.txt");
        fw.write("username1       FS 999000.00\n");
        fw.write("username2       BS 001000.00\n");
        fw.write("username3       SS 000500.99\n");
        fw.write("username4       AA 500000.00\n");
        fw.write("admin1          AA 001500.00\n");
        fw.write("nomoney         FS 000000.00\n");
        fw.close();

        update.addCredit("06 udnoexist       BS 001000.00");
        Assert.assertEquals(FileUtils.fileRead("src/output/dc01_add_uaf_output.txt"),
                FileUtils.fileRead("src/user_account_file.txt"));

        update.addCredit("06 username2       BS 001000.00");
        Assert.assertEquals(FileUtils.fileRead("src/output/dc02_add_uaf_output.txt"),
                FileUtils.fileRead("src/user_account_file.txt"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void testBuy() throws IOException {
    	UpdateBackEnd  update = new UpdateBackEnd ();
        //statement coverage
        update.buy("04 Concert3            username3     094 300.00");
        try {
			Assert.assertEquals(FileUtils.fileRead("src/output/buy01_uaf_output.txt"),
			        FileUtils.fileRead("src/user_account_file.txt"));
			Assert.assertEquals(FileUtils.fileRead("src/output/buy01_atf_output.txt"),
                FileUtils.fileRead("src/available_tickets_file.txt"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }


    public static void testSell() throws IOException {
        try {
        	UpdateBackEnd  update = new UpdateBackEnd ();
            update.sell("03 Little House 5      username1     009 015.00");
			Assert.assertEquals(FileUtils.fileRead("src/output/sell01_atf_output.txt"),
			        FileUtils.fileRead("src/available_tickets_file.txt"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    public static void testCreate() throws IOException {
    	UpdateBackEnd  update = new UpdateBackEnd ();
    	try {
        update.create("01 usertest321     BS 000500.00");
			Assert.assertEquals(FileUtils.fileRead("src/output/create01_uaf_output.txt"),
			        FileUtils.fileRead("src/user_account_file.txt"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void testDelete() throws IOException {
    	UpdateBackEnd  update = new UpdateBackEnd ();
        try {
			update.delete("02 username1       FS 999000.00");
		
			Assert.assertEquals(FileUtils.fileRead("src/output/delete01_uaf_output.txt"),
			        FileUtils.fileRead("src/user_account_file.txt"));
			Assert.assertEquals(FileUtils.fileRead("src/output/delete01_atf_output.txt"),
			        FileUtils.fileRead("src/available_tickets_file.txt"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void testRefund() throws IOException {
        UpdateBackEnd update = new UpdateBackEnd ();
        update.refund("05 username2      username3      000200.00");
        try {
			Assert.assertEquals(FileUtils.fileRead("src/output/refund01_uaf_output.txt"),
			        FileUtils.fileRead("src/user_account_file.txt"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void testModify() throws IOException {
        UpdateBackEnd.modifyFiles("src/user_account_file.txt", "username2       BS 001000.00", "username2       BS 002000.00");
        try {
			Assert.assertEquals(FileUtils.fileRead("src/output/add01_uaf_output.txt"),
			        FileUtils.fileRead("src/user_account_file.txt"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void testMerger() throws IOException {
        new BackEnd();
        BackEnd.mergeFiles();

        try {
			Assert.assertEquals(FileUtils.fileRead("src/output/merger01_dtf_output.txt"),
			        FileUtils.fileRead("src/daily_transaction_file.txt"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }

    public static void testBackEnd() throws IOException {
        try {
            new BackEnd();
            String[] args = null;
            final InputStream original = System.in;
            final FileInputStream fips = new FileInputStream(new File("src/BackEnd.java"));
            System.setIn(fips);
            BackEnd.main(args);
            System.setIn(original);
			Assert.assertEquals(FileUtils.fileRead("src/output/main01_dtf_output.txt"),
			        FileUtils.fileRead("daily_transaction_file.txt"));
	
			Assert.assertEquals(FileUtils.fileRead("src/output/main01_atf_output.txt"),
                FileUtils.fileRead("src/available_tickets_file.txt"));
			Assert.assertEquals(FileUtils.fileRead("src/output/main01_uaf_output.txt"),
                FileUtils.fileRead("src/user_account_file.txt"));
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void undo() throws IOException {
        FileWriter fw = new FileWriter("src/user_account_file.txt");
        fw.write("username1       FS 999000.00\n");
        fw.write("username2       BS 001000.00\n");
        fw.write("username3       SS 000500.99\n");
        fw.write("username4       AA 500000.00\n");
        fw.write("admin1          AA 001500.00\n");
        fw.write("nomoney         FS 000000.00\n");
        fw.close();

        fw = new FileWriter("src/available_tickets_file.txt");
        fw.write("Concert1            username4     030 050.00\n");
        fw.write("Concert2            username1     079 250.00\n");
        fw.write("Concert3            username3     100 300.00\n");
        fw.write("END");
        fw.close();

    }
}
