/**
 * Created with IntelliJ IDEA.
 * User: zeriiva
 * Date: 9/25/13
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */

class GpathTest {

    static main(args) {

        BasicCrawlController.initAndStartCrawler();

        /*def parser = new org.cyberneko.html.parsers.SAXParser()

        def content = ""
        def file = new File('1.htm').eachLine {
            content += it + '\n'
        }

        def slurper = new XmlSlurper(parser)
        def htmlParser = slurper.parseText(content)

        htmlParser.'**'.findAll{ it.@class != '' }.each {
            println "Class of elemnt ${it.name()} is ${it.@class}"
        }*/
    }
}
