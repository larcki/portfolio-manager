<template>
    <v-list-item-content>
        <v-list-item-title>{{title}}</v-list-item-title>
        <coloured-profit-label :amount=profit></coloured-profit-label>
        <coloured-profit-label :amount=percentage suffix="%"></coloured-profit-label>
    </v-list-item-content>
</template>

<script>
    import api from "./backend-api";
    import ColouredProfitLabel from "./ColouredProfitLabel";

    export default {
        name: "ProfitItem",
        components: {ColouredProfitLabel},
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