<template>

    <div id="app" class="container">
        <div class="columns">
            <div class="column">

                <section class="info-tiles">
                    <div class="tile is-ancestor has-text-centered">
                        <div class="tile is-parent">
                            <article class="tile is-child box">
                                <portfolio-chart initial-period="180" initial-tags="BOND,STOCK"/>
                            </article>
                        </div>
                        <div class="tile is-parent">
                            <article class="tile is-child box">
                                <portfolio-chart initial-period="30" initial-tags="BOND"/>
                            </article>
                        </div>
                    </div>
                </section>


            </div>
        </div>
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
