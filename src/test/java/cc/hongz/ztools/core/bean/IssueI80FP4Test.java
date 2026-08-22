package cc.hongz.ztools.core.bean;

import cc.hongz.ztools.core.bean.copier.CopyOptions;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BeanUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("回归测试")
class IssueI80FP4Test {
    @Test
    @DisplayName("copyProperties ignoreProperties caseSensitive")
    void copyPropertiesTest() {
        final Dest sourceDest = new Dest();
        sourceDest.setCPF(33699);
        sourceDest.setEnderDest("abc");

        final Dest dest = new Dest();
        final CopyOptions copyOptions = CopyOptions.create().setIgnoreNullValue(true).setIgnoreCase(true).setIgnoreProperties("enderDest");
        BeanUtil.copyProperties(sourceDest, dest, copyOptions);
        assertNull(dest.getEnderDest());
    }

    @Test
    @DisplayName("copyProperties ignoreProperties ignoreCase")
    void copyPropertiesTest2() {
        final Dest sourceDest = new Dest();
        sourceDest.setCPF(33699);
        sourceDest.setEnderDest("abc");

        final Dest dest = new Dest();
        final CopyOptions copyOptions = CopyOptions.create().setIgnoreNullValue(true).setIgnoreCase(true).setIgnoreProperties("enderdest");
        BeanUtil.copyProperties(sourceDest, dest, copyOptions);
        assertNull(dest.getEnderDest());
    }

    @Data
    static class Dest{
        private int cPF;
        private String enderDest;
    }
}
