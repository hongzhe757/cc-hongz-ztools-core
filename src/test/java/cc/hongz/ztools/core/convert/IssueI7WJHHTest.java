/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cc.hongz.ztools.core.convert;

import cc.hongz.ztools.core.lang.Opt;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@DisplayName("IssueI7WJHH Optional/Opt 转 int")
public class IssueI7WJHHTest {

    @Test
    @DisplayName("Optional 转 int")
    public void toIntTest() {
        final Optional<Integer> optional = Optional.of(1);
        final Integer integer = Convert.toInt(optional);

        assertEquals(Integer.valueOf(1), integer);
    }

    @Test
    @DisplayName("Opt 转 int")
    public void toIntTest2() {
        final Opt<Integer> optional = Opt.of(1);
        final Integer integer = Convert.toInt(optional);

        assertEquals(Integer.valueOf(1), integer);
    }
}
