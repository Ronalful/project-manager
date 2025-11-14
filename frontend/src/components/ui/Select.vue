<template>
  <div class="form-group">
    <select
        :id="id"
        :value="modelValue"
        @input="handleInput"
        @blur="handleBlur"
        @focus="handleFocus"
        :required="required"
        :disabled="disabled"
        :multiple="multiple"
        class="form-select"
    >
      <option value="" disabled>{{ placeholder }}</option>
      <option
          v-for="option in options"
          :value="option.id"
      >
        {{ option.name }}
      </option>
    </select>
  </div>
</template>

<script>
export default {
  name: 'BaseSelect',
  props: {
    modelValue: {
      type: [String, Number, Array],
      default: ''
    },
    options: {
      type: Array,
      required: true,
      default: () => []
    },
    placeholder: {
      type: String,
      default: 'Выберите вариант'
    },
    required: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    multiple: {
      type: Boolean,
      default: false
    },
    id: {
      type: String,
      default: () => `select-${Math.random().toString(36).substr(2, 9)}`
    },
    optionLabel: {
      type: String,
      default: 'label'
    },
    optionValue: {
      type: String,
      default: 'value'
    },
    error: {
      type: String,
      default: ''
    }
  },
  emits: ['update:modelValue', 'change', 'blur', 'focus'],
  data() {
    return {
      isTouched: false
    }
  },
  methods: {
    handleInput(event) {
      let value = event.target.value

      // Для multiple select
      if (this.multiple) {
        const selectedOptions = Array.from(event.target.selectedOptions)
        value = selectedOptions.map(option => option.value)
      }

      this.$emit('update:modelValue', value)
      this.$emit('change', value)
    },

    handleBlur(event) {
      this.isTouched = true
      this.$emit('blur', event)
    },

    handleFocus(event) {
      this.$emit('focus', event)
    },

    getOptionValue(option) {
      if (typeof option === 'object' && option !== null) {
        return option[this.optionValue]
      }
      return option
    },

    getOptionLabel(option) {
      if (typeof option === 'object' && option !== null) {
        return option[this.optionLabel]
      }
      return option
    },

    validate() {
      if (this.required && !this.modelValue) {
        return 'Это поле обязательно для заполнения'
      }
      return ''
    },

    // Метод для ручной валидации
    isValid() {
      const error = this.validate()
      return !error
    }
  }
}
</script>

<style scoped>

</style>