package com.example.gitdemo.bean;

/*
"suggestion":{
"comf":{
"type":"comf",
"brf":"较 舒适",
"txt":"白天有雨，从而使空气湿度加大，会使人们感觉有点儿闷热，但早晚的天气很凉爽、舒适。"
},
"sport":{
"type":"sport",
"brf":"较不宜",
"txt":"有较强降水，建议您选择在室 内进行健身休闲运动。"
},
"cw":{
"type":"cw",
"brf":"不宜",
"txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"
}
}
 */

public class Suggestion {

    private Comf comf;

    private Sport sport;

    private Cw cw;

    public Comf getComf() {
        return comf;
    }

    public void setComf(Comf comf) {
        this.comf = comf;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Cw getCw() {
        return cw;
    }

    public void setCw(Cw cw) {
        this.cw = cw;
    }

    public abstract class BaseSuggest {

        private String type;

        private String brf;

        private String txt;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBrf() {
            return brf;
        }

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }
    }

    public class Comf extends BaseSuggest {

    }

    public class Sport extends BaseSuggest {

    }

    public class Cw extends BaseSuggest {

    }
}
