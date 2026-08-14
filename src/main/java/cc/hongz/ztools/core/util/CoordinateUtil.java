package cc.hongz.ztools.core.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * 坐标系转换相关工具类，主流坐标系包括：.
 * <ul>
 *     <li>WGS84坐标系：即地球坐标系，中国外谷歌地图</li>
 *     <li>GCJ02坐标系：即火星坐标系，高德、腾讯、阿里等使用</li>
 *     <li>BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系。百度、搜狗等使用</li>
 * </ul>
 * <p>
 * 坐标转换相关参考: https://tool.lu/coordinate/<br>
 * 参考：https://github.com/JourWon/coordinate-transform
 *
 * @author hongzhe.qin(qin462328037at163.com), looly
 * @since 5.7.16
 */
public final class CoordinateUtil {

    /**
     * 经度偏移基准常数.
     */
    private static final double TRANS_LNG_BASE = 300.0;
    /**
     * 纬度偏移基准常数.
     */
    private static final double TRANS_LAT_BASE = -100.0;
    /**
     * 变换系数 2.0.
     */
    private static final double TRANS_COEFF_2 = 2.0;
    /**
     * 变换系数 3.0.
     */
    private static final double TRANS_COEFF_3 = 3.0;
    /**
     * 变换系数 0.1.
     */
    private static final double TRANS_COEFF_0_1 = 0.1;
    /**
     * 变换系数 0.2.
     */
    private static final double TRANS_COEFF_0_2 = 0.2;
    /**
     * 正弦振幅 20.0.
     */
    private static final double TRANS_SIN_AMP_20 = 20.0;
    /**
     * 正弦振幅 40.0.
     */
    private static final double TRANS_SIN_AMP_40 = 40.0;
    /**
     * 正弦振幅 150.0.
     */
    private static final double TRANS_SIN_AMP_150 = 150.0;
    /**
     * 正弦振幅 300.0.
     */
    private static final double TRANS_SIN_AMP_300 = 300.0;
    /**
     * 正弦振幅 160.0.
     */
    private static final double TRANS_SIN_AMP_160 = 160.0;
    /**
     * 正弦振幅 320.0.
     */
    private static final double TRANS_SIN_AMP_320 = 320.0;
    /**
     * 正弦频率 6.0.
     */
    private static final double TRANS_SIN_FREQ_6 = 6.0;
    /**
     * 正弦除数 12.0.
     */
    private static final double TRANS_SIN_DIV_12 = 12.0;
    /**
     * 正弦除数 30.0.
     */
    private static final double TRANS_SIN_DIV_30 = 30.0;
    /**
     * 变换缩放因子 (2/3).
     */
    private static final double TRANS_SCALE = 2.0 / 3.0;

    /**
     * 坐标转换参数：π.
     */
    public static final double PI = 3.1415926535897932384626433832795D;
    /**
     * 角度转换常量（度）.
     */
    public static final double DEGREES = 180.0;
    /**
     * BD-09 坐标系转换缩放因子.
     */
    public static final double BD09_SCALE = 3000.0;
    /**
     * 坐标转换参数：（火星坐标系与百度坐标系转换的中间量）.
     */
    public static final double X_PI = PI * BD09_SCALE / DEGREES;
    /**
     * 地球半径（Krasovsky 1940）.
     */
    public static final double RADIUS = 6378245.0D;
    /**
     * 修正参数（偏率ee）.
     */
    public static final double CORRECTION_PARAM = 0.00669342162296594323D;
    /**
     * 全圆角度（度）.
     */
    public static final double FULL_CIRCLE = 360.0;
    /**
     * 直角角度（度）.
     */
    public static final double RIGHT_ANGLE = 90.0;
    /**
     * 墨卡托投影地球半径常数.
     */
    public static final double MERCATOR_EARTH_RADIUS = 20037508.342789244;
    /**
     * BD-09 转换X轴偏移量.
     */
    public static final double BD09_OFFSET_X = 0.0065;
    /**
     * BD-09 转换Y轴偏移量.
     */
    public static final double BD09_OFFSET_Y = 0.006;
    /**
     * BD-09 转换Z项偏移量.
     */
    public static final double BD09_OFFSET_Z = 0.00002;
    /**
     * BD-09 转换theta项偏移量.
     */
    public static final double BD09_OFFSET_THETA = 0.000003;
    /**
     * GCJ-02 参考经度.
     */
    public static final double GCJ02_REF_LNG = 105.0;
    /**
     * GCJ-02 参考纬度.
     */
    public static final double GCJ02_REF_LAT = 35.0;
    /**
     * 中国边界最小经度.
     */
    public static final double CHINA_LNG_MIN = 72.004;
    /**
     * 中国边界最大经度.
     */
    public static final double CHINA_LNG_MAX = 137.8347;
    /**
     * 中国边界最小纬度.
     */
    public static final double CHINA_LAT_MIN = 0.8293;
    /**
     * 中国边界最大纬度.
     */
    public static final double CHINA_LAT_MAX = 55.8271;

    private CoordinateUtil() {
    }

    /**
     * 判断坐标是否在国外.
     * 火星坐标系 (GCJ-02)只对国内有效，国外无需转换
     *
     * @param lng 经度
     * @param lat 纬度
     * @return 坐标是否在国外
     */
    public static boolean outOfChina(final double lng, final double lat) {
        return (lng < CHINA_LNG_MIN || lng > CHINA_LNG_MAX) || (lat < CHINA_LAT_MIN || lat > CHINA_LAT_MAX);
    }

    //----------------------------------------------------------------------------------- WGS84

    /**
     * WGS84 转换为 火星坐标系 (GCJ-02).
     *
     * @param lng 经度值
     * @param lat 纬度值
     * @return 火星坐标 (GCJ-02)
     */
    public static Coordinate wgs84ToGcj02(final double lng, final double lat) {
        return new Coordinate(lng, lat).offset(offset(lng, lat, true));
    }

    /**
     * WGS84 坐标转为 百度坐标系 (BD-09) 坐标.
     *
     * @param lng 经度值
     * @param lat 纬度值
     * @return bd09 坐标
     */
    public static Coordinate wgs84ToBd09(final double lng, final double lat) {
        final Coordinate gcj02 = wgs84ToGcj02(lng, lat); return gcj02ToBd09(gcj02.lng, gcj02.lat);
    }

    //----------------------------------------------------------------------------------- GCJ-02

    /**
     * 火星坐标系 (GCJ-02) 转换为 WGS84.
     *
     * @param lng 经度坐标
     * @param lat 纬度坐标
     * @return WGS84 坐标
     */
    public static Coordinate gcj02ToWgs84(final double lng, final double lat) {
        return new Coordinate(lng, lat).offset(offset(lng, lat, false));
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换.
     *
     * @param lng 经度值
     * @param lat 纬度值
     * @return BD-09 坐标
     */
    public static Coordinate gcj02ToBd09(final double lng, final double lat) {
        double z = Math.sqrt(lng * lng + lat * lat) + BD09_OFFSET_Z * Math.sin(lat * X_PI);
        double theta = Math.atan2(lat, lng) + BD09_OFFSET_THETA * Math.cos(lng * X_PI);
        double bdLng = z * Math.cos(theta) + BD09_OFFSET_X; double bdLat = z * Math.sin(theta) + BD09_OFFSET_Y;
        return new Coordinate(bdLng, bdLat);
    }

    //----------------------------------------------------------------------------------- BD-09

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换.
     * 即 百度 转 谷歌、高德.
     *
     * @param lng 经度值
     * @param lat 纬度值
     * @return GCJ-02 坐标
     */
    public static Coordinate bd09ToGcj02(final double lng, final double lat) {
        double x = lng - BD09_OFFSET_X; double y = lat - BD09_OFFSET_Y;
        double z = Math.sqrt(x * x + y * y) - BD09_OFFSET_Z * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - BD09_OFFSET_THETA * Math.cos(x * X_PI); double ggLng = z * Math.cos(theta);
        double ggLat = z * Math.sin(theta); return new Coordinate(ggLng, ggLat);
    }

    /**
     * 百度坐标系 (BD-09) 与 WGS84 的转换.
     *
     * @param lng 经度值
     * @param lat 纬度值
     * @return WGS84坐标
     */
    public static Coordinate bd09toWgs84(final double lng, final double lat) {
        final Coordinate gcj02 = bd09ToGcj02(lng, lat); return gcj02ToWgs84(gcj02.lng, gcj02.lat);
    }

    /**
     * WGS84 坐标转为 墨卡托投影.
     *
     * @param lng 经度值
     * @param lat 纬度值
     * @return 墨卡托投影
     */
    public static Coordinate wgs84ToMercator(final double lng, final double lat) {
        double x = lng * MERCATOR_EARTH_RADIUS / DEGREES;
        double y = Math.log(Math.tan((RIGHT_ANGLE + lat) * Math.PI / FULL_CIRCLE)) / (Math.PI / DEGREES);
        y = y * MERCATOR_EARTH_RADIUS / DEGREES; return new Coordinate(x, y);
    }

    /**
     * 墨卡托投影 转为 WGS84 坐标.
     *
     * @param mercatorX 墨卡托X坐标
     * @param mercatorY 墨卡托Y坐标
     * @return WGS84 坐标
     */
    public static Coordinate mercatorToWgs84(final double mercatorX, final double mercatorY) {
        double x = mercatorX / MERCATOR_EARTH_RADIUS * DEGREES; double y = mercatorY / MERCATOR_EARTH_RADIUS * DEGREES;
        y = DEGREES / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / DEGREES)) - Math.PI / 2);
        return new Coordinate(x, y);
    }

    //----------------------------------------------------------------------------------- Private methods begin

    /**
     * WGS84 与 火星坐标系 (GCJ-02)转换的偏移算法（非精确）.
     *
     * @param lng    经度值
     * @param lat    纬度值
     * @param isPlus 是否正向偏移：WGS84转GCJ-02使用正向，否则使用反向
     * @return 偏移坐标
     */
    private static Coordinate offset(final double lng, final double lat, final boolean isPlus) {
        double dlng = transLng(lng - GCJ02_REF_LNG, lat - GCJ02_REF_LAT);
        double dlat = transLat(lng - GCJ02_REF_LNG, lat - GCJ02_REF_LAT);

        double magic = Math.sin(lat / DEGREES * PI); magic = 1 - CORRECTION_PARAM * magic * magic;
        final double sqrtMagic = Math.sqrt(magic);

        dlng = (dlng * DEGREES) / (RADIUS / sqrtMagic * Math.cos(lat / DEGREES * PI) * PI);
        dlat = (dlat * DEGREES) / ((RADIUS * (1 - CORRECTION_PARAM)) / (magic * sqrtMagic) * PI);

        if (false == isPlus) {
            dlng = -dlng; dlat = -dlat;
        }

        return new Coordinate(dlng, dlat);
    }

    /**
     * 计算经度坐标.
     *
     * @param lng 经度坐标
     * @param lat 纬度坐标
     * @return ret 计算完成后的
     */
    private static double transLng(final double lng, final double lat) {
        double ret = TRANS_LNG_BASE + lng + TRANS_COEFF_2 * lat
                + TRANS_COEFF_0_1 * lng * lng + TRANS_COEFF_0_1 * lng * lat
                + TRANS_COEFF_0_1 * Math.sqrt(Math.abs(lng));
        ret += (TRANS_SIN_AMP_20 * Math.sin(TRANS_SIN_FREQ_6 * lng * PI)
                + TRANS_SIN_AMP_20 * Math.sin(TRANS_COEFF_2 * lng * PI)) * TRANS_SCALE;
        ret += (TRANS_SIN_AMP_20 * Math.sin(lng * PI)
                + TRANS_SIN_AMP_40 * Math.sin(lng / TRANS_COEFF_3 * PI)) * TRANS_SCALE;
        ret += (TRANS_SIN_AMP_150 * Math.sin(lng / TRANS_SIN_DIV_12 * PI)
                + TRANS_SIN_AMP_300 * Math.sin(lng / TRANS_SIN_DIV_30 * PI)) * TRANS_SCALE;
        return ret;
    }

    /**
     * 计算纬度坐标.
     *
     * @param lng 经度
     * @param lat 纬度
     * @return ret 计算完成后的
     */
    private static double transLat(final double lng, final double lat) {
        double ret = TRANS_LAT_BASE + TRANS_COEFF_2 * lng + TRANS_COEFF_3 * lat
                + TRANS_COEFF_0_2 * lat * lat + TRANS_COEFF_0_1 * lng * lat
                + TRANS_COEFF_0_2 * Math.sqrt(Math.abs(lng));
        ret += (TRANS_SIN_AMP_20 * Math.sin(TRANS_SIN_FREQ_6 * lng * PI)
                + TRANS_SIN_AMP_20 * Math.sin(TRANS_COEFF_2 * lng * PI)) * TRANS_SCALE;
        ret += (TRANS_SIN_AMP_20 * Math.sin(lat * PI)
                + TRANS_SIN_AMP_40 * Math.sin(lat / TRANS_COEFF_3 * PI)) * TRANS_SCALE;
        ret += (TRANS_SIN_AMP_160 * Math.sin(lat / TRANS_SIN_DIV_12 * PI)
                + TRANS_SIN_AMP_320 * Math.sin(lat * PI / TRANS_SIN_DIV_30)) * TRANS_SCALE;
        return ret;
    }

    //----------------------------------------------------------------------------------- Private methods end

    /**
     * 坐标经纬度.
     *
     * @author looly
     */
    public static class Coordinate implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 经度.
         */
        private double lng;

        /**
         * 纬度.
         */
        private double lat;

        /**
         * 构造.
         *
         * @param lng 经度
         * @param lat 纬度
         */
        public Coordinate(final double lng, final double lat) {
            this.lng = lng; this.lat = lat;
        }

        /**
         * 获取经度.
         *
         * @return 经度
         */
        public double getLng() {
            return lng;
        }

        /**
         * 设置经度.
         *
         * @param lngVal 经度
         * @return this
         */
        public Coordinate setLng(final double lngVal) {
            this.lng = lngVal; return this;
        }

        /**
         * 获取纬度.
         *
         * @return 纬度
         */
        public double getLat() {
            return lat;
        }

        /**
         * 设置纬度.
         *
         * @param latVal 纬度
         * @return this
         */
        public Coordinate setLat(final double latVal) {
            this.lat = latVal; return this;
        }

        /**
         * 当前坐标偏移指定坐标.
         *
         * @param offset 偏移量
         * @return this
         */
        public Coordinate offset(final Coordinate offset) {
            this.lng += offset.lng; this.lat += offset.lat; return this;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Coordinate that = (Coordinate) o;
            return Double.compare(that.lng, lng) == 0 && Double.compare(that.lat, lat) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(lng, lat);
        }

        @Override
        public String toString() {
            return "Coordinate{" + "lng=" + lng + ", lat=" + lat + '}';
        }
    }
}
