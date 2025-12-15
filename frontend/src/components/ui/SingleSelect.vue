<template>
  <div class="single-select">
    <!-- Поле поиска -->
    <div class="search-container">
      <input
          v-model="searchQuery"
          @focus="isOpen = true"
          @blur="handleBlur"
          @keydown.esc="isOpen = false"
          @keydown.down="highlightNext"
          @keydown.up="highlightPrev"
          @keydown.enter="selectHighlighted"
          :placeholder="selectedItem ? '' : placeholder"
          class="search-input"
          type="text"
      />
      <button
          @click="toggleDropdown"
          class="dropdown-toggle"
          :class="{ 'open': isOpen }"
      >
        ▼
      </button>
    </div>

    <!-- Выпадающий список -->
    <div v-if="isOpen" class="dropdown-list">
      <div
          v-for="(item, index) in filteredItems"
          :key="item.value"
          @click="selectItem(item)"
          @mouseenter="highlightedIndex = index"
          class="dropdown-item"
          :class="{
          'selected': isSelected(item),
          'highlighted': index === highlightedIndex
        }"
      >
        {{ item.label }}
        <span v-if="isSelected(item)" class="checkmark">✓</span>
      </div>

      <div v-if="filteredItems.length === 0" class="no-results">
        Ничего не найдено
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SingleSelect',
  props: {
    options: {
      type: Array,
      required: true,
      default: () => []
    },
    value: {
      type: [Object, null],
      default: null
    },
    placeholder: {
      type: String,
      default: 'Выберите вариант...'
    },
    preSelectedItem: {
      type: [Object, null],
      default: null
    },
    modelValue: {
      type: [Object, null],
      default: null
    }
  },
  data() {
    return {
      isOpen: false,
      searchQuery: '',
      highlightedIndex: -1,
      selectedItem: null,
      blurTimeout: null
    }
  },
  created() {
    if (this.preSelectedItem) {
      this.selectedItem = this.preSelectedItem
    } else if (this.value) {
      this.selectedItem = this.value
    } else if (this.modelValue) {
      this.selectedItem = this.modelValue
    }
  },
  computed: {
    filteredItems() {
      if (!this.searchQuery) {
        return this.options
      }

      const query = this.searchQuery.toLowerCase()
      return this.options.filter(option =>
          option.label.toLowerCase().includes(query)
      )
    }
  },
  emits: ['update:modelValue', 'change'],
  watch: {
    value: {
      immediate: true,
      handler(newVal) {
        this.selectedItem = newVal
      }
    },
    modelValue: {
      immediate: true,
      handler(newVal) {
        this.selectedItem = newVal
      }
    },
    selectedItem: {
      deep: true,
      handler(newVal) {
        // Эмитим события для v-model и обычного value
        this.$emit('update:modelValue', newVal)
        this.$emit('change', newVal)

        // Для обратной совместимости
        this.$emit('input', newVal)
      }
    }
  },
  methods: {
    selectItem(item) {
      this.selectedItem = item
      this.searchQuery = ''
      this.isOpen = false
      this.highlightedIndex = -1
    },

    clearSelection() {
      this.selectedItem = null
      this.searchQuery = ''
      this.$nextTick(() => {
        this.isOpen = true
      })
    },

    isSelected(item) {
      return this.selectedItem && this.selectedItem.value === item.value
    },

    toggleDropdown() {
      this.isOpen = !this.isOpen
      if (this.isOpen) {
        this.$nextTick(() => {
          this.highlightedIndex = -1
          if (!this.selectedItem) {
            this.searchQuery = ''
          }
        })
      }
    },

    highlightNext() {
      if (this.highlightedIndex < this.filteredItems.length - 1) {
        this.highlightedIndex++
      }
    },

    highlightPrev() {
      if (this.highlightedIndex > 0) {
        this.highlightedIndex--
      }
    },

    selectHighlighted() {
      if (this.highlightedIndex >= 0 && this.filteredItems[this.highlightedIndex]) {
        this.selectItem(this.filteredItems[this.highlightedIndex])
      }
    },

    handleBlur() {
      // Небольшая задержка перед закрытием, чтобы клик по опции успел сработать
      this.blurTimeout = setTimeout(() => {
        this.isOpen = false
      }, 200)
    }
  },
  mounted() {
    document.addEventListener('click', (e) => {
      if (!this.$el.contains(e.target)) {
        this.isOpen = false
      }
    })
  },
  beforeUnmount() {
    if (this.blurTimeout) {
      clearTimeout(this.blurTimeout)
    }
  }
}
</script>

<style scoped>
.single-select {
  position: relative;
  width: 100%;
  padding: 10px;
  border: 0;
  border-radius: 10px;
  box-sizing: border-box;
  background-color: var(--field);
  color: var(--font-main);
  margin-top: 15px;
  margin-bottom: 5px;
}

.selected-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--main-pale);
  color: var(--font-additional);
  padding: 8px 12px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 8px;
  border: 1px solid var(--border);
}

.clear-btn {
  background: none;
  border: none;
  color: var(--font-main);
  cursor: pointer;
  font-size: 18px;
  padding: 0;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background-color 0.2s ease;
}

.clear-btn:hover {
  background-color: rgba(0, 0, 0, 0.1);
}

.search-container {
  position: relative;
  display: flex;
  align-items: center;
}

.search-input {
  width: 100%;
  padding: 10px 40px 10px 12px;
  border: 2px solid var(--border);
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.3s ease;
}

.search-input:focus {
  border-color: var(--additional-color);
}

.dropdown-toggle {
  position: absolute;
  right: 8px;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 12px;
  color: var(--font-additional);
  transition: transform 0.3s ease;
  padding: 4px;
}

.dropdown-toggle.open {
  transform: rotate(180deg);
}

.dropdown-list {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: var(--background);
  border: 1px solid var(--border);
  border-radius: 8px;
  box-shadow: 0 4px 12px var(--shadow);
  max-height: 200px;
  overflow-y: auto;
  z-index: 1000;
  margin-top: 4px;
}

.dropdown-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 12px;
  cursor: pointer;
  transition: background-color 0.2s ease;
  border-bottom: 1px solid var(--gray2);
}

.dropdown-item:last-child {
  border-bottom: none;
}

.dropdown-item:hover {
  background: var(--border);
}

.dropdown-item.highlighted {
  background: var(--main-pale);
}

.dropdown-item.selected {
  background: var(--main-pale);
  color: var(--additional-color);
  font-weight: 500;
}

.checkmark {
  color: var(--additional-color);
  font-weight: bold;
  font-size: 14px;
}

.no-results {
  padding: 16px;
  text-align: center;
  color: var(--font-additional);
}

/* Скрыть скроллбар для Chrome, Safari и Opera */
.dropdown-list::-webkit-scrollbar {
  width: 6px;
}

.dropdown-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.dropdown-list::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.dropdown-list::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>