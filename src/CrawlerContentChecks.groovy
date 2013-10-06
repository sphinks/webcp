import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.ServerAddress
import edu.uci.ics.crawler4j.url.WebURL
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import java.util.regex.Pattern
import org.jsoup.nodes.Document
import com.mongodb.MongoClient

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
    private static CONFIG = new ConfigSlurper().parse(new File('src/ParserConfig.groovy').toURL())
    private static domainName = CONFIG.dx.crawler.domain

    /**
     * Checks if current page worth to visit
     * @param url
     * @return
     */
    def static shouldVisit(WebURL url) {
        def href = url.getURL().toLowerCase()
        return !FILTERS.matcher(href).matches() && href.startsWith(domainName) && href.contains('case') && href.contains('iphone')
    }

    /**
     * Extract data from html code of page
     * @param html
     */
    def static dataExtractor(String html, String url) {

        //walk(CONFIG)

        MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017))
        DB db = mongoClient.getDB("covers");
        DBCollection colls = db.getCollection('Cover')
        BasicDBObject object = new BasicDBObject()

        Document doc = Jsoup.parse(html)
        Element title = doc.select(CONFIG.dx.parser.title).first()
        //println("Title: ${title.text()}")
        if (title != null) {
            object.append('title', title.text())
            Element price = doc.select(CONFIG.dx.parser.price).first()
            //println("Price: ${title.text()}")
            object.append('price', price.text())

            object.append('url', url)
            Element sku = doc.select(CONFIG.dx.parser.sku).first()
            object.append('id', sku.text() )
            //println("Object: ${object.toString()}")
            colls.insert(object)
        }
    }

    //Test method to iterate over config file
    def static walk( map, root=true ) {
        map.each { key, value ->
            if( value instanceof Map ) {
                println "$key (${root?'root':'subroot'})"
                walk( value, false )
            }
            else {
                println "$key=$value"
            }
        }
    }
}
