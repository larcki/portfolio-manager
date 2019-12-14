<template>
    <v-content>
        <v-container fluid>
            <v-row>

                <v-col cols="12" md="8">
                    <v-card :elevation="n - 1">
                        <chart-selector></chart-selector>
                    </v-card>
                </v-col>
                <v-col cols="4">
                    <v-card :elevation="n - 1">
                        <pie-chart asset-classes="BOND,STOCK,PROPERTY"/>
                    </v-card>
                </v-col>
            </v-row>

        </v-container>
    </v-content>
</template>
<script>
    import PieChart from "../components/PieChart";
    import ChartSelector from "../components/ChartSelector";

    export default {
        name: 'Dashboard',
        components: {
            PieChart,
            ChartSelector
        }
    }
</script>
