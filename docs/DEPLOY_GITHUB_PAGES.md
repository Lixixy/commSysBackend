# GitHub Pages 部署指南

本指南将帮助您将社团管理系统的API文档部署到GitHub Pages上。

## 准备工作

1. 确保您已经在GitHub上创建了一个仓库（建议使用`commonSys`作为仓库名）
2. 确保您的项目已经配置了正确的`.gitignore`文件
3. 确保您已经安装了Node.js和npm

## 配置VitePress

我们已经在`.vitepress/config.mts`文件中配置了GitHub Pages所需的基础路径：

```javascript
// 配置GitHub Pages基础路径
base: '/commonSys/',
```

如果您的仓库名称不是`commonSys`，请相应地修改这个值。

## 构建文档

在部署之前，您需要先构建文档：

```bash
npm run docs:build
```

这个命令会在`docs/.vitepress/dist`目录下生成静态文件。

## 部署方法

### 方法一：使用GitHub Actions自动部署（推荐）

1. 在项目根目录创建`.github/workflows/deploy.yml`文件：

```yaml
name: Deploy VitePress site to GitHub Pages

on:
  # 每当推送到master分支时触发部署
  push:
    branches: [master]
  # 允许从Actions选项卡手动触发部署
  workflow_dispatch:

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
        with:
          fetch-depth: 0 # 如果您的文档需要Git历史记录
      
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: 18
      
      - name: Install dependencies
        run: npm install
      
      - name: Build documentation
        run: npm run docs:build
      
      - name: Deploy to GitHub Pages
        uses: peaceiris/actions-gh-pages@v3
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
          publish_dir: ./docs/.vitepress/dist
```

2. 提交并推送到GitHub仓库

3. 在GitHub仓库的设置中，转到"Pages"选项卡，将"Source"设置为"GitHub Actions"

GitHub Actions将会自动构建和部署您的文档。

### 方法二：手动部署

1. 构建文档：

```bash
npm run docs:build
```

2. 安装`gh-pages`包：

```bash
npm install -g gh-pages
```

3. 部署文档：

```bash
gh-pages -d docs/.vitepress/dist
```

4. 在GitHub仓库的设置中，转到"Pages"选项卡，确保"Source"设置为`gh-pages`分支

## 访问文档

部署完成后，您可以通过以下URL访问文档：

```url
https://[your-username].github.io/commonSys/
```

请将`[your-username]`替换为您的GitHub用户名。

## 常见问题

### 文档显示404错误

- 检查`.vitepress/config.mts`中的`base`配置是否与仓库名称一致
- 确保GitHub Pages的源设置正确
- 等待几分钟让GitHub Pages完全部署

### 样式或JavaScript不加载

- 确保`base`配置正确
- 检查构建输出中是否有错误
- 清除浏览器缓存后重试

### GitHub Actions部署失败

- 检查工作流文件中的语法错误
- 查看Actions日志以获取详细错误信息
- 确保您有足够的权限

如果您遇到其他问题，请参考[VitePress文档](https://vitepress.dev/guide/deploy)获取更多帮助。
