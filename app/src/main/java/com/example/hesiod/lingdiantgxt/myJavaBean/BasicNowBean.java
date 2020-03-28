package com.example.hesiod.lingdiantgxt.myJavaBean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by Hesiod on 2019/12/18.
 */

@Data
public class BasicNowBean {
    private List<BasicNow> HeWeather6 = new ArrayList<>();

    @Data
    public class BasicNow {
        private basic basic;
        private update update;
        private String status;
        private now now;
    }

    @Data
    public class basic {
        String cid, location, parent_city, admin_area, cnty, lat, lon, tz;
    }

    @Data
    public class update {
        String loc, utc;
    }

    @Data
    public class now {
        private String fl, tmp, cond_code, cond_txt, hum, pcpn, pres, vis;
        private String cloud, wind_deg, wind_dir, wind_sc, wind_spd;
    }
}
