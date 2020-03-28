package com.example.hesiod.lingdiantgxt.myJavaBean;

import java.util.List;

import lombok.Data;

/**
 * Created by Hesiod on 2019/12/18.
 */
public class TestBean {
    @Data
    public class TestQD {
        String message, nu, ischeck, com, status, condition, state;
        List<Data> data;

        @lombok.Data
        public class Data {
            String time, context, ftime;
        }
    }
    @Data
    public class TestTB{

    }

    public class TestTP{

        /**
         * status : 1
         * info : 获取内容成功
         * data : [{"news_id":"13811","news_title":"深港澳台千里连线，嘉年华会今夏入川","news_summary":"6月17\u201420日，\u201c2016成都深港澳台嘉年华会\u201d(简称嘉年华会)将在成都世纪城国际会展中心举办。其主办方励展华博借力旗","pic_url":"http://f.expoon.com/sub/news/2016/01/21/887844_230x162_0.jpg"},{"news_id":"13810","news_title":"第14届温州国际汽车展4月举行 设9大主题展馆","news_summary":"来自前不久举行的温州国际汽车展览会第一次新闻发布会的消息， 2016第14届温州国际汽车展览会定于4月7-10日在温州国","pic_url":"http://f.expoon.com/sub/news/2016/01/21/580828_230x162_0.jpg"},{"news_id":"13808","news_title":"第十二届中国(南安)国际水暖泵阀交易会 四大亮点","news_summary":"第十二届中国(南安)国际水暖泵阀交易会将于2月10日至12日(即农历正月初三至初五)在成功国际会展中心拉开帷幕。","pic_url":"http://f.expoon.com/sub/news/2016/01/21/745921_230x162_0.jpg"},{"news_id":"13805","news_title":"2016上海灯光音响展 商机无限，一触即发","news_summary":"2016上海国际专业灯光音响展即日起全面启动，海内外高端演艺设备商贸平台，商机无限，一触即发。6大洲，80个国家，25,","pic_url":"http://f.expoon.com/sub/news/2016/01/21/158040_230x162_0.jpg"},{"news_id":"13804","news_title":"第四届南京国际佛事展5月举行","news_summary":"2016年，\u201c第四届南京国际佛事文化用品展览会\u201d将于5月26-29日在南京国际展览中心举办。","pic_url":"http://f.expoon.com/sub/news/2016/01/21/865222_230x162_0.jpg"},{"news_id":"13802","news_title":"上海国际牛仔服装博览会 拓展国际贸易大市场","news_summary":"2016年第三届上海国际牛仔服装博览会将于4月19-21日再次璀璨再现上海世博展览馆，共同探讨牛仔流行趋势，诠释牛仔文化","pic_url":"http://f.expoon.com/sub/news/2016/01/20/370858_230x162_0.jpg"},{"news_id":"13800","news_title":"第三届兰州年货会在甘肃国际会展中心本月19日开幕","news_summary":"由中国商业联合会、甘肃省商业联合会、兰州市商务局主办，甘肃省酒类商品管理局、兰州市城关区商务局、第十四届西安年货会组委会","pic_url":"http://f.expoon.com/sub/news/2016/01/20/868385_230x162_0.jpg"},{"news_id":"13799","news_title":"首届移动拍卖艺术博览会启动","news_summary":"首届移动拍卖博览会已于2016年1月全面启动，由大咖拍卖主办，联合全国艺术机构共同打造拍卖艺术博览会主会场，近百场拍卖专","pic_url":"http://f.expoon.com/sub/news/2016/01/20/768695_230x162_0.jpg"},{"news_id":"13798","news_title":"武汉金融理财投资博览会将在5月举办","news_summary":"由武汉市贸促会、上海《理财周刊》社、湖北好博塔苏斯展览有限公司等单位联合发起的\u201c2016武汉金融理财投资博览会\u201d，将在武","pic_url":"http://f.expoon.com/sub/news/2016/01/20/512947_230x162_0.jpg"},{"news_id":"13796","news_title":"第三届中国微商博览会 3月底济南举办","news_summary":"2015年，沸点天下开创了微商行业第一个展会\u2014\u2014中国微商博览会，并于2015年成功举行两届，让微商展会从无到有，并且起了","pic_url":"http://f.expoon.com/sub/news/2016/01/20/348021_230x162_0.jpg"},{"news_id":"13793","news_title":"2016中国西部国际丝绸博览会","news_summary":"\u201c2016年中国西部国际丝绸博览会\u201d最新确定于2016年5月11日至15日在南充举行。据悉，\u201c丝博会\u201d的会徽、会标及宣传","pic_url":"http://f.expoon.com/sub/news/2016/01/19/113912_230x162_0.jpg"},{"news_id":"13792","news_title":"中国针棉织品交易会开拓\u201c西部市场\u201d","news_summary":"由国家商务部重点支持、中国纺织品商业协会主办的第98届中国针棉织品交易会将于3月15日～17日绽放成都。作为中国国内针棉","pic_url":"http://f.expoon.com/sub/news/2016/01/19/650175_230x162_0.jpg"},{"news_id":"13791","news_title":"乐山市第二十届房地产展示交易会开幕","news_summary":"美丽乐山，生态宜居。今日，乐山市第二十届房地产展示交易会在该市中心城区乐山广场开幕，展会将持续到1月24日。","pic_url":"http://f.expoon.com/sub/news/2016/01/19/321787_230x162_0.jpg"},{"news_id":"13790","news_title":"2016华中屋面与建筑防水技术展3月即将开幕","news_summary":"由湖北省建筑防水协会联合湖南、河南、江西、安徽五省建筑防水协会主办\u201c2016第二届华中屋面与建筑防水技术展览会\u201d将于20","pic_url":"http://f.expoon.com/sub/news/2016/01/19/376254_230x162_0.jpg"},{"news_id":"13789","news_title":"2016海南国际旅游贸易博览会召开新闻发布会","news_summary":"近日，三亚旅游官方网从海南省\u201c首届海博会\u201d新闻发布会上获悉，海南省\u201c首届海博会\u201d将于2016年3月26日至4月1日在三亚","pic_url":"http://f.expoon.com/sub/news/2016/01/19/958046_230x162_0.jpg"},{"news_id":"13788","news_title":"2016阿里巴巴·贵州年货节展销会开幕","news_summary":"\u201c2016阿里巴巴·贵州年货节\u201d的展销会及迎春庙会昨日启动。150多家餐饮商参与的美食节、近千个品种组成的年货展销会等，","pic_url":"http://f.expoon.com/sub/news/2016/01/19/371688_230x162_0.jpg"},{"news_id":"13787","news_title":"第二届中国盆栽花卉交易会\u200b 本月28日开幕","news_summary":"据广州市政府获悉，经中国花卉协会和广州市政府批准，第二届中国盆栽花卉交易会将于本月28日至31日在广州花卉博览园举行。届","pic_url":"http://f.expoon.com/sub/news/2016/01/18/687647_230x162_0.jpg"},{"news_id":"13786","news_title":"李益：视野、品质、融合是展览工程国际化的必由路径","news_summary":"\u201c视野、品质、融合是中国展览工程走向国际化的必由路径。\u201d北京逸格天骄国际展览有限公司副总经理李益日前在第二十二届国际(常","pic_url":"http://f.expoon.com/sub/news/2016/01/18/343556_230x162_0.jpg"},{"news_id":"13785","news_title":"第八届中国国际集成住宅产业博览会将于5月在广州举办","news_summary":"2016年1月14日，第八届中国(广州)国际集成住宅产业博览会暨2016亚太建筑科技论坛\u2014\u2014新闻发布会在广州馆隆重召开。","pic_url":"http://f.expoon.com/sub/news/2016/01/18/581830_230x162_0.jpg"},{"news_id":"13784","news_title":"丝绸之路敦煌国际文化博览会筹备工作进展顺利","news_summary":"近日，丝绸之路(敦煌)国际文化博览会组委会第二次会议在兰召开。会议研究讨论了省直厅局一对一服务保障沿线省区市方案、文博会","pic_url":"http://f.expoon.com/sub/news/2016/01/18/656693_230x162_0.jpg"}]
         */

        private int status;
        private String info;
        private List<DataBean> data;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public class DataBean {
            /**
             * news_id : 13811
             * news_title : 深港澳台千里连线，嘉年华会今夏入川
             * news_summary : 6月17—20日，“2016成都深港澳台嘉年华会”(简称嘉年华会)将在成都世纪城国际会展中心举办。其主办方励展华博借力旗
             * pic_url : http://f.expoon.com/sub/news/2016/01/21/887844_230x162_0.jpg
             */

            private String news_id;
            private String news_title;
            private String news_summary;
            private String pic_url;

            public String getNews_id() {
                return news_id;
            }

            public void setNews_id(String news_id) {
                this.news_id = news_id;
            }

            public String getNews_title() {
                return news_title;
            }

            public void setNews_title(String news_title) {
                this.news_title = news_title;
            }

            public String getNews_summary() {
                return news_summary;
            }

            public void setNews_summary(String news_summary) {
                this.news_summary = news_summary;
            }

            public String getPic_url() {
                return pic_url;
            }

            public void setPic_url(String pic_url) {
                this.pic_url = pic_url;
            }
        }
    }
}
