package pl.portfolio.kotlinapp.nasafeed

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.util.*

class ParseApplication {
    private val nasaNews: ArrayList<FeedEntry> = arrayListOf(FeedEntry())

    fun getNasaNews(): ArrayList<FeedEntry> {
        return nasaNews
    }

    fun parse(xmlData: String?): Boolean {
        val entryName = "item"
        val titleName = "title"
        val descName = "description"
        val publishDateName = "pubdate"
        val linkName = "link"
        val sourceName = "source"
        var status = true
        var inEntry = false
        var currentRecord: FeedEntry? = null
        var textValue = ""
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(StringReader(xmlData))
            var eventType = xpp.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name
                when (eventType) {
                    XmlPullParser.START_TAG -> if (entryName.equals(tagName, ignoreCase = true)) {
                        inEntry = true
                        currentRecord = FeedEntry()
                    }
                    XmlPullParser.TEXT -> textValue = xpp.text
                    XmlPullParser.END_TAG -> if (inEntry) {
                        when {
                            entryName.equals(tagName, ignoreCase = true) -> {
                                currentRecord?.let { nasaNews.add(it) }
                                inEntry = false
                            }
                            titleName.equals(tagName, ignoreCase = true) -> {
                                currentRecord?.title = textValue
                            }
                            descName.equals(tagName, ignoreCase = true) -> {
                                currentRecord?.description = textValue
                            }
                            publishDateName.equals(tagName, ignoreCase = true) -> {
                                currentRecord?.pubdate = textValue
                            }
                            linkName.equals(tagName, ignoreCase = true) -> {
                                currentRecord?.link = textValue
                            }
                            sourceName.equals(tagName, ignoreCase = true) -> {
                                currentRecord?.source = textValue
                            }
                        }
                    }
                    else -> {
                    }
                }
                eventType = xpp.next()
            }
        } catch (e: Exception) {
            status = false
            e.printStackTrace()
        }
        return status
    }
}