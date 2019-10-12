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
        name: 'StackedAreaChart',
        props: {
            initialPeriod: String,
            initialTags: String
        },
        data() {
            return {
                period: this.initialPeriod,
                tags: this.initialTags,
                chart: null
            }
        },
        methods: {
            getChartData(successAction) {
                api.getChart(this.period, this.tags)
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

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    h3 {
        margin: 40px 0 0;
    }

    ul {
        list-style-type: none;
        padding: 0;
    }

    li {
        display: inline-block;
        margin: 0 10px;
    }

    a {
        color: #42b983;
    }
        .period-selector {
            padding-top: 10px;
            padding-left: 30px;
        }
</style>
