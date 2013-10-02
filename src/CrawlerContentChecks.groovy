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
    private static domainName = 'http://dx.com/'

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

        def config = new ConfigSlurper().parse(new File('src/ParserConfig.groovy').toURL())
        //println config.dx.title
        for (String string : config.keySet()) {
            println string
        }

        Document doc = Jsoup.parse(html)
        Element title = doc.select(config.dx.title).first()
        println("Title: ${title.text()}")
        title = doc.select(config.dx.price).first()
        println("Price: ${title.text()}")


        /*def parser = new org.cyberneko.html.parsers.SAXParser()

        def slurper = new XmlSlurper(parser)
        def htmlParser = slurper.parseText(html)

        htmlParser.'**'.findAll{ it.@id == 'headline' }.each {
            println "Class of element ${it.name()} is ${it.@class}, parent is ${it.parent().name()}"
        }*/
    }
}
