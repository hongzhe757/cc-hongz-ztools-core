package cc.hongz.ztools.core.text;

import cc.hongz.ztools.core.map.SafeConcurrentHashMap;
import cc.hongz.ztools.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ant风格的路径匹配器.<br>
 * 来自Spring-core和Ant.
 *
 * <p>匹配URL的规则如下：<br>
 * <ul>
 * <li>{@code ?} 匹配单个字符</li>
 * <li>{@code *} 匹配0个或多个字符</li>
 * <li>{@code **} 0个或多个路径中的<em>目录节点</em></li>
 * <li>{@code {hutool:[a-z]+}} 匹配以"hutool"命名的正则 {@code [a-z]+}</li>
 * </ul>
 *
 * <p>例子: </p>
 * <ul>
 * <li>{@code com/t?st.jsp} &mdash; 匹配 {@code com/test.jsp} 或
 * {@code com/tast.jsp} 或 {@code com/txst.jsp}</li>
 * <li>{@code com/*.jsp} &mdash; 匹配{@code com}目录下全部 {@code .jsp}文件</li>
 * <li>{@code com/&#42;&#42;/test.jsp} &mdash;
 * 匹配{@code com}目录下全部 {@code test.jsp}文件</li>
 * <li>{@code cn/hutool/&#42;&#42;/*.jsp} &mdash;
 * 匹配{@code cn/hutool}路径下全部{@code .jsp} 文件</li>
 * <li>{@code org/&#42;&#42;/servlet/bla.jsp} &mdash;
 * 匹配{@code cn/hutool/servlet/bla.jsp}
 * 或{@code cn/hutool/testing/servlet/bla.jsp}
 * 或 {@code org/servlet/bla.jsp}</li>
 * <li>{@code com/{filename:\\w+}.jsp} 匹配 {@code com/test.jsp}
 * 并将 {@code test} 关联到 {@code filename} 变量</li>
 * </ul>
 *
 * <p><strong>注意:</strong> 表达式和路径必须都为绝对路径或都为相对路径.
 *
 * @author Alef Arendsen, Juergen Hoeller, Rob Harrop, Arjen Poutsma,
 *         Rossen Stoyanchev, Sam Brannen, Vladislav Kisel
 * @since 5.7.22
 */
public class AntPathMatcher {

    /**
     * Default path separator: "/".
     */
    /**
     * Default path separator: "/".
     */
    public static final String DEFAULT_PATH_SEPARATOR = StrUtil.SLASH;

    /**
     * 缓存关闭阈值，超过此数量自动关闭缓存.
     */
    private static final int CACHE_TURNOFF_THRESHOLD = 65536;

    /**
     * 用于计算表达式长度的正则模式，匹配URI模板变量.
     */
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{[^/]+?}");

    /**
     * 通配符字符数组.
     */
    private static final char[] WILDCARD_CHARS = {'*', '?', '{'};

    /**
     * 路径分隔符.
     */
    private String pathSeparator;

    /**
     * 路径分隔符模式缓存.
     */
    private PathSeparatorPatternCache pathSeparatorPatternCache;

    /**
     * 是否大小写敏感，默认为{@code true}.
     */
    private boolean caseSensitive = true;

    /**
     * 是否去除路径节点两边的空白符，默认为{@code false}.
     */
    private boolean trimTokens = false;

    /**
     * 是否缓存表达式，{@code null}表示自动判断.
     */
    private volatile Boolean cachePatterns;

    /**
     * 分词后的表达式缓存.
     */
    private final Map<String, String[]> tokenizedPatternCache = new SafeConcurrentHashMap<>(256);

    /**
     * 表达式匹配器缓存.
     */
    private final Map<String, AntPathStringMatcher> stringMatcherCache = new SafeConcurrentHashMap<>(256);

    /**
     * 使用 {@link #DEFAULT_PATH_SEPARATOR} 作为分隔符构造.
     */
    public AntPathMatcher() {
        this(DEFAULT_PATH_SEPARATOR);
    }

    /**
     * 使用自定义的分隔符构造.
     *
     * @param pathSeparator 路径分隔符，不能为 {@code null}
     * @since 4.1
     */
    public AntPathMatcher(final String pathSeparator) {
        String separator = pathSeparator;
        if (null == separator) {
            separator = DEFAULT_PATH_SEPARATOR;
        }
        setPathSeparator(separator);
    }

    /**
     * 设置路径分隔符.
     *
     * @param aPathSeparator 分隔符，{@code null}表示使用默认分隔符
     * @return this
     */
    public AntPathMatcher setPathSeparator(final String aPathSeparator) {
        String separator = aPathSeparator;
        if (null == separator) {
            separator = DEFAULT_PATH_SEPARATOR;
        }
        this.pathSeparator = separator;
        this.pathSeparatorPatternCache = new PathSeparatorPatternCache(this.pathSeparator);
        return this;
    }

    /**
     * 设置是否大小写敏感，默认为{@code true}.
     *
     * @param caseSensitive 是否大小写敏感
     * @return this
     */
    public AntPathMatcher setCaseSensitive(final boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }

    /**
     * 设置是否去除路径节点两边的空白符，默认为{@code false}.
     *
     * @param trimTokens 是否去除路径节点两边的空白符
     * @return this
     */
    public AntPathMatcher setTrimTokens(final boolean trimTokens) {
        this.trimTokens = trimTokens;
        return this;
    }

    /**
     * 设置是否缓存表达式.<br>
     * {@code true}表示开启无限缓存，{@code false}表示完全关闭缓存.
     * 默认为开启，但当运行时遇到过多不同表达式时（阈值65536），
     * 会自动关闭缓存。
     *
     * @param cachePatterns 是否缓存表达式
     * @return this
     */
    public AntPathMatcher setCachePatterns(final boolean cachePatterns) {
        this.cachePatterns = cachePatterns;
        return this;
    }

    /**
     * 判断给定路径是否是表达式.
     *
     * @param path 路径
     * @return 是否为表达式
     */
    public boolean isPattern(final String path) {
        if (path == null) {
            return false;
        }
        boolean uriVar = false;
        final int length = path.length();
        char c;
        for (int i = 0; i < length; i++) {
            c = path.charAt(i);
            // 含有通配符
            if (c == '*' || c == '?') {
                return true;
            }
            if (c == CharPool.DELIM_START) {
                uriVar = true;
                continue;
            }
            if (c == CharPool.DELIM_END && uriVar) {
                return true;
            }
        }
        return false;
    }

    /**
     * 给定路径是否匹配表达式.
     *
     * @param pattern 表达式
     * @param path    路径
     * @return 是否匹配
     */
    public boolean match(final String pattern, final String path) {
        return doMatch(pattern, path, true, null);
    }

    /**
     * 前置部分匹配.
     *
     * @param pattern 表达式
     * @param path    路径
     * @return 是否匹配
     */
    public boolean matchStart(final String pattern, final String path) {
        return doMatch(pattern, path, false, null);
    }

    /**
     * 执行匹配，判断给定的{@code path}是否匹配{@code pattern}.
     *
     * @param pattern              表达式
     * @param path                 路径
     * @param fullMatch            是否全匹配，{@code true} 表示全路径匹配
     * @param uriTemplateVariables URI模板变量映射
     * @return {@code true} 表示匹配成功, {@code false} 表示不匹配
     */
    protected boolean doMatch(final String pattern, final String path, final boolean fullMatch,
            final Map<String, String> uriTemplateVariables) {
        if (path == null || path.startsWith(this.pathSeparator) != pattern.startsWith(this.pathSeparator)) {
            return false;
        }

        final String[] pattDirs = tokenizePattern(pattern);
        if (fullMatch && this.caseSensitive && false == isPotentialMatch(path, pattDirs)) {
            return false;
        }

        final String[] pathDirs = tokenizePath(path);
        int pattIdxStart = 0;
        int pattIdxEnd = pattDirs.length - 1;
        int pathIdxStart = 0;
        int pathIdxEnd = pathDirs.length - 1;

        // 匹配直到第一个 ** 之前的所有元素
        while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            String pattDir = pattDirs[pattIdxStart];
            if ("**".equals(pattDir)) {
                break;
            }
            if (notMatchStrings(pattDir, pathDirs[pathIdxStart], uriTemplateVariables)) {
                return false;
            }
            pattIdxStart++;
            pathIdxStart++;
        }

        if (pathIdxStart > pathIdxEnd) {
            // 路径已用完，仅当剩余表达式全部为 * 或 ** 时匹配
            if (pattIdxStart > pattIdxEnd) {
                return (pattern.endsWith(this.pathSeparator) == path.endsWith(this.pathSeparator));
            }
            if (false == fullMatch) {
                return true;
            }
            if (pattIdxStart == pattIdxEnd && pattDirs[pattIdxStart].equals("*")
                    && path.endsWith(this.pathSeparator)) {
                return true;
            }
            for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
                if (false == pattDirs[i].equals("**")) {
                    return false;
                }
            }
            return true;
        } else if (pattIdxStart > pattIdxEnd) {
            // 路径未用完但表达式已用完，匹配失败
            return false;
        } else if (false == fullMatch && "**".equals(pattDirs[pattIdxStart])) {
            // 表达式以 ** 开头，路径开头肯定匹配
            return true;
        }

        // 从后往前匹配到最后一个 **
        while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            String pattDir = pattDirs[pattIdxEnd];
            if (pattDir.equals("**")) {
                break;
            }
            if (notMatchStrings(pattDir, pathDirs[pathIdxEnd], uriTemplateVariables)) {
                return false;
            }
            pattIdxEnd--;
            pathIdxEnd--;
        }
        if (pathIdxStart > pathIdxEnd) {
            // 路径已用完
            for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
                if (false == pattDirs[i].equals("**")) {
                    return false;
                }
            }
            return true;
        }

        while (pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            int patIdxTmp = -1;
            for (int i = pattIdxStart + 1; i <= pattIdxEnd; i++) {
                if (pattDirs[i].equals("**")) {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == pattIdxStart + 1) {
                // '**/**' 的情况，跳过一个
                pattIdxStart++;
                continue;
            }
            // 在路径中查找 patIdxStart 到 patIdxTmp 之间的表达式匹配
            int patLength = (patIdxTmp - pattIdxStart - 1);
            int strLength = (pathIdxEnd - pathIdxStart + 1);
            int foundIdx = -1;

            strLoop:
            for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    String subPat = pattDirs[pattIdxStart + j + 1];
                    String subStr = pathDirs[pathIdxStart + i + j];
                    if (notMatchStrings(subPat, subStr, uriTemplateVariables)) {
                        continue strLoop;
                    }
                }
                foundIdx = pathIdxStart + i;
                break;
            }

            if (foundIdx == -1) {
                return false;
            }

            pattIdxStart = patIdxTmp;
            pathIdxStart = foundIdx + patLength;
        }

        for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
            if (false == pattDirs[i].equals("**")) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查路径是否可能匹配表达式.
     *
     * @param path     路径
     * @param pattDirs 表达式分段
     * @return 是否可能匹配
     */
    private boolean isPotentialMatch(final String path, final String[] pattDirs) {
        if (!this.trimTokens) {
            int pos = 0;
            for (String pattDir : pattDirs) {
                int skipped = skipSeparator(path, pos, this.pathSeparator);
                pos += skipped;
                skipped = skipSegment(path, pos, pattDir);
                if (skipped < pattDir.length()) {
                    return (skipped > 0 || (pattDir.length() > 0
                            && isWildcardChar(pattDir.charAt(0))));
                }
                pos += skipped;
            }
        }
        return true;
    }

    /**
     * 跳过路径中的一个段.
     *
     * @param path   路径
     * @param pos    起始位置
     * @param prefix 前缀
     * @return 跳过的字符数
     */
    private int skipSegment(final String path, final int pos, final String prefix) {
        int skipped = 0;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (isWildcardChar(c)) {
                return skipped;
            }
            int currPos = pos + skipped;
            if (currPos >= path.length()) {
                return 0;
            }
            if (c == path.charAt(currPos)) {
                skipped++;
            }
        }
        return skipped;
    }

    /**
     * 跳过分隔符.
     *
     * @param path      路径
     * @param pos       起始位置
     * @param separator 分隔符
     * @return 跳过的字符数
     */
    private int skipSeparator(final String path, final int pos, final String separator) {
        int skipped = 0;
        while (path.startsWith(separator, pos + skipped)) {
            skipped += separator.length();
        }
        return skipped;
    }

    /**
     * 判断字符是否为通配符.
     *
     * @param c 字符
     * @return 是否为通配符
     */
    private boolean isWildcardChar(final char c) {
        for (char candidate : WILDCARD_CHARS) {
            if (c == candidate) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将表达式分词，根据配置可能缓存结果.
     *
     * @param pattern 表达式
     * @return 分词后的数组
     */
    protected String[] tokenizePattern(final String pattern) {
        String[] tokenized = null;
        Boolean cachePatterns = this.cachePatterns;
        if (cachePatterns == null || cachePatterns) {
            tokenized = this.tokenizedPatternCache.get(pattern);
        }
        if (tokenized == null) {
            tokenized = tokenizePath(pattern);
            if (cachePatterns == null
                    && this.tokenizedPatternCache.size() >= CACHE_TURNOFF_THRESHOLD) {
                // 根据运行时情况自动调整：遇到过多不同表达式时关闭缓存
                deactivatePatternCache();
                return tokenized;
            }
            if (cachePatterns == null || cachePatterns) {
                this.tokenizedPatternCache.put(pattern, tokenized);
            }
        }
        return tokenized;
    }

    /**
     * 关闭表达式缓存.
     */
    private void deactivatePatternCache() {
        this.cachePatterns = false;
        this.tokenizedPatternCache.clear();
        this.stringMatcherCache.clear();
    }

    /**
     * 将路径分词.
     *
     * @param path 路径
     * @return 分词后的数组
     */
    protected String[] tokenizePath(final String path) {
        return StrSplitter.splitToArray(path, this.pathSeparator, 0, this.trimTokens, true);
    }

    /**
     * 判断字符串是否不匹配表达式.
     *
     * @param pattern               表达式
     * @param str                   字符串
     * @param uriTemplateVariables URI模板变量
     * @return 是否不匹配
     */
    private boolean notMatchStrings(final String pattern, final String str,
            final Map<String, String> uriTemplateVariables) {
        return false == getStringMatcher(pattern).matchStrings(str, uriTemplateVariables);
    }

    /**
     * 获取或创建 {@link AntPathStringMatcher} 用于表达式匹配.
     * 会检查缓存，如果缓存达到阈值则自动关闭缓存.
     *
     * @param pattern 表达式
     * @return 表达式匹配器
     */
    protected AntPathStringMatcher getStringMatcher(final String pattern) {
        AntPathStringMatcher matcher = null;
        Boolean cachePatterns = this.cachePatterns;
        if (cachePatterns == null || cachePatterns) {
            matcher = this.stringMatcherCache.get(pattern);
        }
        if (matcher == null) {
            matcher = new AntPathStringMatcher(pattern, this.caseSensitive);
            if (cachePatterns == null
                    && this.stringMatcherCache.size() >= CACHE_TURNOFF_THRESHOLD) {
                // 根据运行时情况自动调整：遇到过多不同表达式时关闭缓存
                deactivatePatternCache();
                return matcher;
            }
            if (cachePatterns == null || cachePatterns) {
                this.stringMatcherCache.put(pattern, matcher);
            }
        }
        return matcher;
    }

    /**
     * 从路径中提取表达式匹配的部分.
     *
     * @param pattern 表达式
     * @param path    路径
     * @return 表达式匹配到的部分
     */
    public String extractPathWithinPattern(final String pattern, final String path) {
        String[] patternParts = tokenizePath(pattern);
        String[] pathParts = tokenizePath(path);
        StringBuilder builder = new StringBuilder();
        boolean pathStarted = false;

        for (int segment = 0; segment < patternParts.length; segment++) {
            String patternPart = patternParts[segment];
            if (patternPart.indexOf('*') > -1 || patternPart.indexOf('?') > -1) {
                for (; segment < pathParts.length; segment++) {
                    if (pathStarted || (segment == 0
                            && !pattern.startsWith(this.pathSeparator))) {
                        builder.append(this.pathSeparator);
                    }
                    builder.append(pathParts[segment]);
                    pathStarted = true;
                }
            }
        }

        return builder.toString();
    }

    /**
     * 提取URI模板变量.
     *
     * @param pattern 表达式
     * @param path    路径
     * @return URI模板变量映射
     * @throws IllegalStateException 如果路径不匹配表达式
     */
    public Map<String, String> extractUriTemplateVariables(final String pattern,
            final String path) {
        Map<String, String> variables = new LinkedHashMap<>();
        boolean result = doMatch(pattern, path, true, variables);
        if (!result) {
            throw new IllegalStateException("Pattern \"" + pattern
                    + "\" is not a match for \"" + path + "\"");
        }
        return variables;
    }

    /**
     * 合并两个表达式.
     *
     * @param pattern1 表达式1
     * @param pattern2 表达式2
     * @return 合并后的表达式
     * @throws IllegalArgumentException 如果两个表达式无法合并
     */
    public String combine(final String pattern1, final String pattern2) {
        if (StrUtil.isEmpty(pattern1) && StrUtil.isEmpty(pattern2)) {
            return StrUtil.EMPTY;
        }
        if (StrUtil.isEmpty(pattern1)) {
            return pattern2;
        }
        if (StrUtil.isEmpty(pattern2)) {
            return pattern1;
        }

        boolean pattern1ContainsUriVar = (pattern1.indexOf('{') != -1);
        if (!pattern1.equals(pattern2) && !pattern1ContainsUriVar
                && match(pattern1, pattern2)) {
            // /* + /hotel -> /hotel ; "/*.*" + "/*.html" -> /*.html
            // 但 /user + /user -> /usr/user ; /{foo} + /bar -> /{foo}/bar
            return pattern2;
        }

        // /hotels/* + /booking -> /hotels/booking
        // /hotels/* + booking -> /hotels/booking
        if (pattern1.endsWith(this.pathSeparatorPatternCache.getEndsOnWildCard())) {
            return concat(pattern1.substring(0, pattern1.length() - 2), pattern2);
        }

        // /hotels/** + /booking -> /hotels/**/booking
        // /hotels/** + booking -> /hotels/**/booking
        if (pattern1.endsWith(this.pathSeparatorPatternCache.getEndsOnDoubleWildCard())) {
            return concat(pattern1, pattern2);
        }

        int starDotPos1 = pattern1.indexOf("*.");
        if (pattern1ContainsUriVar || starDotPos1 == -1
                || this.pathSeparator.equals(".")) {
            // 直接拼接两个表达式
            return concat(pattern1, pattern2);
        }

        String ext1 = pattern1.substring(starDotPos1 + 1);
        int dotPos2 = pattern2.indexOf('.');
        String file2 = (dotPos2 == -1 ? pattern2 : pattern2.substring(0, dotPos2));
        String ext2 = (dotPos2 == -1 ? "" : pattern2.substring(dotPos2));
        boolean ext1All = (ext1.equals(".*") || ext1.isEmpty());
        boolean ext2All = (ext2.equals(".*") || ext2.isEmpty());
        if (!ext1All && !ext2All) {
            throw new IllegalArgumentException("Cannot combine patterns: "
                    + pattern1 + " vs " + pattern2);
        }
        String ext = (ext1All ? ext2 : ext1);
        return file2 + ext;
    }

    /**
     * 连接两个路径.
     *
     * @param path1 路径1
     * @param path2 路径2
     * @return 连接后的路径
     */
    private String concat(final String path1, final String path2) {
        boolean path1EndsWithSeparator = path1.endsWith(this.pathSeparator);
        boolean path2StartsWithSeparator = path2.startsWith(this.pathSeparator);

        if (path1EndsWithSeparator && path2StartsWithSeparator) {
            return path1 + path2.substring(1);
        } else if (path1EndsWithSeparator || path2StartsWithSeparator) {
            return path1 + path2;
        } else {
            return path1 + this.pathSeparator + path2;
        }
    }

    /**
     * 获取表达式排序比较器.<br>
     * 排序规则：更具体的表达式排在前面（如无通配符的精确匹配优先于通配符匹配）。
     *
     * @param path 用于比较的完整路径
     * @return 表达式排序比较器
     */
    public Comparator<String> getPatternComparator(final String path) {
        return new AntPatternComparator(path);
    }

    /**
     * 通过正则表达式匹配字符串的匹配器.
     */
    protected static class AntPathStringMatcher {

        /**
         * 通配符模式正则，匹配 ?、* 和 {var} 表达式.
         */
        private static final Pattern GLOB_PATTERN =
                Pattern.compile("\\?|\\*|\\{((?:\\{[^/]+?}|[^/{}]|\\\\[{}])+?)}");

        /**
         * 默认变量模式：匹配任意字符（包括换行）.
         */
        private static final String DEFAULT_VARIABLE_PATTERN = "((?s).*)";

        private final String rawPattern;

        private final boolean caseSensitive;

        private final boolean exactMatch;

        private final Pattern pattern;

        private final List<String> variableNames = new ArrayList<>();

        /**
         * 构造.
         *
         * @param pattern       表达式
         * @param caseSensitive 是否大小写敏感
         */
        public AntPathStringMatcher(final String pattern, final boolean caseSensitive) {
            this.rawPattern = pattern;
            this.caseSensitive = caseSensitive;
            StringBuilder patternBuilder = new StringBuilder();
            Matcher matcher = GLOB_PATTERN.matcher(pattern);
            int end = 0;
            while (matcher.find()) {
                patternBuilder.append(quote(pattern, end, matcher.start()));
                String match = matcher.group();
                if ("?".equals(match)) {
                    patternBuilder.append('.');
                } else if ("*".equals(match)) {
                    patternBuilder.append(".*");
                } else if (match.startsWith("{") && match.endsWith("}")) {
                    int colonIdx = match.indexOf(':');
                    if (colonIdx == -1) {
                        patternBuilder.append(DEFAULT_VARIABLE_PATTERN);
                        this.variableNames.add(matcher.group(1));
                    } else {
                        String variablePattern = match.substring(colonIdx + 1,
                                match.length() - 1);
                        patternBuilder.append('(');
                        patternBuilder.append(variablePattern);
                        patternBuilder.append(')');
                        String variableName = match.substring(1, colonIdx);
                        this.variableNames.add(variableName);
                    }
                }
                end = matcher.end();
            }
            if (end == 0) {
                // 未找到通配符模式，这是精确字符串匹配
                this.exactMatch = true;
                this.pattern = null;
            } else {
                this.exactMatch = false;
                patternBuilder.append(quote(pattern, end, pattern.length()));
                this.pattern = (this.caseSensitive
                        ? Pattern.compile(patternBuilder.toString())
                        : Pattern.compile(patternBuilder.toString(),
                                Pattern.CASE_INSENSITIVE));
            }
        }

        /**
         * 转义字符串中的特殊字符.
         *
         * @param s     字符串
         * @param start 起始位置
         * @param end   结束位置
         * @return 转义后的字符串
         */
        private String quote(final String s, final int start, final int end) {
            if (start == end) {
                return "";
            }
            return Pattern.quote(s.substring(start, end));
        }

        /**
         * 判断字符串是否匹配表达式.
         *
         * @param str                  字符串
         * @param uriTemplateVariables URI模板变量
         * @return 是否匹配
         */
        public boolean matchStrings(final String str,
                final Map<String, String> uriTemplateVariables) {
            if (this.exactMatch) {
                return this.caseSensitive
                        ? this.rawPattern.equals(str) : this.rawPattern.equalsIgnoreCase(str);
            } else if (this.pattern != null) {
                Matcher matcher = this.pattern.matcher(str);
                if (matcher.matches()) {
                    if (uriTemplateVariables != null) {
                        if (this.variableNames.size() != matcher.groupCount()) {
                            throw new IllegalArgumentException(
                                    "The number of capturing groups in the pattern segment "
                                    + this.pattern
                                    + " does not match the number of URI template variables"
                                    + " it defines, which can occur if capturing groups are"
                                    + " used in a URI template regex."
                                    + " Use non-capturing groups instead.");
                        }
                        for (int i = 1; i <= matcher.groupCount(); i++) {
                            String name = this.variableNames.get(i - 1);
                            if (name.startsWith("*")) {
                                throw new IllegalArgumentException(
                                        "Capturing patterns (" + name + ") are not "
                                        + "supported by the AntPathMatcher."
                                        + " Use the PathPatternParser instead.");
                            }
                            String value = matcher.group(i);
                            uriTemplateVariables.put(name, value);
                        }
                    }
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 表达式排序比较器.<br>
     * 按照详细程度排序，越具体的表达式排在越前面.
     */
    protected static class AntPatternComparator implements Comparator<String> {

        private final String path;

        /**
         * 构造.
         *
         * @param path 路径
         */
        public AntPatternComparator(final String path) {
            this.path = path;
        }

        /**
         * 比较两个表达式的详细程度.
         *
         * @param pattern1 表达式1
         * @param pattern2 表达式2
         * @return 比较结果
         */
        @Override
        public int compare(final String pattern1, final String pattern2) {
            PatternInfo info1 = new PatternInfo(pattern1);
            PatternInfo info2 = new PatternInfo(pattern2);

            if (info1.isLeastSpecific() && info2.isLeastSpecific()) {
                return 0;
            } else if (info1.isLeastSpecific()) {
                return 1;
            } else if (info2.isLeastSpecific()) {
                return -1;
            }

            boolean pattern1EqualsPath = pattern1.equals(this.path);
            boolean pattern2EqualsPath = pattern2.equals(this.path);
            if (pattern1EqualsPath && pattern2EqualsPath) {
                return 0;
            } else if (pattern1EqualsPath) {
                return -1;
            } else if (pattern2EqualsPath) {
                return 1;
            }

            if (info1.isPrefixPattern() && info2.isPrefixPattern()) {
                return info2.getLength() - info1.getLength();
            } else if (info1.isPrefixPattern() && info2.getDoubleWildcards() == 0) {
                return 1;
            } else if (info2.isPrefixPattern() && info1.getDoubleWildcards() == 0) {
                return -1;
            }

            if (info1.getTotalCount() != info2.getTotalCount()) {
                return info1.getTotalCount() - info2.getTotalCount();
            }

            if (info1.getLength() != info2.getLength()) {
                return info2.getLength() - info1.getLength();
            }

            if (info1.getSingleWildcards() < info2.getSingleWildcards()) {
                return -1;
            } else if (info2.getSingleWildcards() < info1.getSingleWildcards()) {
                return 1;
            }

            if (info1.getUriVars() < info2.getUriVars()) {
                return -1;
            } else if (info2.getUriVars() < info1.getUriVars()) {
                return 1;
            }

            return 0;
        }

        /**
         * 表达式信息.
         */
        private static class PatternInfo {

            private final String pattern;
            private int uriVars;
            private int singleWildcards;
            private int doubleWildcards;
            private boolean catchAllPattern;
            private boolean prefixPattern;
            private Integer length;

            PatternInfo(final String pattern) {
                this.pattern = pattern;
                if (this.pattern != null) {
                    initCounters();
                    this.catchAllPattern = this.pattern.equals("/**");
                    this.prefixPattern = !this.catchAllPattern
                            && this.pattern.endsWith("/**");
                }
                if (this.uriVars == 0) {
                    this.length = (this.pattern != null ? this.pattern.length() : 0);
                }
            }

            /**
             * 初始化计数器.
             */
            protected void initCounters() {
                int pos = 0;
                if (this.pattern != null) {
                    while (pos < this.pattern.length()) {
                        if (this.pattern.charAt(pos) == '{') {
                            this.uriVars++;
                            pos++;
                        } else if (this.pattern.charAt(pos) == '*') {
                            if (pos + 1 < this.pattern.length()
                                    && this.pattern.charAt(pos + 1) == '*') {
                                this.doubleWildcards++;
                                pos += 2;
                            } else if (pos > 0
                                    && !this.pattern.substring(pos - 1, pos + 1)
                                            .equals(".*")) {
                                this.singleWildcards++;
                                pos++;
                            } else {
                                pos++;
                            }
                        } else {
                            pos++;
                        }
                    }
                }
            }

            /**
             * 获取URI变量数量.
             *
             * @return URI变量数量
             */
            public int getUriVars() {
                return this.uriVars;
            }

            /**
             * 获取单通配符数量.
             *
             * @return 单通配符数量
             */
            public int getSingleWildcards() {
                return this.singleWildcards;
            }

            /**
             * 获取双通配符数量.
             *
             * @return 双通配符数量
             */
            public int getDoubleWildcards() {
                return this.doubleWildcards;
            }

            /**
             * 是否最不具体（匹配所有）.
             *
             * @return 是否最不具体
             */
            public boolean isLeastSpecific() {
                return (this.pattern == null || this.catchAllPattern);
            }

            /**
             * 是否为前缀模式.
             *
             * @return 是否为前缀模式
             */
            public boolean isPrefixPattern() {
                return this.prefixPattern;
            }

            /**
             * 获取通配符总数（加权）.
             *
             * @return 通配符总数
             */
            public int getTotalCount() {
                return this.uriVars + this.singleWildcards + (2 * this.doubleWildcards);
            }

            /**
             * 获取表达式长度，其中模板变量计为1.
             *
             * @return 长度
             */
            public int getLength() {
                if (this.length == null) {
                    this.length = (this.pattern != null
                            ? VARIABLE_PATTERN.matcher(this.pattern)
                                    .replaceAll("#").length()
                            : 0);
                }
                return this.length;
            }
        }
    }

    /**
     * 路径分隔符模式缓存.
     */
    private static class PathSeparatorPatternCache {

        /**
         * 以通配符结尾的模式.
         */
        private final String endsOnWildCard;

        /**
         * 以双通配符结尾的模式.
         */
        private final String endsOnDoubleWildCard;

        /**
         * 构造.
         *
         * @param pathSeparator 路径分隔符
         */
        PathSeparatorPatternCache(final String pathSeparator) {
            this.endsOnWildCard = pathSeparator + "*";
            this.endsOnDoubleWildCard = pathSeparator + "**";
        }

        /**
         * 获取以单通配符结尾的模式.
         *
         * @return 以单通配符结尾的模式
         */
        public String getEndsOnWildCard() {
            return this.endsOnWildCard;
        }

        /**
         * 获取以双通配符结尾的模式.
         *
         * @return 以双通配符结尾的模式
         */
        public String getEndsOnDoubleWildCard() {
            return this.endsOnDoubleWildCard;
        }
    }
}
