package me.toptas.rssconvertersample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.rlContainer, RssFragment.newInstance("http://www.maoinvestor.com/feed"))
                //.add(R.id.rlContainer, RssFragment.newInstance("http://knowledge.bualuang.co.th/article-categories/investmentsustainable/feed/"))
                //.add(R.id.rlContainer, RssFragment.newInstance("https://www.stock2morrow.com/feed/"))
                //.add(R.id.rlContainer, RssFragment.newInstance("http://feeds.feedburner.com/settrade/saaDailyUpdate"))
                .commit()


    }
}
