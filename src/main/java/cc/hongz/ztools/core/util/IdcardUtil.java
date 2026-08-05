package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.date.DateTime;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.lang.Validator;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 身份证相关工具类.<br>
 * see <a href="https://www.oschina.net/code/snippet_1611_2881">https://www.oschina.net/code/snippet_1611_2881</a>
 *
 * <p>
 * 本工具并没有对行政区划代码做校验，如有需求，请参阅（2018年10月）：
 * <a href="http://www.mca.gov.cn/article/sj/xzqh/2018/201804-12/20181011221630.html">http://www.mca.gov.cn/article/sj/xzqh/2018/201804-12/20181011221630.html</a>
 * </p>
 *
 * @author Looly
 * @since 3.0.4
 */
public final class IdcardUtil {

    /**
     * 中国公民身份证号码最小长度.
     */
    private static final int CHINA_ID_MIN_LENGTH = 15;
    /**
     * 中国公民身份证号码最大长度.
     */
    private static final int CHINA_ID_MAX_LENGTH = 18;
    /**
     * 每位加权因子.
     */
    private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    /**
     * 数字正则.
     */
    private static final Pattern NUMBERS = Pattern.compile("\\d+");
    /**
     * 纯日期格式.
     */
    private static final String PURE_DATE_FORMAT = "yyyyMMdd";
    /**
     * 出生日期起始位置（0-based）.
     */
    private static final int BIRTH_START_INDEX = 6;
    /**
     * 15位身份证年份结束位置.
     */
    private static final int YEAR_END_INDEX_15 = 12;
    /**
     * 18位身份证出生日期结束位置.
     */
    private static final int BIRTH_END_INDEX_18 = 14;
    /**
     * 18位身份证校验位位置.
     */
    private static final int CHECK_CODE_INDEX = 17;
    /**
     * 2位年份转4位的世纪分界.
     */
    private static final int CENTURY_THRESHOLD = 2000;
    /**
     * 2位年份世纪修正值.
     */
    private static final int CENTURY_OFFSET = 100;
    /**
     * 身份证号省市代码长度.
     */
    private static final int PROVINCE_CODE_LENGTH = 2;
    /**
     * 台湾身份证长度.
     */
    private static final int TW_ID_LENGTH = 10;
    /**
     * 香港身份证校验和基数（单字母开头）.
     */
    private static final int HK_SUM_BASE_SINGLE = 522;
    /**
     * 校验和模数.
     */
    private static final int CHECKSUM_MOD = 11;
    /**
     * 性别位位置（18位身份证）.
     */
    private static final int GENDER_INDEX_18 = 16;
    /**
     * 15位身份证出生月日起始位置.
     */
    private static final int BIRTH_MD_START_15 = 8;
    /**
     * 港澳身份证数字部分最小长度.
     */
    private static final int HK_MACAU_MIN_LENGTH = 8;
    /**
     * 港澳身份证数字部分最大长度.
     */
    private static final int HK_MACAU_MAX_LENGTH = 9;
    /**
     * 省会代码表.
     */
    private static final Map<String, String> CITY_CODES = new HashMap<>();
    /**
     * 台湾身份首字母对应数字.
     */
    private static final Map<Character, Integer> TW_FIRST_CODE = new HashMap<>();

    private IdcardUtil() {
    }

    static {
        CITY_CODES.put("11", "北京");
        CITY_CODES.put("12", "天津");
        CITY_CODES.put("13", "河北");
        CITY_CODES.put("14", "山西");
        CITY_CODES.put("15", "内蒙古");
        CITY_CODES.put("21", "辽宁");
        CITY_CODES.put("22", "吉林");
        CITY_CODES.put("23", "黑龙江");
        CITY_CODES.put("31", "上海");
        CITY_CODES.put("32", "江苏");
        CITY_CODES.put("33", "浙江");
        CITY_CODES.put("34", "安徽");
        CITY_CODES.put("35", "福建");
        CITY_CODES.put("36", "江西");
        CITY_CODES.put("37", "山东");
        CITY_CODES.put("41", "河南");
        CITY_CODES.put("42", "湖北");
        CITY_CODES.put("43", "湖南");
        CITY_CODES.put("44", "广东");
        CITY_CODES.put("45", "广西");
        CITY_CODES.put("46", "海南");
        CITY_CODES.put("50", "重庆");
        CITY_CODES.put("51", "四川");
        CITY_CODES.put("52", "贵州");
        CITY_CODES.put("53", "云南");
        CITY_CODES.put("54", "西藏");
        CITY_CODES.put("61", "陕西");
        CITY_CODES.put("62", "甘肃");
        CITY_CODES.put("63", "青海");
        CITY_CODES.put("64", "宁夏");
        CITY_CODES.put("65", "新疆");
        CITY_CODES.put("71", "台湾");
        CITY_CODES.put("81", "香港");
        CITY_CODES.put("82", "澳门");
        //issue#1277，台湾身份证号码以83开头，但是行政区划为71
        CITY_CODES.put("83", "台湾");
        CITY_CODES.put("91", "国外");

        TW_FIRST_CODE.put('A', 10);
        TW_FIRST_CODE.put('B', 11);
        TW_FIRST_CODE.put('C', 12);
        TW_FIRST_CODE.put('D', 13);
        TW_FIRST_CODE.put('E', 14);
        TW_FIRST_CODE.put('F', 15);
        TW_FIRST_CODE.put('G', 16);
        TW_FIRST_CODE.put('H', 17);
        TW_FIRST_CODE.put('J', 18);
        TW_FIRST_CODE.put('K', 19);
        TW_FIRST_CODE.put('L', 20);
        TW_FIRST_CODE.put('M', 21);
        TW_FIRST_CODE.put('N', 22);
        TW_FIRST_CODE.put('P', 23);
        TW_FIRST_CODE.put('Q', 24);
        TW_FIRST_CODE.put('R', 25);
        TW_FIRST_CODE.put('S', 26);
        TW_FIRST_CODE.put('T', 27);
        TW_FIRST_CODE.put('U', 28);
        TW_FIRST_CODE.put('V', 29);
        TW_FIRST_CODE.put('X', 30);
        TW_FIRST_CODE.put('Y', 31);
        TW_FIRST_CODE.put('W', 32);
        TW_FIRST_CODE.put('Z', 33);
        TW_FIRST_CODE.put('I', 34);
        TW_FIRST_CODE.put('O', 35);
    }

    /**
     * 将15位身份证号码转换为18位.
     *
     * @param idCard 15位身份编码
     * @return 18位身份编码
     */
    public static String convert15To18(final String idCard) {
        StringBuilder idCard18;
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return null;
        }
        if (NUMBERS.matcher(idCard).matches()) {
            // 获取出生年月日
            String birthday = idCard.substring(BIRTH_START_INDEX, YEAR_END_INDEX_15);
            Date birthDate = DateUtil.parse(birthday, "yyMMdd");
            // 获取出生年(完全表现形式,如：2010)
            int sYear = DateUtil.year(birthDate);
            if (sYear > CENTURY_THRESHOLD) {
                // 2000年之后不存在15位身份证号，此处用于修复此问题的判断
                sYear -= CENTURY_OFFSET;
            }
            idCard18 = StrUtil.builder()
                    .append(idCard, 0, BIRTH_START_INDEX)
                    .append(sYear)
                    .append(idCard.substring(BIRTH_MD_START_15));
            // 获取校验位
            char sVal = getCheckCode18(idCard18.toString());
            idCard18.append(sVal);
        } else {
            return null;
        }
        return idCard18.toString();
    }

    /**
     * 将18位身份证号码转换为15位.
     *
     * @param  idCard 18位身份编码
     * @return 15位身份编码
     */
    public static String convert18To15(final String idCard) {
        if (StrUtil.isNotBlank(idCard) && IdcardUtil.isValidCard18(idCard)) {
            return idCard.substring(0, BIRTH_START_INDEX) + idCard.substring(BIRTH_MD_START_15, CHECK_CODE_INDEX);
        }
        return idCard;
    }

    /**
     * 是否有效身份证号，忽略X的大小写.<br>
     * 如果身份证号码中含有空格始终返回{@code false}
     *
     * @param idCard 身份证号，支持18位、15位和港澳台的10位
     * @return 是否有效
     */
    public static boolean isValidCard(final String idCard) {
        if (StrUtil.isBlank(idCard)) {
            return false;
        }

        //idCard = idCard.trim();
        int length = idCard.length();
        switch (length) {
            case CHINA_ID_MAX_LENGTH:// 18位身份证
                return isValidCard18(idCard);
            case CHINA_ID_MIN_LENGTH:// 15位身份证
                return isValidCard15(idCard);
            case TW_ID_LENGTH: { // 10位身份证，港澳台地区
                String[] cardVal = isValidCard10(idCard);
                return null != cardVal && "true".equals(cardVal[2]);
            }
            default:
                return false;
        }
    }

    /**
     * 判断18位身份证的合法性.
     *
     * @param idcard 待验证的身份证
     * @return 是否有效的18位身份证，忽略x的大小写
     */
    public static boolean isValidCard18(final String idcard) {
        return isValidCard18(idcard, true);
    }

    /**
     * 判断18位身份证的合法性.
     *
     * @param idcard     待验证的身份证
     * @param ignoreCase 是否忽略大小写。{@code true}则忽略X大小写，否则严格匹配大写。
     * @return 是否有效的18位身份证
     * @since 5.5.7
     */
    public static boolean isValidCard18(final String idcard, final boolean ignoreCase) {
        if (idcard == null) {
            return false;
        }
        if (CHINA_ID_MAX_LENGTH != idcard.length()) {
            return false;
        }

        // 截取省份代码。新版外国人永久居留身份证以9开头，第二三位是受理地代码
        final String proCode = idcard.startsWith("9")
                ? idcard.substring(1, 3) : idcard.substring(0, PROVINCE_CODE_LENGTH);
        if (null == CITY_CODES.get(proCode)) {
            return false;
        }

        //校验生日
        if (false == Validator.isBirthday(idcard.substring(BIRTH_START_INDEX, BIRTH_END_INDEX_18))) {
            return false;
        }

        // 前17位
        final String code17 = idcard.substring(0, CHECK_CODE_INDEX);
        if (NUMBERS.matcher(code17).matches()) {
            // 获取校验位
            char val = getCheckCode18(code17);
            // 第18位
            return CharUtil.equals(val, idcard.charAt(CHECK_CODE_INDEX), ignoreCase);
        }
        return false;
    }

    /**
     * 验证15位身份编码是否合法.
     *
     * @param idcard 身份编码
     * @return 是否合法
     */
    public static boolean isValidCard15(final String idcard) {
        if (idcard == null) {
            return false;
        }
        if (CHINA_ID_MIN_LENGTH != idcard.length()) {
            return false;
        }
        if (NUMBERS.matcher(idcard).matches()) {
            // 省份
            String proCode = idcard.substring(0, PROVINCE_CODE_LENGTH);
            if (null == CITY_CODES.get(proCode)) {
                return false;
            }

            //校验生日（两位年份，补充为19XX）
            return false != Validator.isBirthday("19" + idcard.substring(BIRTH_START_INDEX, YEAR_END_INDEX_15));
        } else {
            return false;
        }
    }

    /**
     * 验证10位身份编码是否合法.
     *
     * @param idcard 身份编码
     * @return 身份证信息数组
     * <p>
     * [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false) 若不是身份证件号码则返回null
     * </p>
     */
    public static String[] isValidCard10(final String idcard) {
        if (StrUtil.isBlank(idcard)) {
            return null;
        }

        // issue#IBP6T1 中文空格替换为英文
        String idcardNorm = StrUtil.replace(idcard, "（", "(");
        idcardNorm = StrUtil.replace(idcardNorm, "）", ")");

        String[] info = new String[3];
        String card = idcardNorm.replaceAll("[()]", "");
        if (card.length() != HK_MACAU_MIN_LENGTH && card.length() != HK_MACAU_MAX_LENGTH
                && idcardNorm.length() != TW_ID_LENGTH) {
            return null;
        }
        if (idcardNorm.matches("^[a-zA-Z][0-9]{9}$")) { // 台湾
            info[0] = "台湾";
            char char2 = idcardNorm.charAt(1);
            if ('1' == char2) {
                info[1] = "M";
            } else if ('2' == char2) {
                info[1] = "F";
            } else {
                info[1] = "N";
                info[2] = "false";
                return info;
            }
            info[2] = isValidTWCard(idcardNorm) ? "true" : "false";
        } else if (idcardNorm.matches("^[157][0-9]{6}\\(?[0-9A-Z]\\)?$")) { // 澳门
            info[0] = "澳门";
            info[1] = "N";
            info[2] = "true";
        } else if (idcardNorm.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { // 香港
            info[0] = "香港";
            info[1] = "N";
            info[2] = isValidHKCard(idcardNorm) ? "true" : "false";
        } else {
            return null;
        }
        return info;
    }

    /**
     * 验证台湾身份证号码.
     *
     * @param idcard 身份证号码
     * @return 验证码是否符合
     */
    public static boolean isValidTWCard(final String idcard) {
        if (null == idcard || idcard.length() != TW_ID_LENGTH) {
            return false;
        }
        final Integer iStart = TW_FIRST_CODE.get(idcard.charAt(0));
        if (null == iStart) {
            return false;
        }
        int sum = iStart / 10 + (iStart % 10) * 9;

        final String mid = idcard.substring(1, 9);
        final char[] chars = mid.toCharArray();
        int iflag = 8;
        for (char c : chars) {
            sum += Integer.parseInt(String.valueOf(c)) * iflag;
            iflag--;
        }

        final String end = idcard.substring(9, 10);
        return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.parseInt(end);
    }

    /**
     * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查).
     *
     * @param idcard 身份证号码
     * @return 验证码是否符合
     */
    public static boolean isValidHKCard(final String idcard) {
        if (false == idcard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) {
            return false;
        }

        String card = idcard.replaceAll("[()]", "");
        int sum;
        if (card.length() == 9) {
            sum = (Character.toUpperCase(card.charAt(0)) - 55) * 9 + (Character.toUpperCase(card.charAt(1)) - 55) * 8;
            card = card.substring(1, 9);
        } else {
            sum = HK_SUM_BASE_SINGLE + (Character.toUpperCase(card.charAt(0)) - 55) * 8;
        }

        // 首字母A-Z，A表示1，以此类推
        String mid = card.substring(1, 7);
        String end = card.substring(7, 8);
        char[] chars = mid.toCharArray();
        int iflag = 7;
        for (char c : chars) {
            sum = sum + Integer.parseInt(String.valueOf(c)) * iflag;
            iflag--;
        }
        if ("A".equalsIgnoreCase(end)) {
            sum += 10;
        } else {
            sum += Integer.parseInt(end);
        }
        return sum % CHECKSUM_MOD == 0;
    }

    /**
     * 根据身份编号获取生日，只支持15或18位身份证号码.
     *
     * @param idcard 身份编号
     * @return 生日(yyyyMMdd)
     * @see #getBirth(String)
     */
    public static String getBirthByIdCard(final String idcard) {
        return getBirth(idcard);
    }

    /**
     * 根据身份编号获取生日，只支持15或18位身份证号码.
     *
     * @param idCard 身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getBirth(final String idCard) {
        Assert.notBlank(idCard, "id card must be not blank!");
        final int len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        }
        final String idCard18 = len == CHINA_ID_MIN_LENGTH ? convert15To18(idCard) : idCard;
        return Objects.requireNonNull(idCard18).substring(BIRTH_START_INDEX, BIRTH_END_INDEX_18);
    }

    /**
     * 从身份证号码中获取生日日期，只支持15或18位身份证号码.
     *
     * @param idCard 身份证号码
     * @return 日期
     */
    public static DateTime getBirthDate(final String idCard) {
        final String birthByIdCard = getBirthByIdCard(idCard);
        return null == birthByIdCard ? null : DateUtil.parse(birthByIdCard, PURE_DATE_FORMAT);
    }

    /**
     * 根据身份编号获取年龄，只支持15或18位身份证号码.
     *
     * @param idcard 身份编号
     * @return 年龄
     */
    public static int getAgeByIdCard(final String idcard) {
        return getAgeByIdCard(idcard, DateUtil.date());
    }

    /**
     * 根据身份编号获取指定日期当时的年龄年龄，只支持15或18位身份证号码.
     *
     * @param idcard        身份编号
     * @param dateToCompare 以此日期为界，计算年龄。
     * @return 年龄
     */
    public static int getAgeByIdCard(final String idcard, final Date dateToCompare) {
        String birth = getBirthByIdCard(idcard);
        return DateUtil.age(DateUtil.parse(birth, "yyyyMMdd"), dateToCompare);
    }

    /**
     * 根据身份编号获取生日年，只支持15或18位身份证号码.
     *
     * @param idcard 身份编号
     * @return 生日(yyyy)
     */
    public static Short getYearByIdCard(final String idcard) {
        final int len = idcard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        }
        final String idcard18 = len == CHINA_ID_MIN_LENGTH ? convert15To18(idcard) : idcard;
        return Short.valueOf(Objects.requireNonNull(idcard18).substring(BIRTH_START_INDEX, BIRTH_START_INDEX + 4));
    }

    /**
     * 根据身份编号获取生日月，只支持15或18位身份证号码.
     *
     * @param idcard 身份编号
     * @return 生日(MM)
     */
    public static Short getMonthByIdCard(final String idcard) {
        final int len = idcard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        }
        final String idcard18 = len == CHINA_ID_MIN_LENGTH ? convert15To18(idcard) : idcard;
        return Short.valueOf(Objects.requireNonNull(idcard18).substring(BIRTH_START_INDEX + 4, BIRTH_END_INDEX_18 - 2));
    }

    /**
     * 根据身份编号获取生日天，只支持15或18位身份证号码.
     *
     * @param idcard 身份编号
     * @return 生日(dd)
     */
    public static Short getDayByIdCard(final String idcard) {
        final int len = idcard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        }
        final String idcard18 = len == CHINA_ID_MIN_LENGTH ? convert15To18(idcard) : idcard;
        return Short.valueOf(Objects.requireNonNull(idcard18).substring(BIRTH_END_INDEX_18 - 2, BIRTH_END_INDEX_18));
    }

    /**
     * 根据身份编号获取性别，只支持15或18位身份证号码.
     *
     * @param idcard 身份编号
     * @return 性别(1 : 男 ， 0 : 女)
     */
    public static int getGenderByIdCard(final String idcard) {
        Assert.notBlank(idcard);
        final int len = idcard.length();
        if (!(len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH)) {
            throw new IllegalArgumentException("ID Card length must be 15 or 18");
        }

        final String idcard18 = len == CHINA_ID_MIN_LENGTH ? convert15To18(idcard) : idcard;
        char sCardChar = Objects.requireNonNull(idcard18).charAt(GENDER_INDEX_18);
        return (sCardChar % 2 != 0) ? 1 : 0;
    }

    /**
     * 根据身份编号获取户籍省份编码，只支持15或18位身份证号码.
     *
     * @param idcard 身份编码
     * @return 省份编码
     * @since 5.7.2
     */
    public static String getProvinceCodeByIdCard(final String idcard) {
        int len = idcard.length();
        if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
            return idcard.substring(0, PROVINCE_CODE_LENGTH);
        }
        return null;
    }

    /**
     * 根据身份编号获取户籍省份，只支持15或18位身份证号码.
     *
     * @param idcard 身份编码
     * @return 省份名称。
     */
    public static String getProvinceByIdCard(final String idcard) {
        final String code = getProvinceCodeByIdCard(idcard);
        if (StrUtil.isNotBlank(code)) {
            return CITY_CODES.get(code);
        }
        return null;
    }

    /**
     * 根据身份编号获取地市级编码，只支持15或18位身份证号码.<br>
     * 获取编码为4位
     *
     * @param idcard 身份编码
     * @return 地市级编码
     */
    public static String getCityCodeByIdCard(final String idcard) {
        int len = idcard.length();
        if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
            return idcard.substring(0, 4);
        }
        return null;
    }

    /**
     * 根据身份编号获取区县级编码，只支持15或18位身份证号码.<br>
     * 获取编码为6位
     *
     * @param idcard 身份编码
     * @return 地市级编码
     * @since 5.8.0
     */
    public static String getDistrictCodeByIdCard(final String idcard) {
        int len = idcard.length();
        if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
            return idcard.substring(0, BIRTH_START_INDEX);
        }
        return null;
    }

    /**
     * 隐藏指定位置的几个身份证号数字为"*".
     *
     * @param idcard       身份证号
     * @param startInclude 开始位置（包含）
     * @param endExclude   结束位置（不包含）
     * @return 隐藏后的身份证号码
     * @see StrUtil#hide(CharSequence, int, int)
     * @since 3.2.2
     */
    public static String hide(final String idcard, final int startInclude, final int endExclude) {
        return StrUtil.hide(idcard, startInclude, endExclude);
    }

    /**
     * 获取身份证信息，包括身份、城市代码、生日、性别等.
     *
     * @param idcard 15或18位身份证
     * @return {@link Idcard}
     * @since 5.4.3
     */
    public static Idcard getIdcardInfo(final String idcard) {
        return new Idcard(idcard);
    }

    // ----------------------------------------------------------------------------------- Private method start

    /**
     * 获得18位身份证校验码.
     *
     * @param code17 18位身份证号中的前17位
     * @return 第18位
     */
    private static char getCheckCode18(final String code17) {
        int sum = getPowerSum(code17.toCharArray());
        return getCheckCode18(sum);
    }

    /**
     * 将power和值与11取模获得余数进行校验码判断.
     *
     * @param iSum 加权和
     * @return 校验位
     */
    private static char getCheckCode18(final int iSum) {
        switch (iSum % 11) {
            case 10:
                return '2';
            case 9:
                return '3';
            case 8:
                return '4';
            case 7:
                return '5';
            case 6:
                return '6';
            case 5:
                return '7';
            case 4:
                return '8';
            case 3:
                return '9';
            case 2:
                return 'X';
            case 1:
                return '0';
            case 0:
                return '1';
            default:
                return CharUtil.SPACE;
        }
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值.
     *
     * @param iArr 身份证号码的数组
     * @return 身份证编码
     */
    private static int getPowerSum(final char[] iArr) {
        int iSum = 0;
        if (POWER.length == iArr.length) {
            for (int i = 0; i < iArr.length; i++) {
                iSum += Integer.parseInt(String.valueOf(iArr[i])) * POWER[i];
            }
        }
        return iSum;
    }
    // ----------------------------------------------------------------------------------- Private method end

    /**
     * 身份证信息，包括身份、城市代码、生日、性别等.
     *
     * @author looly
     * @since 5.4.3
     */
    public static class Idcard implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String provinceCode;
        private final String cityCode;
        private final DateTime birthDate;
        private final Integer gender;
        private final int age;

        /**
         * 构造.
         *
         * @param idcard 身份证号码
         */
        public Idcard(final String idcard) {
            this.provinceCode = IdcardUtil.getProvinceCodeByIdCard(idcard);
            this.cityCode = IdcardUtil.getCityCodeByIdCard(idcard);
            this.birthDate = IdcardUtil.getBirthDate(idcard);
            this.gender = IdcardUtil.getGenderByIdCard(idcard);
            this.age = IdcardUtil.getAgeByIdCard(idcard);
        }

        public String getProvinceCode() {
            return this.provinceCode;
        }

        public String getProvince() {
            return CITY_CODES.get(this.provinceCode);
        }

        public String getCityCode() {
            return this.cityCode;
        }

        public DateTime getBirthDate() {
            return this.birthDate;
        }

        public Integer getGender() {
            return this.gender;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Idcard{" +
                    "provinceCode='" + provinceCode + '\'' +
                    ", cityCode='" + cityCode + '\'' +
                    ", birthDate=" + birthDate +
                    ", gender=" + gender +
                    ", age=" + age +
                    '}';
        }
    }
}
