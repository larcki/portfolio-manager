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
                <v-list-item-title>Performance</v-list-item-title>
                <v-list-item-subtitle class="green--text darken-1 headline">+{{totalProfit}}</v-list-item-subtitle>
                <v-list-item-subtitle class="green--text darken-1 headline">+{{totalPercentage}}%</v-list-item-subtitle>
            </v-list-item-content>
        </v-list-item>

        <v-list-item three-line>
            <profit-item days="365" title="1 year"></profit-item>
            <profit-item days="30" title="1 month"></profit-item>
            <profit-item days="7" title="7 days"></profit-item>
            <profit-item days="1" title="1 day"></profit-item>
        </v-list-item>

    </v-card>
</template>

<script>
    import api from "./backend-api";
    import ProfitItem from "./ProfitItem";

    export default {
        name: "SummaryTile",
        components: {ProfitItem},
        data() {
            return {
                title: 'Summary',
                totalValue: '',
                totalProfit: '',
                totalPercentage: ''
            }
        },
        mounted() {
            api.getSummary().then(response => {
                this.totalValue = response.data.totalValue
                this.totalProfit = response.data.totalProfit
                this.totalPercentage = response.data.totalProfitPercentage
            });
        }
    }
</script>

<style scoped>

</style>