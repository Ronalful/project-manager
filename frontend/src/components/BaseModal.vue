<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <div class="modal-header">
        <h2>{{ title }}</h2>
        <button @click="close" class="close-btn">&times;</button>
      </div>
      <div class="modal-main">
        <slot></slot>
      </div>
      <div class="modal-footer">

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
    title: {
      type: String,
      default: ''
    },
    backto: {
      type: String,
      default: '/',
    }
  },
  emits: ['close'],
  setup(props, { emit }) {

    const close = () => {
      emit('close')
      router.push(props.backto)
      this.isOpen = false
    }

    return {
      close
    }
  },
  mounted() {
    const handleEscape = (e) => {
      if (e.key === 'Escape') {
        this.close()
      }
    }
    document.addEventListener('keydown', handleEscape)

    this.handleLoadDevelopers()
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
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  max-width: 500px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
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
}
</style>