<template>
    <div class="hello">
        <div>{{msg}}</div>
        <canvas ref="chart"></canvas>
    </div>
</template>

<script>
    import api from "./backend-api";
    import Chart from "../../node_modules/chart.js";

    export default {
        name: 'PortfolioChart',
        data() {
            return {
                msg: {}
            }
        },
        mounted() {
            api.getChart()
                .then(response => {
                    this.msg = response.data
                    let ctx = this.$refs.chart
                    var myChart = new Chart(ctx, response.data);
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
