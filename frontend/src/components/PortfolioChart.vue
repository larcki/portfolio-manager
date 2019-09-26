<template>
    <div>
        Period <input v-model="period" :placeholder="initialPeriod">
        Tags <input v-model="tags" :placeholder="initialTags">
        <button v-on:click=loadChart>Refresh</button>
        <canvas ref="chart"></canvas>
    </div>
</template>

<script>
    import api from "./backend-api";
    import Chart from "../../node_modules/chart.js";

    export default {
        name: 'PortfolioChart',
        props: {
            initialPeriod: Number,
            initialTags: Array
        },
        data() {
            return {
                period: this.initialPeriod,
                tags: this.initialTags
            }
        },
        methods: {
            loadChart() {
                api.getChart(this.period, 'STACKED_VALUE', this.tags)
                    .then(response => {
                        let ctx = this.$refs.chart
                        new Chart(ctx, response.data);
                    })
            }
        },
        mounted() {
            this.loadChart()
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
