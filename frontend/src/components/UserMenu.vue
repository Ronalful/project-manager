<template>
  <div class="profile">
    <span class="profile__span">{{ fullname }}</span>
    <DropdownIcon/>
  </div>
</template>

<script>
import {mapState} from "pinia";
import {useUserStore} from "@/stores/UserStore.js";
import DropdownIcon from "@/components/icons/DropdownIcon.vue";

export default {
  components: {DropdownIcon},
  computed: {
    ...mapState(useUserStore, ['fullname']),
  },
}
</script>

<style scoped>
.profile {
  width: fit-content;
  display: flex;
  flex-wrap: nowrap;
  flex-direction: row;
  align-items: flex-end;
  border-radius: 5px;
  height: 100%;
  margin: 0 0.5em 0 0.5em;
  padding: 1em;
}

.profile:hover {
  background: rgba(191, 191, 191, 0.37);
  cursor: pointer;
}

</style>