# 变更日志

## 2026-07-12

- **迁移 `net` 包**：自 Hutool `hutool-core` 迁入 `RFC3986`、`URLEncodeUtil`、`URLDecoder` 至 `cc.hongz.ztools.core.net`，基于已有的 `PercentCodec`（BitSet）实现 RFC3986 编码层级（UNRESERVED → PCHAR → SEGMENT → PATH → QUERY → FRAGMENT）
- **重构 `URLUtil`**：encode/decode 方法从 JDK `URLEncoder`/`URLDecoder` + 字符串替换改为委托 `URLEncodeUtil`/`URLDecoder`，消除字面量 `%2F` 被错误替换的缺陷，减少 166 行代码
- **补强 `CharUtil`**：新增 `isNumber`、`isHexChar`、`digit16` 方法（`URLDecoder` 依赖）
- 新增单元测试 3 类 75 个用例：`URLEncodeUtilTest`（44）、`URLDecoderTest`（22）、`RFC3986Test`（9）；`URLUtilTest` 保持 105 个用例全通过；总计 **180** 个用例

## 2026-06-22

- `RadixUtil` 34/59 选型说明迁入 **模块 `README.md`**「RadixUtil 任意进制」；源码 Javadoc 改为摘要 + 指向 README
- `.cursor/rules/java-comments-ivics-style.mdc` 新增「预设常量 / 魔法数字」注释规则；globs 纳入 `hongz-ztools-core`
- `RadixUtilTest` 新增字符表长度与打乱版基数不变用例
- `RadixUtil` 类 Javadoc 补充各进制预设字符表（34/59 及打乱版）的业务场景、选型对照表与使用注意；方法注释规范化
- `RadixUtilTest` 扩充：文档示例（2/3 进制）、四种预设字符表往返、打乱与标准差异、连续 ID 防规律、负数/空串边界（共 12 用例）
- 阶段 1 迁入类按 ivics 规范补全 Javadoc：`ByteUtil`、`RadixUtil`、`PageUtil`、`Filter`、`Segment`、`DefaultSegment`、`UUID`、异常类及 `UtilException` 等；工具类统一 `final` + 私有构造
- 阶段 1 单元测试补全：类/方法 Javadoc、`@DisplayName`、扩充 `PageUtil`/`ExceptionUtil`/`RandomUtil`/`Convert`/`StrUtil` 用例；`ByteUtilTest` 去除行内注释并按规范重写

## 2026-06-21

- **阶段 1 Hutool 迁入完成**：`ByteUtil`、`RadixUtil`、`PageUtil`、`Filter`、`Segment`、`DefaultSegment`、`UUID`；异常补齐 `ExceptionUtil`（轻量）、`StatefulException`、`NotInitedException`、`DependencyException`、`InvocationTargetRuntimeException`；`UtilException` 对齐 Hutool
- 依赖补强：`NumberUtil#sub(Number)`、`Convert#convert(Type)`、`RandomUtil#getSecureRandom`/`randomLong`/`randomDouble`、`StrUtil#NULL`/`stringBuilder`
- 同步脚本 `scripts/bootstrap-hongz-core-phase1.py`；新增/改写单元测试（`ByteUtilTest`、`RadixUtilTest`、`PageUtilTest`、`SegmentTest`、`UUIDTest`、`ExceptionUtilTest`）
- README 新增「Hutool 迁入现状与规划」：现状对比、依赖层次、分阶段迁移计划（阶段 1～4）、暂不建议迁入清单与学习式节奏

## 2026-06-19

- 迁入 `hongz-ztools/hongz-ztools-core/`；artifactId 重命名为 `hongz-ztools-core`（原 `hongz-core`）
- **合并文件工具包**：删除 `cc.hongz.ztools.core.util.file`；`FileTypeUtils` 能力并入 `cc.hongz.ztools.core.io.FileTypeUtil`，`MimeTypeUtils` 迁至 `cc.hongz.ztools.core.io`；`FileUtil.getType(File)` 对齐 Hutool
- ivics `ExcelUtil` / `FileUploadUtils` import 改为 `cc.hongz.ztools.core.io.*`
- 自 Hutool `hutool-core` 迁入 `FileTypeUtil`、`FileMagicNumber` 至 `cc.hongz.ztools.core.io`
- 自 `hongz-ivics-java-web` 迁入通用工具：`DateUtil`、`Convert`、`ConsoleColorsUtil`；`CharsetUtil` 补充字符集转换
- 同步脚本 `scripts/bootstrap-hongz-core-file-type.py`、`scripts/bootstrap-hongz-core-ivics-utils.py`（file 类不再经 ivics 脚本同步）

## 2026-06-18

- 自 Hutool `hutool-core` 迁入 **codec 编码包**（20 个源文件）至 `cc.hongz.ztools.core.codec`；同步脚本 `scripts/bootstrap-hongz-core-codec.py`
- 补齐 codec 依赖最小子集：`Assert`、`MutableInt`、`UtilException`、`ValidateException`、`CharsetUtil`、`ArrayUtil`、`HexUtil`；扩展 `StrUtil`、`CharUtil`、`IoUtil`、`FileUtil`
- `Base64` 由 captcha 极简版替换为 Hutool 完整实现（含 URL Safe、文件/流、解码）；更新 `Base64Test`
- README 新增编码方式分类表与业界常见编码对照
- 新增 codec 单元测试 11 类（`Base16Codec`～`PunyCode`/`PercentCodec` 等），共 42 个用例
- **扩展编码**（公开标准）：`QuotedPrintable`、`Ascii85`、`GzipBase64`、`DigestCodec`、`XmlEscape`、`HtmlEscape`、`UnicodeCodec`、`UuCodec`、`YEnc`；各附单元测试
- 补充 codec 单元测试：`UuCodec`/`YEnc` 边界与异常路径；新增 `Base64EncoderTest`、`Base64DecoderTest`、`Base32CodecTest`、`Base58CodecTest`、`Base62CodecTest`；扩展扩展编码类 null/往返用例（共 209 个 codec 相关用例）

## 2026-06-10

- 模块初始化：新建 `hongz-core` 子模块并在 `hongz-parent` 注册
- 自 Hutool `hutool-core` **按需最小子集**迁移 captcha 依赖（约 20 个源文件），包名 `cc.hongz.ztools.core.*`；同步脚本 `scripts/bootstrap-hongz-core-captcha-minimal.py`
- 新增 JUnit 5 单元测试（`src/test/java` 与 main 包结构一致，18 个测试类）
