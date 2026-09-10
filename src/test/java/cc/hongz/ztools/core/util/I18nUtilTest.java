package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link I18nUtil} 单元测试。
 */
@DisplayName("I18nUtil 国际化消息工具测试")
class I18nUtilTest {

    @Test
    @DisplayName("getMessage 按英文 locale 取消息")
    void getMessageByEnglishLocale() {
        assertEquals("Hello!", I18nUtil.getMessage("messages", "greeting", Locale.US));
    }

    @Test
    @DisplayName("getMessage 按中文 locale 取消息")
    void getMessageByChineseLocale() {
        assertEquals("你好!", I18nUtil.getMessage("messages", "greeting", Locale.SIMPLIFIED_CHINESE));
    }

    @Test
    @DisplayName("getMessage 支持占位符参数替换")
    void getMessageWithParams() {
        assertEquals("Welcome, Tom!", I18nUtil.getMessage("messages", "welcome", Locale.US, "Tom"));
    }

    @Test
    @DisplayName("getMessage 未找到 key 时返回 key 本身")
    void getMessageMissingKeyReturnsKey() {
        assertEquals("nonexistent", I18nUtil.getMessage("messages", "nonexistent", Locale.US));
    }
}
