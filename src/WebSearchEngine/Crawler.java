package WebSearchEngine;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler 
	{
	//HashSet is used in our project as it prevents the duplication of value
		private static Set<String> crawledList = new HashSet<String>();
		private static int maxdepth = 2; //We have kept the depth to 2 as our system took more time to crawl
		private static String regex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

		/**
		 * @param url to crawl
		 * @param depth at which the crawler should crawl
		 */
		public static void StartCrawler(String url, int depth) 
		{
			Pattern patternObject = Pattern.compile(regex);
			if (depth <= maxdepth) 
			{
				try 
				{
					Document document = Jsoup.connect(url).get();
					ParseURL.saveDoc(document, url);
					depth++;
					if (depth < maxdepth) 
					{
						Elements links = document.select("a[href]");
						for (Element page : links) 
						{
							if (Verify_URL(page.attr("abs:href")) && patternObject.matcher(page.attr("href")).find()) 
							{	
								System.out.println(page.attr("abs:href"));
								StartCrawler(page.attr("abs:href"), depth);
								crawledList.add(page.attr("abs:href"));
							}
						}
					}
				} 
				catch (Exception e) 
				{
					System.out.println("Exception:" + e);
				}
			}
		}

		/**
		 * Verify the correctness of extracted URL
		 * @param nextUrl
		 * @return
		 */
		private static boolean Verify_URL(String nextUrl) 
		{
			if (crawledList.contains(nextUrl)) 
			{
				return false;
			}
			if (nextUrl.endsWith(".jpeg") || nextUrl.endsWith(".jpg") || nextUrl.endsWith(".png")
					|| nextUrl.endsWith(".pdf") || nextUrl.contains("#") || nextUrl.contains("?")
					|| nextUrl.contains("mailto:") || nextUrl.startsWith("javascript:") || nextUrl.endsWith(".gif")
					||nextUrl.endsWith(".xml")) {
				return false;
			}
			return true;
		}

}
