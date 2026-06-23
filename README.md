# KG Rule Studio

Android APP - KG 规则可视化配置工具

## 功能

- 站点管理：新建/编辑/删除/导入/导出 KG 站点配置
- 7大配置模块：基础、分类、搜索、详情、播放、图片、功能开关
- Steps 编辑器：可视化配置播放解析步骤（GET/POST、请求头、变量提取）
- 实时测试：一键测试每个 Step 的请求和响应
- JSON 导出：生成兼容 KG 的 ext JSON，支持剪贴板/文件/二维码

## 项目结构

    build.gradle
    proguard-rules.pro
    app/
        src/main/
            AndroidManifest.xml
            java/com/github/kgstudio/
                model/         - 数据模型
                editor/        - 编辑器 Fragments + Steps 编辑器
                adapter/       - RecyclerView Adapters
                util/          - JSON 序列化、HTTP 工具
                MainActivity.java
                SiteEditActivity.java
                KGStudioApp.java
            res/               - 布局、主题、菜单
        build.gradle
    README.md
    .gitignore

## 构建

使用 Android Studio 打开本项目，或在线构建平台（GitHub Actions / GitLab CI）。

## 依赖

- AndroidX + Material Components
- OkHttp 4.x（网络请求）
- Gson（JSON 序列化）
- Jsoup（HTML 解析）
