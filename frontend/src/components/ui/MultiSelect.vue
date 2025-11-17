<template>
  <div class="multi-select">
    <div class="selected-tags" v-if="selectedItems.length > 0">
      <span
          v-for="item in selectedItems"
          :key="item.value"
          class="selected-tag"
      >
        {{ item.label }}
        <button
            @click="removeItem(item)"
            class="remove-btn"
        >
          ×
        </button>
      </span>
    </div>

    <div class="search-container">
      <input
          v-model="searchQuery"
          @focus="isOpen = true"
          @keydown.esc="isOpen = false"
          @keydown.down="highlightNext"
          @keydown.up="highlightPrev"
          @keydown.enter="selectHighlighted"
          :placeholder="placeholder"
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

    <div v-if="isOpen" class="dropdown-list">
      <div
          v-for="(item, index) in filteredItems"
          :key="item.value"
          @click="toggleItem(item)"
          @mouseenter="highlightedIndex = index"
          class="dropdown-item"
          :class="{
          'selected': isSelected(item),
          'highlighted': index === highlightedIndex
        }"
      >
        <span class="checkbox">
          {{ isSelected(item) ? '✓' : '' }}
        </span>
        {{ item.label }}
      </div>

      <div v-if="filteredItems.length === 0" class="no-results">
        Ничего не найдено
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MultiSelect',
  props: {
    options: {
      type: Array,
      required: true,
      default: () => []
    },
    value: {
      type: Array,
      default: () => []
    },
    placeholder: {
      type: String,
      default: 'Выберите варианты...'
    }
  },
  data() {
    return {
      isOpen: false,
      searchQuery: '',
      highlightedIndex: -1,
      selectedItems: []
    }
  },
  computed: {
    filteredItems() {
      if (!this.searchQuery) {
        return this.options.filter(option =>
            !this.isSelected(option)
        )
      }

      const query = this.searchQuery.toLowerCase()
      return this.options.filter(option =>
          option.label.toLowerCase().includes(query) &&
          !this.isSelected(option)
      )
    }
  },
  watch: {
    value: {
      immediate: true,
      handler(newVal) {
        this.selectedItems = newVal || []
      }
    },
    selectedItems: {
      deep: true,
      handler(newVal) {
        this.$emit('input', newVal)
      }
    }
  },
  methods: {
    toggleItem(item) {
      if (this.isSelected(item)) {
        this.removeItem(item)
      } else {
        this.selectItem(item)
      }
    },

    selectItem(item) {
      if (!this.isSelected(item)) {
        this.selectedItems.push(item)
        this.searchQuery = ''
        this.highlightedIndex = -1
      }
    },

    removeItem(item) {
      this.selectedItems = this.selectedItems.filter(
          selected => selected.value !== item.value
      )
    },

    isSelected(item) {
      return this.selectedItems.some(
          selected => selected.value === item.value
      )
    },

    toggleDropdown() {
      this.isOpen = !this.isOpen
      if (this.isOpen) {
        this.$nextTick(() => {
          this.highlightedIndex = -1
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
    }
  },
  mounted() {
    // Закрытие dropdown при клике вне компонента
    document.addEventListener('click', (e) => {
      if (!this.$el.contains(e.target)) {
        this.isOpen = false
      }
    })
  }
}
</script>

<style scoped>
.multi-select {
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

.selected-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 8px;
}

.selected-tag {
  display: inline-flex;
  align-items: center;
  background: var(--main-pale);
  color: var(--font-additional);
  padding: 4px 8px;
  border-radius: 16px;
  font-size: 14px;
  font-weight: 500;
  border: 1px solid var(--border);
}

.remove-btn {
  background: none;
  border: none;
  color: var(--font-main);
  cursor: pointer;
  font-size: 16px;
  margin-left: 4px;
  padding: 0;
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

.remove-btn:hover {
  color: var(--font-additional);
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

.checkbox {
  width: 16px;
  height: 16px;
  border: 2px solid var(--border);
  border-radius: 3px;
  margin-right: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
}

.no-results {
  padding: 16px;
  text-align: center;
  color: #999;
  font-style: italic;
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