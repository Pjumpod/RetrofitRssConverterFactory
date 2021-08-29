package me.toptas.rssconverter

import android.util.Log
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler

/**
 * RSS Feed XML parser
 */
internal class XMLParser : DefaultHandler() {

    private var elementOn = false
    private var parsingTitle = false
    private var parsingDescription = false
    private var parsingLink = false
    private var parsingtmp = false

    private var elementValue: String? = null
    private var title = EMPTY_STRING
    private var link: String? = null
    private var image: String? = null
    private var date: String? = null
    private var description: String? = null

    private var ignorecontent: Boolean? = false
    private var tmpstring: String? = null

    private var rssItem: RssItem? = null
    val items = arrayListOf<RssItem>()


    @Throws(SAXException::class)
    override fun startElement(uri: String, localName: String, qName: String,
                              attributes: Attributes?) {
        elementOn = true
        Log.w("StartElement", localName.lowercase() + " " + ignorecontent.toString())
        when (localName.lowercase()) {
            ITEM, ARTICLES -> if (ignorecontent == false) {rssItem = RssItem()}
            TITLE -> if (!qName.contains(MEDIA)) {
                if (ignorecontent == false) {
                    parsingTitle = true
                    title = EMPTY_STRING
                }
            }
            DESCRIPTION, CONTENT -> {
                if (ignorecontent == false) {
                    parsingDescription = true
                    description = EMPTY_STRING
                }
            }
            LINK, OriginLink, SOURCEURL -> {
                if (ignorecontent == false) {
                    if (qName != ATOM_LINK) {
                        parsingLink = true
                        link = EMPTY_STRING
                    }
                }
            }

            rcmArticle, PUBLISHER, COPYRIGHT -> {
                ignorecontent = true
                tmpstring = EMPTY_STRING
                Log.w("ignoreContent", localName.lowercase() + " true")
                parsingtmp = true
            }
        }

        if (attributes != null) {
            val url = attributes.getValue(URL)
            if (url != null && url.isNotEmpty() && (url.contains("jpg") || url.contains("gif") || url.contains("bmp") || url.contains("png"))) {
                image = url
                Log.w("get image: ", image!!)
            }
        }

        if (localName.lowercase() == "image") {
            Log.w("come here: ", "Yes")
            val href:String? = attributes?.getValue(HREF)
            if (href != null && href.isNotEmpty() && href.contains("jpg") ) {
                image = href
                Log.w("come here too: ", image!!)
            }
        }

    }

    @Throws(SAXException::class)
    override fun endElement(uri: String, localName: String, qName: String) {
        elementOn = false
        Log.w("endElement", localName.lowercase() + " " + ignorecontent.toString())

        when (localName.lowercase()) {
            rcmArticle, PUBLISHER, COPYRIGHT -> {
                ignorecontent = false
                parsingtmp = false
                elementValue = EMPTY_STRING
                tmpstring = EMPTY_STRING
                Log.w("tmpString", tmpstring!!)
                Log.w("ignoreContent", localName.lowercase() + " false")
            }
        }

        if (rssItem != null) {
            when (localName.lowercase()) {
                ITEM, ARTICLES -> {
                    if (ignorecontent == false) {
                        rssItem = RssItem()
                        rssItem?.let {
                            it.title = title.trim { it <= ' ' }
                            it.link = link
                            it.image = image
                            //Log.w("pic_1: ", it.image)
                            it.publishDate = date
                            it.description = description

                            if (image == null && description != null && getImageSourceFromDescription(description) != null) {
                                it.image = getImageSourceFromDescription(description!!)
                            }
                            //Log.w("pic_2: ", it.image)
                            items.add(it)
                        }
                        Log.w("final title: ", title)
                        if (description != null) {
                            Log.w("final desc: ", description!!)
                        }
                        if (link != null) {
                            Log.w("final link: ", link!!)
                        }
                        if (image != null) {
                            Log.w("final image: ", image!!)
                        }
                        link = EMPTY_STRING
                        image = null
                        date = EMPTY_STRING
                    }
                }
                TITLE ->
                    if (ignorecontent == false) {
                        if (!qName.contains(MEDIA)) {
                            parsingTitle = false
                            elementValue = EMPTY_STRING
                            title = removeNewLine(title)
                            Log.w("title: ", title)
                        }
                    }
                LINK, OriginLink, SOURCEURL ->
                    if (ignorecontent == false) {
                        if (elementValue?.isNotEmpty() == true) {
                            parsingLink = false
                            elementValue = EMPTY_STRING
                            link = removeNewLine(link)
                            Log.w("link2", link!!)
                        }
                    }
                IMAGE, URL, THUMBNAIL ->
                    if (ignorecontent == false) {
                        if (elementValue != null && elementValue?.isNotEmpty() == true) {
                            if (elementValue?.lowercase()!!.contains("http")) {
                                image = elementValue
                                Log.w("image2", image!!)
                            }
                        }
                    }
                PUBLISH_DATE, PUBLISH_TIME -> date = elementValue
                DESCRIPTION, CONTENT -> {
                    if (ignorecontent == false) {
                        parsingDescription = false
                        elementValue = EMPTY_STRING
                        Log.w("des2", description!!)
                    }
                }
                SYSBOL -> { Log.w("move on", "gogo")}

            }
        }
    }

    @Throws(SAXException::class)
    override fun characters(ch: CharArray, start: Int, length: Int) {
        val buff = String(ch, start, length)
        if (elementOn) {
            if (buff.length > 2) {
                elementValue = buff
                elementOn = false
            }
        }
        if (parsingTitle) {
            title += buff
        }
        if (parsingDescription) {
            description = description!! + buff
        }
        if (parsingLink) {
            link = link!! + buff
        }
        if (parsingtmp) {
            tmpstring = tmpstring!! + buff
        }
    }


    /**
     * Image is parsed from description if image is null
     *
     * @param description description
     * @return Image url
     */
    private fun getImageSourceFromDescription(description: String?): String? {
        Log.w("see here",description!!)
        if (description.contains("<img") && description.contains("src")) {
            val parts = description.split("src=\"".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            if (parts.size == 2 && parts[1].isNotEmpty()) {
                var src = parts[1].substring(0, parts[1].indexOf("\""))
                val srcParts = src.split("http".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray() // can be removed
                if (srcParts.size > 2) {
                    src = "http" + srcParts[2]
                }
                Log.w("---|",src)
                return src
            }
        }
        
        return null
    }

    private fun removeNewLine(s: String?): String {
        return s?.replace("\n", "") ?: EMPTY_STRING
    }

    companion object {

        private const val EMPTY_STRING = ""
        private const val ITEM = "item"
        private const val TITLE = "title"
        private const val MEDIA = "media"
        private const val DESCRIPTION = "description"
        private const val LINK = "link"
        private const val ATOM_LINK = "atom:link"
        private const val OriginLink = "feedburner:origlink"
        private const val URL = "url"
        private const val HREF = "href"
        //private const val SRC = "src"
        private const val IMAGE = "image"
        private const val PUBLISH_DATE = "pubdate"
        private const val PUBLISH_TIME = "publishTime"
        //private const val CONTENTS = "contents"
        private const val CONTENT = "content"
        private const val SOURCEURL = "sourceurl"
        private const val ARTICLES = "article"
        private const val rcmArticle ="recommendarticles"
        private const val PUBLISHER = "publisher"
        private const val COPYRIGHT = "copyright"
        private const val THUMBNAIL = "thumbnail"
        private const val SYSBOL = "sysbol"
    }
}
