<template>
    <v-content>
        <v-container fluid>
            <v-row>

                <v-col cols="16" lg="4" md="16" sm="16">
                    <v-card :elevation="n - 1">
                        <v-card-title>{{name}}</v-card-title>

                        <v-card-text>

                            <v-simple-table>
                                <template v-slot:default>
                                    <tbody>
                                    <tr>
                                        <td>Account</td>
                                        <td>{{account}}</td>
                                    </tr>
                                    <tr>
                                        <td>Link</td>
                                        <td><a :href="link">{{link}}</a></td>
                                    </tr>
                                    <tr>
                                        <td>Value</td>
                                        <td>{{value}}</td>
                                    </tr>
                                    </tbody>
                                </template>
                            </v-simple-table>

                        </v-card-text>
                    </v-card>
                </v-col>

                <v-col cols="16" lg="8" md="16" sm="16">
                    <v-card :elevation="n - 1">
                        <line-chart initial-period="180" :asset-id="this.$route.params.assetId"/>
                    </v-card>
                </v-col>
            </v-row>

            <v-row>
            </v-row>
        </v-container>
    </v-content>

</template>

<script>
    import LineChart from "../components/LineChart";
    import Api from "../components/backend-api";


    export default {
        name: 'Asset',
        components: {
            LineChart
        },
        data() {
            return {
                name: '',
                link: '',
                account: '',
                value: '',
            }
        },
        created() {

            Api.getAsset(this.$route.params.assetId)
                .then(response => {
                    this.name = response.data.name;
                    this.link = response.data.link;
                    this.account = response.data.account;
                    this.value = response.data.totalValue;
                })
        }
    }
</script>

<style scoped>

</style>