# hongz-ztools-core

Hongz 平台 **核心公共库**（`jar`），对应 Hutool **`hutool-core`**，供各业务模块共享基础类型、工具与常量（与 `hongz-java-core` 插件框架核心区分）。

当前为 **按需最小子集**：自 Hutool `hutool-core` 平移/精简，包名 `cc.hongz.ztools.core.*`。包含 captcha 图像工具与 **codec 编码包**（Hutool 迁入 20 类 + 扩展 9 类，见下文）。

密码学（ASN.1、加解密等）见 [`hongz-ztools-crypto`](../hongz-ztools-crypto/)（对应 `hutool-crypto`）。

## Hutool 迁入现状与规划

源镜像：`hongz-zthrids/hongz-zthrids-hutool-v5-master/hutool-core`（`cn.hutool.core.*`）。  
目标包名：`cc.hongz.ztools.core.*`。原则：**最小依赖闭合、按批迁移、每批必测**。

### 现状对比

| 维度 | Hutool `hutool-core` | 本模块 `hongz-ztools-core` |
|------|----------------------|----------------------------|
| 源文件数 | **713** | **75**（约 11%） |
| 顶层包 | 24 个 | 8 个 |

| 包 | Hutool 文件数 | 已迁入 | 说明 |
|----|---------------|--------|------|
| `codec` | 20 | **29** | Hutool 全量 + 自研扩展（DigestCodec、HtmlEscape 等） |
| `io` | 92 | **6** | 精简子集（FileUtil、IoUtil、FileTypeUtil…） |
| `img` | 14 | **7** | 主要服务 [`hongz-ztools-captcha`](../hongz-ztools-captcha/) |
| `util` | 42 | **15** | StrUtil、DateUtil、ByteUtil、RadixUtil、PageUtil… |
| `text` | 49 | **2** | StrBuilder、Convert（含 IVICS 迁入 + `convert(Type)`） |
| `lang` | 127 | **8** | Assert、Filter、Segment、UUID、Mutable… |
| `math` | 7 | **1** | Calculator |
| `exceptions` | 9 | **7** | UtilException、ExceptionUtil（轻量）、StatefulException… |

尚未迁入的主要包：`lang`（剩余）、`collection`、`convert`、`date`（Hutool 版）、`map`、`bean`、`reflect`、`net`、`thread`、`compress` 等。

### 依赖层次（决定迁移顺序）

```text
零依赖叶子
  ByteUtil, RadixUtil, Filter（接口）
        ↓
已有基础（本模块已具备）
  StrUtil, NumberUtil, Convert, Assert, RandomUtil, ArrayUtil…
        ↓
小组合
  Segment → PageUtil
  UUID(lang) → IdUtil
        ↓
集合枢纽（门槛）
  IterUtil → CollUtil（子集）→ ReUtil / EnumUtil…
        ↓
大模块（按需）
  convert*、bean*、reflect*、date*（Hutool 版）、io*（完整 FileUtil）
```

**禁止**在未闭合依赖链时整包迁入 `CollUtil`、`BeanUtil`、`ReflectUtil`、`convert` 全包或 Hutool 完整 `FileUtil`。

### 分阶段迁移计划

#### 阶段 1：零/极低 Hutool 内依赖（**已完成**）

| 类 / 包 | Hutool 路径 | 状态 |
|---------|-------------|------|
| `ByteUtil`、`RadixUtil` | `util` | ✅ 已迁入 |
| `Filter` | `lang` | ✅ 已迁入 |
| `Segment`、`DefaultSegment` | `lang` | ✅ 已迁入（补强 `NumberUtil#sub`、`Convert#convert`） |
| `PageUtil` | `util` | ✅ 已迁入 |
| `UUID` | `lang` | ✅ 已迁入（补强 `RandomUtil#getSecureRandom` 等） |
| `exceptions` 轻量补齐 | `exceptions` | ✅ `ExceptionUtil`（轻量）、`StatefulException`、`NotInitedException`、`DependencyException`、`InvocationTargetRuntimeException`；`UtilException` 对齐 Hutool |

同步脚本：`scripts/bootstrap-hongz-core-phase1.py`。未迁入：`ExceptionUtil` 堆栈/反射重方法、`CheckedException`。

#### 阶段 2：补强已在用能力（建议下一批）

| 优先级 | 类 / 包 | 说明 |
|--------|---------|------|
| ★★★ | `ReUtil` | 正则封装；IVICS / 校验常用；需先迁 `IterUtil` 或 `CollUtil` 子集 |
| ★★☆ | `lang` 函数式 | `Matcher`、`Editor`、`func.Func1` 等；为集合工具铺路 |
| ★★☆ | `IterUtil` | 比 `CollUtil` 轻；集合工具入口 |
| ★★☆ | `ObjUtil` | 继承 `ObjectUtil`（Hutool 6.x 命名） |
| ★☆☆ | `text.StrPool` | 字符串常量；Hutool 版引用 `XmlUtil`，可只迁常量或去依赖 |

预估新增 **15～25** 个源文件。

#### 阶段 3：集合枢纽（按需子集）

| 优先级 | 类 / 包 | 说明 |
|--------|---------|------|
| ★★★ | `CollUtil`（**方法子集**） | 按 IVICS / ztools 实际调用迁移，勿一次 30 文件 |
| ★★☆ | `map` 精选 | 如 `MapWrapper`、`CaseInsensitiveMap`（视业务） |
| ★★☆ | `IdUtil` + `Snowflake` + `SystemClock` | 分布式 ID；会带入 `date`、`lang.id` |

#### 阶段 4：业务驱动、不急

| 包 | 建议 |
|----|------|
| `convert`（49） | 与 `text.Convert` 重叠；仅当必须用 Hutool 转换器时再迁 |
| `date`（43） | 已有 IVICS `DateUtil`；Hutool 版功能更全但体积大 |
| `bean` / `reflect` | IVICS 自有 `ReflectUtils`；Hutool 链依赖最深 |
| `io` 剩余 | `ResourceUtil`、`FileWatcher` 等按文件上传 / 配置场景再迁 |
| `net` / `thread` / `compress` | 有对应场景再迁 |
| `annotation` / `compiler` / `swing` | **建议不迁** |

### 暂不建议迁入

| 类 / 包 | 原因 |
|---------|------|
| `CollUtil` 整包 | 牵 `bean`、`convert`、`comparator`、`map` |
| `BeanUtil` / `ReflectUtil` | 依赖链最深 |
| `convert` 整包 | 与 `text.Convert` 重复 |
| Hutool 完整 `FileUtil` | 依赖 `CollUtil`、`io.file.*` |
| `Validator` | 依赖 `ReUtil`、`IdcardUtil`、`CreditCodeUtil` 等 |
| `StrSplitter` 整树 | 依赖 `text.finder.*` 十多个类 |
| `JNDIUtil` | 安全风险，一般业务不需要 |

### 推荐学习式节奏

```text
第 1 批：ByteUtil + RadixUtil + Filter          ✅ 已完成
第 2 批：Segment + DefaultSegment + PageUtil    ✅ 已完成
第 3 批：lang.UUID + exceptions 补齐            ✅ 已完成
第 4 批：IterUtil → ReUtil（按方法子集）        ← 建议下一批
第 5 批：评估 CollUtil 子集 / IdUtil 链
```

每批 checklist：

1. 对源类执行 `grep "^import cn.hutool"`，确认**新增** Hutool 内依赖
2. 只迁调用链闭合的最小集合；包名改为 `cc.hongz.ztools.core.*`
3. 同步或改写 Hutool 对应单元测试
4. `mvn -f hongz-ztools/hongz-ztools-core/pom.xml clean test`
5. 更新本 README「已迁入」表与 [CHANGELOG.md](./CHANGELOG.md)

### 业务引用优先级（本仓库）

结合 IVICS、captcha、ztools-response 等现有 import，阶段 1～2 中对业务最直接的是：

- `PageUtil` + `Segment` — 列表分页
- `ReUtil` — 解析与校验
- `UUID` / 后续 `Snowflake` — 统一 ID 生成

`ByteUtil` / `RadixUtil` 业务紧迫度较低，但最适合练手（零依赖、与 `codec` 连贯）。

## RadixUtil 任意进制（`cc.hongz.ztools.core.util`）

`RadixUtil` 把十进制整数按**自定义字符表**编短码；**进制基数 = 字符表长度**（`radixs.length()`），可传入任意字符表，**不强制**使用 34 或 59。

### 为何预设 34 与 59？（不是必须用这两个数）

Hutool 提供四个**便捷常量**，数字 34 / 59 来自字符个数，不是随意取的「魔法数」：

| 常量 | 基数 | 字符表组成 | 设计取舍 |
|------|------|------------|----------|
| `RADIXS_34` | **34** | `10`（0-9）+ `24`（A-Z 去 I、O） | 仅大写、去 I/O，便于**人工报读**（邀请码、兑换码）；比 Base36 少 2 字符但更易辨认 |
| `RADIXS_SHUFFLE_34` | 34 | 与上**字符集合相同**，顺序打乱 | 隐藏自增 ID 规律，仍适合人工场景 |
| `RADIXS_59` | **59** | `10` + `25`（a-z 去 l）+ `24`（A-Z 去 I、O） | 字符表更长，**编码更短**（短链、分享 slug）；思路接近 Base58，但保留 `0` |
| `RADIXS_SHUFFLE_59` | 59 | 与上**字符集合相同**，顺序打乱 | 短链 + 降低规律猜测（非加密） |

对比参考：

| 方案 | 字符数 | 说明 |
|------|--------|------|
| Base36 | 36 | 0-9 + A-Z，含 I/O，易与 1/0 混淆 |
| **本模块 34** | 34 | 大写 + 去 I/O，偏人工可读 |
| Base58 | 58 | 去 0、O、I、l（比特币地址常用） |
| **本模块 59** | 59 | 去 I、O、l，保留 0，大小写混用更短 |
| Base62 | 62 | 完整 0-9a-zA-Z，最短但 l/I/1 易混 |

### 选型建议

| 场景 | 推荐 |
|------|------|
| 邀请码、短信/电话报码、客服核对 | `RADIXS_34` 或 `RADIXS_SHUFFLE_34` |
| 短链 path、分享 token、二维码内嵌（机器读） | `RADIXS_59` 或 `RADIXS_SHUFFLE_59` |
| 品牌专属字母表、固定 20 字符等 | 自定义 `encode(你的radixs, id)`，基数自定 |

使用注意：编码与解码须**同一字符表**；字符不可重复；打乱仅增加猜测成本，**不能替代加密**。

API 与示例见源码 [`RadixUtil.java`](./src/main/java/cc/hongz/ztools/core/util/RadixUtil.java)；单测见 `RadixUtilTest`。

## 模块关系

| Hutool | Hongz |
|---|---|
| `hutool-core` | **`hongz-ztools-core`**（本模块） |
| `hutool-crypto` | [`hongz-ztools-crypto`](../hongz-ztools-crypto/) |
| `hutool-captcha` | [`hongz-ztools-captcha`](../hongz-ztools-captcha/) |

## 同步源码

### captcha 依赖（历史脚本，路径待补）

```bash
python scripts/bootstrap-hongz-core-captcha-minimal.py
```

### codec 编码包

自 `hongz-zthrids/hongz-zthrids-hutool-v5-master/hutool-core/.../cn/hutool/core/codec` 同步：

```bash
python scripts/bootstrap-hongz-core-codec.py
mvn -f hongz-ztools/hongz-ztools-core/pom.xml clean test
```

### ivics 通用 utils

自 `hongz-zplatforms/hongz-zplatforms-ivics-java-web/.../common/utils` 同步非业务工具类：

```bash
python scripts/bootstrap-hongz-core-ivics-utils.py
mvn -f hongz-ztools/hongz-ztools-core/pom.xml clean test
```

| 包 | 类 |
|---|---|
| `cc.hongz.ztools.core.util` | `DateUtil`、`ConsoleColorsUtil` |
| `cc.hongz.ztools.core.text` | `Convert` |

### 文件 IO 与类型（`cc.hongz.ztools.core.io`）

Hutool 侧文件工具均在 **`cn.hutool.core.io`**。`hongz-ztools-core` 统一收拢至同包，**不再使用** `util.file`：

```bash
python scripts/bootstrap-hongz-core-file-type.py
mvn -f hongz-ztools/hongz-ztools-core/pom.xml clean test
```

| 类 | 说明 |
|---|---|
| `FileUtil` | 文件读写、扩展名、`getType(File)` |
| `IoUtil` | 流读写、十六进制头读取 |
| `FileTypeUtil` | 魔数识别（Hutool 迁入 + ivics 后缀/`getFileExtendName` 合并） |
| `FileMagicNumber` | 魔数枚举 |
| `MimeTypeUtils` | MIME 常量与扩展名映射（ivics 迁入） |
| `IORuntimeException` | IO 运行时异常 |

## 编码方式（`cc.hongz.ztools.core.codec`）

Hutool codec 目录已整体迁入 `hongz-ztools-core`，按用途分类如下。

### Base-N 二进制 ↔ 文本

| 类 | 说明 | 典型场景 |
|---|---|---|
| `Base16Codec` | 十六进制（Hex），大小写两套字母表 | 字节转可读 Hex、Unicode `\uXXXX` |
| `Base32` / `Base32Codec` | RFC 4648 Base32 及 Hex 变体 | TOTP 密钥、部分协议字段 |
| `Base58` / `Base58Codec` | 58 字符集（无 0/O/I/l） | 比特币地址、短 ID |
| `Base62` / `Base62Codec` | 0-9a-zA-Z，含倒序字母表变体 | URL 短链、混淆 ID |
| `Base64` / `Base64Encoder` / `Base64Decoder` | 标准 / URL Safe / 无 padding / 多行 MIME | 图片 Data URI、JWT 载荷、附件传输 |

`Base64` 在 captcha 中用于验证码图片 Data URI（`AbstractCaptcha`）。

### 电信与数据格式

| 类 | 说明 |
|---|---|
| `BCD` | 二进制 coded decimal，8421 码 |
| `Morse` | 摩尔斯电码编解码 |

### 文本与国际化

| 类 | 说明 |
|---|---|
| `PercentCodec` | URL 百分号编码（RFC 3986），可自定义安全字符 |
| `PunyCode` | 国际化域名（IDNA）编解码 |

### 轻量“编码”/变换（非加密）

| 类 | 说明 |
|---|---|
| `Caesar` | 凯撒密码 |
| `Rot` | ROT-N 字母移位 |
| `Hashids` | 数字数组 ↔ 短字符串（带 salt、最小长度） |

### 扩展编码（公开标准补充，非 Hutool 迁入）

| 类 | 说明 | 标准 / 场景 |
|---|---|---|
| `QuotedPrintable` | MIME 可打印编码 | RFC 2045 邮件正文 |
| `Ascii85` | Base85 五字符组编码 | Adobe PDF / PostScript |
| `GzipBase64` | GZIP 压缩 + Base64 | 配置项、短数据压缩传输 |
| `DigestCodec` | MD5 / SHA-256 等摘要 Hex | 指纹展示、校验 |
| `XmlEscape` / `HtmlEscape` | XML / HTML4 基础实体转义 | `&amp;`、`&lt;` 等 |
| `UnicodeCodec` | `\uXXXX` 转义 | Java / JavaScript 字符串字面量 |
| `UuCodec` | UUencode（Unix-to-Unix） | 早期邮件/Usenet 附件 |
| `YEnc` | yEnc 行编码 + CRC 头尾 | Usenet 二进制附件 |

### 接口

| 类 | 说明 |
|---|---|
| `Encoder` / `Decoder` | 通用编解码函数式接口 |

---

## 常见编码方式对照（公开资料）

下表列出业界常见编码及在本模块中的状态，便于选型与后续扩展。

| 编码 / 标准 | 说明 | hongz-ztools-core 状态 |
|---|---|---|
| Hex（Base16） | 每字节 2 个十六进制字符 | **已有** `Base16Codec` |
| Base32 / Base58 / Base62 / Base64 | Base-N 族 | **已有** 对应类 |
| Base64URL | JWT、OAuth 无 padding 变体 | **已有** `Base64.encodeUrlSafe` 等 |
| URL 百分号编码 | `application/x-www-form-urlencoded` | **已有** `PercentCodec` |
| Punycode / IDNA | 国际化域名 | **已有** `PunyCode` |
| Quoted-Printable | MIME 邮件可打印编码 | **已有** `QuotedPrintable` |
| UUencode / yEnc | 早期 Usenet 附件 | **已有** `UuCodec` / `YEnc` |
| ASCII85 (Base85) | Adobe PDF 等 | **已有** `Ascii85` |
| GZIP + Base64 | 压缩后编码传输 | **已有** `GzipBase64` |
| MessageDigest Hex | MD5/SHA 摘要 Hex 展示 | **已有** `DigestCodec` |
| HTML 实体 / XML 转义 | `&amp;` 等 | **已有** `HtmlEscape` / `XmlEscape` |
| Unicode `\uXXXX` | Java 源码/JSON 转义 | **已有** `UnicodeCodec`；单字符见 `Base16Codec#toUnicodeHex` |
| ASN.1 DER / BER / DL | 证书、密钥、签名结构 | 见 [`hongz-ztools-crypto`](../hongz-ztools-crypto/) `ASN1Util` |

## 构建

```bash
mvn -f hongz-ztools/hongz-ztools-core/pom.xml clean test
```

测试目录与 `src/main/java` 包结构一致：`src/test/java/cc/hongz/core/**/{Class}Test.java`。`codec` 包含 **27** 个测试类，**209** 个用例。

## 变更记录

详见 [CHANGELOG.md](./CHANGELOG.md).
