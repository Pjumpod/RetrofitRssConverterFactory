package me.toptas.rssconvertersample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //var URLfeed = "https://www.moneybuffalo.in.th/category/%E0%B8%AB%E0%B8%B8%E0%B9%89%E0%B8%99/feed"
        //var URLfeed = "http://feeds.feedburner.com/Setorth-Xd"
        var URLfeed = "http://news.thaipbs.or.th/rss/news/economy"
        supportFragmentManager
                .beginTransaction()
                .add(R.id.rlContainer, RssFragment.newInstance( URLfeed))
                //.add(R.id.rlContainer, RssFragment.newInstance("http://feeds.feedburner.com/Setorth-Xd"))
                //.add(R.id.rlContainer, RssFragment.newInstance("http://www.maoinvestor.com/feed"))
                //.add(R.id.rlContainer, RssFragment.newInstance("http://knowledge.bualuang.co.th/article-categories/investmentsustainable/feed/"))
                //.add(R.id.rlContainer, RssFragment.newInstance("https://www.stock2morrow.com/feed/"))
                //.add(R.id.rlContainer, RssFragment.newInstance("http://feeds.feedburner.com/settrade/saaDailyUpdate"))
                .commit()


    }
}
