package cc.hongz.ztools.core.util;

import static org.junit.jupiter.api.Assertions.*;

import cc.hongz.ztools.core.lang.hash.Number128;
import org.junit.jupiter.api.Test;

public class HashUtilTest {

	@Test
	public void cityHash128Test() {
		String s = "Google发布的Hash计算算法：CityHash64 与 CityHash128";
		final long[] hash = HashUtil.cityHash128(StrUtil.utf8Bytes(s));
		assertEquals(0x5944f1e788a18db0L, hash[0]);
		assertEquals(0xc2f68d8b2bf4a5cfL, hash[1]);
	}

	@Test
	public void cityHash64Test() {
		String s = "Google发布的Hash计算算法：CityHash64 与 CityHash128";
		final long hash = HashUtil.cityHash64(StrUtil.utf8Bytes(s));
		assertEquals(0x1d408f2bbf967e2aL, hash);
	}

	@Test
	public void cityHash32Test() {
		String s = "Google发布的Hash计算算法：CityHash64 与 CityHash128";
		final int hash = HashUtil.cityHash32(StrUtil.utf8Bytes(s));
		assertEquals(0xa8944fbe, hash);
	}

	@Test
	public void additiveHashTest() {
		int hash = HashUtil.additiveHash("hello", 31);
		assertTrue(hash >= 0 && hash < 31);
	}

	@Test
	public void rotatingHashTest() {
		int hash = HashUtil.rotatingHash("hello", 31);
		assertTrue(hash >= 0);
	}

	@Test
	public void oneByOneHashTest() {
		int hash = HashUtil.oneByOneHash("hello");
		assertNotEquals(0, hash);
	}

	@Test
	public void bernsteinTest() {
		int hash = HashUtil.bernstein("hello");
		assertNotEquals(0, hash);
	}

	@Test
	public void fnvHashTest() {
		int hash1 = HashUtil.fnvHash("hello");
		int hash2 = HashUtil.fnvHash("hello");
		assertEquals(hash1, hash2);
		assertTrue(hash1 >= 0);

		// byte[] version
		int hash3 = HashUtil.fnvHash("hello".getBytes());
		assertEquals(hash1, hash3);
	}

	@Test
	public void intHashTest() {
		int hash = HashUtil.intHash(42);
		assertNotEquals(42, hash);
	}

	@Test
	public void rsHashTest() {
		int hash = HashUtil.rsHash("hello");
		assertTrue(hash >= 0);
	}

	@Test
	public void jsHashTest() {
		int hash = HashUtil.jsHash("hello");
		assertTrue(hash >= 0);
	}

	@Test
	public void pjwHashTest() {
		int hash = HashUtil.pjwHash("hello");
		assertTrue(hash >= 0);
	}

	@Test
	public void elfHashTest() {
		int hash = HashUtil.elfHash("hello");
		assertTrue(hash >= 0);
	}

	@Test
	public void bkdrHashTest() {
		int hash = HashUtil.bkdrHash("hello");
		assertTrue(hash >= 0);
	}

	@Test
	public void sdbmHashTest() {
		int hash = HashUtil.sdbmHash("hello");
		assertTrue(hash >= 0);
	}

	@Test
	public void djbHashTest() {
		int hash = HashUtil.djbHash("hello");
		assertTrue(hash >= 0);
	}

	@Test
	public void dekHashTest() {
		int hash = HashUtil.dekHash("hello");
		assertTrue(hash >= 0);
	}

	@Test
	public void apHashTest() {
		int hash = HashUtil.apHash("hello");
		assertNotEquals(0, hash);
	}

	@Test
	public void tianlHashTest() {
		long hash = HashUtil.tianlHash("hello");
		assertTrue(hash >= 0);
		assertEquals(0, HashUtil.tianlHash(""));
	}

	@Test
	public void javaDefaultHashTest() {
		int hash = HashUtil.javaDefaultHash("hello");
		assertEquals("hello".hashCode(), hash);
	}

	@Test
	public void mixHashTest() {
		long hash = HashUtil.mixHash("hello");
		assertNotEquals(0, hash);
	}

	@Test
	public void identityHashCodeTest() {
		Object obj = new Object();
		assertEquals(System.identityHashCode(obj), HashUtil.identityHashCode(obj));
	}

	@Test
	public void murmur32Test() {
		int hash = HashUtil.murmur32("hello".getBytes());
		assertNotEquals(0, hash);
	}

	@Test
	public void murmur64Test() {
		long hash = HashUtil.murmur64("hello".getBytes());
		assertNotEquals(0, hash);
	}

	@Test
	public void murmur128Test() {
		long[] hash = HashUtil.murmur128("hello".getBytes());
		assertNotNull(hash);
		assertEquals(2, hash.length);
	}

	@Test
	public void cityHash64WithSeedTest() {
		byte[] data = "hello".getBytes();
		long hash1 = HashUtil.cityHash64(data);
		long hash2 = HashUtil.cityHash64(data, 12345L);
		// Different seeds should produce different results
		assertNotEquals(hash1, hash2);
	}

	@Test
	public void cityHash64WithTwoSeedsTest() {
		byte[] data = "hello".getBytes();
		long hash = HashUtil.cityHash64(data, 111L, 222L);
		assertNotEquals(0, hash);
	}

	@Test
	public void cityHash128WithSeedTest() {
		byte[] data = "hello world test data".getBytes();
		Number128 seed = new Number128(111L, 222L);
		long[] hash = HashUtil.cityHash128(data, seed);
		assertNotNull(hash);
		assertEquals(2, hash.length);
	}

	@Test
	public void metroHash64Test() {
		byte[] data = "hello".getBytes();
		long hash = HashUtil.metroHash64(data);
		assertNotEquals(0, hash);
	}

	@Test
	public void metroHash64WithSeedTest() {
		byte[] data = "hello".getBytes();
		long hash1 = HashUtil.metroHash64(data);
		long hash2 = HashUtil.metroHash64(data, 42L);
		assertNotEquals(hash1, hash2);
	}

	@Test
	public void metroHash128Test() {
		byte[] data = "hello".getBytes();
		long[] hash = HashUtil.metroHash128(data);
		assertNotNull(hash);
		assertEquals(2, hash.length);
	}

	@Test
	public void metroHash128WithSeedTest() {
		byte[] data = "hello".getBytes();
		long[] hash = HashUtil.metroHash128(data, 42L);
		assertNotNull(hash);
		assertEquals(2, hash.length);
	}

	@Test
	public void hfHashTest() {
		long hash = HashUtil.hfHash("hello");
		assertTrue(hash >= 0);
	}

	@Test
	public void hfIpHashTest() {
		long hash = HashUtil.hfIpHash("hello");
		// Just verify it doesn't throw
		assertNotNull(hash);
	}

	@Test
	public void hashConsistencyTest() {
		// Same input should always produce same output
		String input = "test consistency";
		assertEquals(HashUtil.bkdrHash(input), HashUtil.bkdrHash(input));
		assertEquals(HashUtil.sdbmHash(input), HashUtil.sdbmHash(input));
		assertEquals(HashUtil.djbHash(input), HashUtil.djbHash(input));
		assertEquals(HashUtil.rsHash(input), HashUtil.rsHash(input));
		assertEquals(HashUtil.jsHash(input), HashUtil.jsHash(input));
	}

	@Test
	public void differentInputDifferentHashTest() {
		// Different inputs should (very likely) produce different hashes
		assertNotEquals(HashUtil.bkdrHash("abc"), HashUtil.bkdrHash("def"));
		assertNotEquals(HashUtil.fnvHash("abc"), HashUtil.fnvHash("def"));
	}
}
