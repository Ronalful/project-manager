<template>
  <div class="form-group">
    <select
        multiple
        v-model="selectedValues"
        class="form-select"
     options="options">
      <option
          v-for="option in options"
          :key="getOptionValue(option)"
          :value="getOptionValue(option)"
      >
        {{ getOptionLabel(option) }}
      </option>
    </select>
  </div>
</template>


<script>
export default {
  name: 'Select',
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
    labelKey: {
      type: String,
      default: 'firstname'
    },
    valueKey: {
      type: String,
      default: 'id'
    }
  },
  computed: {
    selectedValues: {
      get() {
        return this.value
      },
      set(newValue) {
        this.$emit('input', newValue)
      }
    }
  },
  methods: {
    getOptionLabel(option) {
      if (typeof option === 'string') {
        return option
      }
      return option[this.labelKey] || option.label || option.name || String(option)
    },

    getOptionValue(option) {
      if (typeof option === 'string') {
        return option
      }
      return option[this.valueKey] || option.value || option.id || String(option)
    }
  }
}

</script>
