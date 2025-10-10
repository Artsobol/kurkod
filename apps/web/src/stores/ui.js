import {defineStore} from "pinia";

export const uiStore = defineStore("ui", {
  state: () => ({
    isSidebarClosed: false
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
    }
  }
})