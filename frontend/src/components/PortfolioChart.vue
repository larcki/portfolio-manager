<template>
    <div>
        <div class="columns">
            <div class="column">
                Period
                <b-input v-model="period" :placeholder="initialPeriod"></b-input>
            </div>
            <div class="column">
                Tags
                <b-input v-model="tags" :placeholder="initialTags"></b-input>
            </div>
            <div class="column">
                <b-button  v-on:click=updateChart>Refresh</b-button>
            </div>
        </div>

        <canvas ref="chart"></canvas>
    </div>
</template>

<script>
    import api from "./backend-api";
    import Chart from "chart.js";

    export default {
        name: 'PortfolioChart',
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
                api.getChart(this.period, 'STACKED_VALUE', this.tags)
                    .then(successAction)
            },
            updateChart() {
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
</style>
