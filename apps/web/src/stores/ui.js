import {defineStore} from "pinia";

export const uiStore = defineStore("ui", {
  state: () => ({
    isSidebarClosed: false,
    theme: localStorage.getItem("theme") || "light"
  }),
  actions: {
    toggleSidebar() {
      this.isSidebarClosed = !this.isSidebarClosed
    },
    openSidebar() {
      this.isSidebarClosed = false
    },
    closeSidebar() {
      this.isSidebarClosed = true
    },
    toggleTheme() {
      this.theme = this.theme === 'light' ? 'dark' : 'light'
      localStorage.setItem('theme', this.theme)
      document.body.className=`${this.theme}-theme`
    },
    initTheme() {
      document.body.className =`${this.theme}-theme`
    }
  }
})