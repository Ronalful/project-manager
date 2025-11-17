<script setup>

</script>

<template>
  <div class="form-group">
    <input
        :id="id"
        :type="inputType"
        :value="modelValue"
        :placeholder="placeholder"
        :required="required"
        :disabled="disabled"
        :maxlength="maxlength"
        @input="handleInput"
        @blur="handleBlur"
        @focus="handleFocus"
        class="form-field__input"
        :class="inputClasses"
    />
  </div>
  <div v-if="error" class="form-field__error">
    {{ error }}
  </div>
</template>

<script>
export default {
  name: 'Input',
  props: {
    modelValue: {
      type: [String, Number],
      default: ''
    },
    type: {
      type: String,
      default: 'text',
      validator: (value) => [
        'text', 'email', 'password', 'tel', 'number', 'url'
      ].includes(value)
    },
    placeholder: {
      type: String,
      default: ''
    },
    required: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    id: {
      type: String,
      default: () => `field-${Math.random().toString(36).substr(2, 9)}`
    },
    maxlength: {
      type: Number,
      default: null
    },
  },
  emits: ['update:modelValue', 'blur', 'focus'],
  data() {
    return {
      error: '',
      isTouched: false
    }
  },
  computed: {
    inputType() {
      if (this.type === 'password') return 'password'
      return this.type
    },
    inputClasses() {
      return {
        'form-field__input--error': this.error,
        'form-field__input--disabled': this.disabled
      }
    }
  },
  methods: {
    handleFocus(event) {
      this.$emit('focus', event)
    },

    handleInput(event) {
      const value = event.target.value
      this.$emit('update:modelValue', value)
      // Валидация при вводе, если поле уже было тронуто
      if (this.isTouched) {
        this.validate(value)
      }
    },

    handleBlur(event) {
      this.isTouched = true
      this.validate(event.target.value)
      this.$emit('blur', event)
    },

    validate(value) {
      this.error = ''

      if (this.required && !value.trim()) {
        this.error = 'Это поле обязательно для заполнения'
        return false
      }

      if (value && !this.validateByType(value)) {
        return false
      }

      return true
    },

    validateByType(value) {
      const validators = {
        email: (val) => {
          const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
          if (!emailRegex.test(val)) {
            this.error = 'Введите корректный email адрес'
            return false
          }
          return true
        },

        /*password: (val) => {
          if (val.length < 6) {
            this.error = 'Пароль должен содержать минимум 6 символов'
            return false
          }
          return true
        },*/

        tel: (val) => {
          const phoneRegex = /^[\+]?[0-9\s\-\(\)]{10,}$/
          if (!phoneRegex.test(val.replace(/\s/g, ''))) {
            this.error = 'Введите корректный номер телефона'
            return false
          }
          return true
        },

        url: (val) => {
          try {
            new URL(val)
            return true
          } catch {
            this.error = 'Введите корректный URL'
            return false
          }
        }
      }

      return validators[this.type] ? validators[this.type](value) : true
    },

    isValid() {
      return this.validate(this.modelValue)
    },
  }
}
</script>

<style scoped>
.form-field__input {
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

.form-field__input::placeholder {
  color: var(--font-additional);
}

.form-field__error{
  text-align: left;
  color: var(--error-text);
  font-size: 12px;
}

.form-field__input--error {
  border: 2px solid var(--error-text);
}

.form-field__input--disabled {
  background-color: var(--disabled);
  cursor: not-allowed;
}


</style>