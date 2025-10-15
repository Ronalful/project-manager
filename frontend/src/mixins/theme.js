export default {
    name: 'themeMixin',

    data() {
        return {
            theme: localStorage.getItem('theme') || 'light'
        }
    },
    mounted() {
        this.updateTheme(this.theme);
    },
    methods:{
        updateTheme(theme){
            try {
                document.documentElement.setAttribute('data-theme', theme);
                localStorage.setItem('theme', theme);
                this.theme = theme;
            } catch (error) {
                console.error('Ошибка при установке темы:', error);
            }
        },
    },
    watch: {
        theme(newTheme) {
            this.updateTheme(newTheme);
        }
    }
}