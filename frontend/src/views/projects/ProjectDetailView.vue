<template>
  <BaseModal :backto="backto" :open="true">
    <template #header>
      <h2>{{ project.name }}</h2>
    </template>

    <template #main>
      <div class="description-section">
        <h3 class="section-title">Описание проекта</h3>
        <p class="project-description">{{ project.description }}</p>
      </div>

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
    </template>
  </BaseModal>
</template>

<script>
import {userAdminService} from "@/services/UserAdminService.js";
import {projectUserService} from "@/services/ProjectUserService.js";
import BaseModal from "@/components/BaseModal.vue";

export default {
  components: {BaseModal},
  data() {
    return {
      backto: '/projects',
      projectId: '',
      project: {
        name: 'Project',
        description: 'проект балванка',
        developers: [
          {
            id: '1',
            firstname: 'Ivan',
            lastname: 'Ivanov',
            email: 'ivanov@gmail.com'
          },
          {
            id: '2',
            firstname: 'Elen',
            lastname: 'Sergeeva',
            email: 'sergeeva@gmail.com'
          },
        ],
      }
    }
  },
  mounted() {

    this.projectId = this.$route.params.id;

    this.handleLoadProject()
    this.handleLoadDevelopers()
  },
  methods: {
    async handleLoadProject() {
      this.loading = true
      try {
        const response = await projectUserService.getProjectInfo(this.projectId)
        if (response.success) {
          this.setProjectInfo(response.data)
        }
      } catch (error) {
        console.error('Error loading projects:', error)
      } finally {
        this.loading = false
      }
    },
    async handleLoadDevelopers() {
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
}
</script>

<style scoped>
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