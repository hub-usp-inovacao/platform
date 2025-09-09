<template>
  <v-text-field
    :value="value"
    :label="label"
    :hint="hint"
    :placeholder="placeholder"
    :error="hasError"
    :error-messages="errorMessage"
    @input="handleInput"
    @blur="handleBlur"
    persistent-hint
    clearable
  />
</template>

<script>
export default {
  props: {
    value: {
      type: String,
      default: '',
    },
    label: {
      type: String,
      default: 'URL',
    },
    hint: {
      type: String,
      default: 'Digite uma URL válida',
    },
    placeholder: {
      type: String,
      default: 'https://exemplo.com',
    },
    urlType: {
      type: String,
      default: 'generic',
      validator: (value) => ['generic', 'linkedin', 'instagram', 'facebook', 'youtube'].includes(value)
    },
  },
  data() {
    return {
      hasError: false,
      errorMessage: '',
    };
  },
  methods: {
    handleInput(value) {
      this.$emit('input', value);
      this.clearError();
    },

    handleBlur() {
    },

    clearError() {
      this.hasError = false;
      this.errorMessage = '';
    },

    isValid() {
      if (!this.value || this.value.trim() === '') {
        return true;
      }

      const url = this.value.trim();

      switch (this.urlType) {
        case 'linkedin':
          return this.isValidLinkedinUrl(url);
        case 'instagram':
          return this.isValidInstagramUrl(url);
        case 'facebook':
          return this.isValidFacebookUrl(url);
        case 'youtube':
          return this.isValidYoutubeUrl(url);
        default:
          return this.isValidGenericUrl(url);
      }
    },

    validate() {
      if (!this.value || this.value.trim() === '') {
        this.clearError();
        return true;
      }

      const url = this.value.trim();

      if (!this.isValid()) {
        this.hasError = true;
        this.errorMessage = this.getErrorMessage();
        return false;
      }

      this.clearError();
      return true;
    },

    getErrorMessage() {
      switch (this.urlType) {
        case 'linkedin':
          return 'Use o formato: https://www.linkedin.com/company/sua-empresa';
        case 'instagram':
          return 'Use o formato: https://www.instagram.com/sua-empresa';
        case 'facebook':
          return 'Use o formato: https://www.facebook.com/sua-empresa';
        case 'youtube':
          return 'Use o formato: https://www.youtube.com/c/sua-empresa';
        default:
          return 'URL inválida';
      }
    },

    isValidGenericUrl(url) {
      try {
        new URL(url);
        return url.startsWith('http://') || url.startsWith('https://');
      } catch {
        return false;
      }
    },

    isValidLinkedinUrl(url) {
      const linkedinPattern = /^https:\/\/(www\.)?linkedin\.com\/(company|in|pub)\/[a-zA-Z0-9._-]+\/?$/;
      return linkedinPattern.test(url);
    },

    isValidInstagramUrl(url) {
      const instagramPattern = /^https:\/\/(www\.)?instagram\.com\/[a-zA-Z0-9._@-]+\/?$/;
      return instagramPattern.test(url);
    },

    isValidFacebookUrl(url) {
      const facebookPattern = /^https:\/\/(www\.)?facebook\.com\/[a-zA-Z0-9.@_-]+\/?$/;
      return facebookPattern.test(url);
    },

    isValidYoutubeUrl(url) {
      const youtubePattern = /^https:\/\/(www\.)?youtube\.com\/(c\/|channel\/|user\/|@)[a-zA-Z0-9._@-]+\/?$/;
      return youtubePattern.test(url);
    },
  },
};
</script>
