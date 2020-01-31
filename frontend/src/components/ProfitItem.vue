<template>
    <v-list-item-content>
        <v-list-item-title>{{title}}</v-list-item-title>
        <v-list-item-subtitle class="green--text darken-1">+{{profit}}</v-list-item-subtitle>
        <v-list-item-subtitle class="green--text darken-1">+{{percentage}}%</v-list-item-subtitle>
    </v-list-item-content>
</template>

<script>
    import api from "./backend-api";

    export default {
        name: "ProfitItem",
        props: {
            days: String,
            title: String
        },
        data() {
            return {
                profit: null,
                percentage: null,
            }
        },
        mounted() {
            api.getSummarySince(this.days).then(response => {
                this.profit = response.data.totalProfit
                this.percentage = response.data.totalProfitPercentage
            })
        }
    }
</script>

<style scoped>

</style>