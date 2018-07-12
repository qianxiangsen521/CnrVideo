package com.cnr.cnrvideo.fragment.response;


import com.cnr.cnrvideo.fragment.entity.BaseResponse;

import java.util.List;

/**
 * Created by qianxiangsen on 18-5-30.
 */

public class VideoListResponse extends BaseResponse{


    private List<BannerListBean> bannerList;
    private List<VideoListBean> videoList;

    public List<BannerListBean> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerListBean> bannerList) {
        this.bannerList = bannerList;
    }

    public List<VideoListBean> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoListBean> videoList) {
        this.videoList = videoList;
    }

    public static class BannerListBean {
        /**
         * playId : 29
         * playType : 5
         * adType : 0
         * rorder : 1
         * imgUrl : https://img.tea.cnrmobile.com/recommend/941/4a8/5b2089a7272a0.jpg
         * titleText : 视频列表
         * explainText :
         * optionalText1 : http://api.tea.test.cnrmobile.com/program/linkurl/NSQyOSQyODU1ODM5MTczMDIyOUMzRkNFREYyOEQwOTE0Q0QwOCRDTlJMMDEwMDAxJDAkMjg5NTEkMQ==/219aa886f70e4d2f6361d9d2e2f0cf81
         * optionalText2 :
         * chargeUnit : {"needCharge":false,"chargeId":0,"leftFreeSeconds":0,"mPrice":0,"tPrice":0}
         * is_icon : 0,0
         * advName : 视频列表图片
         */

        private int playId;
        private int playType;
        private int adType;
        private int rorder;
        private String imgUrl;
        private String titleText;
        private String explainText;
        private String optionalText1;
        private String optionalText2;
        private ChargeUnitBean chargeUnit;
        private String is_icon;
        private String advName;

        public int getPlayId() {
            return playId;
        }

        public void setPlayId(int playId) {
            this.playId = playId;
        }

        public int getPlayType() {
            return playType;
        }

        public void setPlayType(int playType) {
            this.playType = playType;
        }

        public int getAdType() {
            return adType;
        }

        public void setAdType(int adType) {
            this.adType = adType;
        }

        public int getRorder() {
            return rorder;
        }

        public void setRorder(int rorder) {
            this.rorder = rorder;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getTitleText() {
            return titleText;
        }

        public void setTitleText(String titleText) {
            this.titleText = titleText;
        }

        public String getExplainText() {
            return explainText;
        }

        public void setExplainText(String explainText) {
            this.explainText = explainText;
        }

        public String getOptionalText1() {
            return optionalText1;
        }

        public void setOptionalText1(String optionalText1) {
            this.optionalText1 = optionalText1;
        }

        public String getOptionalText2() {
            return optionalText2;
        }

        public void setOptionalText2(String optionalText2) {
            this.optionalText2 = optionalText2;
        }

        public ChargeUnitBean getChargeUnit() {
            return chargeUnit;
        }

        public void setChargeUnit(ChargeUnitBean chargeUnit) {
            this.chargeUnit = chargeUnit;
        }

        public String getIs_icon() {
            return is_icon;
        }

        public void setIs_icon(String is_icon) {
            this.is_icon = is_icon;
        }

        public String getAdvName() {
            return advName;
        }

        public void setAdvName(String advName) {
            this.advName = advName;
        }

        public static class ChargeUnitBean {
            /**
             * needCharge : false
             * chargeId : 0
             * leftFreeSeconds : 0
             * mPrice : 0
             * tPrice : 0
             */

            private boolean needCharge;
            private int chargeId;
            private int leftFreeSeconds;
            private int mPrice;
            private int tPrice;

            public boolean isNeedCharge() {
                return needCharge;
            }

            public void setNeedCharge(boolean needCharge) {
                this.needCharge = needCharge;
            }

            public int getChargeId() {
                return chargeId;
            }

            public void setChargeId(int chargeId) {
                this.chargeId = chargeId;
            }

            public int getLeftFreeSeconds() {
                return leftFreeSeconds;
            }

            public void setLeftFreeSeconds(int leftFreeSeconds) {
                this.leftFreeSeconds = leftFreeSeconds;
            }

            public int getMPrice() {
                return mPrice;
            }

            public void setMPrice(int mPrice) {
                this.mPrice = mPrice;
            }

            public int getTPrice() {
                return tPrice;
            }

            public void setTPrice(int tPrice) {
                this.tPrice = tPrice;
            }
        }
    }

    public static class VideoListBean {
        /**
         * playId : 4
         * playType : 9
         * titleText : 万里茶道
         * explainText :
         * imgUrl : https://img.tea.cnrmobile.com/photo/056/4d4/5b1f3a644bdf3.png
         * duration : 0:40
         * intro : 万里茶道
         * playTimes : 1
         */

        private int playId;
        private int playType;
        private String titleText;
        private String explainText;
        private String imgUrl;
        private String duration;
        private String intro;
        private int playTimes;

        public int getPlayId() {
            return playId;
        }

        public void setPlayId(int playId) {
            this.playId = playId;
        }

        public int getPlayType() {
            return playType;
        }

        public void setPlayType(int playType) {
            this.playType = playType;
        }

        public String getTitleText() {
            return titleText;
        }

        public void setTitleText(String titleText) {
            this.titleText = titleText;
        }

        public String getExplainText() {
            return explainText;
        }

        public void setExplainText(String explainText) {
            this.explainText = explainText;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getPlayTimes() {
            return playTimes;
        }

        public void setPlayTimes(int playTimes) {
            this.playTimes = playTimes;
        }
    }
}
