<template>
    <v-list-item-subtitle :class=colour>{{symbol}}{{amount}}{{suffix}}</v-list-item-subtitle>
</template>

<script>
    export default {
        name: "ColouredProfitLabel",
        props: {
            amount: String,
            suffix: {
                default: ''
            }
        },
        computed: {
            symbol() {
                if (this.amount >= 0) {
                    return '+'
                } else {
                    return ''
                }
            },
            colour() {
                if (this.amount >= 0) {
                    return 'green--text darken-1'
                } else {
                    return 'red--text darken-1'
                }
            }
        },
    }
</script>

<style scoped>

</style>