<template>
    <v-app>

            <v-app-bar color="primary" hide-on-scroll dark app>
            <v-toolbar-items>
                <v-btn text>Dashboard</v-btn>
                <v-btn text>Assets</v-btn>
                <v-btn text>Transactions</v-btn>
            </v-toolbar-items>
            <v-spacer></v-spacer>
            <v-btn text
                   href="https://github.com/larcki/portfolio-manager"
                   target="_blank">
                <v-icon>mdi-github-circle</v-icon>
            </v-btn>
        </v-app-bar>

        <v-content>
            <v-container fluid>
                <v-row>
                    <v-col cols="12" md="8">
                        <v-card :elevation="n - 1">
                            <portfolio-chart initial-period="180" initial-tags="BOND,STOCK"/>
                        </v-card>
                    </v-col>
                    <v-col cols="4">
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
