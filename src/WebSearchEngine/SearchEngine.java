package WebSearchEngine;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SearchEngine {

	private static Scanner sc = new Scanner(System.in);

	/**
	 * The main method.
	 * @param args the arguments
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SearchEngine SearchEngine = new SearchEngine();
		String choose = "n";
		System.out.println("\n-------------------------------------------------------------------------\n");
		System.out.println("\n                  >>> ACC Web Search Engine Project <<<             \n");
		System.out.println("\n >>>>>>>>>>>>>>>>>>>> ------------------------------- <<<<<<<<<<<<<<<<<<<\n");
		System.out.println("                        --------- Team Members----------                    \n");
		System.out.println("                 Krupa Patel, Yogesh Mavani, Meet Shukla                 ");
		System.out.println("\n >>>>>>>>>>>>>>>>>>>>-------------------------------- <<<<<<<<<<<<<<<<<<<\n");
		System.out.println("                   			     Welcome                              \n ");
		
		do {
			
		System.out.println("                       Select the option listed below:                \n ");
		System.out.println("\n >>>>>>>>>>>>>>>>>>>> ---------------------------- <<<<<<<<<<<<<<<<<<<\n");
		System.out.println(" 1) Press 1 for URL you want to search");
		System.out.println(" 2) Press 2 for Static URL (https://www.w3schools.com//)");
		System.out.println(" 3) Press 3 for Exit ");
		System.out.println("\n-----------------------------------------------------------------------\n");
		
		int OptionSelection = sc.nextInt();
		switch (OptionSelection) 
		{
			case 1:
				System.out.println("\n Please do enter valid URL for example 'www.abc.com'");
				String URL = sc.next();
				URL = "https://" + URL +"/";
				choose = SearchEngine.searchWord(URL);
				break;

			case 2:
				choose = SearchEngine.searchWord("https://www.w3schools.com/");
				break;

			case 3:
				System.out.println("Exiting...");
				choose = "n";
				break;

			default:
				System.out.println("Please enter the correct option !");
				choose = "y";
			}
		
		} 
		while (choose.equals("y"));

		System.out.println("\n-------------------------------------------------------------------------\n");
		System.out.println("               :) THANK YOU FOR USING WEB SEARCH ENGINE !! :)               ");
		System.out.println("\n-------------------------------------------------------------------------\n");
	}

	private String searchWord(String URL) 
	{	
		if(!is_valid(URL)) 
		{
			 System.out.println("Enterd URL " + URL + " isn't valid");
			 System.out.println("Please try again....\n");
			 return "y";
		}
			System.out.println("Enterd URL " + URL + " is valid\n");
			System.out.println("Crawling Started...");
			Crawler.StartCrawler(URL, 0); 						//crawling the URL mentioned
			System.out.println("Crawling Compelted...");

		//Because null values cannot be inserted into a Hash Map, a Hash Table is utilized instead.
		Hashtable<String, Integer> FileList = new Hashtable<String, Integer>();
		
		String choice = "y";
		do 
		{
			System.out.println("\n-------------------------------------------------------------------------");
			System.out.println("\n                     Enter a word you want to search                  ");
			System.out.println("\n-------------------------------------------------------------------------");
			String wordToSearch = sc.next();
			System.out.println("\n-------------------------------------------------------------------------");
			int WordFrequency = 0;
			int TotFiles = 0;
			FileList.clear();
			try 
			{
				System.out.println("\nSearching for URL...");
				File Files = new File(Path.txtDirectoryPath);

				File[] ArrayofFiles = Files.listFiles();

				for (int i = 0; i < ArrayofFiles.length; i++) 
				{
					In data = new In(ArrayofFiles[i].getAbsolutePath());
					String txt = data.readAll();
					data.close();
					Pattern p = Pattern.compile("::");
					String[] file_name = p.split(txt);
					WordFrequency = SearchWord.WordSearch(txt, wordToSearch.toLowerCase(), file_name[0]); // search word in txt files

					if (WordFrequency != 0) {
						FileList.put(file_name[0], WordFrequency);
						TotFiles++;
					}
				}

				if(TotFiles>0) 
				{
				System.out.println("\n Total Number of Files contained word : " + wordToSearch + " is : " + TotFiles);
				}
				else 
				{
					System.out.println("\n File not found!!!! containing word : "+ wordToSearch);
					SearchWord.WordSuggestions(wordToSearch.toLowerCase()); // If a word is not found or is incorrect, suggest another word.
				}

				SearchWord.fileRanking(FileList, TotFiles); // From higher to lower, the files are ranked by the frequency of the word count.
			} 
			catch (Exception e) 
			{
				System.out.println("Exception:" + e);
			}
			System.out.println("\n------ Are you wiling to search any other word (y/n)?------");
			choice = sc.next();
		} 
		while (choice.equals("y"));
		FilesDeleted();	
		
		System.out.println("\n Switch back to main menu (y/n)?");   // returns to the main menu to choose from new or static URL or exit the code. 
		return sc.next();
	}

	// All files created during the crawling and word search are deleted.
	private void FilesDeleted() 
	{
		File files = new File(Path.txtDirectoryPath);
		File[] ArrayofFiles = files.listFiles();
		for (int i = 0; i < ArrayofFiles.length; i++) 
		{
			ArrayofFiles[i].delete();
		}
		File HTMLFiles= new File(Path.htmlDirectoryPath);
		File[] fileArrayhtml = HTMLFiles.listFiles();

		for (int i = 0; i < fileArrayhtml.length; i++) 
		{	
			fileArrayhtml[i].delete();
		}
	}
	
	/*
	 Validation of URL with DNS
	 */
	public boolean is_valid(String URL)
    {
        /* Try creating a valid URL*/
        try 
        {
        	System.out.println("Validating URL...");
        	URL obj = new URL(URL);
            HttpURLConnection CON = (HttpURLConnection) obj.openConnection();
            
            //Sending the request
            CON.setRequestMethod("GET");
            int response = CON.getResponseCode();
            if(response==200) 
            {
            	 return true;
            }
            else 
            {
            	return false;
            }  
        }    
        // If there is an Exception
        // while creating URL object
        catch (Exception e) 
        {
            return false;
        }
    }

}
