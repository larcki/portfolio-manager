<template>
    <v-app>

        <v-app-bar app>
            <v-toolbar-title class="headline text-uppercase">
                <span class="font-weight-light">Asset Portfolio</span>
            </v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn text
                   href="https://github.com/larcki/portfolio-manager"
                   target="_blank">
                <span class="mr-2">GitHub</span>
            </v-btn>
        </v-app-bar>

        <v-content>
            <v-container fluid>
                <v-row>
                    <v-col>
                        <v-card :elevation="n - 1">
                            <portfolio-chart initial-period="180" initial-tags="BOND,STOCK"/>
                        </v-card>
                    </v-col>
                    <v-col>
                        <v-card :elevation="n - 1">
                            <portfolio-chart initial-period="180" initial-tags="BOND,STOCK"/>
                        </v-card>
                    </v-col>
                </v-row>
            </v-container>
        </v-content>

    </v-app>
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
