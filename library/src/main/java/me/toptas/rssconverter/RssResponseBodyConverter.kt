package me.toptas.rssconverter

import android.util.Log
import org.xml.sax.InputSource
import javax.xml.parsers.SAXParserFactory

import okhttp3.ResponseBody
import retrofit2.Converter


internal class RssResponseBodyConverter : Converter<ResponseBody, RssFeed> {

    override fun convert(value: ResponseBody): RssFeed {
        val rssFeed = RssFeed()
        try {
            val parser = XMLParser()
            val parserFactory = SAXParserFactory.newInstance()
            val saxParser = parserFactory.newSAXParser()
            val xmlReader = saxParser.xmlReader
            xmlReader.contentHandler = parser
            val inputSource = InputSource(value.charStream())
            try {
                //Log.w("inputSource",inputSource.toString())
                xmlReader.parse(inputSource)
            } catch(e: Exception) {
                Log.e("error",e.message!!)
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