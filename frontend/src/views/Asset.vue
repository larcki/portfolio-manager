<template>
    <v-content>
        <v-container fluid>
            <v-row>
                <v-col cols="12" md="8">
                    <v-card :elevation="n - 1">
                        <line-chart initial-period="180" :asset-id="$route.params.assetId"/>
                    </v-card>
                </v-col>
            </v-row>
        </v-container>
    </v-content>

</template>

<script>
    import LineChart from "../components/LineChart";

    export default {
        name: 'Asset',
        components: {
            LineChart
        }
    }
</script>

<style scoped>

</style>