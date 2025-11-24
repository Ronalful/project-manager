<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <div class="modal-header">
        <div class="modal-title">
          <slot name="header"></slot>
        </div>
        <button @click="close" class="close-btn">&times;</button>
      </div>
      <div class="modal-body">
        <slot name="main"></slot>
      </div>
      <div class="modal-footer">
        <slot name="footer"></slot>
      </div>
    </div>
  </div>
</template>

<script>
import router from "@/router/index.js";

export default {
  name: 'BaseModal',
  data(){
    return{
      isOpen: true,
    }
  },
  props: {
    open: {
      type: Boolean,
      default: false,
    },
    backto: {
      type: String,
      default: '/',
    }
  },
  emits: ['close'],
  methods:{
    close() {
      this.$emit('close')
      router.push(this.backto)
      this.isOpen = false
    }
  },
  mounted() {
    const handleEscape = (e) => {
      if (e.key === 'Escape') {
        this.close()
      }
    }
    document.addEventListener('keydown', handleEscape)
  },
  provide() {
    return {
      closeModal: this.close
    }
  },
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.modal-content {
  background: var(--background2);
  padding: 20px;
  border-radius: 16px;
  max-width: 600px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 10px 30px var(--shadow);
  animation: modalAppear 0.3s ease-out;
}

@keyframes modalAppear {
  from {
    opacity: 0;
    transform: scale(0.9) translateY(-20px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

.modal-header {
  padding: 24px;
  border-bottom: 1px solid var(--border);
  display: flex;
  justify-content: space-between;
}

.modal-title {
  color: var(--font-main);
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--font-main);
}

.modal-body {
  padding: 24px;
  overflow-y: auto;
  max-height: calc(90vh - 100px);
}

.modal-footer{
  padding: 0 24px;
  display: flex;
  flex-direction: row-reverse;

}

@media (max-width: 640px) {
  .modal-content {
    margin: 10px;
    max-width: none;
  }

  .modal-body {
    padding: 20px;
  }
}
</style>