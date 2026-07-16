package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JNDIUtil 单元测试
 */
class JNDIUtilTest {

	@Test
	void createInitialDirContextWithDangerousProtocolShouldThrow() {
		Map<String, String> env = new HashMap<>();
		env.put("java.naming.provider.url", "ldap://evil.com:1389/exploit");
		env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");

		// ldap:// 不在安全协议白名单(java:, dns:)中，应被拦截
		UtilException ex = assertThrows(UtilException.class,
			() -> JNDIUtil.createInitialDirContext(env));
		assertTrue(ex.getMessage().contains("JNDI protocol not allowed"));
	}

	@Test
	void createInitialContextWithDangerousProtocolShouldThrow() {
		Map<String, String> env = new HashMap<>();
		env.put("java.naming.provider.url", "rmi://evil.com:1099/exploit");

		UtilException ex = assertThrows(UtilException.class,
			() -> JNDIUtil.createInitialContext(env));
		assertTrue(ex.getMessage().contains("JNDI protocol not allowed"));
	}

	@Test
	void createInitialContextWithDangerousFactoryShouldThrow() {
		Map<String, String> env = new HashMap<>();
		env.put("java.naming.factory.initial", "com.evil.Factory");

		UtilException ex = assertThrows(UtilException.class,
			() -> JNDIUtil.createInitialContext(env));
		assertTrue(ex.getMessage().contains("JNDI factory not allowed"));
	}

	@Test
	void createInitialDirContextWithSafeProtocolShouldNotThrowProtocolError() {
		Map<String, String> env = new HashMap<>();
		env.put("java.naming.provider.url", "java:comp/env");
		env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

		// java: 是安全协议，不应抛出协议错误
		// 但可能因环境缺少JNDI配置而抛出 NamingException（被包装为 UtilException）
		try {
			JNDIUtil.createInitialDirContext(env);
		} catch (UtilException e) {
			// 不应是协议拦截错误
			assertFalse(e.getMessage().contains("JNDI protocol not allowed"));
		}
	}

	@Test
	void createInitialDirContextWithDnsProtocolShouldNotThrowProtocolError() {
		Map<String, String> env = new HashMap<>();
		env.put("java.naming.provider.url", "dns://8.8.8.8");
		env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

		try {
			JNDIUtil.createInitialDirContext(env);
		} catch (UtilException e) {
			assertFalse(e.getMessage().contains("JNDI protocol not allowed"));
		}
	}

	@Test
	void createInitialContextWithAllowedFactoryShouldNotThrowFactoryError() {
		Map<String, String> env = new HashMap<>();
		env.put("java.naming.provider.url", "java:comp/env");
		env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");

		// com.sun.jndi.ldap. 是允许的工厂，不应抛出工厂错误
		try {
			JNDIUtil.createInitialContext(env);
		} catch (UtilException e) {
			assertFalse(e.getMessage().contains("JNDI factory not allowed"));
		}
	}

	@Test
	void createInitialContextWithNullEnvShouldNotThrowValidationError() {
		// null 环境不触发验证，直接创建默认 InitialContext
		// 在无JNDI提供者的环境中可能抛出 NamingException，但不是验证错误
		try {
			JNDIUtil.createInitialContext(null);
		} catch (UtilException e) {
			assertFalse(e.getMessage().contains("JNDI protocol not allowed"));
			assertFalse(e.getMessage().contains("JNDI factory not allowed"));
		}
	}

	@Test
	void createInitialContextWithEmptyEnvShouldNotThrowValidationError() {
		Map<String, String> env = new HashMap<>();
		try {
			JNDIUtil.createInitialContext(env);
		} catch (UtilException e) {
			assertFalse(e.getMessage().contains("JNDI protocol not allowed"));
		}
	}
}
