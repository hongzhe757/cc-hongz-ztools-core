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

package cc.hongz.ztools.core.date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("IssueI7QI6R 解析日期")

public class IssueI7QI6RTest {
    @Test
    @DisplayName("解析日期")
    public void parseTest() {
        assertThrows(DateException.class, () -> {
            DateUtil.parse("2023-08-04 1");
        });
    }

    @Test
    @DisplayName("解析日期（另一种）")
    public void parseTest2() {
        assertThrows(DateException.class, () -> {
            DateUtil.parse("2023-08-04-1");
        });
    }
}
