import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.util.List;

/**
 * @author Ivan Zerin
 */
class GCrawlerController {

    static void initAndStartCrawler() throws Exception {

        /*
         * crawlStorageFolder is a folder where intermediate crawl data is
         * stored.
         */
        def crawlStorageFolder = "tmpCrawler"

        /*
         * numberOfCrawlers shows the number of concurrent threads that should
         * be initiated for crawling.
         */
        def numberOfCrawlers = 1

        CrawlConfig config = new CrawlConfig()

        config.setCrawlStorageFolder(crawlStorageFolder)

        /*
         * Be polite: Make sure that we don't send more than 1 request per
         * second (1000 milliseconds between requests).
         */
        config.setPolitenessDelay(200)

        /*
         * You can set the maximum crawl depth here. The default value is -1 for
         * unlimited depth
         */
        config.setMaxDepthOfCrawling(2)

        /*
         * You can set the maximum number of pages to crawl. The default value
         * is -1 for unlimited number of pages
         */
        config.setMaxPagesToFetch(1)

        /*
         * Do you need to set a proxy? If so, you can use:
         * config.setProxyHost("proxyserver.example.com");
         * config.setProxyPort(8080);
         *
         * If your proxy also needs authentication:
         * config.setProxyUsername(username); config.getProxyPassword(password);
          */
        //config.setProxyHost("surf-proxy.intranet.db.com");
        //config.setProxyPort(8080);

        /*
         * This config parameter can be used to set your crawl to be resumable
         * (meaning that you can resume the crawl from a previously
         * interrupted/crashed crawl). Note: if you enable resuming feature and
         * want to start a fresh crawl, you need to delete the contents of
         * rootFolder manually.
         */
        config.setResumableCrawling(false)

        config.setUserAgentString("Opera/9.80 (Windows NT 6.1; U; ru) Presto/2.8.131 Version/11.10")

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config)
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig()
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher)
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer)

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */

        controller.addSeed("http://dx.com/p/protective-soft-silicone-bumper-frame-w-screen-protector-film-for-iphone-5-white-192429")

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(BasicGCrawler.class, numberOfCrawlers)

        List<Object> crawlersLocalData = controller.getCrawlersLocalData()
        long totalLinks = 0
        long totalTextSize = 0
        int totalProcessedPages = 0
        for (Object localData : crawlersLocalData) {
            CrawlStat stat = (CrawlStat) localData
            totalLinks += stat.getTotalLinks()
            totalTextSize += stat.getTotalTextSize()
            totalProcessedPages += stat.getTotalProcessedPages()
        }
        println("Aggregated Statistics:")
        println("   Processed Pages: " + totalProcessedPages)
        println("   Total Links found: " + totalLinks)
        println("   Total Text Size: " + totalTextSize)
    }
}