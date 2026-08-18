package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.collection.CollUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link Opt} 单元测试。
 *
 * @author VampireAchao
 */
@DisplayName("Opt 可选值")
class OptTest {

    @Test
    void ofBlankAbleTest() {
        // ofBlankAble相对于ofNullable考虑了字符串为空串的情况
        final String hutool = Opt.ofBlankAble("").orElse("hutool");
        assertEquals("hutool", hutool);
    }

    @Test
    void getTest() {
        // 和原版Optional有区别的是，get不会抛出NoSuchElementException
        // 如果想使用原版Optional中的get这样，获取一个一定不为空的值，则应该使用orElseThrow
        final Object opt = Opt.ofNullable(null).get();
        assertNull(opt);
    }

    @Test
    void isEmptyTest() {
        // 这是jdk11 Optional中的新函数，直接照搬了过来
        // 判断包裹内元素是否为空，注意并没有判断空字符串的情况
        final boolean isEmpty = Opt.empty().isEmpty();
        assertTrue(isEmpty);
    }

    @Test
    @Disabled
    void ifPresentOrElseTest() {
        // 存在就打印对应的值，不存在则用{@code System.err.println}打印另一句字符串
        Opt.ofNullable("Hello Hutool!").ifPresentOrElse(Console::log, () -> Console.error("Ops!Something is wrong!"));

        Opt.empty().ifPresentOrElse(Console::log, () -> Console.error("Ops!Something is wrong!"));

        // 拓展为支持链式调用
        Opt.empty().ifPresentOrElse(Console::log, () -> Console.error("Ops!Something is wrong!"))
                .ifPresentOrElse(Console::log, () -> Console.error("Ops!Something is wrong!"));
    }

    @Test
    void peekTest() {
        final User user = new User();
        // 相当于ifPresent的链式调用
        Opt.ofNullable("hutool").peek(user::setUsername).peek(user::setNickname);
        assertEquals("hutool", user.getNickname());
        assertEquals("hutool", user.getUsername());

        // 注意，传入的lambda中，对包裹内的元素执行赋值操作并不会影响到原来的元素
        final String name = Opt.ofNullable("hutool").peek(username -> username = "123").peek(username -> username = "456").get();
        assertEquals("hutool", name);
    }

    @Test
    void peeksTest() {
        final User user = new User();
        // 相当于上面peek的动态参数调用，更加灵活，你可以像操作数组一样去动态设置中间的步骤，也可以使用这种方式去编写你的代码
        // 可以一行搞定
        Opt.ofNullable("hutool").peeks(user::setUsername, user::setNickname);
        // 也可以在适当的地方换行使得代码的可读性提高
        Opt.of(user).peeks(
                u -> assertEquals("hutool", u.getNickname()),
                u -> assertEquals("hutool", u.getUsername())
        );
        assertEquals("hutool", user.getNickname());
        assertEquals("hutool", user.getUsername());

        // 注意，传入的lambda中，对包裹内的元素执行赋值操作并不会影响到原来的元素,这是java语言的特性。。。
        // 这也是为什么我们需要getter和setter而不直接给bean中的属性赋值中的其中一个原因
        final String name = Opt.ofNullable("hutool").peeks(
                username -> username = "123", username -> username = "456",
                n -> assertEquals("hutool", n)).get();
        assertEquals("hutool", name);

        // 当然，以下情况不会抛出NPE，但也没什么意义
        Opt.ofNullable("hutool").peeks().peeks().peeks();
        Opt.ofNullable(null).peeks(i -> {
        });

    }

    @Test
    void orTest() {
        // 这是jdk9 Optional中的新函数，直接照搬了过来
        // 给一个替代的Opt
        final String str = Opt.<String>ofNullable(null).or(() -> Opt.ofNullable("Hello hutool!")).map(String::toUpperCase).orElseThrow();
        assertEquals("HELLO HUTOOL!", str);

        final User user = User.builder().username("hutool").build();
        final Opt<User> userOpt = Opt.of(user);
        // 获取昵称，获取不到则获取用户名
        final String name = userOpt.map(User::getNickname).or(() -> userOpt.map(User::getUsername)).get();
        assertEquals("hutool", name);
    }

    @Test
    void orElseThrowTest() {
        assertThrows(NoSuchElementException.class, () -> {
            // 获取一个不可能为空的值，否则抛出NoSuchElementException异常
            final Object obj = Opt.ofNullable(null).orElseThrow();
            assertNull(obj);
        });
    }

    @Test
    void orElseThrowTest2() {
        assertThrows(IllegalStateException.class, () -> {
            // 获取一个不可能为空的值，否则抛出自定义异常
            final Object assignException = Opt.ofNullable(null).orElseThrow(IllegalStateException::new);
            assertNull(assignException);
        });
    }

    @Test
    void orElseThrowTest3() {
        assertThrows(IllegalStateException.class, () -> {
            // 获取一个不可能为空的值，否则抛出带自定义消息的自定义异常
            final Object exceptionWithMessage = Opt.empty().orElseThrow(IllegalStateException::new, "Ops!Something is wrong!");
            assertNull(exceptionWithMessage);
        });
    }

    @Test
    void flattedMapTest() {
        // 和Optional兼容的flatMap
        final List<User> userList = new ArrayList<>();
        // 以前，不兼容
        // Opt.ofNullable(userList).map(List::stream).flatMap(Stream::findFirst);
        // 现在，兼容
        final User user = Opt.ofNullable(userList).map(List::stream)
                .flattedMap(Stream::findFirst).orElseGet(User.builder()::build);
        assertNull(user.getUsername());
        assertNull(user.getNickname());
    }

    @Test
    void ofEmptyAbleTest() {
        // 以前，输入一个CollectionUtil感觉要命，类似前缀的类一大堆，代码补全形同虚设(在项目中起码要输入完CollectionUtil才能在第一个调出这个函数)
        // 关键它还很常用，判空和判空集合真的太常用了...
        final List<String> past = Opt.ofNullable(Collections.<String>emptyList()).filter(CollUtil::isNotEmpty).orElseGet(() -> Collections.singletonList("hutool"));
        // 现在，一个ofEmptyAble搞定
        final List<String> hutool = Opt.ofEmptyAble(Collections.<String>emptyList()).orElseGet(() -> Collections.singletonList("hutool"));
        assertEquals(past, hutool);
        assertEquals(hutool, Collections.singletonList("hutool"));
    }

    @Test
    void mapOrElseTest() {
        // 如果值存在就转换为大写，否则打印一句字符串，支持链式调用、转换为其他类型
        final String hutool = Opt.ofBlankAble("hutool").mapOrElse(String::toUpperCase, () -> Console.log("yes")).mapOrElse(String::intern, () -> Console.log("Value is not present~")).get();
        assertEquals("HUTOOL", hutool);
    }

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "ConstantConditions"})
    @Test
    void execTest() {
        // 有一些资深的程序员跟我说你这个lambda，双冒号语法糖看不懂...
        // 为了尊重资深程序员的意见，并且提升代码可读性，封装了一下 "try catch NPE 和 数组越界"的情况

        // 以前这种写法，简洁但可读性稍低，对资深程序员不太友好
        final List<String> last = null;
        final String npeSituation = Opt.ofEmptyAble(last).flattedMap(l -> l.stream().findFirst()).orElse("hutool");
        final String indexOutSituation = Opt.ofEmptyAble(last).map(l -> l.get(0)).orElse("hutool");

        // 现在代码整洁度降低，但可读性up，如果再人说看不懂这代码...
        final String npe = Opt.ofTry(() -> last.get(0)).exceptionOrElse("hutool");
        final String indexOut = Opt.ofTry(() -> {
            final List<String> list = new ArrayList<>();
            // 你可以在里面写一长串调用链 list.get(0).getUser().getId()
            return list.get(0);
        }).exceptionOrElse("hutool");
        assertEquals(npe, npeSituation);
        assertEquals(indexOut, indexOutSituation);
        assertEquals("hutool", npe);
        assertEquals("hutool", indexOut);

        // 多线程下情况测试
        Stream.iterate(0, i -> ++i).limit(20000).parallel().forEach(i -> {
            final Opt<Object> opt = Opt.ofTry(() -> {
                if (i % 2 == 0) {
                    throw new IllegalStateException(i + "");
                } else {
                    throw new NullPointerException(i + "");
                }
            });
            assertTrue(
                    (i % 2 == 0 && opt.getException() instanceof IllegalStateException) ||
                            (i % 2 != 0 && opt.getException() instanceof NullPointerException)
            );
        });
    }

    static class User {
        private String username;
        private String nickname;

        public String getUsername() {
            return username;
        }

        public void setUsername(final String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(final String nickname) {
            this.nickname = nickname;
        }

        public static UserBuilder builder() {
            return new UserBuilder();
        }

        static class UserBuilder {
            private String username;
            private String nickname;

            public UserBuilder username(final String username) {
                this.username = username;
                return this;
            }

            public User build() {
                final User user = new User();
                user.setUsername(this.username);
                user.setNickname(this.nickname);
                return user;
            }
        }
    }
}
