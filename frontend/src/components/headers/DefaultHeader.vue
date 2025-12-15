<script setup>

import LogoIcon from "@/components/icons/LogoIcon.vue";
import ThemeSwitch from "@/components/ThemeSwitch.vue";
import DropdownIcon from "@/components/icons/DropdownIcon.vue";
import LogoutButton from "@/components/LogoutButton.vue";
import NotificationButton from "@/components/NotificationButton.vue";
import MenuButton from "@/components/ui/MenuButton.vue";

import { useRoute } from 'vue-router'
import UserMenu from "@/components/UserMenu.vue";
const route = useRoute()

const menuItems = [
  { path: '/tasks', title: 'Задачи' },
  { path: '/projects', title: 'Проекты' },
  { path: '/users', title: 'Пользователи' },
]

const isActive = (path) => {
  if (path === '/') {
    return route.path === '/'
  }
  return route.path.startsWith(path)
}
</script>

<template>
  <header>
    <div class="header__container">
      <div class="header__container-inter">
        <section class="header__section">
          <div class="header__left-menu">
            <LogoIcon/>

            <nav class="header__nav">
              <router-link
                  v-for="item in menuItems"
                  :key="item.path"
                  :to="item.path"
                  class="header__nav-link"
                  :class="{ 'active': isActive(item.path) }"
              >
                {{ item.title }}
              </router-link>
            </nav>
          </div>

          <div class="header__right-menu">
            <ThemeSwitch/>
            <NotificationButton/>
            <UserMenu/>
            <LogoutButton/>
          </div>
        </section>
      </div>
    </div>
  </header>
</template>

<script>

</script>

<style scoped>
.header__section{
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header__nav{
  margin-left: 1rem;
}

.header__nav-link {
  text-align: center;
  color: var(--font-main);
  padding: 20px;
  margin: 2em;
}

.header__nav-link:hover {
  color: var(--font-additional);
  text-decoration: none;
}

.header__nav-link:hover:not(.active){
  text-shadow: var(--main-color) 1px 0 10px;
}

.header__nav-link.active{
  border-bottom: 5px solid;
  color: var(--main-color);
}

.header__left-menu{
  display: flex;
  align-items: center;
}

.header__right-menu{
  display: flex;
  align-items: center;
}

.header__container{
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
  width: 100%;
  background-color: var(--background2);
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  border: 10px;
}

.header__container-inter{
  box-shadow: 0 1px 2px 0 rgb(0 0 0 / 0.05);;
}
</style>