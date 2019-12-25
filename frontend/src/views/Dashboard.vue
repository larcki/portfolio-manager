<template>
    <v-content>
        <v-container fluid>
            <v-row>

                <v-col cols="12" lg="8">
                    <v-card :elevation="n - 1">
                        <chart-selector></chart-selector>
                    </v-card>
                </v-col>

                <v-col cols="12" lg="4">
                    <pie-chart/>
                    <div class="summary-card">
                        <summary-tile/>
                    </div>

                </v-col>
            </v-row>

        </v-container>
    </v-content>
</template>
<script>
    import PieChart from "../components/PieChart";
    import ChartSelector from "../components/ChartSelector";
    import SummaryTile from "../components/SummaryCard";

    export default {
        name: 'Dashboard',
        components: {
            SummaryTile,
            PieChart,
            ChartSelector
        }
    }
</script>
<style>
    .summary-card {
        padding-top: 24px;
    }
</style>
