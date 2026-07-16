package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ReUtil 测试.
 */
class ReUtilTest {

    final String content = "ZZZaaabbbccc中文1234";

    // ---- get ----

    @Test
    void getTest() {
        assertEquals("ZZ", ReUtil.get("\\w{2}", content, 0));
    }

    @Test
    void getGroup0Test() {
        assertEquals("ZZ", ReUtil.getGroup0("\\w{2}", content));
    }

    @Test
    void getGroup1Test() {
        assertEquals("Z", ReUtil.getGroup1("(\\w)Z", content));
    }

    @Test
    void getWithPatternTest() {
        Pattern pattern = Pattern.compile("\\w{2}");
        assertEquals("ZZ", ReUtil.getGroup0(pattern, content));
        assertEquals("Za", ReUtil.getGroup1(pattern, content));
    }

    @Test
    void getByGroupNameTest() {
        final String dateStr = "2021-10-11";
        final String regex = "(?<year>\\d+)-(?<month>\\d+)-(?<day>\\d+)";
        assertEquals("2021", ReUtil.get(regex, dateStr, "year"));
        assertEquals("10", ReUtil.get(regex, dateStr, "month"));
        assertEquals("11", ReUtil.get(regex, dateStr, "day"));
    }

    @Test
    void getNullTest() {
        assertNull(ReUtil.get("\\d+", null, 0));
        assertNull(ReUtil.get(null, "abc", 0));
        assertNull(ReUtil.get((Pattern) null, "abc", 0));
        assertNull(ReUtil.get(Pattern.compile("\\d+"), null, 0));
    }

    @Test
    void getByPatternAndGroupIndexTest() {
        Pattern pattern = Pattern.compile("(\\d+)");
        assertEquals("1234", ReUtil.get(pattern, content, 1));
    }

    // ---- getAllGroups ----

    @Test
    void getAllGroupsTest() {
        Pattern pattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+)");
        List<String> allGroups = ReUtil.getAllGroups(pattern, "192-168-1-1");
        assertEquals("192-168-1", allGroups.get(0));
        assertEquals("192", allGroups.get(1));
        assertEquals("168", allGroups.get(2));
        assertEquals("1", allGroups.get(3));
    }

    @Test
    void getAllGroupsNoGroup0Test() {
        Pattern pattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+)");
        List<String> allGroups = ReUtil.getAllGroups(pattern, "192-168-1-1", false);
        assertEquals("192", allGroups.get(0));
        assertEquals("168", allGroups.get(1));
        assertEquals("1", allGroups.get(2));
    }

    @Test
    void getAllGroupsFindAllTest() {
        Pattern pattern = Pattern.compile("(\\d+)");
        List<String> allGroups = ReUtil.getAllGroups(pattern, "12-34", true, true);
        assertEquals(2, allGroups.size());
        assertEquals("12", allGroups.get(0));
        assertEquals("34", allGroups.get(1));
    }

    @Test
    void getAllGroupsNullTest() {
        assertNull(ReUtil.getAllGroups(null, "abc"));
        assertNull(ReUtil.getAllGroups(Pattern.compile("\\d+"), null));
    }

    // ---- extractMulti ----

    @Test
    void extractMultiTest() {
        assertEquals("Z-a", ReUtil.extractMulti("(\\w)aa(\\w)", content, "$1-$2"));
    }

    @Test
    void extractMultiWithStringRegexTest() {
        assertEquals("Z-a", ReUtil.extractMulti("(\\w)aa(\\w)", content, "$1-$2"));
    }

    @Test
    void extractMultiNullTest() {
        assertNull(ReUtil.extractMulti((Pattern) null, content, "$1"));
        assertNull(ReUtil.extractMulti(Pattern.compile("(\\w)"), null, "$1"));
        assertNull(ReUtil.extractMulti(Pattern.compile("(\\w)"), content, null));
    }

    // ---- delFirst ----

    @Test
    void delFirstTest() {
        assertEquals("ZZbbbccc中文1234", ReUtil.delFirst("(\\w)aa(\\w)", content));
    }

    @Test
    void delFirstWithPatternTest() {
        assertEquals("ZZbbbccc中文1234", ReUtil.delFirst(Pattern.compile("(\\w)aa(\\w)"), content));
    }

    @Test
    void delFirstBlankTest() {
        assertEquals("abc", ReUtil.delFirst("", "abc"));
        assertEquals("abc", ReUtil.delFirst((String) null, "abc"));
    }

    // ---- delLast ----

    @Test
    void delLastTest() {
        assertEquals("公斤", ReUtil.delLast("\\d+", "180公斤"));
    }

    @Test
    void delLastWithPatternTest() {
        assertEquals("公斤", ReUtil.delLast(Pattern.compile("\\d+"), "180公斤"));
    }

    @Test
    void delLastMultipleMatchesTest() {
        String sentence = "10.商品KLS100021型号xxl适合身高180体重130斤的用户";
        String result = ReUtil.delLast("\\d+", sentence);
        assertEquals("10.商品KLS100021型号xxl适合身高180体重斤的用户", result);
    }

    @Test
    void delLastEmptyTest() {
        assertEquals("", ReUtil.delLast("\\d+", ""));
    }

    // ---- delAll ----

    @Test
    void delAllTest() {
        String c = "发东方大厦eee![images]http://abc.com/2.gpg]好机会eee![images]http://abc.com/2.gpg]好机会";
        assertEquals("发东方大厦eee好机会eee好机会", ReUtil.delAll("!\\[images\\][^\\u4e00-\\u9fa5\\\\s]*", c));
    }

    @Test
    void delAllWithPatternTest() {
        assertEquals("ZZZaaabbbccc中文", ReUtil.delAll("\\d+", content));
    }

    @Test
    void delAllEmptyTest() {
        assertEquals("abc", ReUtil.delAll("\\d+", "abc"));
    }

    // ---- delPre ----

    @Test
    void delPreTest() {
        assertEquals("bbbccc中文1234", ReUtil.delPre("\\w+aa", content));
    }

    @Test
    void delPreWithPatternTest() {
        assertEquals("bbbccc中文1234", ReUtil.delPre(Pattern.compile("\\w+aa"), content));
    }

    @Test
    void delPreNoMatchTest() {
        assertEquals("abc", ReUtil.delPre("\\d+", "abc"));
    }

    // ---- findAll ----

    @Test
    void findAllTest() {
        List<String> result = ReUtil.findAll("\\w{2}", content, 0, new ArrayList<>());
        List<String> expected = List.of("ZZ", "Za", "aa", "bb", "bc", "cc", "12", "34");
        assertEquals(expected, result);
    }

    @Test
    void findAllGroup0Test() {
        List<String> result = ReUtil.findAllGroup0("\\d+", content);
        assertEquals(1, result.size());
        assertEquals("1234", result.get(0));
    }

    @Test
    void findAllGroup1Test() {
        List<String> result = ReUtil.findAllGroup1("(\\d+)", content);
        assertEquals(1, result.size());
        assertEquals("1234", result.get(0));
    }

    @Test
    void findAllWithPatternTest() {
        Pattern pattern = Pattern.compile("\\d+");
        List<String> result = ReUtil.findAll(pattern, content, 0);
        assertEquals(1, result.size());
        assertEquals("1234", result.get(0));
    }

    @Test
    void findAllWithPatternGroup0Test() {
        List<String> result = ReUtil.findAllGroup0(Pattern.compile("\\d+"), content);
        assertEquals(1, result.size());
    }

    @Test
    void findAllWithPatternGroup1Test() {
        List<String> result = ReUtil.findAllGroup1(Pattern.compile("(\\d+)"), content);
        assertEquals(1, result.size());
    }

    @Test
    void findAllNullTest() {
        assertNull(ReUtil.findAll((Pattern) null, "abc", 0, new ArrayList<>()));
        assertNull(ReUtil.findAll(Pattern.compile("\\d+"), null, 0, new ArrayList<>()));
    }

    @Test
    void findAllWithConsumerTest() {
        Pattern pattern = Pattern.compile("\\d+");
        List<String> results = new ArrayList<>();
        ReUtil.findAll(pattern, content, matcher -> results.add(matcher.group()));
        assertEquals(1, results.size());
        assertEquals("1234", results.get(0));
    }

    @Test
    void findAllNullRegexTest() {
        List<String> result = ReUtil.findAll((String) null, content, 0, new ArrayList<>());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ---- count ----

    @Test
    void countTest() {
        assertEquals(2, ReUtil.count("\\w{3}", content));
    }

    @Test
    void countWithPatternTest() {
        assertEquals(1, ReUtil.count(Pattern.compile("\\d+"), content));
    }

    @Test
    void countNullTest() {
        assertEquals(0, ReUtil.count(null, content));
        assertEquals(0, ReUtil.count("\\d+", null));
        assertEquals(0, ReUtil.count((Pattern) null, null));
    }

    // ---- contains ----

    @Test
    void containsTest() {
        assertTrue(ReUtil.contains("\\d+", content));
        assertFalse(ReUtil.contains("\\d+", "abc"));
    }

    @Test
    void containsWithPatternTest() {
        assertTrue(ReUtil.contains(Pattern.compile("\\d+"), content));
        assertFalse(ReUtil.contains(Pattern.compile("\\d+"), "abc"));
    }

    @Test
    void containsNullTest() {
        assertFalse(ReUtil.contains(null, "abc"));
        assertFalse(ReUtil.contains("\\d+", null));
    }

    // ---- indexOf / lastIndexOf ----

    @Test
    void indexOfTest() {
        MatchResult result = ReUtil.indexOf("\\d+", content);
        assertNotNull(result);
        assertEquals(12, result.start());
    }

    @Test
    void indexOfWithPatternTest() {
        MatchResult result = ReUtil.indexOf(Pattern.compile("\\d+"), content);
        assertNotNull(result);
        assertEquals(12, result.start());
    }

    @Test
    void indexOfNoMatchTest() {
        assertNull(ReUtil.indexOf("\\d+", "abc"));
        assertNull(ReUtil.indexOf(null, "abc"));
    }

    @Test
    void lastIndexOfTest() {
        MatchResult result = ReUtil.lastIndexOf("\\d+", "12-34-56");
        assertNotNull(result);
        assertEquals("56", result.group());
    }

    @Test
    void lastIndexOfWithPatternTest() {
        MatchResult result = ReUtil.lastIndexOf(Pattern.compile("\\d+"), "12-34-56");
        assertNotNull(result);
        assertEquals("56", result.group());
    }

    @Test
    void lastIndexOfNoMatchTest() {
        assertNull(ReUtil.lastIndexOf("\\d+", "abc"));
    }

    // ---- getFirstNumber ----

    @Test
    void getFirstNumberTest() {
        assertEquals(Integer.valueOf(1234), ReUtil.getFirstNumber(content));
    }

    @Test
    void getFirstNumberNoNumberTest() {
        assertNull(ReUtil.getFirstNumber("abc"));
    }

    // ---- isMatch ----

    @Test
    void isMatchTest() {
        assertTrue(ReUtil.isMatch("\\w+[\\u4E00-\\u9FFF]+\\d+", content));
    }

    @Test
    void isMatchWithPatternTest() {
        assertTrue(ReUtil.isMatch(Pattern.compile("\\w+[\\u4E00-\\u9FFF]+\\d+"), content));
    }

    @Test
    void isMatchNullTest() {
        assertFalse(ReUtil.isMatch("\\d+", null));
        assertTrue(ReUtil.isMatch("", "abc"));
        assertTrue(ReUtil.isMatch((String) null, "abc"));
    }

    @Test
    void isMatchProvinceTest() {
        assertTrue(ReUtil.isMatch("(.+?)省(.+?)市(.+?)区", "广东省深圳市南山区"));
    }

    // ---- replaceAll ----

    @Test
    void replaceAllTemplateTest() {
        assertEquals("ZZZaaabbbccc中文->1234<-", ReUtil.replaceAll(content, "(\\d+)", "->$1<-"));
    }

    @Test
    void replaceAllWithPatternTemplateTest() {
        assertEquals("ZZZaaabbbccc中文->1234<-",
                ReUtil.replaceAll(content, Pattern.compile("(\\d+)"), "->$1<-"));
    }

    @Test
    void replaceAllFuncTest() {
        assertEquals("ZZZaaabbbccc中文->1234<-",
                ReUtil.replaceAll(content, "(\\d+)", parameters -> "->" + parameters.group(1) + "<-"));
    }

    @Test
    void replaceAllWithPatternFuncTest() {
        assertEquals("ZZZaaabbbccc中文->1234<-",
                ReUtil.replaceAll(content, Pattern.compile("(\\d+)"), parameters -> "->" + parameters.group(1) + "<-"));
    }

    @Test
    void replaceAllEmptyTest() {
        assertEquals("abc", ReUtil.replaceAll("abc", "\\d+", "$1"));
        assertNull(ReUtil.replaceAll(null, "\\d+", "$1"));
    }

    @Test
    void replaceAllIPTest() {
        Pattern patternIp = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})\\.((2(5[0-5]|[0-4]\\d))"
                + "|[0-1]?\\d{1,2})\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})");
        assertEquals("1.**.**.4", ReUtil.replaceAll("1.2.3.4", patternIp, "$1.**.**.$10"));
    }

    // ---- escape ----

    @Test
    void escapeCharTest() {
        assertEquals("\\$", ReUtil.escape('$'));
        assertEquals("a", ReUtil.escape('a'));
    }

    @Test
    void escapeStringTest() {
        assertEquals("我有个\\$符号\\{\\}", ReUtil.escape("我有个$符号{}"));
    }

    @Test
    void escapeWithMatchTest() {
        String str = "a[bbbc";
        String re = "[";
        assertEquals("[", ReUtil.get(ReUtil.escape(re), str, 0));
    }

    @Test
    void escapeIsMatchTest() {
        String context = "{prefix}_";
        String regex = "{prefix}_";
        assertTrue(ReUtil.isMatch(ReUtil.escape(regex), context));
    }

    @Test
    void escapeBlankTest() {
        assertNull(ReUtil.escape((CharSequence) null));
        assertEquals("", ReUtil.escape(""));
    }

    // ---- RE_KEYS ----

    @Test
    void reKeysTest() {
        assertNotNull(ReUtil.RE_KEYS);
        assertTrue(ReUtil.RE_KEYS.contains('$'));
        assertTrue(ReUtil.RE_KEYS.contains('*'));
        assertTrue(ReUtil.RE_KEYS.contains('.'));
        assertFalse(ReUtil.RE_KEYS.contains('a'));
    }

    // ---- replaceFirst ----

    @Test
    void replaceFirstTest() {
        assertEquals("ZZZbbbccc中文1234",
                ReUtil.replaceFirst(Pattern.compile("aa"), content, ""));
    }

    @Test
    void replaceFirstNullTest() {
        assertEquals("abc", ReUtil.replaceFirst(null, "abc", ""));
        assertEquals("abc", ReUtil.replaceFirst(Pattern.compile("\\d+"), "abc", "X"));
    }

    // ---- issue tests ----

    @Test
    void issueI9T1TGTest() {
        String regex = "^model";
        String c = "model-v";
        assertEquals("model", ReUtil.get(regex, c, 0));

        regex = "^model.*?";
        c = "model-v";
        assertTrue(ReUtil.isMatch(regex, c));
    }

    @Test
    void getEmailAddressTest() {
        Pattern emailPattern = Pattern.compile("[\\w.]+@[\\w.]+");
        String mail = "姓名<a.b@Hutool.cn>";
        assertEquals("a.b@Hutool.cn", ReUtil.get(emailPattern, mail, 0));

        mail = "a.b@Hutool.cn";
        assertEquals("a.b@Hutool.cn", ReUtil.get(emailPattern, mail, 0));
    }

    @Test
    void delAllWhitespaceTest() {
        assertEquals("", ReUtil.delAll("[\\s]*", " "));
    }
}
