<template>
    <div id="app">
        <img alt="Vue logo" src="./assets/logo.png">
        <PortfolioChart/>
        <PortfolioChart/>
        <PortfolioChart/>
        <PortfolioChart/>

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
