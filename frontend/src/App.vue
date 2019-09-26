<template>
    <div id="app">
        <PortfolioChart initial-period="180" initial-tags="BOND,STOCK"/>
        <PortfolioChart initial-period="360" initial-tags="BOND"/>
    </div>
</template>

<script>
    import PortfolioChart from './components/PortfolioChart.vue'

    export default {
        name: 'app',
        components: {
            PortfolioChart
        }
    }
</script>

<style>
    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
        margin-top: 60px;
    }
</style>
