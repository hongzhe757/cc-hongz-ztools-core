package cc.hongz.ztools.core.bean;

import cc.hongz.ztools.core.annotation.Alias;
import lombok.Data;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BeanUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("回归测试")
class IssueI8JASOTest {

    @Test
    @DisplayName("copyProperties aliasFieldCopied")
    void copyTest() {
        final UserOne userOne = new UserOne();
        userOne.setEmail("123@qq.com");
        final UserTwo userTwo = new UserTwo();
        BeanUtil.copyProperties(userOne, userTwo);
        assertEquals(userOne.getEmail(), userTwo.getEmail());
    }

    @Data
    public static class UserOne {
        private Long id;
        @Alias("邮箱")
        private String email;
    }

    @Data
    public static class UserTwo {
        private Long id;
        @Alias("邮箱")
        private String email;
    }
}
