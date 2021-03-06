package com.gymkhana.iitbapp.items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bijoy on 8/5/15.
 */
public class FeedSubscriptionItem implements Serializable {
    public String title, description;
    public String url;
    public String updated;
    public Integer feed_config;
    public List<FeedCategoryItem> categories = new ArrayList<>();

    public String prefKey() {
        return "FEED_PREF" + feed_config;
    }

    public String filename() {
        return "cache_feed_" + feed_config + ".txt";
    }

}
