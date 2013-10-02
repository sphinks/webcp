import edu.uci.ics.crawler4j.url.WebURL
import org.jsoup.Jsoup

import java.util.regex.Pattern
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * Created with IntelliJ IDEA.
 * User: zeriiva
 * Date: 10/2/13
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
class CrawlerContentChecks {


    private final static Pattern FILTERS = Pattern.compile(""".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4
        |wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))""")
    private static config = new ConfigSlurper().parse(new File('src/ParserConfig.groovy').toURL())
    private static domainName = config.dx.crawler.domain

    /**
     * Checks if current page worth to visit
     * @param url
     * @return
     */
    static boolean shouldVisit(WebURL url) {
        def href = url.getURL().toLowerCase()
        return !FILTERS.matcher(href).matches() && href.startsWith(domainName)
    }

    /**
     * Extract data from html code of page
     * @param html
     */
    static void dataExtractor(String html) {

        for (String string : config.keySet()) {
            println string
        }

        Document doc = Jsoup.parse(html)
        Element title = doc.select(config.dx.parser.title).first()
        println("Title: ${title.text()}")
        title = doc.select(config.dx.parser.price).first()
        println("Price: ${title.text()}")

    }
}
