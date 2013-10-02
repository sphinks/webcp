
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.*;
import java.util.List;
import org.apache.log4j.Logger;
import java.util.regex.Pattern;

import org.apache.http.Header;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class BasicCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    Logger LOGGER = Logger.getLogger(BasicCrawler.class);

    CrawlStat myCrawlStat;
    BufferedWriter bw;

    public BasicCrawler() throws IOException {
        myCrawlStat = new CrawlStat();
        try {
            File outFile = new File("titles.txt");
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            FileWriter fw = new FileWriter(outFile.getAbsoluteFile());
            bw = new BufferedWriter(fw);
        }catch (FileNotFoundException e){
            System.out.println("File not found");
            System.out.println("Unexpected file close");
            bw.close();
        }
    }

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && href.startsWith("http://dx.com/");
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        /*String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getPath();
        String subDomain = page.getWebURL().getSubDomain();
        String parentUrl = page.getWebURL().getParentUrl();
        String anchor = page.getWebURL().getAnchor();*/

        //System.out.println("Docid: " + docid);
        System.out.println("URL: " + url);
        //LOGGER.debug("Visit: " + url);
        /*System.out.println("Domain: '" + domain + "'");
        System.out.println("Sub-domain: '" + subDomain + "'");
        System.out.println("Path: '" + path + "'");
        System.out.println("Parent page: " + parentUrl);
        System.out.println("Anchor text: " + anchor);*/

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            String title = htmlParseData.getTitle();
            List<WebURL> links = htmlParseData.getOutgoingUrls();
            if (title.contains("case")) {
                try {
                    bw.write(title);
                    bw.write('\n');
                } catch(IOException e){
                    System.out.print("Get error while to to write " + title);
                }
            }
            /*System.out.println("Text length: " + text.length());
            System.out.println("Html length: " + html.length());
            System.out.println("Number of outgoing links: " + links.size());*/
            myCrawlStat.incTotalLinks(links.size());
            myCrawlStat.incProcessedPages();
            try {
                myCrawlStat.incTotalTextSize(htmlParseData.getText().getBytes("UTF-8").length);
            } catch (UnsupportedEncodingException ignored) {
                // Do nothing
            }
        }
        // We dump this crawler statistics after processing every 50 pages
        if (myCrawlStat.getTotalProcessedPages() % 50 == 0) {
            dumpMyData();
        }

        /*Header[] responseHeaders = page.getFetchResponseHeaders();
        if (responseHeaders != null) {
            System.out.println("Response headers:");
            for (Header header : responseHeaders) {
                System.out.println("\t" + header.getName() + ": " + header.getValue());
            }
        }*/

        //System.out.println("=============");
    }

    @Override
    public Object getMyLocalData() {
        return myCrawlStat;
    }

    // This function is called by controller before finishing the job.
    // You can put whatever stuff you need here.
    @Override
    public void onBeforeExit() {
        dumpMyData();
        try {
            bw.flush();
            bw.close();
        }catch(IOException e){
            System.out.print("Error on closing file");
        }
    }

    public void dumpMyData() {
        int id = getMyId();
        // This is just an example. Therefore I print on screen. You may
        // probably want to write in a text file.
        System.out.println("Crawler " + id + "> Processed Pages: " + myCrawlStat.getTotalProcessedPages());
        System.out.println("Crawler " + id + "> Total Links Found: " + myCrawlStat.getTotalLinks());
        System.out.println("Crawler " + id + "> Total Text Size: " + myCrawlStat.getTotalTextSize());
    }
}