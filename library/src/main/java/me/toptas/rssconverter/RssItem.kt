package me.toptas.rssconverter

import java.io.Serializable

/**
 * Model for Rss Item
 */
class RssItem : Serializable {

    var title: String? = null
        set(title) {
            field = title?.replace("&#39;", "'")?.replace("&#039;", "'")
        }
    var link: String? = null
        set(link) {
            field = link?.trim { it <= ' ' }
        }
    var image: String? = null
    var publishDate: String? = null
    var description: String? = null
        set(description) {
            if (description?.contains("feedburner")) {
                field = "Click"
            }
        }
    var origLink: String? = null


    override fun toString(): String {
        val builder = StringBuilder()
        if (title != null) {
            builder.append(title).append("\n")
        }
        if (link != null) {
            builder.append(link).append("\n")
        }
        if (image != null) {
            builder.append(image).append("\n")
        }
        if (origLink != null) {
            builder.append(description)
        } else
        if (description != null) {
            builder.append(description)
        }
        return builder.toString()
    }
}
