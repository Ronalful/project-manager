<script setup>
import LogoutIcon from "@/components/icons/LogoutIcon.vue";
</script>

<template>
  <div class="logout">
    <button
        class="logout-button"
        type="button"
        @click="logoutHandle"
    >
      <LogoutIcon/>
    </button>
  </div>
</template>

<script>
import {authService} from "@/services/AuthService.js";

export default {
  methods:{
    logoutHandle(){
      authService.logout()
    }
  }
}
</script>

<style>
.logout-button{
  background: rgb(0, 0, 0, 0);
  border: 0;
  cursor: pointer;
  margin: 0 0 0 1em;
}
</style>