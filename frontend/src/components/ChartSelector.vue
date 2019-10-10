<template>
    <div>
        <v-card-title>

            <v-col>
                <v-select class="title"
                          :items="charts"
                          label=""
                          v-model="selection"
                          @change="updateChartType()"
                ></v-select>
            </v-col>
            <v-col>
                <div class="period-selector float-right">
                    <v-btn-toggle v-model="toggle_exclusive">
                        <v-btn class="body-2" v-on:click="updateChart(1825)">5Y</v-btn>
                        <v-btn class="body-2" v-on:click="updateChart(1095)">3Y</v-btn>
                        <v-btn class="body-2" v-on:click="updateChart(365)">1Y</v-btn>
                        <v-btn class="body-2" v-on:click="updateChart(183)">6M</v-btn>
                        <v-btn class="body-2" v-on:click="updateChart(92)">3M</v-btn>
                        <v-btn class="body-2" v-on:click="updateChart(30)">1M</v-btn>
                    </v-btn-toggle>
                </div>
            </v-col>
        </v-card-title>

        <canvas ref="chart"></canvas>

    </div>
</template>

<script>
    import api from "./backend-api";
    import Chart from "chart.js";

    export default {
        name: "ChartSelector",
        data() {
            return {
                charts: [
                    {text: "Asset class allocation over time", value: "STACKED_ASSET_CLASS_ALLOCATION"},
                    {text: "Portfolio performance over time (%)", value: "LINE_TOTAL_PERFORMANCE_PERCENTAGE"},
                    {text: "Portfolio performance over time (EUR)", value: "LINE_TOTAL_PERFORMANCE"},
                ],
                selection: "STACKED_ASSET_CLASS_ALLOCATION",
                period: 180,
                chart: null
            }
        },
        methods: {
            getChartData(successAction) {
                api.getGeneric(this.period, this.selection)
                    .then(successAction)
            },
            updateChart(period) {
                this.period = period
                this.getChartData(response => {
                    this.chart.destroy()
                    this.chart = new Chart(this.$refs.chart, response.data);
                });
            },
            updateChartType() {
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

</style>