package cc.hongz.ztools.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 统一社会信用代码（GB32100-2015）工具类.<br>
 * 标准见：https://www.cods.org.cn/c/2020-10-29/12575.html
 *
 * <pre>
 * 第一部分：登记管理部门代码1位 (数字或大写英文字母)
 * 第二部分：机构类别代码1位 (数字或大写英文字母)
 * 第三部分：登记管理机关行政区划码6位 (数字)
 * 第四部分：主体标识码（组织机构代码）9位 (数字或大写英文字母)
 * 第五部分：校验码1位 (数字或大写英文字母)
 * </pre>
 *
 * @author looly
 * @since 5.2.4
 */
public final class CreditCodeUtil {

    /** 统一社会信用代码长度. */
    private static final int CREDIT_CODE_LENGTH = 18;
    /** 主体标识码起始位置. */
    private static final int ORG_CODE_START = 8;
    /** 校验码位置. */
    private static final int CHECK_CODE_POS = 17;
    /** 校验和模数. */
    private static final int CHECKSUM_MOD = 31;
    /** 数字字符数量. */
    private static final int DIGIT_COUNT = 10;
    /** 登记部门代码长度. */
    private static final int REG_DEPT_LEN = 2;
    /** 行政区划码长度. */
    private static final int ADMIN_DIV_LEN = 6;

    /**
     * 统一社会信用代码正则.<br>
     * ^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$
     */
    public static final Pattern CREDIT_CODE_PATTERN =
            Pattern.compile("^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$");

    /**
     * 加权因子.
     */
    private static final int[] WEIGHT = {1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28};

    /**
     * 代码字符集.
     */
    private static final char[] BASE_CODE_ARRAY = "0123456789ABCDEFGHJKLMNPQRTUWXY".toCharArray();

    /**
     * 代码字符到索引的映射.
     */
    private static final Map<Character, Integer> CODE_INDEX_MAP;

    private CreditCodeUtil() {
    }

    static {
        CODE_INDEX_MAP = new ConcurrentHashMap<>(BASE_CODE_ARRAY.length);
        for (int i = 0; i < BASE_CODE_ARRAY.length; i++) {
            CODE_INDEX_MAP.put(BASE_CODE_ARRAY[i], i);
        }
    }

    /**
     * 正则校验统一社会信用代码（18位）.
     *
     * <pre>
     * 第一部分：登记管理部门代码1位 (数字或大写英文字母)
     * 第二部分：机构类别代码1位 (数字或大写英文字母)
     * 第三部分：登记管理机关行政区划码6位 (数字)
     * 第四部分：主体标识码（组织机构代码）9位 (数字或大写英文字母)
     * 第五部分：校验码1位 (数字或大写英文字母)
     * </pre>
     *
     * @param creditCode 统一社会信用代码
     * @return 校验结果
     */
    public static boolean isCreditCodeSimple(final CharSequence creditCode) {
        if (StrUtil.isBlank(creditCode)) {
            return false;
        }
        return CREDIT_CODE_PATTERN.matcher(creditCode).matches();
    }

    /**
     * 是否是有效的统一社会信用代码.
     *
     * <pre>
     * 第一部分：登记管理部门代码1位 (数字或大写英文字母)
     * 第二部分：机构类别代码1位 (数字或大写英文字母)
     * 第三部分：登记管理机关行政区划码6位 (数字)
     * 第四部分：主体标识码（组织机构代码）9位 (数字或大写英文字母)
     * 第五部分：校验码1位 (数字或大写英文字母)
     * </pre>
     *
     * @param creditCode 统一社会信用代码
     * @return 校验结果
     */
    public static boolean isCreditCode(final CharSequence creditCode) {
        if (false == isCreditCodeSimple(creditCode)) {
            return false;
        }

        final int parityBit = getParityBit(creditCode);
        if (parityBit < 0) {
            return false;
        }

        return creditCode.charAt(CHECK_CODE_POS) == BASE_CODE_ARRAY[parityBit];
    }

    /**
     * 获取一个随机的统一社会信用代码.
     *
     * @return 统一社会信用代码
     */
    public static String randomCreditCode() {
        final StringBuilder buf = new StringBuilder(CREDIT_CODE_LENGTH);

        for (int i = 0; i < REG_DEPT_LEN; i++) {
            int num = RandomUtil.randomInt(BASE_CODE_ARRAY.length);
            buf.append(Character.toUpperCase(BASE_CODE_ARRAY[num]));
        }
        for (int i = REG_DEPT_LEN; i < REG_DEPT_LEN + ADMIN_DIV_LEN; i++) {
            int num = RandomUtil.randomInt(DIGIT_COUNT);
            buf.append(BASE_CODE_ARRAY[num]);
        }
        for (int i = ORG_CODE_START; i < CHECK_CODE_POS; i++) {
            int num = RandomUtil.randomInt(BASE_CODE_ARRAY.length);
            buf.append(BASE_CODE_ARRAY[num]);
        }

        final String code = buf.toString();
        return code + BASE_CODE_ARRAY[getParityBit(code)];
    }

    /**
     * 获取校验位的值.
     *
     * @param creditCode 统一社会信息代码
     * @return 获取校验位的值，-1表示获取错误
     */
    private static int getParityBit(final CharSequence creditCode) {
        int sum = 0;
        Integer codeIndex;
        for (int i = 0; i < CHECK_CODE_POS; i++) {
            codeIndex = CODE_INDEX_MAP.get(creditCode.charAt(i));
            if (null == codeIndex) {
                return -1;
            }
            sum += codeIndex * WEIGHT[i];
        }
        final int result = CHECKSUM_MOD - sum % CHECKSUM_MOD;
        return result == CHECKSUM_MOD ? 0 : result;
    }
}
