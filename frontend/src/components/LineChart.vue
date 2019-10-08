<template>
    <div>
        <div class="period-selector">
            <v-btn-toggle v-model="toggle_exclusive">
                <v-btn v-on:click="updateChart(1825)">5Y</v-btn>
                <v-btn v-on:click="updateChart(1095)">3Y</v-btn>
                <v-btn v-on:click="updateChart(365)">1Y</v-btn>
                <v-btn v-on:click="updateChart(183)">6M</v-btn>
                <v-btn v-on:click="updateChart(92)">3Y</v-btn>
                <v-btn v-on:click="updateChart(30)">1M</v-btn>
            </v-btn-toggle>
        </div>
        <canvas ref="chart"></canvas>
    </div>
</template>

<script>
    import api from "./backend-api";
    import Chart from "chart.js";

    export default {
        name: 'LineChart',
        props: {
            initialPeriod: String,
            assetId: String,
        },
        data() {
            return {
                period: this.initialPeriod,
                chart: null
            }
        },
        methods: {
            getChartData(successAction) {
                api.getLineChart(this.period, this.assetId)
                    .then(successAction)
            },
            updateChart(period) {
                this.period = period
                this.getChartData(response => {
                    this.chart.destroy()
                    this.chart = new Chart(this.$refs.chart, response.data);
                });
            }
        },
        mounted() {
            this.getChartData(response => {
                this.chart = new Chart(this.$refs.chart, response.data);
            })
        }
    }
</script>

<style scoped>
    .period-selector {
        padding-top: 10px;
        padding-left: 30px;
    }
</style>