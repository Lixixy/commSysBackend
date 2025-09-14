import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "社团管理后台api文档",
  description: "Community System",
  // 配置GitHub Pages基础路径
  base: '/commSys/',
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: '首页', link: '/' },
      { text: 'API文档', link: '/api' }
    ],

    sidebar: [
      {
        text: '文档',
        items: [
          { text: 'API文档', link: '/api' }
        ]
      }
    ],

    socialLinks: [
      { icon: 'github', link: 'https://github.com/vuejs/vitepress' }
    ]
  }
})
