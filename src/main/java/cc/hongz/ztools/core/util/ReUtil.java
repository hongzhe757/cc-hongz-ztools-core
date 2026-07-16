package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.lang.func.Func1;
import cc.hongz.ztools.core.text.Convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则相关工具类.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.util.ReUtil}。
 *
 * @author xiaoleilu
 * @author hongzhe.qin (migrated)
 * @since 3.0.9
 */
public final class ReUtil {

    /**
     * 正则中需要被转义的关键字.
     */
    public static final java.util.Set<Character> RE_KEYS = new HashSet<>(java.util.Arrays.asList(
            '$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|'
    ));

    /**
     * 用于匹配模板中分组变量（如 $1, $2）的正则.
     */
    private static final Pattern GROUP_VAR = Pattern.compile("\\$(\\d+)");

    /**
     * 匹配数字的正则.
     */
    private static final Pattern NUMBERS = Pattern.compile("\\d+");

    private ReUtil() {
    }

    // --------------------------------------------------------------------------------------------------------- get

    /**
     * 获得匹配的字符串，获得正则中分组0的内容.
     *
     * @param regex   匹配的正则
     * @param content 被匹配的内容
     * @return 匹配后得到的字符串，未匹配返回null
     */
    public static String getGroup0(final String regex, final CharSequence content) {
        return get(regex, content, 0);
    }

    /**
     * 获得匹配的字符串，获得正则中分组1的内容.
     *
     * @param regex   匹配的正则
     * @param content 被匹配的内容
     * @return 匹配后得到的字符串，未匹配返回null
     */
    public static String getGroup1(final String regex, final CharSequence content) {
        return get(regex, content, 1);
    }

    /**
     * 获得匹配的字符串.
     *
     * @param regex      匹配的正则
     * @param content    被匹配的内容
     * @param groupIndex 匹配正则的分组序号
     * @return 匹配后得到的字符串，未匹配返回null
     */
    public static String get(final String regex, final CharSequence content, final int groupIndex) {
        if (null == content || null == regex) {
            return null;
        }
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return get(pattern, content, groupIndex);
    }

    /**
     * 获得匹配的字符串.
     *
     * @param regex     匹配的正则
     * @param content   被匹配的内容
     * @param groupName 匹配正则的分组名称
     * @return 匹配后得到的字符串，未匹配返回null
     */
    public static String get(final String regex, final CharSequence content, final String groupName) {
        if (null == content || null == regex) {
            return null;
        }
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return get(pattern, content, groupName);
    }

    /**
     * 获得匹配的字符串，获得正则中分组0的内容.
     *
     * @param pattern 编译后的正则模式
     * @param content 被匹配的内容
     * @return 匹配后得到的字符串，未匹配返回null
     */
    public static String getGroup0(final Pattern pattern, final CharSequence content) {
        return get(pattern, content, 0);
    }

    /**
     * 获得匹配的字符串，获得正则中分组1的内容.
     *
     * @param pattern 编译后的正则模式
     * @param content 被匹配的内容
     * @return 匹配后得到的字符串，未匹配返回null
     */
    public static String getGroup1(final Pattern pattern, final CharSequence content) {
        return get(pattern, content, 1);
    }

    /**
     * 获得匹配的字符串，对应分组0表示整个匹配内容，1表示第一个括号分组内容，依次类推.
     *
     * @param pattern    编译后的正则模式
     * @param content    被匹配的内容
     * @param groupIndex 匹配正则的分组序号
     * @return 匹配后得到的字符串，未匹配返回null
     */
    public static String get(final Pattern pattern, final CharSequence content, final int groupIndex) {
        if (null == content || null == pattern) {
            return null;
        }
        final String[] result = new String[1];
        get(pattern, content, matcher -> result[0] = matcher.group(groupIndex));
        return result[0];
    }

    /**
     * 获得匹配的字符串.
     *
     * @param pattern   匹配的正则
     * @param content   被匹配的内容
     * @param groupName 匹配正则的分组名称
     * @return 匹配后得到的字符串，未匹配返回null
     */
    public static String get(final Pattern pattern, final CharSequence content, final String groupName) {
        if (null == content || null == pattern || null == groupName) {
            return null;
        }
        final String[] result = new String[1];
        get(pattern, content, matcher -> result[0] = matcher.group(groupName));
        return result[0];
    }

    /**
     * 在给定字符串中查找给定规则的字符，如果找到则使用 {@link Consumer} 处理之.
     * 如果内容中有多个匹配项，则只处理找到的第一个结果。
     *
     * @param pattern  匹配的正则
     * @param content  被匹配的内容
     * @param consumer 匹配到的内容处理器
     */
    public static void get(final Pattern pattern, final CharSequence content, final Consumer<Matcher> consumer) {
        if (null == content || null == pattern || null == consumer) {
            return;
        }
        final Matcher m = pattern.matcher(content);
        if (m.find()) {
            consumer.accept(m);
        }
    }

    // --------------------------------------------------------------------------------------------------------- getAllGroups

    /**
     * 获得匹配的字符串匹配到的所有分组.
     *
     * @param pattern 编译后的正则模式
     * @param content 被匹配的内容
     * @return 匹配后得到的字符串数组，按照分组顺序依次列出，未匹配到返回空列表
     */
    public static List<String> getAllGroups(final Pattern pattern, final CharSequence content) {
        return getAllGroups(pattern, content, true);
    }

    /**
     * 获得匹配的字符串匹配到的所有分组.
     *
     * @param pattern    编译后的正则模式
     * @param content    被匹配的内容
     * @param withGroup0 是否包括分组0
     * @return 匹配后得到的字符串数组
     */
    public static List<String> getAllGroups(final Pattern pattern, final CharSequence content, final boolean withGroup0) {
        return getAllGroups(pattern, content, withGroup0, false);
    }

    /**
     * 获得匹配的字符串匹配到的所有分组.
     *
     * @param pattern    编译后的正则模式
     * @param content    被匹配的内容
     * @param withGroup0 是否包括分组0
     * @param findAll    是否查找所有匹配到的内容
     * @return 匹配后得到的字符串数组
     */
    public static List<String> getAllGroups(final Pattern pattern, final CharSequence content, final boolean withGroup0, final boolean findAll) {
        if (null == content || null == pattern) {
            return null;
        }
        final ArrayList<String> result = new ArrayList<>();
        final Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            final int startGroup = withGroup0 ? 0 : 1;
            final int groupCount = matcher.groupCount();
            for (int i = startGroup; i <= groupCount; i++) {
                result.add(matcher.group(i));
            }
            if (!findAll) {
                break;
            }
        }
        return result;
    }

    // --------------------------------------------------------------------------------------------------------- extractMulti

    /**
     * 从content中匹配出多个值并根据template生成新的字符串.
     *
     * @param pattern  匹配正则
     * @param content  被匹配的内容
     * @param template 生成内容模板，变量 $1 表示group1的内容
     * @return 新字符串
     */
    public static String extractMulti(final Pattern pattern, final CharSequence content, final String template) {
        if (null == content || null == pattern || null == template) {
            return null;
        }
        final TreeSet<Integer> varNums = new TreeSet<>((o1, o2) -> Integer.compare(o2, o1));
        final Matcher matcherForTemplate = GROUP_VAR.matcher(template);
        while (matcherForTemplate.find()) {
            varNums.add(Integer.parseInt(matcherForTemplate.group(1)));
        }

        final Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String result = template;
            for (final Integer group : varNums) {
                result = result.replace("$" + group, matcher.group(group));
            }
            return result;
        }
        return null;
    }

    /**
     * 从content中匹配出多个值并根据template生成新的字符串.
     *
     * @param regex    匹配正则字符串
     * @param content  被匹配的内容
     * @param template 生成内容模板
     * @return 按照template拼接后的字符串
     */
    public static String extractMulti(final String regex, final CharSequence content, final String template) {
        if (null == content || null == regex || null == template) {
            return null;
        }
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return extractMulti(pattern, content, template);
    }

    // --------------------------------------------------------------------------------------------------------- del / replace

    /**
     * 删除匹配的第一个内容.
     *
     * @param regex   正则
     * @param content 被匹配的内容
     * @return 删除后剩余的内容
     */
    public static String delFirst(final String regex, final CharSequence content) {
        if (StrUtil.isBlank(regex) || StrUtil.isBlank(content)) {
            return StrUtil.str(content);
        }
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return delFirst(pattern, content);
    }

    /**
     * 删除匹配的第一个内容.
     *
     * @param pattern 正则
     * @param content 被匹配的内容
     * @return 删除后剩余的内容
     */
    public static String delFirst(final Pattern pattern, final CharSequence content) {
        return replaceFirst(pattern, content, StrUtil.EMPTY);
    }

    /**
     * 替换匹配的第一个内容.
     *
     * @param pattern     正则
     * @param content     被匹配的内容
     * @param replacement 替换的内容
     * @return 替换后剩余的内容
     */
    public static String replaceFirst(final Pattern pattern, final CharSequence content, final String replacement) {
        if (null == pattern || StrUtil.isEmpty(content)) {
            return StrUtil.str(content);
        }
        return pattern.matcher(content).replaceFirst(replacement);
    }

    /**
     * 删除匹配的最后一个内容.
     *
     * @param regex 正则
     * @param str   被匹配的内容
     * @return 删除后剩余的内容
     */
    public static String delLast(final String regex, final CharSequence str) {
        if (StrUtil.isBlank(regex) || StrUtil.isBlank(str)) {
            return StrUtil.str(str);
        }
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return delLast(pattern, str);
    }

    /**
     * 删除匹配的最后一个内容.
     *
     * @param pattern 正则
     * @param str     被匹配的内容
     * @return 删除后剩余的内容
     */
    public static String delLast(final Pattern pattern, final CharSequence str) {
        if (null != pattern && StrUtil.isNotEmpty(str)) {
            final MatchResult matchResult = lastIndexOf(pattern, str);
            if (null != matchResult) {
                return StrUtil.subPre(str, matchResult.start()) + StrUtil.subSuf(str, matchResult.end());
            }
        }
        return StrUtil.str(str);
    }

    /**
     * 删除匹配的全部内容.
     *
     * @param regex   正则
     * @param content 被匹配的内容
     * @return 删除后剩余的内容
     */
    public static String delAll(final String regex, final CharSequence content) {
        if (StrUtil.isEmpty(regex) || StrUtil.isEmpty(content)) {
            return StrUtil.str(content);
        }
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return delAll(pattern, content);
    }

    /**
     * 删除匹配的全部内容.
     *
     * @param pattern 正则
     * @param content 被匹配的内容
     * @return 删除后剩余的内容
     */
    public static String delAll(final Pattern pattern, final CharSequence content) {
        if (null == pattern || StrUtil.isEmpty(content)) {
            return StrUtil.str(content);
        }
        return pattern.matcher(content).replaceAll(StrUtil.EMPTY);
    }

    /**
     * 删除正则匹配到的内容之前的字符，如果没有找到，则返回原文.
     *
     * @param regex   定位正则
     * @param content 被查找的内容
     * @return 删除前缀后的新内容
     */
    public static String delPre(final String regex, final CharSequence content) {
        if (null == content || null == regex) {
            return StrUtil.str(content);
        }
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return delPre(pattern, content);
    }

    /**
     * 删除正则匹配到的内容之前的字符，如果没有找到，则返回原文.
     *
     * @param pattern 定位正则模式
     * @param content 被查找的内容
     * @return 删除前缀后的新内容
     */
    public static String delPre(final Pattern pattern, final CharSequence content) {
        if (null == content || null == pattern) {
            return StrUtil.str(content);
        }
        final Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return content.subSequence(matcher.end(), content.length()).toString();
        }
        return StrUtil.str(content);
    }

    // --------------------------------------------------------------------------------------------------------- findAll

    /**
     * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组0的内容.
     *
     * @param regex   正则
     * @param content 被查找的内容
     * @return 结果列表
     */
    public static List<String> findAllGroup0(final String regex, final CharSequence content) {
        return findAll(regex, content, 0);
    }

    /**
     * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组1的内容.
     *
     * @param regex   正则
     * @param content 被查找的内容
     * @return 结果列表
     */
    public static List<String> findAllGroup1(final String regex, final CharSequence content) {
        return findAll(regex, content, 1);
    }

    /**
     * 取得内容中匹配的所有结果.
     *
     * @param regex   正则
     * @param content 被查找的内容
     * @param group   正则的分组
     * @return 结果列表
     */
    public static List<String> findAll(final String regex, final CharSequence content, final int group) {
        return findAll(regex, content, group, new ArrayList<>());
    }

    /**
     * 取得内容中匹配的所有结果.
     *
     * @param <T>        集合类型
     * @param regex      正则
     * @param content    被查找的内容
     * @param group      正则的分组
     * @param collection 返回的集合类型
     * @return 结果集
     */
    public static <T extends Collection<String>> T findAll(final String regex, final CharSequence content, final int group, final T collection) {
        if (null == regex) {
            return collection;
        }
        return findAll(Pattern.compile(regex, Pattern.DOTALL), content, group, collection);
    }

    /**
     * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组0的内容.
     *
     * @param pattern 编译后的正则模式
     * @param content 被查找的内容
     * @return 结果列表
     */
    public static List<String> findAllGroup0(final Pattern pattern, final CharSequence content) {
        return findAll(pattern, content, 0);
    }

    /**
     * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组1的内容.
     *
     * @param pattern 编译后的正则模式
     * @param content 被查找的内容
     * @return 结果列表
     */
    public static List<String> findAllGroup1(final Pattern pattern, final CharSequence content) {
        return findAll(pattern, content, 1);
    }

    /**
     * 取得内容中匹配的所有结果.
     *
     * @param pattern 编译后的正则模式
     * @param content 被查找的内容
     * @param group   正则的分组
     * @return 结果列表
     */
    public static List<String> findAll(final Pattern pattern, final CharSequence content, final int group) {
        return findAll(pattern, content, group, new ArrayList<>());
    }

    /**
     * 取得内容中匹配的所有结果.
     *
     * @param <T>        集合类型
     * @param pattern    编译后的正则模式
     * @param content    被查找的内容
     * @param group      正则的分组
     * @param collection 返回的集合类型
     * @return 结果集
     */
    public static <T extends Collection<String>> T findAll(final Pattern pattern, final CharSequence content, final int group, final T collection) {
        if (null == pattern || null == content) {
            return null;
        }
        Assert.notNull(collection, "Collection must be not null !");
        findAll(pattern, content, (matcher) -> collection.add(matcher.group(group)));
        return collection;
    }

    /**
     * 取得内容中匹配的所有结果，使用 {@link Consumer} 完成匹配结果处理.
     *
     * @param pattern  编译后的正则模式
     * @param content  被查找的内容
     * @param consumer 匹配结果处理函数
     */
    public static void findAll(final Pattern pattern, final CharSequence content, final Consumer<Matcher> consumer) {
        if (null == pattern || null == content) {
            return;
        }
        final Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            consumer.accept(matcher);
        }
    }

    // --------------------------------------------------------------------------------------------------------- count / contains

    /**
     * 计算指定字符串中，匹配pattern的个数.
     *
     * @param regex   正则表达式
     * @param content 被查找的内容
     * @return 匹配个数
     */
    public static int count(final String regex, final CharSequence content) {
        if (null == regex || null == content) {
            return 0;
        }
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return count(pattern, content);
    }

    /**
     * 计算指定字符串中，匹配pattern的个数.
     *
     * @param pattern 编译后的正则模式
     * @param content 被查找的内容
     * @return 匹配个数
     */
    public static int count(final Pattern pattern, final CharSequence content) {
        if (null == pattern || null == content) {
            return 0;
        }
        int count = 0;
        final Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    /**
     * 指定内容中是否有表达式匹配的内容.
     *
     * @param regex   正则表达式
     * @param content 被查找的内容
     * @return 指定内容中是否有表达式匹配的内容
     */
    public static boolean contains(final String regex, final CharSequence content) {
        if (null == regex || null == content) {
            return false;
        }
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return contains(pattern, content);
    }

    /**
     * 指定内容中是否有表达式匹配的内容.
     *
     * @param pattern 编译后的正则模式
     * @param content 被查找的内容
     * @return 指定内容中是否有表达式匹配的内容
     */
    public static boolean contains(final Pattern pattern, final CharSequence content) {
        if (null == pattern || null == content) {
            return false;
        }
        return pattern.matcher(content).find();
    }

    // --------------------------------------------------------------------------------------------------------- indexOf / lastIndexOf

    /**
     * 找到指定正则匹配到字符串的开始位置.
     *
     * @param regex   正则
     * @param content 字符串
     * @return 位置，{@code null}表示未找到
     */
    public static MatchResult indexOf(final String regex, final CharSequence content) {
        if (null == regex || null == content) {
            return null;
        }
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return indexOf(pattern, content);
    }

    /**
     * 找到指定模式匹配到字符串的开始位置.
     *
     * @param pattern 模式
     * @param content 字符串
     * @return 位置，{@code null}表示未找到
     */
    public static MatchResult indexOf(final Pattern pattern, final CharSequence content) {
        if (null != pattern && null != content) {
            final Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                return matcher.toMatchResult();
            }
        }
        return null;
    }

    /**
     * 找到指定正则匹配到第一个字符串的位置.
     *
     * @param regex   正则
     * @param content 字符串
     * @return 位置，{@code null}表示未找到
     */
    public static MatchResult lastIndexOf(final String regex, final CharSequence content) {
        if (null == regex || null == content) {
            return null;
        }
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return lastIndexOf(pattern, content);
    }

    /**
     * 找到指定模式匹配到最后一个字符串的位置.
     *
     * @param pattern 模式
     * @param content 字符串
     * @return 位置，{@code null}表示未找到
     */
    public static MatchResult lastIndexOf(final Pattern pattern, final CharSequence content) {
        MatchResult result = null;
        if (null != pattern && null != content) {
            final Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                result = matcher.toMatchResult();
            }
        }
        return result;
    }

    // --------------------------------------------------------------------------------------------------------- getFirstNumber

    /**
     * 从字符串中获得第一个整数.
     *
     * @param stringWithNumber 带数字的字符串
     * @return 整数
     */
    public static Integer getFirstNumber(final CharSequence stringWithNumber) {
        return Convert.toInt(get(NUMBERS, stringWithNumber, 0), null);
    }

    // --------------------------------------------------------------------------------------------------------- isMatch

    /**
     * 给定内容是否匹配正则.
     *
     * @param regex   正则
     * @param content 内容
     * @return 正则为null或者""则不检查，返回true，内容为null返回false
     */
    public static boolean isMatch(final String regex, final CharSequence content) {
        if (content == null) {
            return false;
        }
        if (StrUtil.isEmpty(regex)) {
            return true;
        }
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return isMatch(pattern, content);
    }

    /**
     * 给定内容是否匹配正则.
     *
     * @param pattern 模式
     * @param content 内容
     * @return 正则为null或者""则不检查，返回true，内容为null返回false
     */
    public static boolean isMatch(final Pattern pattern, final CharSequence content) {
        if (content == null || pattern == null) {
            return false;
        }
        return pattern.matcher(content).matches();
    }

    // --------------------------------------------------------------------------------------------------------- replaceAll

    /**
     * 正则替换指定值.
     *
     * @param content             文本
     * @param regex               正则
     * @param replacementTemplate 替换的文本模板
     * @return 处理后的文本
     */
    public static String replaceAll(final CharSequence content, final String regex, final String replacementTemplate) {
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return replaceAll(content, pattern, replacementTemplate);
    }

    /**
     * 正则替换指定值.
     *
     * @param content             文本
     * @param pattern             {@link Pattern}
     * @param replacementTemplate 替换的文本模板
     * @return 处理后的文本
     */
    public static String replaceAll(final CharSequence content, final Pattern pattern, final String replacementTemplate) {
        if (StrUtil.isEmpty(content)) {
            return StrUtil.str(content);
        }
        final Matcher matcher = pattern.matcher(content);
        boolean result = matcher.find();
        if (result) {
            final java.util.Set<String> varNums = findAll(GROUP_VAR, replacementTemplate, 1, new TreeSet<>(java.util.Comparator.comparingInt(String::length).reversed()));
            final StringBuffer sb = new StringBuffer();
            do {
                String replacement = replacementTemplate;
                for (final String var : varNums) {
                    final int group = Integer.parseInt(var);
                    replacement = StrUtil.replace(replacement, "$" + var, matcher.group(group));
                }
                matcher.appendReplacement(sb, escape(replacement));
                result = matcher.find();
            } while (result);
            matcher.appendTail(sb);
            return sb.toString();
        }
        return StrUtil.str(content);
    }

    /**
     * 替换所有正则匹配的文本，并使用自定义函数决定如何替换.
     *
     * @param str        要替换的字符串
     * @param regex      用于匹配的正则式
     * @param replaceFun 决定如何替换的函数
     * @return 替换后的文本
     */
    public static String replaceAll(final CharSequence str, final String regex, final Func1<Matcher, String> replaceFun) {
        return replaceAll(str, Pattern.compile(regex), replaceFun);
    }

    /**
     * 替换所有正则匹配的文本，并使用自定义函数决定如何替换.
     *
     * @param str        要替换的字符串
     * @param pattern    用于匹配的正则式
     * @param replaceFun 决定如何替换的函数
     * @return 替换后的字符串
     */
    public static String replaceAll(final CharSequence str, final Pattern pattern, final Func1<Matcher, String> replaceFun) {
        if (StrUtil.isEmpty(str)) {
            return StrUtil.str(str);
        }
        final Matcher matcher = pattern.matcher(str);
        final StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(buffer, replaceFun.call(matcher));
            } catch (Exception e) {
                throw new UtilException(e);
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    // --------------------------------------------------------------------------------------------------------- escape

    /**
     * 转义字符，将正则的关键字转义.
     *
     * @param c 字符
     * @return 转义后的文本
     */
    public static String escape(final char c) {
        final StringBuilder builder = new StringBuilder();
        if (RE_KEYS.contains(c)) {
            builder.append('\\');
        }
        builder.append(c);
        return builder.toString();
    }

    /**
     * 转义字符串，将正则的关键字转义.
     *
     * @param content 文本
     * @return 转义后的文本
     */
    public static String escape(final CharSequence content) {
        if (StrUtil.isBlank(content)) {
            return StrUtil.str(content);
        }
        final StringBuilder builder = new StringBuilder();
        final int len = content.length();
        char current;
        for (int i = 0; i < len; i++) {
            current = content.charAt(i);
            if (RE_KEYS.contains(current)) {
                builder.append('\\');
            }
            builder.append(current);
        }
        return builder.toString();
    }
}
