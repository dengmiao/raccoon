package com.cure.captcha.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @title: CaptchaProperties
 * @description: 验证码配置
 * @author: dengmiao
 * @create: 2019-05-23 17:26
 **/
@Data
@ConfigurationProperties(prefix = CaptchaProperties.PREFIX)
public class CaptchaProperties {

    public static final String PREFIX = "cure.captcha";

    /**
     * 图片验证码配置
     */
    private Image image = new Image();

    /**
     * 短信验证码配置
     */
    private Sms sms = new Sms();

    /**
     * 图形验证码
     */
    @Data
    public static class Image {
        /**
         * 验证码过期时间(分)
         */
        private Integer expire = 10;
        /**
         * 图片宽度
         */
        private Integer width = 125;
        /**
         * 图片高度
         */
        private Integer height = 45;
        /**
         * 图片样式
         */
        private ImageStyle style = ImageStyle.WATER_RIPPLE;
        /**
         * 是否显示图片边框. yes/no
         */
        private YesNo border = YesNo.NO;
        /**
         * 边框颜色
         */
        private String borderColor = "black";
        /**
         * 边框厚度
         */
        private Integer borderThickness = 1;
        /**
         * 验证码字符来源，验证码值从中获取
         */
        private String charSource = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        /**
         * 验证码字符长度
         */
        private Integer charLength = 4;
        /**
         * 文字之间的间隔. 大于0的数值
         */
        private Integer charSpace = 2;
        /**
         * 字体
         */
        private String fontNames = "Arial,Courier";
        /**
         * 字体大小
         */
        private Integer fontSize = 40;
        /**
         * 字体颜色
         */
        private String fontColor = "blue";
        /**
         * 干扰线颜色
         */
        private String noiseColor = "blue";
        /**
         * 背景颜色渐变，开始颜色
         */
        private String backgroundColorFrom = "lightGray";
        /**
         * 背景颜色渐变， 结束颜色
         */
        private String backgroundColorTo = "lightGray";

        enum YesNo {
            /**
             * yes
             */
            YES("yes"),
            /**
             * no
             */
            NO("no");

            private String value;

            YesNo(String value) {
                this.value = value;
            }

            public String value() {
                return value;
            }

        }

        enum ImageStyle {

            /**
             * 水纹
             */
            WATER_RIPPLE("com.google.code.kaptcha.impl.WaterRipple"),
            /**
             * 鱼眼
             */
            FISH_EYE("com.google.code.kaptcha.impl.FishEyeGimpy"),
            /**
             * 阴影
             */
            SHADOW("com.google.code.kaptcha.impl.ShadowGimpy");

            private String value;

            ImageStyle (String value) {
                this.value = value;
            }

            public String value() {
                return value;
            }
        }
    }

    /**
     * 短信验证码
     */
    @Data
    public static class Sms {
        /**
         * 验证码过期时间(分)
         */
        private Integer expire = 5;
        /**
         * 验证码字符来源，验证码值从中获取
         */
        private String charSource = "0123456789";
        /**
         * 验证码字符长度
         */
        private Integer charLength = 6;
        /**
         * 发送验证码间隔时间(秒)
         */
        private Integer interval = 60;
        /**
         * 限制时间内发送次数上限(-1则无限制)
         */
        private Integer limitTime = -1;
        /**
         * 次数限制在多长时间内(小时)
         */
        private Integer limitInterval = 12;
    }
}
