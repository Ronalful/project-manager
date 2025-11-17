<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <!-- Заголовок -->
      <div class="modal-header">
        <h2 class="project-title">{{ project.name }}</h2>
        <button @click="close" class="close-btn">&times;</button>
      </div>

      <div class="modal-body">
        <div class="description-section">
          <h3 class="section-title">Описание проекта</h3>
          <p class="project-description">{{ project.description }}</p>
        </div>

        <!-- Разработчики -->
        <div class="developers-section">
          <h3 class="section-title">Команда разработки</h3>
          <div class="developers-grid">
            <div
                v-for="developer in project.developers"
                :key="developer.id"
                class="developer-card"
            >
              <div class="developer-avatar">
                {{ getInitials(developer.firstname, developer.lastname) }}
              </div>
              <div class="developer-info">
                <h4 class="developer-name">
                  {{ developer.firstname }} {{ developer.lastname }}
                </h4>
                <p class="developer-email">{{ developer.email }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
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
import {projectUserService} from "@/services/ProjectUserService.js";
import {compile} from "vue";

export default {
  data() {
    return {
      projectId: '',
      project: {
        name: '',
        description: '',
        developers: [],
      }
    }
  },
  mounted() {
    // запрос в бэк
    this.projectId = this.$route.params.id;

    const handleEscape = (e) => {
      if (e.key === 'Escape') {
        this.close()
      }
    }
    document.addEventListener('keydown', handleEscape)

    this.handleLoadProject()
  },
  methods: {
    async handleLoadProject() {
      this.loading = true
      try {
        const response = await projectUserService.getProjectInfo(this.projectId)
        if (response.success) {
          this.setProjectInfo(response.data)
          console.log(this.project)
        }
      } catch (error) {
        console.error('Error loading projects:', error)
      } finally {
        this.loading = false
      }
    },
    setProjectInfo(data) {
      this.project.name = data.name
      this.project.description = data.description
      for (const developer of data.developers) {
        this.project.developers.push({
          id: developer.id,
          firstname: developer.firstname,
          lastname: developer.lastname,
          email: developer.email
        })
      }
    },
    getInitials(firstname, lastname) {
      return `${firstname.charAt(0)}${lastname.charAt(0)}`.toUpperCase();
    },
  },

  components: {
    Select, SubmitButton, Input, Textarea
  },

  emits: ['close', 'success'],

  setup(props, {emit}) {

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
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(4px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  padding: 20px;
}

.modal-content {
  background: var(--background2);
  border-radius: 16px;
  box-shadow: var(--shadow);
  max-width: 600px;
  width: 100%;
  max-height: 90vh;
  overflow: hidden;
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

/* Заголовок */
.modal-header {
  padding: 24px;
  border-bottom: 1px solid var(--border);
  display: flex;
}

.project-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--font-main);
  margin: 0;
  flex: 1;
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

.section-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--font-additional);
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 2px solid var(--border);
}

.description-section {
  margin-bottom: 32px;
}

.project-description {
  color: var(--font-main);
  line-height: 1.6;
  font-size: 1rem;
  margin: 0;
}

/* Разработчики */
.developers-section {
  margin-top: 24px;
}

.developers-grid {
  display: grid;
  gap: 16px;
}

.developer-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: var(--background3);
  border-radius: 12px;
  border: 1px solid var(--border);
  transition: all 0.2s ease;
}

.developer-card:hover {
  background: var(--background2);
  border-color: var(--border);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.developer-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--main-color) 0%, var(--additional-color) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--font-main);
  font-weight: 600;
  font-size: 0.9rem;
  flex-shrink: 0;
}

.developer-info {
  flex: 1;
}

.developer-name {
  font-weight: 600;
  color: var(--font-main);
  margin: 0 0 4px 0;
  font-size: 1rem;
}

.developer-email {
  color: var(--font-additional);
  margin: 0;
  font-size: 0.9rem;
}

@media (max-width: 640px) {
  .modal-content {
    margin: 10px;
    max-width: none;
  }

  .modal-body {
    padding: 20px;
  }

  .developer-card {
    padding: 12px;
  }

  .developer-avatar {
    width: 40px;
    height: 40px;
    font-size: 0.8rem;
  }
}
</style>