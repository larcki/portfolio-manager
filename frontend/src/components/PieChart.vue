<template>
    <v-card :elevation="n - 1">
        <v-card-title>Asset allocation</v-card-title>
        <div class="pie-chart-container">
            <canvas ref="chart"></canvas>
        </div>
    </v-card>
</template>

<script>
    import api from "./backend-api";
    import Chart from "chart.js";

    export default {
        name: "PieChart",
        data() {
            return {
                chart: null
            }
        },
        mounted() {
            api.getPieChart()
                .then(response => {
                    this.chart = new Chart(this.$refs.chart, response.data)
                })
        }
    }
</script>

<style scoped>
    .pie-chart-container {
        padding-top: 15px;
        padding-bottom: 30px;
    }

</style>