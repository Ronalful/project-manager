<script setup>

import DropdownIcon from "@/components/icons/DropdownIcon.vue";
</script>

<template>
  <div class="profile">
    <span class="profile__span">{{username}}</span>
    <DropdownIcon/>
  </div>
</template>

<script>


export default {
  data(){
    return{
      username: 'username'
    }
  },
}
</script>

<style scoped>
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

</style>