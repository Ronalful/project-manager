<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <div class="modal-header">
        <h2>Создать проект</h2>
        <button @click="close" class="close-btn">&times;</button>
      </div>

      <form @submit.prevent="submitForm" class="modal-body" novalidate>
          <Input
              ref="nameField"
              v-model="form.name"
              type="text"
              placeholder="Название проекта"
              required
          ></Input>

          <Select
              v-model="form.developers"
              :options="users"
              placeholder="Исполнители"
              multiple
          ></Select>

          <Textarea
            ref="descriptionField"
            v-model="form.description"
            placeholder="Описание проекта"
            required
            >
          </Textarea>

        <div class="form-actions">
          <SubmitButton
          >
            {{ loading ? 'Создание...' : 'Создать' }}
          </SubmitButton>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import router from "@/router/index.js";
import {projectAdminService} from "@/services/ProjectAdminService.js";
import {userAdminService} from "@/services/UserAdminService.js";
import Input from "@/components/ui/Input.vue";
import Textarea from "@/components/ui/Textarea.vue"
import SubmitButton from "@/components/ui/SubmitButton.vue";
import Select from "@/components/ui/Select.vue";

export default {
  data() {
    return {
      form: {
        name: '',
        description: '',
        developers: [],
      },
      users: [],
      loading: false,
      loadingUsers: false,
    }
  },

  methods: {
    async submitForm() {
      this.loading = true
      try {
        if (!this.validateForm()) {
          return;
        }
        const response = await projectAdminService.createProject({
          name: this.form.name,
          description: this.form.description
        })

        if(response.success){
          for(const developer of this.form.developers){
            const assign = await projectAdminService.assignDeveloper({
              projectId: response.data.id,
              userId: developer
            })
          }
        }

        this.$emit('success', response.data)

        this.close()
      } catch (error) {
        console.error('Error creating project:', error)
      } finally {
        this.loading = false
      }
    },

    async handleLoadDevelopers(){
      this.loadingUsers = true
      try {
        const response = await userAdminService.getAllUsers()
        if (response.success) {
          this.users = response.data;
        }
      } catch (error) {
        console.error('Error loading users:', error)
      } finally {
        this.loadingUsers = false
      }
    },

    validateForm(){
      const fields = this.$refs
      let isValid = true

      Object.values(fields).forEach(field => {
        if (!field.isValid()) {
          isValid = false
        }
      })

      return isValid
    }
  },

  components: {Select, SubmitButton, Input, Textarea},
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