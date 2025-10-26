<script setup>

import LogoIcon from "@/components/icons/LogoIcon.vue";
import ThemeSwitch from "@/components/ThemeSwitch.vue";
import DropdownIcon from "@/components/icons/DropdownIcon.vue";
import LogoutButton from "@/components/LogoutButton.vue";
</script>

<template>
  <header>
    <div class="header__container">
      <section class="header__section">
        <LogoIcon/>

        <nav class="header__nav">
          <a class="header__nav-link" href="#">Задачи</a>
          <a class="header__nav-link" href="#">Проекты</a>
        </nav>

        <ThemeSwitch/>

        <div class="profile">
          <span class="profile__span">Имя пользователя</span>
          <DropdownIcon/>
        </div>
        <LogoutButton/>
      </section>
    </div>
  </header>
</template>

<style scoped>
.header__section{
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header__nav-link {
  text-align: center;
  color: var(--gray);
}

.header__nav-link:hover {
  color: var(--gray);
  text-decoration: none;
  text-shadow: var(--main-color) 1px 0 10px;
}

.profile{
  width: fit-content;
  display: flex;
  flex-wrap: nowrap;
  flex-direction: row;
  align-items: baseline;
  border-radius: 5px;
  height: 100%;
}


.profile:hover{
  background: rgba(191, 191, 191, 0.37);
  cursor: pointer;

}
</style>