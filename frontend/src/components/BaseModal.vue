<template>
  <div v-if="visible" class="modal-overlay" @click.self="handleOverlayClick">
    <div class="modal-content">
      <slot></slot>
      <button class="modal-close" @click="close">Закрыть</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BaseModal',
  props: {
    visible: {
      type: Boolean,
      required: true
    }
  },
  methods: {
    close() {
      // Эмитим событие закрытия модального окна
      this.$emit('close');
    },
    handleOverlayClick() {
      // Закрываем при клике на подложку
      this.close();
    }
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex; align-items: center; justify-content: center;
  z-index: 1000;
}
.modal-content {
  background: #fff;
  padding: 2rem;
  border-radius: 4px;
  min-width: 300px;
}
.modal-close {
  margin-top: 1rem;
}
</style>