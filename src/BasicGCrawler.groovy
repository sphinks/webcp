import edu.uci.ics.crawler4j.crawler.Page
import edu.uci.ics.crawler4j.crawler.WebCrawler
import edu.uci.ics.crawler4j.parser.HtmlParseData
import edu.uci.ics.crawler4j.url.WebURL
import org.apache.log4j.Logger

import java.util.regex.Pattern

/**
 * Created with IntelliJ IDEA.
 * User: Ivan Zerin
 * Date: 10/2/13
 * Time: 11:43 AM
 */
class BasicGCrawler extends WebCrawler {

    Logger LOGGER = Logger.getLogger(BasicGCrawler.class)

    CrawlStat myCrawlStat
    private PrintWriter outFile

    BasicGCrawler() {
        myCrawlStat = new CrawlStat()
        try {
            outFile = new File('titles.txt').newPrintWriter()
        }catch (FileNotFoundException e){
            println("File not found")
            println("Unexpected file close")
            outFile.close()
        }
    }

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    boolean shouldVisit(WebURL url) {
        return CrawlerContentChecks.shouldVisit(url)
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    void visit(Page page) {
        int docid = page.getWebURL().getDocid()
        String url = page.getWebURL().getURL()
        println("URL: " + url)

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData()
            //String text = htmlParseData.getText()
            String html = htmlParseData.getHtml()
            CrawlerContentChecks.dataExtractor(html)
            String title = htmlParseData.getTitle()
            List<WebURL> links = htmlParseData.getOutgoingUrls()
            //if (title.contains("case")) {
            try {
                outFile.write(title);
                outFile.write('\n');
            } catch(IOException e){
                println("Get error while to to write " + title)
            }
            //}
            myCrawlStat.incTotalLinks(links.size())
            myCrawlStat.incProcessedPages()
            try {
                myCrawlStat.incTotalTextSize(htmlParseData.getText().getBytes("UTF-8").length)
            } catch (UnsupportedEncodingException ignored) {
                // Do nothing
            }
        }
        // We dump this crawler statistics after processing every 50 pages
        if (myCrawlStat.getTotalProcessedPages() % 50 == 0) {
            dumpMyData()
        }
    }

    @Override
    Object getMyLocalData() {
        return myCrawlStat
    }

    // This function is called by controller before finishing the job.
    // You can put whatever stuff you need here.
    @Override
    void onBeforeExit() {
        dumpMyData()
        try {
            outFile.flush()
            outFile.close()
        }catch(IOException e){
            println("Error on closing file")
        }
    }

    void dumpMyData() {
        def id = getMyId()
        // This is just an example. Therefore I print on screen. You may
        // probably want to write in a text file.
        println("Crawler ${id}> Processed Pages: " + myCrawlStat.getTotalProcessedPages())
        println("Crawler ${id}> Total Links Found: " + myCrawlStat.getTotalLinks())
        println("Crawler ${id}> Total Text Size: " + myCrawlStat.getTotalTextSize())
    }
}
