<script setup>

import LogoIcon from "@/components/icons/LogoIcon.vue";
import ThemeSwitch from "@/components/ThemeSwitch.vue";
import DropdownIcon from "@/components/icons/DropdownIcon.vue";
import LogoutButton from "@/components/LogoutButton.vue";
import NotificationButton from "@/components/NotificationButton.vue";
import MenuButton from "@/components/ui/MenuButton.vue";

import { useRoute } from 'vue-router'
const route = useRoute()

const menuItems = [
  { path: '/tasks', title: 'Задачи' },
  { path: '/projects', title: 'Проекты' },
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
            <MenuButton/>
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
            <div class="profile">
              <span class="profile__span">Имя пользователя</span>
              <DropdownIcon/>
            </div>
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
  color: var(--gray);
  padding: 0.5rem;
  margin: 0.5em;
  font-size: 20px;
}

.header__nav-link:hover {
  color: var(--gray);
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

.profile{
  width: fit-content;
  display: flex;
  flex-wrap: nowrap;
  flex-direction: row;
  align-items: baseline;
  border-radius: 5px;
  height: 100%;
  margin: 0 0.5em 0 0.5em;
  padding: 1em;
}

.profile:hover{
  background: rgba(191, 191, 191, 0.37);
  cursor: pointer;
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