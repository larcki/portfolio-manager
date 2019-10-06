<template>
    <v-content>
        <v-container fluid>
            <v-row>
                <v-col cols="12" md="8">
                    <v-card :elevation="n - 1">
                        <stacked-area-chart initial-period="180" initial-tags="BOND,STOCK,PROPERTY,CASH_EUR"/>
                    </v-card>
                </v-col>
                <v-col cols="4">
                    <v-card :elevation="n - 1">
                        <pie-chart asset-classes="BOND,STOCK,PROPERTY,CASH_EUR"/>
                    </v-card>
                </v-col>
            </v-row>
        </v-container>
    </v-content>
</template>
<script>
    import StackedAreaChart from "../components/StackedAreaChart";
    import PieChart from "../components/PieChart";

    export default {
        name: 'Dashboard',
        components: {
            StackedAreaChart,
            PieChart
        }
    }
</script>
