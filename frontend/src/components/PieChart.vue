<template>
    <div class="pie-chart-container">
        <canvas ref="chart"></canvas>
    </div>
</template>

<script>
    import api from "./backend-api";
    import Chart from "chart.js";

    export default {
        name: "PieChart",
        props: {
            assetClasses: String
        },
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
        padding-top: 30px;
        padding-bottom: 30px;
    }

</style>