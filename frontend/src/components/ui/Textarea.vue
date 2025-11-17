<template>
  <div class="form-group">
    <textarea
        :id="id"
        :value="modelValue"
        :placeholder="placeholder"
        :required="required"
        :disabled="disabled"
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
  name: 'Textarea',
  props: {
    modelValue: {
      type: [String, Number],
      default: ''
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
  },
  emits: ['update:modelValue', 'blur', 'focus'],
  data() {
    return {
      error: '',
      isTouched: false
    }
  },
  computed: {
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

      if (this.required && !value) {
        this.error = 'Это поле обязательно для заполнения'
        return false
      }

      return true
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