# HG51E - 中国移动光猫账号密码查询工具

## 项目描述

针对中国移动吉比特 HG51E 光猫的辅助工具应用。通过向光猫内置 Web 管理接口发送 HTTP 请求，自动获取宽带账号和密码信息，并支持一键复制到剪贴板。

- 仅适用于移动吉比特 HG51E 型号光猫
- 各地区固件可能存在差异，部分地区可能有加密算法（深圳 2024 款已确认无加密）
- 默认网关地址：`192.168.1.1`

## 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 语言 | Kotlin | 1.9.22 |
| 构建 | Gradle (AGP) | 8.2.1 |
| 最低 SDK | Android 7.0 | API 24 |
| 目标 SDK | Android 14 | API 34 |
| JVM | Java 17 | - |
| DI | Hilt (Dagger) | 2.51.1 |
| 网络 | Retrofit + OkHttp | 2.11.0 / 4.12.0 |
| 序列化 | Gson | 2.10.1 |
| 异步 | Kotlin Coroutines + Flow | 1.7.3 |
| UI 组件 | ShapeView / Toaster / AndroidAutoSize | - |

## 项目结构

```
app/src/main/java/com/aotem/hg51e/
├── HG51EApplication.kt          # Application 入口，初始化 Toaster
├── MainActivity.kt               # 主界面，网络检测 + 账号密码查询与复制
├── data/
│   ├── model/
│   │   └── User.kt               # 数据模型，映射光猫 CGI 返回的 JSON
│   ├── remote/
│   │   ├── ApiService.kt         # Retrofit 接口定义
│   │   ├── BaseInterceptor.kt    # OkHttp 拦截器，添加通用 Header
│   │   └── RetrofitClient.kt     # Retrofit 单例管理，支持动态 baseUrl
│   └── repository/
│       └── UserRepositoryImpl.kt # 数据仓库，封装 API 调用
├── di/
│   ├── NetworkModule.kt          # Hilt 模块：提供 OkHttpClient（禁用代理、10s 超时）
│   └── RepositoryModule.kt       # Hilt 模块：绑定 UserRepository 接口
└── ui/main/
    ├── MainViewModel.kt          # ViewModel，管理查询状态和错误处理
    └── UiState.kt                # UI 状态密封类：Idle / Loading / Success / Error
```

## 核心逻辑

### API 端点

```
GET http://{host}/boaform/web_query_user_show.cgi
```

返回 JSON 格式的用户信息，关键字段：
- `web_tele_account_name` — 宽带账号
- `teleAccount_password` — 宽带密码

### 网络检测逻辑（MainActivity.onGetClicked）

1. 检查是否有活跃网络连接
2. 检测 VPN 代理，若有则提示关闭
3. 仅允许 WiFi 和移动数据网络发起请求

### 网络配置

- `NetworkModule` 中设置了 `Proxy.NO_PROXY`，强制绕过系统代理
- `BaseInterceptor` 添加 `Content-Type: application/json;charset=UTF-8` 请求头
- `RetrofitClient` 支持动态切换 baseUrl，用于连接不同地址的光猫

## 构建说明

```bash
# Debug 构建
./gradlew assembleDebug

# Release 构建（需配置签名）
./gradlew assembleRelease
```

- 项目使用 `kapt` 处理 Hilt 注解，首次构建较慢
- 仓库配置了 JitPack（`https://jitpack.io`），用于获取 Toaster、ShapeView 等第三方库
- 项目未启用 ProGuard 混淆（`minifyEnabled false`）
- 网络安全配置引用 `@xml/network_security_config`，需注意是否允许明文 HTTP 通信（光猫管理页面通常为 HTTP）

## 逆向分析要点

若需对此类光猫管理接口进行进一步逆向分析：

- **CGI 接口**：`/boaform/web_query_user_show.cgi` 是光猫内置 Boa Web Server 的 CGI 脚本
- **认证机制**：当前代码未携带认证 Cookie，说明该接口可能无需登录即可访问，或依赖局域网访问控制
- **加密差异**：不同地区固件版本可能对返回数据进行加密，需对比抓包结果确认
- **扩展方向**：同系列光猫可能存在其他 CGI 接口（如 `web_login.cgi`、`web_switch_mac.cgi`），可通过目录枚举发现
