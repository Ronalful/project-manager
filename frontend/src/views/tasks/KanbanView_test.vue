<template>
  <div class="kanban-container">
    <div class="kanban-header">
      <div class="header-left">
        <h1 class="kanban-title">Kanban-доска</h1>
      </div>
      <div class="header-right">
          <SingleSelect
              v-model="projects"
              :options="users"
              placeholder="Проект"
          />
        <button class="add-task-btn" @click="openTaskModal">
          <svg class="add-icon" width="20" height="20" viewBox="0 0 20 20" fill="none">
            <path d="M10 4V16M4 10H16" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          Добавить задачу
        </button>
      </div>
    </div>

    <!-- Канбан доска -->
    <div class="kanban-board" :class="{ 'is-dragging': isDragging }">
      <!-- Колонки -->
      <div
          v-for="status in statuses"
          :key="status.id"
          class="kanban-column"
          :style="{ '--column-color': status.color, '--column-bg': status.bgColor }"
          @dragover.prevent
          @drop="handleDrop($event, status.id)"
      >
        <!-- Заголовок колонки -->
        <div class="column-header">
          <div class="column-title">
            <span class="column-label">{{ status.label }}</span>
          </div>
          <div class="column-count" :style="{ backgroundColor: status.color }">
            {{ getTaskCount(status.id) }}
          </div>
        </div>

        <!-- Контейнер для карточек -->
        <div
            class="cards-container"
            :id="`column-${status.id}`"
        >
          <!-- Карточки -->
          <div
              v-for="task in getTasksByStatus(status.id)"
              :key="task.id"
              class="task-card"
              draggable="true"
              :data-task-id="task.id"
              :data-status="task.status"
              @dragstart="handleDragStart($event, task)"
              @dragend="handleDragEnd"
              @click="openTaskDetails(task)"
          >
            <!-- Заголовок карточки -->
            <div class="task-card-header">
              <h3 class="task-title">{{ task.title }}</h3>
              <span class="task-priority" :style="{
                backgroundColor: getPriorityColor(task.priority),
                color: getPriorityTextColor(task.priority)
              }">
                {{ getPriorityLabel(task.priority) }}
              </span>
            </div>

            <!-- Описание -->
            <p class="task-description" v-if="task.description">
              {{ task.description }}
            </p>

            <!-- Теги -->
            <div class="task-tags" v-if="task.tags?.length">
              <span
                  v-for="tag in task.tags"
                  :key="tag"
                  class="task-tag"
              >
                {{ tag }}
              </span>
            </div>

            <!-- Футер карточки -->
            <div class="task-card-footer">
              <div class="task-assignee" v-if="task.assignee">
                <div class="avatar">
                  {{ getInitials(task.assignee) }}
                </div>
                <span class="assignee-name">{{ task.assignee }}</span>
              </div>
              <div class="task-meta">
                <span class="task-id">#{{ task.id }}</span>
                <span class="task-date">{{ formatDate(task.createdAt) }}</span>
              </div>
            </div>
          </div>

          <!-- Сообщение при пустой колонке -->
          <div
              v-if="getTaskCount(status.id) === 0"
              class="empty-column"
              @drop="handleDrop($event, status.id)"
              @dragover.prevent
          >
            <div class="empty-placeholder">
              <svg class="empty-icon" width="40" height="40" viewBox="0 0 40 40" fill="none">
                <path d="M20 10V30M10 20H30" stroke="var(--column-color)" stroke-width="2" stroke-linecap="round"/>
              </svg>
              <p>Перетащите задачу сюда</p>
            </div>
          </div>
        </div>

        <!-- Кнопка добавления задачи -->
        <button
            class="add-card-btn"
            @click="openTaskModal(status.id)"
            :style="{ color: status.color }"
        >
          <svg class="add-icon-sm" width="16" height="16" viewBox="0 0 16 16" fill="none">
            <path d="M8 3V13M3 8H13" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          Добавить задачу
        </button>
      </div>
    </div>

    <!-- Модальное окно создания задачи -->
    <div v-if="showTaskModal" class="modal-overlay" @click="closeTaskModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Новая задача</h3>
          <button class="modal-close" @click="closeTaskModal">
            <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
              <path d="M15 5L5 15M5 5L15 15" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>Название задачи</label>
            <Input
                ref="nameField"
                v-model="newTask.title"
                type="text"
                placeholder="Название задачи"
                required
            ></Input>
          </div>
          <div class="form-group">
            <label>Описание</label>
            <Textarea
                ref="descriptionField"
                v-model="newTask.description"
                placeholder="Описание задачи"
                required
            >
            </Textarea>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Статус</label>
              <div class="status-select">
                <div
                    v-for="status in statuses"
                    :key="status.id"
                    class="status-option"
                    :class="{ 'is-selected': newTask.status === status.id }"
                    @click="newTask.status = status.id"
                    :style="{
                    borderColor: status.color,
                    backgroundColor: newTask.status === status.id ? status.bgColor : 'var(--background)'
                  }"
                >
                  <span class="status-option-icon">{{ status.icon }}</span>
                  <span class="status-option-label">{{ status.label }}</span>
                </div>
              </div>
            </div>
            <div class="form-group">
              <label>Приоритет</label>
              <div class="priority-select">
                <div
                    v-for="priority in priorities"
                    :key="priority.id"
                    class="priority-option"
                    :class="{ 'is-selected': newTask.priority === priority.id }"
                    @click="newTask.priority = priority.id"
                    :style="{
                    backgroundColor: newTask.priority === priority.id ? priority.color : 'transparent',
                    color: newTask.priority === priority.id ? 'white' : priority.color,
                    borderColor: priority.color
                  }"
                >
                  {{ priority.label }}
                </div>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label>Исполнители</label>
            <MultiSelect

            />
          </div>
        </div>
        <div class="modal-footer">
          <SubmitButton>
            Создать задачу
          </SubmitButton>
        </div>
      </div>
    </div>

    <!-- Модальное окно деталей задачи -->
    <div v-if="showTaskDetails" class="modal-overlay" @click="closeTaskDetails">
      <div class="modal-content task-details" @click.stop>
        <div class="modal-header">
          <h3>Задача #{{ selectedTask?.id }}</h3>
          <button class="modal-close" @click="closeTaskDetails">
            <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
              <path d="M15 5L5 15M5 5L15 15" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </button>
        </div>
        <div class="modal-body">
          <div class="task-details-header">
            <h2 class="details-title">{{ selectedTask?.title }}</h2>
            <div class="details-status" :style="{ color: getStatusColor(selectedTask?.status) }">
              {{ getStatusLabel(selectedTask?.status) }}
            </div>
          </div>

          <div class="details-section">
            <h4>Описание</h4>
            <p class="details-description">{{ selectedTask?.description || 'Нет описания' }}</p>
          </div>

          <div class="details-grid">
            <div class="details-item">
              <span class="details-label">Приоритет:</span>
              <span
                  class="details-value priority-badge"
                  :style="{
                  backgroundColor: getPriorityColor(selectedTask?.priority),
                  color: getPriorityTextColor(selectedTask?.priority)
                }"
              >
                {{ getPriorityLabel(selectedTask?.priority) }}
              </span>
            </div>
            <div class="details-item">
              <span class="details-label">Исполнитель:</span>
              <span class="details-value">
                <div class="avatar-sm">{{ getInitials(selectedTask?.assignee) }}</div>
                {{ selectedTask?.assignee || 'Не назначен' }}
              </span>
            </div>
            <div class="details-item">
              <span class="details-label">Создана:</span>
              <span class="details-value">{{ formatDate(selectedTask?.createdAt) }}</span>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <DeleteButton
              @click="deleteTask(selectedTask?.id)"
          >
            Удалить задачу
          </DeleteButton>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {useUserStore} from "@/stores/UserStore.js";
import {projectAdminService} from "@/services/ProjectAdminService.js";
import {projectUserService} from "@/services/ProjectUserService.js";

export default {
  name: 'KanbanBoard',
  data() {
    return {
      tasks: [],
      projects: [],
      isDragging: false,
      draggedTask: null,
      showTaskModal: false,
      showTaskDetails: false,
      selectedTask: null,
      newTask: {
        title: '',
        description: '',
        status: 'pending',
        priority: 'medium',
        assignee: '',
        tagInput: '',
        tags: []
      }
    }
  },
  computed: {
    statuses() {
      // Здесь должен быть import или определение KANBAN_STATUSES
      return Object.values(KANBAN_STATUSES)
    },
    priorities() {
      return Object.values(TASK_PRIORITY)
    },
    totalTasks() {
      return this.tasks.length
    },
    inProgressTasks() {
      return this.tasks.filter(task => task.status === 'in_progress').length
    }
  },
  mounted() {
    this.handleLoadProjects();
  },
  methods: {
    async handleLoadProjects() {
      this.loading = true
      const userStore = useUserStore()
      try {
        if (userStore.isAdmin) {
          this.withCreate = true

          const response = await projectAdminService.getAllProjects()
          if (response.success) {
            this.projects = response.data;
          }
        } else {
          const response = await projectUserService.getMyProjects()
          if (response.success) {
            this.projects = response.data;
          }
        }
      } catch (error) {
        console.error('Error loading projects:', error)
      } finally {
        this.loading = false
      }
    },
    getTasksByStatus(statusId) {
      return this.tasks.filter(task => task.status === statusId)
          .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    },
    getTaskCount(statusId) {
      return this.getTasksByStatus(statusId).length
    },
    // ... все остальные методы из setup
    handleDragStart(event, task) {
      // ... код метода
    },
    handleDragEnd(event) {
      // ... код метода
    },
    handleDrop(event, newStatus) {
      // ... код метода
    },
    // и так далее для всех методов
  }
}
</script>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { TASK_STATUS, TASK_PRIORITY } from '@/consts/task.js'
import MultiSelect from "@/components/ui/MultiSelect.vue";
import SingleSelect from "@/components/ui/SingleSelect.vue";
import SubmitButton from "@/components/ui/SubmitButton.vue";
import DeleteButton from "@/components/ui/DeleteButton.vue";
import Input from "@/components/ui/Input.vue";
import Textarea from "@/components/ui/Textarea.vue";

// Состояние
const tasks = ref([])
const isDragging = ref(false)
const draggedTask = ref(null)
const showTaskModal = ref(false)
const showTaskDetails = ref(false)
const selectedTask = ref(null)

// Новая задача
const newTask = ref({
  title: '',
  description: '',
  status: 'pending',
  priority: 'medium',
  assignee: '',
  tagInput: '',
  tags: []
})

// Константы
const statuses = computed(() => Object.values(TASK_STATUS))
const priorities = computed(() => Object.values(TASK_PRIORITY))

// Вычисляемые свойства
const totalTasks = computed(() => tasks.value.length)
const inProgressTasks = computed(() =>
    tasks.value.filter(task => task.status === 'in_progress').length
)

// Методы
const getTasksByStatus = (statusId) => {
  return tasks.value.filter(task => task.status === statusId)
      .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
}

const getTaskCount = (statusId) => {
  return getTasksByStatus(statusId).length
}

const getPriorityColor = (priorityId) => {
  const priority = priorities.value.find(p => p.id === priorityId)
  return priority?.color || '#CBD5E0'
}

const getPriorityTextColor = (priorityId) => {
  const priority = priorities.value.find(p => p.id === priorityId)
  return priority?.id === 'high' || priority?.id === 'medium' ? 'white' : '#2D3748'
}

const getPriorityLabel = (priorityId) => {
  const priority = priorities.value.find(p => p.id === priorityId)
  return priority?.label || 'Не указан'
}

const getStatusColor = (statusId) => {
  const status = statuses.value.find(s => s.id === statusId)
  return status?.color || '#CBD5E0'
}

const getStatusLabel = (statusId) => {
  const status = statuses.value.find(s => s.id === statusId)
  return status?.label || 'Неизвестно'
}

const getInitials = (name) => {
  if (!name) return '??'
  return name.split(' ').map(n => n[0]).join('').toUpperCase().substring(0, 2)
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('ru-RU', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric'
  })
}

// Drag & Drop
const handleDragStart = (event, task) => {
  isDragging.value = true
  draggedTask.value = task
  event.dataTransfer.effectAllowed = 'move'
  event.dataTransfer.setData('text/plain', task.id)

  // Добавляем класс к перетаскиваемому элементу
  setTimeout(() => {
    event.target.classList.add('dragging')
  }, 0)
}

const handleDragEnd = (event) => {
  isDragging.value = false
  draggedTask.value = null

  // Удаляем класс со всех элементов
  document.querySelectorAll('.task-card').forEach(card => {
    card.classList.remove('dragging')
  })
}

const handleDrop = (event, newStatus) => {
  event.preventDefault()

  if (!draggedTask.value) return

  // Находим задачу в массиве
  const taskIndex = tasks.value.findIndex(t => t.id === draggedTask.value.id)
  if (taskIndex !== -1) {
    tasks.value[taskIndex].status = newStatus
  }

  isDragging.value = false
  draggedTask.value = null
}

// Управление задачами
const openTaskModal = (status = 'pending') => {
  newTask.value.status = status
  showTaskModal.value = true
}

const closeTaskModal = () => {
  showTaskModal.value = false
  resetNewTask()
}

const addNewTask = () => {
  if (!newTask.value.title.trim()) return

  const task = {
    id: Date.now(),
    title: newTask.value.title,
    description: newTask.value.description,
    status: newTask.value.status,
    priority: newTask.value.priority,
    assignee: newTask.value.assignee,
    tags: newTask.value.tags,
    createdAt: new Date().toISOString()
  }

  tasks.value.unshift(task)
  closeTaskModal()
}

const resetNewTask = () => {
  newTask.value = {
    title: '',
    description: '',
    status: 'pending',
    priority: 'medium',
    assignee: '',
    tagInput: '',
    tags: []
  }
}

const addTag = () => {
  const tag = newTask.value.tagInput.trim()
  if (tag && !newTask.value.tags.includes(tag)) {
    newTask.value.tags.push(tag)
    newTask.value.tagInput = ''
  }
}

const openTaskDetails = (task) => {
  selectedTask.value = task
  showTaskDetails.value = true
}

const closeTaskDetails = () => {
  showTaskDetails.value = false
  selectedTask.value = null
}

const deleteTask = (taskId) => {
  tasks.value = tasks.value.filter(task => task.id !== taskId)
  closeTaskDetails()
}

// Инициализация тестовых данных
onMounted(() => {
  // Тестовые задачи
  const mockTasks = [
    {
      id: 1,
      title: 'Создать дизайн системы',
      description: 'Разработать дизайн-систему для нового проекта',
      status: 'pending',
      priority: 'high',
      assignee: 'Анна Петрова',
      createdAt: '2024-01-15'
    },
    {
      id: 2,
      title: 'Настроить CI/CD пайплайн',
      description: 'Автоматизировать процесс деплоя',
      status: 'evaluation',
      priority: 'medium',
      assignee: 'Иван Сидоров',
      createdAt: '2024-01-14'
    },
    {
      id: 3,
      title: 'Реализовать авторизацию',
      description: 'Добавить JWT авторизацию на бэкенде',
      status: 'in_progress',
      priority: 'high',
      assignee: 'Мария Иванова',
      createdAt: '2024-01-13'
    },
    {
      id: 4,
      title: 'Написать тесты для API',
      description: 'Покрыть тестами все эндпоинты API',
      status: 'testing',
      priority: 'medium',
      assignee: 'Алексей Козлов',
      createdAt: '2024-01-12'
    },
    {
      id: 5,
      title: 'Исправить баг с модалками',
      description: 'Модальные окна не закрываются по клику вне',
      status: 'completed',
      priority: 'low',
      assignee: 'Елена Смирнова',
      createdAt: '2024-01-11'
    },
    {
      id: 6,
      title: 'Оптимизировать загрузку изображений',
      description: 'Добавить lazy loading для изображений',
      status: 'in_progress',
      priority: 'medium',
      assignee: 'Дмитрий Волков',
      createdAt: '2024-01-10'
    },
    {
      id: 7,
      title: 'Добавить темную тему',
      description: 'Реализовать переключение светлой/темной темы',
      status: 'evaluation',
      priority: 'low',
      assignee: 'Ольга Новикова',
      createdAt: '2024-01-09'
    }
  ]

  tasks.value = mockTasks
})
</script>

<style scoped>
.kanban-container {
  min-height: 100vh;
  background-color: var(--background3);
  padding: 20px;
  border-radius: 16px;
}

.kanban-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  color: var(--font-main);
  padding: 0 10px;
}

.header-left .kanban-title {
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 8px 0;
}

.header-left .kanban-subtitle {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 30px;
}

.stats {
  display: flex;
  gap: 20px;
  background: rgba(255, 255, 255, 0.1);
  padding: 10px 20px;
  border-radius: 12px;
  backdrop-filter: blur(10px);
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-label {
  font-size: 12px;
  opacity: 0.8;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
}

.add-task-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  background: var(--background);
  color: var(--main-color);
  border: none;
  padding: 12px 24px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.add-task-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px var(--shadow);
}

.add-icon {
  margin-right: 4px;
}

.kanban-board {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 20px;
  min-height: 70vh;
}

.kanban-board.is-dragging .task-card {
  cursor: grabbing;
}

/* Колонки */
.kanban-column {
  background-color: var(--background);
  border-radius: 16px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  min-height: 600px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.kanban-column:hover {
  box-shadow: 0 12px 40px var(--shadow);
}

.column-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid var(--column-bg);
}

.column-title {
  display: flex;
  align-items: center;
  gap: 10px;
}

.column-icon {
  font-size: 20px;
}

.column-label {
  font-size: 18px;
  font-weight: 600;
  color: var(--column-color);
}

.column-count {
  color: var(--font-main);
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  min-width: 32px;
  text-align: center;
}

/* Контейнер карточек */
.cards-container {
  flex: 1;
  min-height: 200px;
  padding: 5px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.cards-container.drag-over {
  background: var(--column-bg);
}

/* Карточки */
.task-card {
  background-color: var(--background2);
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  cursor: grab;
  transition: all 0.3s ease;
  border: 2px solid transparent;
  box-shadow: 0 2px 8px var(--shadow);
  position: relative;
  overflow: hidden;
}

.task-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px var(--shadow);
  border-color: var(--column-color);
}

.task-card.dragging {
  opacity: 0.5;
  transform: rotate(5deg);
  cursor: grabbing;
}

.task-card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.task-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--font-main);
  margin: 0;
  flex: 1;
  line-height: 1.4;
}

.task-priority {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 12px;
  font-weight: 600;
  margin-left: 8px;
  white-space: nowrap;
}

.task-description {
  font-size: 14px;
  color: var(--font-main);
  line-height: 1.5;
  margin: 0 0 12px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.task-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 16px;
}

.task-card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid var(--border);
}

.task-assignee {
  display: flex;
  align-items: center;
  gap: 8px;
}

.avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: var(--column-color);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
}

.assignee-name {
  font-size: 14px;
  color: var(--font-main);
}

/* Пустая колонка */
.empty-column {
  height: 100%;
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px dashed var(--column-color);
  border-radius: 12px;
  background: var(--column-bg);
  opacity: 0.5;
  transition: all 0.3s ease;
}

.empty-column:hover {
  opacity: 0.8;
}

.empty-placeholder {
  text-align: center;
  color: var(--column-color);
}

.empty-placeholder p {
  margin: 8px 0 0 0;
  font-size: 14px;
}

/* Кнопка добавления карточки */
.add-card-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  padding: 12px;
  background: transparent;
  border: 2px dashed var(--column-color);
  border-radius: 12px;
  color: var(--column-color);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  margin-top: 12px;
  transition: all 0.3s ease;
}

.add-card-btn:hover {
  background: var(--column-bg);
  border-style: solid;
}

.add-icon-sm {
  margin-right: 4px;
}

/* Модальные окна */
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
  backdrop-filter: blur(4px);
}

.modal-content {
  background: var(--background2);
  border-radius: 20px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  animation: modalSlideIn 0.3s ease;
}

@keyframes modalSlideIn {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 1px solid var(--border);
}

.modal-header h3 {
  margin: 0;
  font-size: 24px;
  color: var(--font-main);
}

.modal-close {
  background: none;
  border: none;
  color: #718096;
  cursor: pointer;
  padding: 8px;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.modal-close:hover {
  color: var(--font-additional);
}

.modal-body {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--font-additional);
}

.form-input,
.form-textarea {
  width: 100%;
  padding: 12px;
  border: 2px solid var(--border);
  border-radius: 10px;
  font-size: 16px;
  transition: all 0.2s ease;
}

.form-input:focus,
.form-textarea:focus {
  outline: none;
  border-color: var(--main-color);
  box-shadow: 0 0 0 3px var(--shadow);
}

.form-textarea {
  resize: vertical;
  min-height: 80px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.status-select,
.priority-select {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 8px;
}

.status-option,
.priority-option {
  padding: 10px 16px;
  border-radius: 10px;
  border: 2px solid;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-option:hover,
.priority-option:hover {
  transform: translateY(-2px);
}

.status-option.is-selected,
.priority-option.is-selected {
  font-weight: 600;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 24px;
  border-top: 1px solid var(--border);
}

/* Детали задачи */
.task-details .details-title {
  font-size: 24px;
  color: #2D3748;
  margin: 0 0 8px 0;
}

.details-status {
  font-size: 14px;
  font-weight: 600;
  padding: 6px 12px;
  border-radius: 12px;
  background: var(--column-bg, #F7FAFC);
  display: inline-block;
}

.details-section {
  margin-bottom: 24px;
}

.details-section h4 {
  font-size: 18px;
  color: #4A5568;
  margin: 0 0 12px 0;
}

.details-description {
  font-size: 16px;
  line-height: 1.6;
  color: #2D3748;
  background: #F7FAFC;
  padding: 16px;
  border-radius: 10px;
  margin: 0;
}

.details-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.details-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.details-label {
  font-size: 14px;
  color: var(--font-main);
}

.details-value {
  font-size: 16px;
  color: var(--font-main);
  display: flex;
  align-items: center;
  gap: 8px;
}

.priority-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
}

.avatar-sm {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: var(--column-color);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  font-weight: 600;
}

/* Адаптивность */
@media (max-width: 1200px) {
  .kanban-board {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .kanban-header {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }

  .header-right {
    flex-direction: column;
    gap: 15px;
  }

  .stats {
    width: 100%;
  }

  .kanban-board {
    grid-template-columns: 1fr;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .modal-content {
    width: 95%;
    margin: 10px;
  }
}
</style>