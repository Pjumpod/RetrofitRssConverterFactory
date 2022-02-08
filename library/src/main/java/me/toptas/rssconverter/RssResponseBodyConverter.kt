package me.toptas.rssconverter

import android.util.Log
import okhttp3.ResponseBody
import org.xml.sax.InputSource
import retrofit2.Converter
import javax.xml.parsers.SAXParserFactory


internal class RssResponseBodyConverter : Converter<ResponseBody, RssFeed> {

    override fun convert(value: ResponseBody): RssFeed {
        val rssFeed = RssFeed()
        try {
            val parser = XMLParser()
            //val xmlValue = value.toString().replace("(?m)^[ \t]*\r?\n", "")
            val parserFactory = SAXParserFactory.newInstance()
            val saxParser = parserFactory.newSAXParser()
            val xmlReader = saxParser.xmlReader
            xmlReader.contentHandler = parser
            val inputSource = InputSource(value.charStream())//xmlValue.reader())//value.charStream())
            try {
                //Log.w("inputSource",inputSource.toString())
                xmlReader.parse(inputSource)
            } catch(e: Exception) {
                Log.e("error","XML Phase error")
            }
            val items = parser.items
            rssFeed.items = items
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rssFeed
    }

}