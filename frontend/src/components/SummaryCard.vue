<template>
    <v-card :elevation="n - 1">
        <v-card-title>{{title}}</v-card-title>

        <v-list-item two-line>
            <v-list-item-content>
                <v-list-item-title>Total value</v-list-item-title>
                <v-list-item-subtitle class="headline">{{totalValue}}</v-list-item-subtitle>
            </v-list-item-content>
        </v-list-item>

        <v-list-item three-line>
            <v-list-item-content>
                <v-list-item-title>Profit</v-list-item-title>
                <v-list-item-subtitle class="green--text darken-1 headline">+{{profit}}</v-list-item-subtitle>
                <v-list-item-subtitle class="green--text darken-1 headline">+{{percentage}}%</v-list-item-subtitle>
            </v-list-item-content>
        </v-list-item>

    </v-card>
</template>

<script>
    import api from "./backend-api";

    export default {
        name: "SummaryTile",
        data() {
            return {
                title: 'Summary',
                totalValue: '',
                profit: '',
                percentage: ''
            }
        },
        mounted() {
            api.getSummary().then(response => {
                this.totalValue = response.data.totalValue
                this.profit = response.data.totalProfit
                this.percentage = response.data.totalProfitPercentage
            });
        }
    }
</script>

<style scoped>

</style>