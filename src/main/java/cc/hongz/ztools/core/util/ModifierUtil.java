package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;

import java.lang.reflect.*;

/**
 * 淇グ绗﹀伐鍏风被
 *
 * @author looly
 * @since 4.0.5
 */
public class ModifierUtil {

	/**
	 * 淇グ绗︽灇涓?
	 *
	 * @author looly
	 * @since 4.0.5
	 */
	public enum ModifierType {
		/**
		 * public淇グ绗︼紝鎵€鏈夌被閮借兘璁块棶
		 */
		PUBLIC(Modifier.PUBLIC),
		/**
		 * private淇グ绗︼紝鍙兘琚嚜宸辫闂拰淇敼
		 */
		PRIVATE(Modifier.PRIVATE),
		/**
		 * protected淇グ绗︼紝鑷韩銆佸瓙绫诲強鍚屼竴涓寘涓被鍙互璁块棶
		 */
		PROTECTED(Modifier.PROTECTED),
		/**
		 * static淇グ绗︼紝锛堥潤鎬佷慨楗扮锛夋寚瀹氬彉閲忚鎵€鏈夊璞″叡浜紝鍗虫墍鏈夊疄渚嬮兘鍙互浣跨敤璇ュ彉閲忋€傚彉閲忓睘浜庤繖涓被
		 */
		STATIC(Modifier.STATIC),
		/**
		 * final淇グ绗︼紝鏈€缁堜慨楗扮锛屾寚瀹氭鍙橀噺鐨勫€间笉鑳藉彉锛屼娇鐢ㄥ湪鏂规硶涓婅〃绀轰笉鑳借閲嶈浇
		 */
		FINAL(Modifier.FINAL),
		/**
		 * synchronized锛屽悓姝ヤ慨楗扮锛屽湪澶氫釜绾跨▼涓紝璇ヤ慨楗扮鐢ㄤ簬鍦ㄨ繍琛屽墠锛屽浠栨墍灞炵殑鏂规硶鍔犻攣锛屼互闃叉鍏朵粬绾跨▼鐨勮闂紝杩愯缁撴潫鍚庤В閿併€?
		 */
		SYNCHRONIZED(Modifier.SYNCHRONIZED),
		/**
		 * 锛堟槗澶变慨楗扮锛夋寚瀹氳鍙橀噺鍙互鍚屾椂琚嚑涓嚎绋嬫帶鍒跺拰淇敼
		 */
		VOLATILE(Modifier.VOLATILE),
		/**
		 * 锛堣繃搴︿慨楗扮锛夋寚瀹氳鍙橀噺鏄郴缁熶繚鐣欙紝鏆傛棤鐗瑰埆浣滅敤鐨勪复鏃舵€у彉閲忥紝搴忓垪鍖栨椂蹇界暐
		 */
		TRANSIENT(Modifier.TRANSIENT),
		/**
		 * native锛屾湰鍦颁慨楗扮銆傛寚瀹氭鏂规硶鐨勬柟娉曚綋鏄敤鍏朵粬璇█鍦ㄧ▼搴忓閮ㄧ紪鍐欑殑銆?
		 */
		NATIVE(Modifier.NATIVE),

		/**
		 * abstract锛屽皢涓€涓被澹版槑涓烘娊璞＄被锛屾病鏈夊疄鐜扮殑鏂规硶锛岄渶瑕佸瓙绫绘彁渚涙柟娉曞疄鐜般€?
		 */
		ABSTRACT(Modifier.ABSTRACT),
		/**
		 * strictfp锛屼竴鏃︿娇鐢ㄤ簡鍏抽敭瀛梥trictfp鏉ュ０鏄庢煇涓被銆佹帴鍙ｆ垨鑰呮柟娉曟椂锛岄偅涔堝湪杩欎釜鍏抽敭瀛楁墍澹版槑鐨勮寖鍥村唴鎵€鏈夋诞鐐硅繍绠楅兘鏄簿纭殑锛岀鍚圛EEE-754瑙勮寖鐨勩€?
		 */
		STRICT(Modifier.STRICT);

		/**
		 * 淇グ绗︽灇涓惧搴旂殑int淇グ绗﹀€?
		 */
		private final int value;

		/**
		 * 鏋勯€?
		 *
		 * @param modifier 淇グ绗nt琛ㄧず锛岃{@link Modifier}
		 */
		ModifierType(int modifier) {
			this.value = modifier;
		}

		/**
		 * 鑾峰彇淇グ绗︽灇涓惧搴旂殑int淇グ绗﹀€硷紝鍊艰{@link Modifier}
		 *
		 * @return 淇グ绗︽灇涓惧搴旂殑int淇グ绗﹀€?
		 */
		public int getValue() {
			return this.value;
		}
	}

	/**
	 * 绫绘槸鍚﹀瓨鍦ㄧ粰瀹氫慨楗扮涓殑<b>浠绘剰涓€涓?/b><br>
	 * 濡傚畾涔変慨楗扮涓猴細{@code public static final}锛岄偅涔堝鏋滀紶鍏ョ殑modifierTypes涓猴細
	 * <ul>
	 *     <li>public銆乻tatic  杩斿洖{@code true}</li>
	 *     <li>public銆乤bstract杩斿洖{@code true}</li>
	 *     <li>private銆乤bstract杩斿洖{@code false}</li>
	 * </ul>
	 *
	 * @param clazz         绫伙紝濡傛灉涓簕@code null}杩斿洖{@code false}
	 * @param modifierTypes 淇グ绗︽灇涓撅紝濡傛灉涓虹┖杩斿洖{@code false}
	 * @return 鏄惁鏈夋寚瀹氫慨楗扮锛屽鏋滄湁杩斿洖true锛屽惁鍒檉alse锛屽鏋滄彁渚涘弬鏁颁负null杩斿洖false
	 */
	public static boolean hasModifier(Class<?> clazz, ModifierType... modifierTypes) {
		if (null == clazz || ArrayUtil.isEmpty(modifierTypes)) {
			return false;
		}
		return 0 != (clazz.getModifiers() & modifiersToInt(modifierTypes));
	}

	/**
	 * 鏋勯€犳槸鍚﹀瓨鍦ㄧ粰瀹氫慨楗扮涓殑<b>浠绘剰涓€涓?/b><br>
	 * 濡傚畾涔変慨楗扮涓猴細{@code public static final}锛岄偅涔堝鏋滀紶鍏ョ殑modifierTypes涓猴細
	 * <ul>
	 *     <li>public銆乻tatic  杩斿洖{@code true}</li>
	 *     <li>public銆乤bstract杩斿洖{@code true}</li>
	 *     <li>private銆乤bstract杩斿洖{@code false}</li>
	 * </ul>
	 *
	 * @param constructor        鏋勯€狅紝濡傛灉涓簕@code null}杩斿洖{@code false}
	 * @param modifierTypes 淇グ绗︽灇涓撅紝濡傛灉涓虹┖杩斿洖{@code false}
	 * @return 鏄惁鏈夋寚瀹氫慨楗扮锛屽鏋滄湁杩斿洖true锛屽惁鍒檉alse锛屽鏋滄彁渚涘弬鏁颁负null杩斿洖false
	 */
	public static boolean hasModifier(Constructor<?> constructor, ModifierType... modifierTypes) {
		if (null == constructor || ArrayUtil.isEmpty(modifierTypes)) {
			return false;
		}
		return 0 != (constructor.getModifiers() & modifiersToInt(modifierTypes));
	}

	/**
	 * 鏂规硶鏄惁瀛樺湪缁欏畾淇グ绗︿腑鐨?b>浠绘剰涓€涓?/b><br>
	 * 濡傚畾涔変慨楗扮涓猴細{@code public static final}锛岄偅涔堝鏋滀紶鍏ョ殑modifierTypes涓猴細
	 * <ul>
	 *     <li>public銆乻tatic  杩斿洖{@code true}</li>
	 *     <li>public銆乤bstract杩斿洖{@code true}</li>
	 *     <li>private銆乤bstract杩斿洖{@code false}</li>
	 * </ul>
	 *
	 * @param method        鏂规硶锛屽鏋滀负{@code null}杩斿洖{@code false}
	 * @param modifierTypes 淇グ绗︽灇涓撅紝濡傛灉涓虹┖杩斿洖{@code false}
	 * @return 鏄惁鏈夋寚瀹氫慨楗扮锛屽鏋滄湁杩斿洖true锛屽惁鍒檉alse锛屽鏋滄彁渚涘弬鏁颁负null杩斿洖false
	 */
	public static boolean hasModifier(Method method, ModifierType... modifierTypes) {
		if (null == method || ArrayUtil.isEmpty(modifierTypes)) {
			return false;
		}
		return 0 != (method.getModifiers() & modifiersToInt(modifierTypes));
	}

	/**
	 * 瀛楁鏄惁瀛樺湪缁欏畾淇グ绗︿腑鐨?b>浠绘剰涓€涓?/b><br>
	 * 濡傚畾涔変慨楗扮涓猴細{@code public static final}锛岄偅涔堝鏋滀紶鍏ョ殑modifierTypes涓猴細
	 * <ul>
	 *     <li>public銆乻tatic  杩斿洖{@code true}</li>
	 *     <li>public銆乤bstract杩斿洖{@code true}</li>
	 *     <li>private銆乤bstract杩斿洖{@code false}</li>
	 * </ul>
	 *
	 * @param field        鏋勯€犮€佸瓧娈垫垨鏂规硶锛屽鏋滀负{@code null}杩斿洖{@code false}
	 * @param modifierTypes 淇グ绗︽灇涓撅紝濡傛灉涓虹┖杩斿洖{@code false}
	 * @return 鏄惁鏈夋寚瀹氫慨楗扮锛屽鏋滄湁杩斿洖true锛屽惁鍒檉alse锛屽鏋滄彁渚涘弬鏁颁负null杩斿洖false
	 */
	public static boolean hasModifier(Field field, ModifierType... modifierTypes) {
		if (null == field || ArrayUtil.isEmpty(modifierTypes)) {
			return false;
		}
		return 0 != (field.getModifiers() & modifiersToInt(modifierTypes));
	}

	// region ----- hasAll

	/**
	 * 绫讳腑鏄惁鍚屾椂瀛樺湪<b>鎵€鏈?/b>缁欏畾淇グ绗?br>
	 * 濡傚畾涔変慨楗扮涓猴細{@code public static final}锛岄偅涔堝鏋滀紶鍏ョ殑modifierTypes涓猴細
	 * <ul>
	 *     <li>public銆乻tatic  杩斿洖{@code true}</li>
	 *     <li>public銆乤bstract杩斿洖{@code false}</li>
	 *     <li>private銆乤bstract杩斿洖{@code false}</li>
	 * </ul>
	 *
	 * @param clazz         绫伙紝濡傛灉涓簕@code null}杩斿洖{@code false}
	 * @param modifierTypes 淇グ绗︽灇涓撅紝濡傛灉涓虹┖杩斿洖{@code false}
	 * @return 鏄惁鍚屾椂瀛樺湪鎵€鏈夋寚瀹氫慨楗扮锛屽鏋滄湁杩斿洖true锛屽惁鍒檉alse锛屽鏋滄彁渚涘弬鏁颁负null杩斿洖false
	 */
	public static boolean hasAllModifiers(final Class<?> clazz, final ModifierType... modifierTypes) {
		if (null == clazz || ArrayUtil.isEmpty(modifierTypes)) {
			return false;
		}
		final int checkedModifiersInt = modifiersToInt(modifierTypes);
		return checkedModifiersInt == (clazz.getModifiers() & checkedModifiersInt);
	}

	/**
	 * 鎴愬憳涓槸鍚﹀悓鏃跺瓨鍦?b>鎵€鏈?/b>缁欏畾淇グ绗?br>
	 * 濡傚畾涔変慨楗扮涓猴細{@code public static final}锛岄偅涔堝鏋滀紶鍏ョ殑modifierTypes涓猴細
	 * <ul>
	 *     <li>public銆乻tatic  杩斿洖{@code true}</li>
	 *     <li>public銆乤bstract杩斿洖{@code false}</li>
	 *     <li>private銆乤bstract杩斿洖{@code false}</li>
	 * </ul>
	 *
	 * @param member        鏋勯€犮€佸瓧娈垫垨鏂规硶锛屽鏋滀负{@code null}杩斿洖{@code false}
	 * @param modifierTypes 淇グ绗︽灇涓撅紝濡傛灉涓虹┖杩斿洖{@code false}
	 * @return 鏄惁鍚屾椂瀛樺湪鎵€鏈夋寚瀹氫慨楗扮锛屽鏋滄湁杩斿洖true锛屽惁鍒檉alse锛屽鏋滄彁渚涘弬鏁颁负null杩斿洖false
	 */
	public static boolean hasAllModifiers(final Member member, final ModifierType... modifierTypes) {
		if (null == member || ArrayUtil.isEmpty(modifierTypes)) {
			return false;
		}
		final int checkedModifiersInt = modifiersToInt(modifierTypes);
		return checkedModifiersInt == (member.getModifiers() & checkedModifiersInt);
	}
	// endregion

	/**
	 * 鏄惁鏄疨ublic瀛楁
	 *
	 * @param field 瀛楁
	 * @return 鏄惁鏄疨ublic
	 */
	public static boolean isPublic(Field field) {
		return hasModifier(field, ModifierType.PUBLIC);
	}

	/**
	 * 鏄惁鏄疨ublic鏂规硶
	 *
	 * @param method 鏂规硶
	 * @return 鏄惁鏄疨ublic
	 */
	public static boolean isPublic(Method method) {
		return hasModifier(method, ModifierType.PUBLIC);
	}

	/**
	 * 鏄惁鏄疨ublic绫?
	 *
	 * @param clazz 绫?
	 * @return 鏄惁鏄疨ublic
	 */
	public static boolean isPublic(Class<?> clazz) {
		return hasModifier(clazz, ModifierType.PUBLIC);
	}

	/**
	 * 鏄惁鏄疨ublic鏋勯€?
	 *
	 * @param constructor 鏋勯€?
	 * @return 鏄惁鏄疨ublic
	 */
	public static boolean isPublic(Constructor<?> constructor) {
		return hasModifier(constructor, ModifierType.PUBLIC);
	}

	/**
	 * 鏄惁鏄痵tatic瀛楁
	 *
	 * @param field 瀛楁
	 * @return 鏄惁鏄痵tatic
	 * @since 4.0.8
	 */
	public static boolean isStatic(Field field) {
		return hasModifier(field, ModifierType.STATIC);
	}

	/**
	 * 鏄惁鏄痵tatic鏂规硶
	 *
	 * @param method 鏂规硶
	 * @return 鏄惁鏄痵tatic
	 * @since 4.0.8
	 */
	public static boolean isStatic(Method method) {
		return hasModifier(method, ModifierType.STATIC);
	}

	/**
	 * 鏄惁鏄痵tatic绫?
	 *
	 * @param clazz 绫?
	 * @return 鏄惁鏄痵tatic
	 * @since 4.0.8
	 */
	public static boolean isStatic(Class<?> clazz) {
		return hasModifier(clazz, ModifierType.STATIC);
	}

	/**
	 * 鏄惁鏄悎鎴愬瓧娈碉紙鐢眏ava缂栬瘧鍣ㄧ敓鎴愮殑锛?
	 *
	 * @param field 瀛楁
	 * @return 鏄惁鏄悎鎴愬瓧娈?
	 * @since 5.6.3
	 */
	public static boolean isSynthetic(Field field) {
		return field.isSynthetic();
	}

	/**
	 * 鏄惁鏄悎鎴愭柟娉曪紙鐢眏ava缂栬瘧鍣ㄧ敓鎴愮殑锛?
	 *
	 * @param method 鏂规硶
	 * @return 鏄惁鏄悎鎴愭柟娉?
	 * @since 5.6.3
	 */
	public static boolean isSynthetic(Method method) {
		return method.isSynthetic();
	}

	/**
	 * 鏄惁鏄悎鎴愮被锛堢敱java缂栬瘧鍣ㄧ敓鎴愮殑锛?
	 *
	 * @param clazz 绫?
	 * @return 鏄惁鏄悎鎴?
	 * @since 5.6.3
	 */
	public static boolean isSynthetic(Class<?> clazz) {
		return clazz.isSynthetic();
	}

	/**
	 * 鏄惁鎶借薄鏂规硶
	 *
	 * @param method 鏂规硶
	 * @return 鏄惁鎶借薄鏂规硶
	 * @since 5.7.23
	 */
	public static boolean isAbstract(Method method) {
		return hasModifier(method, ModifierType.ABSTRACT);
	}

	/**
	 * 璁剧疆final鐨刦ield瀛楁鍙互琚慨鏀?
	 * 鍙涓嶄細琚紪璇戝櫒鍐呰仈浼樺寲鐨?final 灞炴€у氨鍙互閫氳繃鍙嶅皠鏈夋晥鐨勮繘琛屼慨鏀?--  淇敼鍚庝唬鐮佷腑鍙娇鐢ㄥ埌鏂扮殑鍊?
	 * <p>浠ヤ笅灞炴€э紝缂栬瘧鍣ㄤ細鍐呰仈浼樺寲锛屾棤娉曢€氳繃鍙嶅皠淇敼锛?/p>
	 * <ul>
	 *     <li> 鍩烘湰绫诲瀷 byte, char, short, int, long, float, double, boolean</li>
	 *     <li> Literal String 绫诲瀷(鐩存帴鍙屽紩鍙峰瓧绗︿覆)</li>
	 * </ul>
	 * <p>浠ヤ笅灞炴€э紝鍙互閫氳繃鍙嶅皠淇敼锛?/p>
	 * <ul>
	 *     <li>鍩烘湰绫诲瀷鐨勫寘瑁呯被 Byte銆丆haracter銆丼hort銆丩ong銆丗loat銆丏ouble銆丅oolean</li>
	 *     <li>瀛楃涓诧紝閫氳繃 new String("")瀹炰緥鍖?/li>
	 *     <li>鑷畾涔塲ava绫?/li>
	 * </ul>
	 * <pre class="code">
	 * {@code
	 *      //绀轰緥锛岀Щ闄inal淇グ绗?
	 *      class JdbcDialects {private static final List<Number> dialects = new ArrayList<>();}
	 *      Field field = ReflectUtil.getField(JdbcDialects.class, fieldName);
	 * 		ReflectUtil.removeFinalModify(field);
	 * 		ReflectUtil.setFieldValue(JdbcDialects.class, fieldName, dialects);
	 *    }
	 * </pre>
	 *
	 * @param field 琚慨鏀圭殑field锛屼笉鍙互涓虹┖
	 * @throws UtilException IllegalAccessException绛夊紓甯稿寘瑁?
	 * @author dazer
	 * @since 5.8.8
	 */
	public static void removeFinalModify(Field field) {
		if (field != null) {
			if (hasModifier(field, ModifierUtil.ModifierType.FINAL)) {
				//灏嗗瓧娈电殑璁块棶鏉冮檺璁句负true锛氬嵆鍘婚櫎private淇グ绗︾殑褰卞搷
				if (false == field.isAccessible()) {
					field.setAccessible(true);
				}
				try {
					//鍘婚櫎final淇グ绗︾殑褰卞搷锛屽皢瀛楁璁句负鍙慨鏀圭殑
					final Field modifiersField = Field.class.getDeclaredField("modifiers");
					//Field 鐨?modifiers 鏄鏈夌殑
					modifiersField.setAccessible(true);
					//& 锛氫綅涓庤繍绠楃锛屾寜浣嶄笌锛? 杩愮畻瑙勫垯锛氫袱涓暟閮借浆涓轰簩杩涘埗锛岀劧鍚庝粠楂樹綅寮€濮嬫瘮杈冿紝濡傛灉涓や釜鏁伴兘涓?鍒欎负1锛屽惁鍒欎负0銆?
					//~ 锛氫綅闈炶繍绠楃锛屾寜浣嶅彇鍙嶏紱杩愮畻瑙勫垯锛氳浆鎴愪簩杩涘埗锛屽鏋滀綅涓?锛岀粨鏋滄槸1锛屽鏋滀綅涓?锛岀粨鏋滄槸0.
					modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
				} catch (final NoSuchFieldException | IllegalAccessException e) {
					//鍐呴儴锛屽伐鍏风被锛屽熀鏈笉鎶涘嚭寮傚父
					throw new UtilException(e, "IllegalAccess for {}.{}", field.getDeclaringClass(), field.getName());
				}
			}
		}
	}
	//-------------------------------------------------------------------------------------------------------- Private method start

	/**
	 * 澶氫釜淇グ绗﹀仛鈥滄垨鈥濇搷浣滐紝琛ㄧず鍚屾椂瀛樺湪澶氫釜淇グ绗?
	 *
	 * @param modifierTypes 淇グ绗﹀垪琛紝鍏冪礌涓嶈兘涓虹┖
	 * @return 鈥滄垨鈥濅箣鍚庣殑淇グ绗?
	 */
	private static int modifiersToInt(ModifierType... modifierTypes) {
		int modifier = modifierTypes[0].getValue();
		for (int i = 1; i < modifierTypes.length; i++) {
			modifier |= modifierTypes[i].getValue();
		}
		return modifier;
	}
	//-------------------------------------------------------------------------------------------------------- Private method end
}
