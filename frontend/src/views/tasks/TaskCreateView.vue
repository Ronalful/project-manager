<template>
  <BaseModal
      :is-open="isOpen"
      @close="$emit('close')"
      title="Новая задача"
  >
    <div class="task-create-form">
      <div class="form-group">
        <label>Название задачи *</label>
        <input
            v-model="form.title"
            type="text"
            placeholder="Что нужно сделать?"
            class="form-input"
            @keyup.enter="createTask"
        />
      </div>

      <div class="form-group">
        <label>Описание</label>
        <textarea
            v-model="form.description"
            placeholder="Подробное описание задачи..."
            class="form-textarea"
            rows="3"
        ></textarea>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>Статус</label>
          <div class="status-select">
            <div
                v-for="status in statuses"
                :key="status.id"
                class="status-option"
                :class="{ 'is-selected': form.status === status.id }"
                @click="form.status = status.id"
                :style="{
                borderColor: status.color,
                backgroundColor: form.status === status.id ? status.bgColor : 'white'
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
                :class="{ 'is-selected': form.priority === priority.id }"
                @click="form.priority = priority.id"
                :style="{
                backgroundColor: form.priority === priority.id ? priority.color : 'transparent',
                color: form.priority === priority.id ? 'white' : priority.color,
                borderColor: priority.color
              }"
            >
              {{ priority.label }}
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <label>Исполнитель</label>
        <input
            v-model="form.assignee"
            type="text"
            placeholder="Имя исполнителя"
            class="form-input"
        />
      </div>

      <div class="form-group">
        <label>Теги (через запятую)</label>
        <div class="tags-input-wrapper">
          <input
              v-model="tagInput"
              type="text"
              placeholder="bug, frontend, important"
              class="form-input"
              @keydown.enter.prevent="addTag"
          />
          <button class="add-tag-btn" @click="addTag">+</button>
        </div>
        <div class="tags-list" v-if="form.tags.length">
          <span
              v-for="tag in form.tags"
              :key="tag"
              class="tag-item"
          >
            {{ tag }}
            <button class="remove-tag" @click="removeTag(tag)">×</button>
          </span>
        </div>
      </div>
    </div>

    <template #footer>
      <button class="btn-secondary" @click="$emit('close')">Отмена</button>
      <button
          class="btn-primary"
          @click="createTask"
          :disabled="!form.title.trim()"
      >
        Создать задачу
      </button>
    </template>
  </BaseModal>
</template>

<script>
import BaseModal from '@/components/BaseModal.vue'

export default {
  name: 'TaskCreate',
  components: {
    BaseModal
  },
  props: {
    isOpen: {
      type: Boolean,
      default: false
    },
    initialStatus: {
      type: String,
      default: 'pending'
    },
    statuses: {
      type: Array,
      required: true
    },
    priorities: {
      type: Array,
      required: true
    }
  },
  emits: ['close', 'create'],
  data() {
    return {
      form: {
        title: '',
        description: '',
        status: this.initialStatus,
        priority: 'medium',
        assignee: '',
        tags: []
      },
      tagInput: ''
    }
  },
  watch: {
    initialStatus(newVal) {
      if (newVal) {
        this.form.status = newVal
      }
    }
  },
  methods: {
    createTask() {
      if (!this.form.title.trim()) return

      const task = {
        id: Date.now(),
        title: this.form.title.trim(),
        description: this.form.description.trim(),
        status: this.form.status,
        priority: this.form.priority,
        assignee: this.form.assignee.trim(),
        tags: [...this.form.tags],
        createdAt: new Date().toISOString()
      }

      this.$emit('create', task)
      this.resetForm()
    },

    addTag() {
      const tag = this.tagInput.trim()
      if (tag && !this.form.tags.includes(tag)) {
        this.form.tags.push(tag)
        this.tagInput = ''
      }
    },

    removeTag(tagToRemove) {
      this.form.tags = this.form.tags.filter(tag => tag !== tagToRemove)
    },

    resetForm() {
      this.form = {
        title: '',
        description: '',
        status: this.initialStatus,
        priority: 'medium',
        assignee: '',
        tags: []
      }
      this.tagInput = ''
    }
  }
}
</script>

<style scoped>
.task-create-form {
  padding: 20px 0;
}

.form-group {
  margin-bottom: 24px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #4A5568;
  font-size: 14px;
}

.form-input,
.form-textarea {
  width: 100%;
  padding: 12px;
  border: 2px solid #E2E8F0;
  border-radius: 10px;
  font-size: 16px;
  transition: all 0.2s ease;
}

.form-input:focus,
.form-textarea:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
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
  font-size: 14px;
}

.status-option:hover,
.priority-option:hover {
  transform: translateY(-2px);
}

.status-option.is-selected,
.priority-option.is-selected {
  font-weight: 600;
}

.tags-input-wrapper {
  display: flex;
  gap: 8px;
}

.tags-input-wrapper .form-input {
  flex: 1;
}

.add-tag-btn {
  padding: 0 16px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 18px;
  transition: all 0.2s ease;
}

.add-tag-btn:hover {
  background: #5a67d8;
}

.tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 12px;
}

.tag-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: #F7FAFC;
  color: #4A5568;
  padding: 6px 12px;
  border-radius: 12px;
  border: 1px solid #E2E8F0;
  font-size: 14px;
}

.remove-tag {
  background: none;
  border: none;
  color: #A0AEC0;
  cursor: pointer;
  font-size: 18px;
  line-height: 1;
  padding: 0;
  width: 18px;
  height: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

.remove-tag:hover {
  background: #E2E8F0;
  color: #4A5568;
}

.btn-primary,
.btn-secondary {
  padding: 12px 24px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.btn-primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-secondary {
  background: #F7FAFC;
  color: #4A5568;
}

.btn-secondary:hover {
  background: #E2E8F0;
}

.btn-primary + .btn-secondary {
  margin-right: 12px;
}
</style>