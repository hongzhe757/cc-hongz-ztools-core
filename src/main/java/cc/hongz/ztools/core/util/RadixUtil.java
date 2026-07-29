package cc.hongz.ztools.core.util;

/**
 * 任意进制编解码工具类（Hutool 阶段 1 迁入）.
 * <p>
 * 核心思路：用<strong>自定义字符表</strong>把十进制整数编码为更短、可人工辨认的字符串，再按同一字符表解码还原。
 * 字符表长度即为进制基数（如 34 个字符即 34 进制），基数越大，同样数值编码后越短。
 * <p>
 * <strong>34 与 59 并非强制</strong>：二者是 Hutool 提供的<strong>便捷预设</strong>，基数分别等于对应字符表长度。
 * 任意 {@code encode(radixs, n)} 的进制均为 {@code radixs.length()}，业务可自定义字符表。
 * <p>
 * <strong>选型与基数推导、对比 Base36/Base58 等</strong>见本模块根目录 {@code README.md}「RadixUtil 任意进制」一节.
 *
 * <h3>基数来源（摘要）</h3>
 * <ul>
 *   <li><strong>34</strong> = {@code 10（0-9）+ 24（A-Z \ {I,O}）}</li>
 *   <li><strong>59</strong> = {@code 10 + 25（a-z \ {l}）+ 24（A-Z \ {I,O}）}</li>
 *   <li>打乱版：字符集合不变，仅顺序不同，基数仍为 34 / 59。</li>
 * </ul>
 *
 * <h3>典型业务场景</h3>
 * <ul>
 *   <li><strong>邀请码 / 兑换码</strong>：用户 ID、订单号 → 短码发放；去掉易混淆字符（I/O/0/1）减少人工录入错误。</li>
 *   <li><strong>短链 / 分享码</strong>：自增主键 → 短字符串放入 URL 或二维码；59 进制字符更多，同等数值比 34 进制更短。</li>
 *   <li><strong>数字混淆 / 防猜测</strong>：在标准字符表基础上<strong>打乱顺序</strong>
 *       （{@link #RADIXS_SHUFFLE_34}、{@link #RADIXS_SHUFFLE_59}），
 *       使连续 ID 编码结果无明显规律，降低被穷举或推算原 ID 的风险
 *       （非加密，仅增加猜测成本）。</li>
 *   <li><strong>完全自定义字符表</strong>：品牌专属字母表、仅含某语言字符、或二次换表（先 34 再 59）做简单混淆。</li>
 * </ul>
 *
 * <h3>预设字符表（摘要）</h3>
 * <p>完整对照表见 {@code README.md}。{@link #RADIXS_34} 偏人工可读；{@link #RADIXS_59} 偏更短编码；{@code SHUFFLE} 版仅打乱顺序.</p>
 *
 * <h3>使用注意</h3>
 * <ul>
 *   <li>编码与解码必须使用<strong>同一</strong> {@code radixs} 字符串；换表即无法还原。</li>
 *   <li>字符表中字符<strong>不可重复</strong>，否则 {@link #decode(String, String)} 无法唯一还原。</li>
 *   <li>{@link #encode(String, long)} 不支持负数；{@link #encode(String, int)} 对负数按 32 位无符号处理。</li>
 *   <li>打乱字符表仅增加猜测难度，<strong>不能替代加密</strong>；敏感业务应另行加盐或签名。</li>
 * </ul>
 *
 * <p>示例：{@code encode("AB", 10)} → {@code "BABA"}（2 进制）；{@code encode("VIP", 21)} → {@code "PIV"}（3 进制）。</p>
 *
 * @author xl7@qq.com
 * @since 5.5.8
 */
public final class RadixUtil {

    /**
     * 32 位无符号整数范围（2^32），用于 int 负数转无符号.
     */
    private static final long UNSIGNED_INT_RANGE = 0x100000000L;

    /**
     * int 类型位宽.
     */
    private static final int INT_BITS = 32;

    /**
     * long 类型位宽.
     */
    private static final int LONG_BITS = 64;

    /**
     * 私有构造，禁止实例化.
     */
    private RadixUtil() {
    }

    /**
     * 标准 34 进制字符集：数字 + 大写字母，排除 I、O（易与 1、0 混淆）.
     * <p>
     * <strong>基数来源</strong>：{@code 10（0-9）+ 24（A-Z \ {I,O}）= 34}，非随意取数。
     * <p>
     * <strong>场景</strong>：邀请码、兑换码、客服报单号等需人工阅读/输入的短码；
     * 在可读性与长度之间折中（比纯 10 进制更短，比 59 进制字符集更「整齐」）。
     * <p>
     * <strong>不适用</strong>：需极致缩短 URL、或可区分大小写的机器生成码（优先考虑 {@link #RADIXS_59}）。
     */
    public static final String RADIXS_34 = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    /**
     * 打乱顺序的 34 进制字符集（字符集合与 {@link #RADIXS_34} 相同，基数仍为 34）.
     * <p>
     * <strong>场景</strong>：自增用户 ID、订单序号转邀请码时，避免 {@code 1001→1002} 在码上连续可猜；
     * 适用于活动邀请、渠道追踪码等「防一眼看出规律」的需求。
     */
    public static final String RADIXS_SHUFFLE_34 = "H3UM16TDFPSBZJ90CW28QYRE45AXKNGV7L";

    /**
     * 标准 59 进制字符集：大小写字母 + 数字，排除 I、O、小写 l.
     * <p>
     * <strong>基数来源</strong>：{@code 10（0-9）+ 25（a-z \ {l}）+ 24（A-Z \ {I,O}）= 59}，非随意取数。
     * <p>
     * <strong>场景</strong>：短链、分享链接 path、文件取件码等要求<strong>更短字符串</strong>的场景；
     * 同等数值编码长度通常短于 {@link #RADIXS_34}。
     * <p>
     * <strong>不适用</strong>：必须口头报读、电话核对（大小写易混，优先考虑 {@link #RADIXS_34}）。
     */
    public static final String RADIXS_59 = "0123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";

    /**
     * 打乱顺序的 59 进制字符集（字符集合与 {@link #RADIXS_59} 相同，基数仍为 59）.
     * <p>
     * <strong>场景</strong>：短链 + 隐藏自增规律；例如内部 {@code articleId} 转外链 slug，
     * 既控制 URL 长度，又降低被批量枚举的风险。
     */
    public static final String RADIXS_SHUFFLE_59 = "vh9wGkfK8YmqbsoENP3764SeCX0dVzrgy1HRtpnTaLjJW2xQiZAcBMUFDu5";

    // --------------------------------------- encode / decode start

    /**
     * 将 int 值按自定义字符表编码为字符串.
     * <p>
     * 字符表长度即进制基数。负数按 32 位无符号整数处理后再编码。
     *
     * @param radixs 自定义字符表，字符不可重复且长度至少为 2
     * @param num    待编码整数值
     * @return 编码后的字符串
     */
    public static String encode(final String radixs, final int num) {
        final long tmpNum = (num >= 0 ? num : (UNSIGNED_INT_RANGE - (~num + 1)));
        return encode(radixs, tmpNum, INT_BITS);
    }

    /**
     * 将 long 值按自定义字符表编码为字符串.
     *
     * @param radixs 自定义字符表，字符不可重复且长度至少为 2
     * @param num    待编码长整型，须非负
     * @return 编码后的字符串
     * @throws RuntimeException 当 num 为负数时
     */
    public static String encode(final String radixs, final long num) {
        if (num < 0) {
            throw new RuntimeException("暂不支持负数！");
        }
        return encode(radixs, num, LONG_BITS);
    }

    /**
     * 将编码字符串按同一字符表解码为 int.
     *
     * @param radixs    编码时使用的字符表
     * @param encodeStr 编码字符串
     * @return 解码后的 int 值
     */
    public static int decodeToInt(final String radixs, final String encodeStr) {
        return (int) decode(radixs, encodeStr);
    }

    /**
     * 将编码字符串按同一字符表解码为 long.
     *
     * @param radixs    编码时使用的字符表
     * @param encodeStr 编码字符串，不可为 null 或空
     * @return 解码后的 long 值
     * @throws IllegalArgumentException 字符表无效、编码串为空或含非法字符时
     */
    public static long decode(final String radixs, final String encodeStr) {
        if (radixs == null || radixs.length() < 2) {
            throw new IllegalArgumentException("radixs must contain at least 2 characters");
        }
        if (encodeStr == null || encodeStr.isEmpty()) {
            throw new IllegalArgumentException("encodeStr is null or empty");
        }
        final int rl = radixs.length();
        long res = 0L;

        for (final char c : encodeStr.toCharArray()) {
            final int idx = radixs.indexOf(c);
            if (idx < 0) {
                throw new IllegalArgumentException("Illegal character '" + c + "' for radixs");
            }
            res = res * rl + idx;
        }
        return res;
    }

    // --------------------------------------- encode / decode end

    /**
     * 内部编码实现，按余数逐位取字符.
     *
     * @param radixs    字符表
     * @param num       非负数值
     * @param maxLength 结果缓冲最大长度
     * @return 编码字符串
     */
    private static String encode(final String radixs, final long num, final int maxLength) {
        if (radixs.length() < 2) {
            throw new RuntimeException("自定义进制最少两个字符哦！");
        }
        final int rl = radixs.length();
        long tmpNum = num;
        final char[] buffer = new char[maxLength];
        int i = buffer.length;
        do {
            buffer[--i] = radixs.charAt((int) (tmpNum % rl));
            tmpNum /= rl;
        } while (tmpNum > 0);
        return new String(buffer, i, buffer.length - i);
    }
}
