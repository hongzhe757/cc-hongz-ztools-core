package cc.hongz.ztools.core.bean;

import cc.hongz.ztools.core.annotation.Alias;
import cc.hongz.ztools.core.bean.copier.CopyOptions;
import cc.hongz.ztools.core.map.MapUtil;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;

/**
 * {@link BeanUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("回归测试")
class Issue1687Test {

    /**
     * 验证别名错位导致字段无法匹配时返回 null.
     */
    @Test
    @DisplayName("别名错位返回 null")
    void toBeanTest(){
        final SysUserFb sysUserFb = new SysUserFb();
        sysUserFb.setDepId("123");
        sysUserFb.setCustomerId("456");

        final SysUser sysUser = BeanUtil.toBean(sysUserFb, SysUser.class);
        // 别名错位导致找不到字段
        assertNull(sysUser.getDepart());
        assertEquals(new Long(456L), sysUser.getOrgId());
    }

    /**
     * 验证通过字段映射补救别名错位，正确映射字段.
     */
    @Test
    @DisplayName("字段映射补救别名")
    void toBeanTest2(){
        final SysUserFb sysUserFb = new SysUserFb();
        sysUserFb.setDepId("123");
        sysUserFb.setCustomerId("456");

        // 补救别名错位
        final CopyOptions copyOptions = CopyOptions.create().setFieldMapping(
                MapUtil.builder("depart", "depId").build()
        );
        final SysUser sysUser = BeanUtil.toBean(sysUserFb, SysUser.class, copyOptions);

        assertEquals(new Long(123L), sysUser.getDepart());
        assertEquals(new Long(456L), sysUser.getOrgId());
    }

    @Data
    static class SysUserFb implements Serializable {

        private static final long serialVersionUID = 1L;

        @Alias("depart")
        private String depId;

        @Alias("orgId")
        private String customerId;
    }

    @Data
    static class SysUser implements Serializable {

        private static final long serialVersionUID = 1L;

        @Alias("depId")
        private Long depart;

        private Long orgId;
    }
}
