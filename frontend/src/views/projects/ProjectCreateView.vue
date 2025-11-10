<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <div class="modal-header">
        <h2>Создать проект</h2>
        <button @click="close" class="close-btn">&times;</button>
      </div>

      <form @submit.prevent="submitForm" class="modal-body">
        <div class="form-group">
          <label>Название проекта</label>
          <input
              v-model="form.name"
              type="text"
              required
          >
        </div>

        <div class="form-group">
          <label>Описание</label>
          <textarea
              v-model="form.description"
              rows="3"
          ></textarea>
        </div>

        <div class="form-actions">
          <button
              type="button"
              @click="close"
              class="btn btn-secondary"
          >
            Отмена
          </button>
          <button
              type="submit"
              :disabled="loading"
              class="btn btn-primary"
          >
            {{ loading ? 'Создание...' : 'Создать' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>

import router from "@/router/index.js";

export default {
  emits: ['close', 'success'],

  setup(props, { emit }) {

    const close = () => {
      // Закрываем модалку через эмит или роутер
      emit('close')
      // Или через роутер
      if (window.history.length > 1) {
        router.back()
      } else {
        router.push('/projects')
      }
    }

    return {
      close
    }
  },

  data() {
    return {
      form: {
        name: '',
        description: ''
      },
      loading: false
    }
  },

  methods: {
    async submitForm() {
      this.loading = true
      try {
        // Логика создания проекта
        const response = await apiClient.post('/projects', this.form)

        this.$emit('success', response.data)

        // Автоматическое закрытие после успеха
        this.close()
      } catch (error) {
        console.error('Error creating project:', error)
      } finally {
        this.loading = false
      }
    }
  },

  // Закрытие по ESC
  mounted() {
    const handleEscape = (e) => {
      if (e.key === 'Escape') {
        this.close()
      }
    }

    document.addEventListener('keydown', handleEscape)
  }
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

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-primary {
  background: #007bff;
  color: white;
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>