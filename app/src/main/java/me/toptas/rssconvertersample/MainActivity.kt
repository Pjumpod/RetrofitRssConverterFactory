package me.toptas.rssconvertersample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // val URLfeed = "https://www.moneybuffalo.in.th/category/stock/feed"
        //val URLfeed = "http://feeds.feedburner.com/Setorth-Xd"
        val URLfeed = "https://knowledge.bualuang.co.th/article-categories/research/feed/"
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
